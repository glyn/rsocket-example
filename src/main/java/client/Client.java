/*
 * Copyright 2019 The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package client;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Client {
	public static void main(String[] args) {
		if (args.length > 1) {
			System.out.println("Too many arguments");
			System.exit(-1);
		}

		int port = 8080;
		if (args.length == 1) {
			port = Integer.parseInt(args[0]);
		}

		RSocket socket =
				RSocketFactory.connect()
						.transport(TcpClientTransport.create("localhost", port))
						.start()
						.block();

		socket
				.requestChannel(
						Flux.interval(Duration.ofMillis(1000)).map(i -> DefaultPayload.create("Hello")))
				.map(Payload::getDataUtf8)
				.doOnNext(System.out::println)
				.take(10)
				.doFinally(signalType -> socket.dispose())
				.then()
				.block();
	}
}
