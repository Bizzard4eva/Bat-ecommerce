package ia.code.cart_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("item_carrito")
public class ItemCarrito {
    @Id
    @Column("id_item")
    private Integer id;

    @Column("id_carrito")
    private Integer idCarrito;

    @Column("id_producto")
    private Integer idProducto;

    private Integer cantidad;
    @Column("precio_unitario")
    private Double precioUnitario;
    private Double subtotal;

    @Column("fecha_agregado")
    private LocalDateTime fechaAgregado;
}
