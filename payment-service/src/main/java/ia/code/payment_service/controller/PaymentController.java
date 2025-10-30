package ia.code.payment_service.controller;

import ia.code.payment_service.entity.Enum.PaymentMethod;
import ia.code.payment_service.entity.Payment;
import ia.code.payment_service.entity.dto.PaymentRequest;
import ia.code.payment_service.entity.dto.PaymentResponse;
import ia.code.payment_service.usecase.PaymentUseCase;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentUseCase paymentService;
    private final ModelMapper modelMapper;

    @PostMapping("/pay")
    public ResponseEntity<?> Payment(@RequestBody PaymentRequest request) {
        Payment payment = modelMapper.map(request, Payment.class);
        Payment init = paymentService.initPayment(payment);
        PaymentResponse result = modelMapper.map(paymentService.processPayment(init), PaymentResponse.class);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getDetailsByID(@PathVariable Integer id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(modelMapper.map(payment, PaymentResponse.class));
    }
    @GetMapping("/order/{id}")
    public ResponseEntity<PaymentResponse> getDetailsByOrderID(@PathVariable Integer id) {
        Payment payment = paymentService.getPaymentByOrderId(id);
        return ResponseEntity.ok(modelMapper.map(payment, PaymentResponse.class));
    }
}
