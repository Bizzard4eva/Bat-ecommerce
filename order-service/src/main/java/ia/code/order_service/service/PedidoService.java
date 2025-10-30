package ia.code.order_service.service;

import ia.code.order_service.configuration.MapperConfiguration;
import ia.code.order_service.entity.Pedido;
import ia.code.order_service.entity.dto.EstadoPedidoRequest;
import ia.code.order_service.entity.dto.PedidoRequest;
import ia.code.order_service.entity.dto.PedidoResponse;
import ia.code.order_service.entity.dto.DetallePedidoResponse;
import ia.code.order_service.repository.DetallePedidoRepository;
import ia.code.order_service.repository.PedidoRepository;
import ia.code.order_service.usecase.PedidoUseCase;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PedidoService implements PedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final WebClient webClient; // se inyecta desde tu WebClientConfiguration

    private static final String CART_SERVICE_ENDPOINT = "/carrito/usuario/{productId}";

    @Override
    public Mono<PedidoResponse> crearPedido(Integer idUsuario,PedidoRequest pedidoRequest) {

        // Obtener detalles del carrito desde cart-service con token
        return webClient.get()
                .uri(CART_SERVICE_ENDPOINT, idUsuario)
                .retrieve()
                .bodyToFlux(DetallePedidoResponse.class)
                .collectList()
                .flatMap(detalles -> {
                    if (detalles == null || detalles.isEmpty()) {
                        return Mono.error(new RuntimeException("El carrito está vacío"));
                    }

                    double total = detalles.stream()
                            .mapToDouble(d -> d.getSubtotal() != null ? d.getSubtotal() : 0)
                            .sum();

                    Pedido pedido = MapperConfiguration.toEntity(pedidoRequest);
                    pedido.setIdUsuario(idUsuario);
                    pedido.setTotal(total);

                    return pedidoRepository.save(pedido)
                            .map(saved -> {
                                PedidoResponse response = MapperConfiguration.toResponse(saved);
                                response.setDetalles(detalles);
                                return response;
                            });
                });
    }

    @Override
    public Flux<PedidoResponse> listarPedidosPorUsuario(Integer idUsuario) {
        return pedidoRepository.findByIdUsuario(idUsuario)
                .map(MapperConfiguration::toResponse);
    }

    @Override
    public Mono<PedidoResponse> obtenerPedidoPorId(Integer idPedido) {
        return pedidoRepository.findById(idPedido)
                .map(MapperConfiguration::toResponse);
    }

    @Override
    public Flux<PedidoResponse> listarTodosLosPedidos() {
        return pedidoRepository.findAll()
                .map(MapperConfiguration::toResponse);
    }

    @Override
    public Mono<PedidoResponse> actualizarEstadoPedido(Integer idPedido, EstadoPedidoRequest estadoRequest) {
        return pedidoRepository.findById(idPedido)
                .flatMap(pedido -> {
                    pedido.setEstado(estadoRequest.getEstado());
                    return pedidoRepository.save(pedido);
                })
                .map(MapperConfiguration::toResponse);
    }
}

