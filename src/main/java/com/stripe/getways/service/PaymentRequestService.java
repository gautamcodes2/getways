package com.stripe.getways.service;

import com.stripe.exception.StripeException;
import com.stripe.getways.dto.input.PaymentRequest;
import com.stripe.getways.dto.output.PaymentRecord;
import com.stripe.getways.mapper.PaymentRecordMapper;
import com.stripe.getways.model.PaymentSessionEntity;
import com.stripe.getways.repository.PaymentSessionRepository;
import com.stripe.getways.service.repository.PaymentSessionRepositoryService;
import com.stripe.getways.stripe.StripeClient;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentRequestService {
    private final PaymentSessionRepositoryService paymentSessionRepositoryService;
    private final PaymentSessionRepository paymentSessionRepository;
    private final StripeClient stripeClient;

    public PaymentRequestService(PaymentSessionRepositoryService paymentSessionRepositoryService,
                                 PaymentSessionRepository paymentSessionRepository, PaymentSessionRepository paymentSessionRepository1,
                                 StripeClient stripeClient) {
        this.paymentSessionRepositoryService = paymentSessionRepositoryService;
        this.paymentSessionRepository = paymentSessionRepository1;
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

    public Page<PaymentRecord> getPayments(int page, int size, String username, String email) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PaymentSessionEntity> entityPage;

        // Determine which repository method to call
        boolean hasUsername = username != null && !username.isBlank();
        boolean hasEmail = email != null && !email.isBlank();

        if (hasUsername && hasEmail) {
            entityPage = paymentSessionRepository
                    .findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(username, email, pageable);
        } else if (hasUsername) {
            entityPage = paymentSessionRepository.findByUsernameContainingIgnoreCase(username, pageable);
        } else if (hasEmail) {
            entityPage = paymentSessionRepository.findByEmailContainingIgnoreCase(email, pageable);
        } else {
            entityPage = paymentSessionRepository.findAll(pageable);
        }

        return entityPage.map(PaymentRecordMapper::toDto);
    }
}