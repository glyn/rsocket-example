package server;

import io.rsocket.*;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Server {

	public static void main(String[] args) {
		RSocketFactory.receive()
				// .frameDecoder(Frame::retain) // Enable zero copy
				.acceptor(new EchoAcceptor())
				.transport(TcpServerTransport.create("localhost", 8080))
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
