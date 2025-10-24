package ia.code.catalog_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("producto")
public class Producto {

    @Id
    private Integer id_producto;
    private String nombre;
    private Double precio;
    private String descripcion;
    private Integer stock;
    private Integer id_categoria;

}
