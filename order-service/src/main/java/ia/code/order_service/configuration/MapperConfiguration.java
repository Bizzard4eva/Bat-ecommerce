package ia.code.order_service.configuration;

import ia.code.order_service.entity.Pedido;
import ia.code.order_service.entity.dto.DetallePedidoResponse;
import ia.code.order_service.entity.dto.PedidoRequest;
import ia.code.order_service.entity.dto.PedidoResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MapperConfiguration {

    public static Pedido toEntity(PedidoRequest request) {
        Pedido pedido = new Pedido();
        pedido.setIdUsuario(request.getIdUsuario());
        pedido.setEstado("PENDIENTE");
        pedido.setFecha(LocalDateTime.now());
        pedido.setTotal(0.0);
        return pedido;
    }

    public static PedidoResponse toResponse(Pedido pedido) {
        // Los detalles se agregan después en el service
        PedidoResponse response = new PedidoResponse();
        response.setIdPedido(pedido.getIdPedido());
        response.setIdUsuario(pedido.getIdUsuario());
        response.setFecha(pedido.getFecha());
        response.setEstado(pedido.getEstado());
        response.setTotal(pedido.getTotal());
        response.setDetalles(null); // o List.of(), se llenará luego
        return response;
    }

    // (Opcional) Convierte lista de detalles
    public static List<DetallePedidoResponse> mapDetalles(List<DetallePedidoResponse> detalles) {
        return detalles != null ? detalles : List.of();
    }
}