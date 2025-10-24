package ia.code.order_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("detalle_pedido")
public class DetallePedido {

    @Id
    private Integer id_detalle;
    private Integer id_pedido;
    private Integer id_producto;
    private Integer cantidad;
    private Double subtotal;
}
