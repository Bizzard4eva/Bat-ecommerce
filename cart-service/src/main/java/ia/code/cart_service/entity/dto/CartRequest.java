package ia.code.cart_service.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private Integer idUsuario;
    private Integer idProducto;
    private Integer cantidad;
}
