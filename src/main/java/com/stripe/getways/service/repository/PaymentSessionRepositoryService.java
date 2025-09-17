package com.stripe.getways.service.repository;

import com.stripe.getways.dto.input.PaymentRequest;
import com.stripe.getways.model.EntityUtils;
import com.stripe.getways.model.PaymentSessionEntity;
import com.stripe.getways.repository.PaymentSessionRepository;
import com.stripe.getways.utility.JsonUtils;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeError;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentSessionRepositoryService {

    private final PaymentSessionRepository paymentSessionRepository;

    public PaymentSessionRepositoryService(PaymentSessionRepository paymentSessionRepository) {
        this.paymentSessionRepository = paymentSessionRepository;
    }

    @Transactional
    public PaymentSessionEntity persistSession(Session session, @Valid PaymentRequest requestInput) {

        // Extract fields from request input
        final String orderId = requestInput.getOrderId();
        final String notes = requestInput.getNotes();
        final String username = requestInput.getUsername();

        // Extract fields from Stripe session
        final String sessionId = session.getId();
        final Long amount = session.getAmountTotal();
        final String currency = session.getCurrency();
        final String customerId = session.getCustomer();
        final String customerEmail = session.getCustomerEmail();
        final String paymentStatus = session.getPaymentStatus();
        final String sessionStatus = session.getStatus();
        final String sessionUrl = session.getUrl();

        // Build the entity
        PaymentSessionEntity sessionEntity = PaymentSessionEntity.builder()
                .orderId(orderId)
                .sessionId(sessionId)
                .customerId(customerId)
                .email(customerEmail)
                .intentStatus(null) // initially null, will be updated later
                .paymentStatus(paymentStatus)
                .sessionStatus(sessionStatus)
                .sessionUrl(sessionUrl)
                .amount(amount)
                .currency(currency)
                .message(null) // initially null
                .username(username)
                .notes(notes)
                .build();

        // Persist and return
        return paymentSessionRepository.save(sessionEntity);
    }

    @Transactional(rollbackOn = Exception.class)
    public PaymentSessionEntity persistSession(Session session) {
        String sessionId = session.getId();
        String intentId = session.getPaymentIntent();
        String paymentStatus = session.getPaymentStatus();
        String sessionStatus = session.getStatus();
        String sessionUrl = Optional.ofNullable(session.getUrl())
                .orElse("Not Available, Session completed");

        return paymentSessionRepository.findBySessionId(sessionId)
                .map(entity -> {
                    entity.setSessionUrl(sessionUrl);
                    entity.setPaymentStatus(paymentStatus);
                    entity.setSessionStatus(sessionStatus);
                    EntityUtils.setIfAbsent(entity::getIntentId, entity::setIntentId, intentId);
                    return paymentSessionRepository.save(entity); // ensures flush and returns updated entity
                })
                .orElse(null);
    }

    @Transactional
    public void persistPaymentIntent(PaymentIntent intent) {
        String intentId = intent.getId();
        String chargeId = intent.getLatestCharge();
        String orderId = intent.getMetadata().get("orderId");

        // 🔎 Null-safe repository lookup
        PaymentSessionEntity entity = Optional.ofNullable(intentId)
                .flatMap(paymentSessionRepository::findByIntentId)
                .or(() -> Optional.ofNullable(chargeId)
                        .flatMap(paymentSessionRepository::findByChargeId))
                .or(() -> Optional.ofNullable(orderId)
                        .flatMap(paymentSessionRepository::findByOrderId))
                .orElse(PaymentSessionEntity.builder().build());

        // ⏹ Early return if already succeeded
        if ("succeeded".equals(entity.getIntentStatus())) {
            return;
        }

        // 📝 Set identifiers only if missing
        EntityUtils.setIfAbsent(entity::getOrderId, entity::setOrderId, orderId);
        EntityUtils.setIfAbsent(entity::getIntentId, entity::setIntentId, intentId);
        EntityUtils.setIfAbsent(entity::getChargeId, entity::setChargeId, chargeId);

        // 💡 Update status & timestamps
        String status = intent.getStatus();
        entity.setIntentStatus(status);

        if ("succeeded".equals(status) || "canceled".equals(status)) {
            entity.setCompletedAt(OffsetDateTime.now(ZoneOffset.UTC));
        }

        // 💬 Update message
        if ("succeeded".equals(status)) {
            entity.setMessage("The payment succeeded");
        } else {
            Optional.ofNullable(intent.getLastPaymentError())
                    .map(StripeError::getMessage)
                    .ifPresent(entity::setMessage);
        }

        paymentSessionRepository.save(entity);
    }

    @Transactional
    public void persistCharge(Charge charge) {
        String intentId = charge.getPaymentIntent();
        String chargeId = charge.getId();
        String orderId = charge.getMetadata().get("orderId");

        PaymentSessionEntity entity = Optional.ofNullable(intentId)
                .flatMap(paymentSessionRepository::findByIntentId)
                .or(() -> Optional.ofNullable(chargeId)
                        .flatMap(paymentSessionRepository::findByChargeId))
                .or(() -> Optional.ofNullable(orderId)
                        .flatMap(paymentSessionRepository::findByOrderId))
                .orElse(PaymentSessionEntity.builder().build());

        EntityUtils.setIfAbsent(entity::getIntentId, entity::setIntentId, intentId);
        EntityUtils.setIfAbsent(entity::getChargeId, entity::setChargeId, chargeId);
        EntityUtils.setIfAbsent(entity::getOrderId, entity::setOrderId, orderId);

        entity.setChargeStatus(charge.getStatus());
        entity.setIsDisputed(charge.getDisputed());
        entity.setIsRefunded(charge.getRefunded());
        entity.setAmountRefunded(charge.getAmountRefunded());

        Optional.ofNullable(charge.getPaymentMethodDetails())
                .map(StripeObject::toJson)
                .map(JsonUtils::toCleanMap)
                .ifPresent(entity::setPaymentMethods);

        paymentSessionRepository.save(entity);
    }

    public List<PaymentSessionEntity> findAll() {
        return paymentSessionRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
