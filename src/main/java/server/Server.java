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

package server;

import io.rsocket.*;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import util.Host;
import util.Port;

public class Server {
	public static void main(String[] args) {
		if (args.length > 2) {
			System.out.println("Too many arguments");
			System.exit(-1);
		}

		RSocketFactory.receive()
				// .frameDecoder(Frame::retain) // Enable zero copy
				.acceptor(new EchoAcceptor())
				.transport(TcpServerTransport.create(Host.getHost(args), Port.getPort(args)))
				.start()
				.block()
				.onClose()
				.block();
	}

	private static class EchoAcceptor implements SocketAcceptor {
		@Override
		public Mono<RSocket> accept(ConnectionSetupPayload setupPayload, RSocket reactiveSocket) {
			return Mono.just(
					new AbstractRSocket() {
						@Override
						public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
							return Flux.from(payloads)
									.map(Payload::getDataUtf8)
									.map(s -> "Echo: " + s)
									.map(DefaultPayload::create);
						}
					});
		}
	}
}
