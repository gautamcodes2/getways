package com.stripe.getways.repository;

import com.stripe.getways.model.PaymentSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentSessionRepository extends JpaRepository<PaymentSessionEntity, UUID>, QueryByExampleExecutor<PaymentSessionEntity> {

    Optional<PaymentSessionEntity> findByOrderId(String orderId);

    Optional<PaymentSessionEntity> findBySessionId(String sessionId);

    Optional<PaymentSessionEntity> findByIntentId(String intentId);

    Optional<PaymentSessionEntity> findByChargeId(String intentId);
}
