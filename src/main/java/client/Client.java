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
		RSocket socket =
				RSocketFactory.connect()
						.transport(TcpClientTransport.create("localhost", 8080))
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
