package com.stripe.getways.service;

import com.stripe.exception.StripeException;
import com.stripe.getways.dto.input.PaymentRequest;
import com.stripe.getways.dto.output.PaymentRecord;
import com.stripe.getways.model.PaymentSessionEntity;
import com.stripe.getways.repository.PaymentSessionRepository;
import com.stripe.getways.service.repository.PaymentSessionRepositoryService;
import com.stripe.getways.stripe.StripeClient;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentRequestService {
    private final PaymentSessionRepositoryService paymentSessionRepositoryService;
    private final StripeClient stripeClient;

    public PaymentRequestService(PaymentSessionRepositoryService paymentSessionRepositoryService,
                                 PaymentSessionRepository paymentSessionRepository,
                                 StripeClient stripeClient) {
        this.paymentSessionRepositoryService = paymentSessionRepositoryService;
        this.stripeClient = stripeClient;
    }

    public PaymentSessionEntity createPaymentSession(@Valid PaymentRequest requestInput) throws StripeException {
        var checkoutSession = stripeClient.createCheckout(requestInput);
        return paymentSessionRepositoryService
                .persistSession(checkoutSession, requestInput);
    }

    public List<PaymentRecord> getAllPaymentRecords() {
        return paymentSessionRepositoryService.findAll()
                .stream()
                .map(entity -> new PaymentRecord(
                        entity.getOrderId(),
                        entity.getEmail(),
                        entity.getUsername(),
                        BigDecimal.valueOf(entity.getAmount()).movePointLeft(2),
                        entity.getPaymentStatus(),
                        false,
                        entity.getNotes(),
                        entity.getMessage(),
                        entity.getCreatedAt().toString()
                ))
                .toList();
    }
}
