package ia.code.order_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("detalle_pedido")
public class DetallePedido {

    @Id
    @Column("id_detalle")
    private Integer idDetalle;
    @Column("id_pedido")
    private Integer idPedido;
    @Column("id_producto")
    private Integer idProducto;
    @Column("nombre_producto")
    private String nombreProducto;
    private Integer cantidad;
    private Double subtotal;
}
