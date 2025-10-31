package ia.code.order_service.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {
    private Integer idItem;
    private Integer idProducto;
    private String nombreProducto;
    private Double precio;
    private Integer cantidad;
    private Double subtotal;
}
