package ia.code.payment_service.usecase;

import ia.code.payment_service.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public interface PaymentUseCase {
    Payment initPayment(Payment payment);
    Payment processPayment(Payment payment);
    Payment getPaymentById(Integer id);
    Payment getPaymentByOrderId(Integer transactionId);
}
