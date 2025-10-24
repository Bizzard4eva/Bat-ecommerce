package ia.code.order_service.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRequest {

    private Integer idUsuario;
    private List<DetallePedidoRequest> productos;
}
