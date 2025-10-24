package ia.code.catalog_service.entity.dto;

import lombok.Data;

@Data
public class ProductoDto {

    private Integer id_producto;
    private String nombre;
    private Double precio;
    private String nombreCategoria;
}
