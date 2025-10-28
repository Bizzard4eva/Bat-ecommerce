package ia.code.cart_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("cart_item")
public class CartItem {
    @Id
    private Integer id_item;

    @Column("id_cart")
    private Integer idCart;

    @Column("id_producto")
    private Integer idProducto;

    private Integer cantidad;

    @Column("precio_unitario")
    private Double precioUnitario;

    private Double subtotal;
}
