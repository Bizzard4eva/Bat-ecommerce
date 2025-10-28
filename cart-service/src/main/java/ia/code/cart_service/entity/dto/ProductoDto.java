package ia.code.cart_service.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDto {
    private Integer id_producto;
    private String nombre;
    private Double precio;
    private String descripcion;
    private String nombreCategoria;
    private String nombreMarca;
    private String urlImagen;
}
