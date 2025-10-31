package ia.code.payment_service.service;

import ia.code.payment_service.entity.Enum.PaymentStatus;
import ia.code.payment_service.entity.Payment;
import ia.code.payment_service.entity.dto.PaymentResponse;
import ia.code.payment_service.repository.PaymentRepository;
import ia.code.payment_service.usecase.PaymentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final WebClient.Builder webClientBuilder;


    @Override
    public Payment initPayment(Payment payment) {
        Payment pago = new Payment();
        pago.setOrdenId(payment.getOrdenId());
        pago.setMonto(payment.getMonto());
        pago.setMethod(payment.getMethod());
        pago.setStatus(PaymentStatus.PENDIENTE);
        pago.setFechaPago(LocalDateTime.now());
        pago.setTransaccionId(UUID.randomUUID().toString());
        return paymentRepository.save(pago);
    }

    @Override
    public Payment processPayment(Payment payment) {
        //Simulamos la transaccion
        boolean success = Math.random() < 0.8;
        payment.setStatus(success ? PaymentStatus.APROBADO : PaymentStatus.DECLINADO);
        Payment pago = paymentRepository.save(payment);
            // TODO
        if(success) {
            try {
                webClientBuilder.build()
                        .put()
                        .uri("http://order-service/orders/internal/{idPedido}/status", payment.getOrdenId())
                        .bodyValue(Map.of("estado", "PAGADO"))
                        .retrieve()
                        .toBodilessEntity()
                        .block();
            } catch (Exception e) {
                System.out.println("Error actualizando pedido: " + e.getMessage());
            }
        }
        return pago;
    }

    @Override
    public Payment getPaymentById(Integer id) {
        return paymentRepository.findById(id).orElse(null);
    }

    @Override
    public Payment getPaymentByOrderId(Integer id) {
        return paymentRepository.findByOrdenId(id);
    }
}
