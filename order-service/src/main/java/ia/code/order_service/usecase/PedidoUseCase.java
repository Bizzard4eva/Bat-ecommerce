package ia.code.order_service.usecase;

import ia.code.order_service.entity.Pedido;
import ia.code.order_service.entity.dto.PedidoRequest;
import ia.code.order_service.entity.dto.PedidoResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public interface PedidoUseCase {

    Mono<PedidoResponse> createPedido(PedidoRequest pedidoRequest);
    Flux<Pedido> getPedidos();
    Mono<PedidoResponse> getPedidoById(Integer idPedido);
}
