package com.stripe.getways.repository;

import com.stripe.getways.model.BalanceTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BalanceTransactionRepository extends JpaRepository<BalanceTransactionEntity, UUID>, QueryByExampleExecutor<BalanceTransactionEntity> {

    Optional<BalanceTransactionEntity> findByBalanceTransactionId(String balanceTransactionId);
}
