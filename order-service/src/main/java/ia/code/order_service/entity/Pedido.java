package ia.code.order_service.entity;

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
@Table("pedido")
public class Pedido {

    @Id
    @Column("id_pedido")
    private Integer idPedido;
    @Column("id_usuario")
    private Integer idUsuario;
    private Double total;
    private LocalDateTime fecha;
    private String estado;
}
