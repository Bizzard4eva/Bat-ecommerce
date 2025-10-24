package ia.code.order_service.model;

import ia.code.order_service.entity.DetallePedido;
import ia.code.order_service.entity.Pedido;
import ia.code.order_service.entity.dto.PedidoRequest;
import ia.code.order_service.entity.dto.PedidoResponse;
import ia.code.order_service.entity.dto.ProductoDto;
import ia.code.order_service.repository.DetallePedidoRepository;
import ia.code.order_service.repository.PedidoRepository;
import ia.code.order_service.usecase.PedidoUseCase;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PedidoModel implements PedidoUseCase {

//    private final ModelMapper modelMapper;
    private final WebClient webClient;
    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoRepository pedidoRepository;

    @Override
    public Mono<PedidoResponse> createPedido(PedidoRequest pedidoRequest) {
        Pedido pedidov = Pedido.builder()
                .id_usuario(pedidoRequest.getIdUsuario())
                .fecha(LocalDateTime.now())
                .total(0.0)
                .build();

        return pedidoRepository.save(pedidov)
                .flatMap(pedido -> Flux.fromIterable(pedidoRequest.getProductos())
                        .flatMap(productoReq -> getProductoById(productoReq.getId_producto())
                                .map(productoDto -> {
                                    double subtotal = productoDto.getPrecio() * productoReq.getCantidad();
                                    return new DetallePedido(null,
                                            pedido.getId_pedido(),
                                            productoDto.getId_producto(),
                                            productoReq.getCantidad(),
                                            subtotal
                                    );
                                })
                        )
                        .collectList()
                        .flatMap(detalles -> {
                            double total = detalles.stream()
                                    .mapToDouble(DetallePedido::getSubtotal)
                                    .sum();
                            pedido.setTotal(total);

                            return detallePedidoRepository.saveAll(detalles)
                                    .collectList()
                                    .flatMap(savedDetalles -> {
                                        return pedidoRepository.save(pedido)
                                                .map(updatedPedido -> new PedidoResponse(
                                                   updatedPedido.getId_pedido(),
                                                    updatedPedido.getId_usuario(),
                                                    updatedPedido.getTotal(),
                                                    updatedPedido.getFecha(),
                                                    savedDetalles
                                                ));
                                    });
                        })
                );
    }

    @Override
    public Flux<Pedido> getPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Mono<PedidoResponse> getPedidoById(Integer idPedido) {
        return pedidoRepository.findById(idPedido)
                .flatMap(pedido ->
                        detallePedidoRepository.findAll()
                                .filter(det -> det.getId_pedido().equals(pedido.getId_pedido()))
                                .collectList()
                                .map(detalles -> new PedidoResponse(
                                        pedido.getId_pedido(),
                                        pedido.getId_usuario(),
                                        pedido.getTotal(),
                                        pedido.getFecha(),
                                        detalles
                                ))
                );
    }

    private Mono<ProductoDto> getProductoById(Integer idProducto) {
        return webClient.get()
                .uri("/productos/{id}", idProducto)
                .retrieve()
                .bodyToMono(ProductoDto.class);
    }
}
