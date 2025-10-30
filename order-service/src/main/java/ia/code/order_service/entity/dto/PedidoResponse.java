package ia.code.order_service.entity.dto;

import ia.code.order_service.entity.DetallePedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponse {

    private Integer idPedido;
    private Integer idUsuario;
    private Double total;
    private LocalDateTime fecha;
    private String estado;
    private List<DetallePedidoResponse> detalles;
}
