package ia.code.order_service.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private Integer idCart;
    private Integer idUsuario;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<CartItemRequest> items;
    private Double total;
}
