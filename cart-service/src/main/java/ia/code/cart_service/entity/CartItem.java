package ia.code.cart_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("cart_item")
public class CartItem {
    @Id
    private Integer id_item;
    private Integer id_cart;
    private Integer id_producto;
    private Integer cantidad;
    private Double precio_unitario;
    private Double subtotal;
}
