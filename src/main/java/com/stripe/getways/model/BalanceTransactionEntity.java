package com.stripe.getways.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "balance_transaction")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BalanceTransactionEntity extends BaseEntity {
    private String balanceTransactionId;
    private String chargeId;
    private Long paymentAmount;
    private Long stripeFee;
    private Long netAmount;
    private OffsetDateTime completedAt;
}
