package com.stripe.getways.service.repository;

import com.stripe.getways.model.BalanceTransactionEntity;
import com.stripe.getways.repository.BalanceTransactionRepository;
import com.stripe.model.BalanceTransaction;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;

@Service
public class BalanceTransactionRepositoryService {

    private final BalanceTransactionRepository balanceTransactionRepository;

    public BalanceTransactionRepositoryService(BalanceTransactionRepository balanceTransactionRepository) {
        this.balanceTransactionRepository = balanceTransactionRepository;
    }

    @Transactional
    public void persistBalanceTransaction(BalanceTransaction balanceTransaction) {
        BalanceTransactionEntity entity = balanceTransactionRepository
                .findByBalanceTransactionId(balanceTransaction.getId())
                .orElseGet(() -> BalanceTransactionEntity.builder()
                        .balanceTransactionId(balanceTransaction.getId())
                        .build());

        entity.setChargeId(balanceTransaction.getSource());
        entity.setStripeFee(balanceTransaction.getFee());
        entity.setNetAmount(balanceTransaction.getNet());
        entity.setPaymentAmount(balanceTransaction.getAmount());
        entity.setCompletedAt(
                Instant.ofEpochSecond(balanceTransaction.getAvailableOn()).atOffset(ZoneOffset.UTC)
        );

        balanceTransactionRepository.save(entity);
    }
}