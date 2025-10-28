package ia.code.cart_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("cart")
public class Cart {
    @Id
    private Integer id_cart;
    @Column("id_usuario")
    private Integer idUsuario;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_actualizacion;
}
