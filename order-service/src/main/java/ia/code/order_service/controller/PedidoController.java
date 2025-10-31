package ia.code.order_service.controller;

import ia.code.order_service.entity.dto.PedidoResponse;
import ia.code.order_service.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PedidoResponse> crearPedido(@RequestHeader("X-User-Id") Integer idUsuario) {
        return pedidoService.crearPedido(idUsuario);
    }

    @GetMapping
    public Flux<PedidoResponse> listarPedidosUsuario(@RequestHeader("X-User-Id") Integer idUsuario) {
        return pedidoService.listarPedidosPorUsuario(idUsuario);
    }

    @GetMapping("/all")
    public Flux<PedidoResponse> listarTodosLosPedidos(@RequestHeader("X-User-Id") Integer idUsuario) {
        // Validación simple de admin (usuario 1)
        if (!idUsuario.equals(1)) {
            return Flux.error(new RuntimeException("No autorizado: Solo administradores pueden ver todos los pedidos"));
        }
        return pedidoService.listarTodosLosPedidos();
    }

    @PutMapping("/{idPedido}/status")
    public Mono<PedidoResponse> actualizarEstado(
            @PathVariable Integer idPedido,
            @RequestBody Map<String, String> estadoRequest,
            @RequestHeader("X-User-Id") Integer idUsuario) {

        // Validación de admin
        if (!idUsuario.equals(1)) {
            return Mono.error(new RuntimeException("No autorizado: Solo administradores pueden actualizar estados"));
        }

        String nuevoEstado = estadoRequest.get("estado");
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            return Mono.error(new RuntimeException("El campo 'estado' es requerido"));
        }

        return pedidoService.actualizarEstadoPedido(idPedido, nuevoEstado);
    }
}
