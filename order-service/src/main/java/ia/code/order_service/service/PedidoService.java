package ia.code.order_service.service;

import ia.code.order_service.entity.DetallePedido;
import ia.code.order_service.entity.Pedido;
import ia.code.order_service.entity.dto.*;
import ia.code.order_service.repository.DetallePedidoRepository;
import ia.code.order_service.repository.PedidoRepository;
import ia.code.order_service.usecase.PedidoUseCase;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PedidoService implements PedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final WebClient webClient;
    private final ModelMapper modelMapper;

    private static final String CART_SERVICE_ENDPOINT = "/carrito/usuario/{userId}";


    @Override
    public Mono<PedidoResponse> crearPedido(Integer idUsuario) {
        return webClient.get()
                .uri(CART_SERVICE_ENDPOINT, idUsuario)
                .header("X-User-Id", idUsuario.toString())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response ->
                        Mono.error(new RuntimeException("Carrito no encontrado para el usuario auntenticado: " + idUsuario))
                )
                .onStatus(status -> status.is5xxServerError(), response ->
                                Mono.error(new RuntimeException("Error en cart-service"))
                        )
                .bodyToMono(CartRequest.class)
                .flatMap(cartRequest -> {
                    // Validar que el carrito tenga items
                    if (cartRequest.getItems() == null || cartRequest.getItems().isEmpty()) {
                        return Mono.error(new RuntimeException("El carrito está vacío"));
                    }
                    // Se crea el pedido
                    Pedido pedido = new Pedido();
                    pedido.setIdUsuario(idUsuario);
                    pedido.setFecha(LocalDateTime.now());
                    pedido.setTotal(cartRequest.getTotal());
                    pedido.setEstado("PENDIENTE");

                    // Guardamos el pedido con los detalles
                    return pedidoRepository.save(pedido)
                            .flatMap(savedPedido -> {
                                // Creamos los detalles del pedido desde los detalles del carrito
                                List<DetallePedido> detalles = cartRequest.getItems().stream()
                                        .map(item -> {
                                            DetallePedido detalle = new DetallePedido();
                                            detalle.setIdProducto(item.getIdProducto());
                                            detalle.setNombreProducto(item.getNombreProducto());
                                            detalle.setCantidad(item.getCantidad());
                                            detalle.setSubtotal(item.getSubtotal());
                                            return detalle;
                                                })
                                        .peek(detalle -> detalle.setIdPedido(savedPedido.getIdPedido()))
                                        .collect(Collectors.toList());

                                // Guardamos los detalles
                                return detallePedidoRepository.saveAll(detalles)
                                        .collectList()
                                        .map(savedDetalles -> {
                                            //Contruimos la respuesta
                                            PedidoResponse response = modelMapper.map(savedPedido, PedidoResponse.class);
                                            response.setDetalles(mapToDetalleResponse(savedDetalles));
                                            return response;
                                        });
                            });

                });
    }

    private List<DetallePedidoResponse> mapToDetalleResponse(List<DetallePedido> detalles) {
        return  detalles.stream()
                .map(detalle -> {
                    DetallePedidoResponse response = new DetallePedidoResponse();
                    response.setIdProducto(detalle.getIdProducto());
                    response.setNombreProducto(detalle.getNombreProducto());
                    response.setCantidad(detalle.getCantidad());
                    response.setPrecioUnitario(detalle.getSubtotal() / detalle.getCantidad());
                    response.setSubtotal(detalle.getSubtotal());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Flux<PedidoResponse> listarPedidosPorUsuario(Integer idUsuario) {
        return pedidoRepository.findByIdUsuario(idUsuario)
                .flatMap(this::construirPedidoResponseConDetalles);
    }

    private Mono<PedidoResponse> construirPedidoResponseConDetalles(Pedido pedido) {
        return detallePedidoRepository.findByIdPedido(pedido.getIdPedido())
                .collectList()
                .map(detalles -> {
                    PedidoResponse response = modelMapper.map(pedido, PedidoResponse.class);
                    response.setDetalles(mapToDetalleResponse(detalles));
                    return response;
                });
    }

    @Override
    public Mono<PedidoResponse> obtenerPedidoPorId(Integer idPedido) {
        return pedidoRepository.findById(idPedido)
                .flatMap(this::construirPedidoResponseConDetalles);
    }

    @Override
    public Flux<PedidoResponse> listarTodosLosPedidos() {
        return pedidoRepository.findAll()
                .flatMap(this::construirPedidoResponseConDetalles);
    }

    @Override
    public Mono<PedidoResponse> actualizarEstadoPedido(Integer idPedido, String estado) {
        return pedidoRepository.findById(idPedido)
                .flatMap(pedido -> {
                    pedido.setEstado(estado);
                    return pedidoRepository.save(pedido);
                })
                .flatMap(this::construirPedidoResponseConDetalles);
    }
}

