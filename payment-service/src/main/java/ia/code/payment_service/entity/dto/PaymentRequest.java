package ia.code.payment_service.entity.dto;

import ia.code.payment_service.entity.Enum.PaymentMethod;
import ia.code.payment_service.entity.Enum.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    private Integer ordenId;
    private Double monto;
    private String method;
}
