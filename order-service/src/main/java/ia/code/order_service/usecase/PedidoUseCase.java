package ia.code.order_service.usecase;

import ia.code.order_service.entity.dto.PedidoResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public interface PedidoUseCase {

    Mono<PedidoResponse> crearPedido(Integer idUsuario);
    Flux<PedidoResponse> listarPedidosPorUsuario(Integer idUsuario);
    Mono<PedidoResponse> obtenerPedidoPorId(Integer idPedido);
    Flux<PedidoResponse> listarTodosLosPedidos();
    Mono<PedidoResponse> actualizarEstadoPedido(Integer idPedido, String estadoRequest);
}
