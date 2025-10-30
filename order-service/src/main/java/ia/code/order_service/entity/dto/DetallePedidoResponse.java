package ia.code.order_service.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallePedidoResponse {

    private Integer id_detalle;
    private Integer id_producto;
    private Integer cantidad;
    private Double subtotal;
}
