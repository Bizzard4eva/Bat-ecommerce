package ia.code.cart_service.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCarritoDto {
    private Integer id_item;
    private Integer idProducto;
    private String nombreProducto;
    private Double precio;
    private Integer cantidad;
    private Double subtotal;
    private String imagenUrl;
}
