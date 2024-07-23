package com.railansantana.e_commerce.resource.payment;

import com.railansantana.e_commerce.services.payment.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@AllArgsConstructor
public class PaymentResource {
    private final PaymentService paymentService;
    @PostMapping("/{userId}")
    public ResponseEntity<Void> payment(@PathVariable String userId) {
        paymentService.payment(userId);
        return ResponseEntity.noContent().build();
    }
}
