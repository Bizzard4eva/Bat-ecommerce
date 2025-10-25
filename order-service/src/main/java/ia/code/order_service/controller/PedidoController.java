package ia.code.order_service.controller;

import ia.code.order_service.entity.dto.PedidoRequest;
import ia.code.order_service.entity.dto.PedidoResponse;
import ia.code.order_service.usecase.PedidoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoUseCase pedidoUseCase;

    @PostMapping
    public Mono<ResponseEntity<PedidoResponse>> addPedido(@RequestBody PedidoRequest pedidoRequest) {
        return pedidoUseCase.createPedido(pedidoRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }
}
