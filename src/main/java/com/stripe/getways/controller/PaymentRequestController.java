package com.stripe.getways.controller;

import com.stripe.exception.StripeException;
import com.stripe.getways.dto.input.PaymentRequest;
import com.stripe.getways.model.PaymentSessionEntity;
import com.stripe.getways.service.PaymentRequestService;
import com.stripe.getways.utility.PaymentRequestInputTestUtil;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@Data
public class PaymentRequestController {

    private final PaymentRequestService paymentRequestService;

    public PaymentRequestController(PaymentRequestService paymentRequestService) {
        this.paymentRequestService = paymentRequestService;
    }

    @PostMapping("/request")
    public ResponseEntity<Map<String, String>> createCheckoutSession(
            @Valid @RequestBody PaymentRequest requestInput
    ) {
        try {
            PaymentSessionEntity checkoutSession = paymentRequestService.createPaymentSession(requestInput);
            return ResponseEntity.ok(Map.of("sessionUrl", checkoutSession.getSessionUrl()));
        } catch (StripeException e) {
            String errorMessage = String.format(
                    "Error occurred with your order %s. Details: %s",
                    requestInput.getOrderId(),
                    e.getMessage()
            );
            return ResponseEntity.ok(Map.of("error", errorMessage));
        }
    }

    @PostMapping("/test")
    public ResponseEntity<Map<String, String>> createCheckoutSessionTest() {
        PaymentRequest testInput = PaymentRequestInputTestUtil.randomInput();
        try {
            PaymentSessionEntity checkoutSession = paymentRequestService.createPaymentSession(testInput);
            return ResponseEntity.ok(Map.of("sessionUrl", checkoutSession.getSessionUrl()));
        } catch (StripeException e) {
            String errorMessage = String.format(
                    "Error occurred with your order %s. Details: %s",
                    testInput.getOrderId(),
                    e.getMessage()
            );
            return ResponseEntity.ok(Map.of("error", errorMessage));
        }
    }
}

