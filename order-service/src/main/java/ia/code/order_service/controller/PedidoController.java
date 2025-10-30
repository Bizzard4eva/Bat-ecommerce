package ia.code.order_service.controller;

import ia.code.order_service.entity.Pedido;
import ia.code.order_service.entity.dto.EstadoPedidoRequest;
import ia.code.order_service.entity.dto.PedidoRequest;
import ia.code.order_service.entity.dto.PedidoResponse;
import ia.code.order_service.service.PedidoService;
import ia.code.order_service.usecase.PedidoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // Crear pedido
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PedidoResponse> crearPedido(@RequestHeader("X-User-Id") Integer id,
            @RequestBody PedidoRequest pedidoRequest) {  //
        return pedidoService.crearPedido(id, pedidoRequest);
    }

    // Listar pedidos del usuario
    @GetMapping
    public Flux<PedidoResponse> listarPedidosUsuario(@RequestHeader("X-User-Id") Integer idUsuario) {
        return pedidoService.listarPedidosPorUsuario(idUsuario);
    }

    // 3️⃣ Obtener pedido por ID
    @GetMapping("/{id_pedido}")
    public Mono<PedidoResponse> obtenerPedidoPorId(@PathVariable Integer id_pedido) {
        return pedidoService.obtenerPedidoPorId(id_pedido);
    }

    // 4️⃣ Listar todos los pedidos (admin)
    @GetMapping("/todos")
    public Flux<PedidoResponse> listarTodosLosPedidos(@RequestHeader("X-User-Id") Integer idUsuario) {
        if (idUsuario != 1) { // solo el usuario 1 puede ser admin
            return Flux.error(new RuntimeException("No autorizado"));
        }
        return pedidoService.listarTodosLosPedidos();
    }

    // 5️⃣ Actualizar estado de un pedido (admin)
    @PutMapping("/{idPedido}/estado")
    public Mono<PedidoResponse> actualizarEstado(@PathVariable Integer idPedido,
                                                 @RequestBody EstadoPedidoRequest estadoRequest,
                                                 @RequestHeader("X-User-Id") Integer idUsuario) {
        // Solo el admin (por ejemplo, id 1) puede actualizar
        if (idUsuario != 1) {
            return Mono.error(new RuntimeException("No autorizado"));
        }
        return pedidoService.actualizarEstadoPedido(idPedido, estadoRequest);
    }
}
