package ia.code.catalog_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("producto")
public class Producto {

    @Id
    @Column("id_producto")
    private Integer id;
    private String nombre;
    private Double precio;
    private String descripcion;
    private Integer stock;

    @Column("id_categoria")
    private Integer idCategoria;

    @Column("id_marca")
    private Integer idMarca;

    @Column("id_img")
    private Integer idImg;

}
