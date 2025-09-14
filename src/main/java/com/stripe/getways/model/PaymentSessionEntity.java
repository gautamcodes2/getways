package com.stripe.getways.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;

@Entity
@Table(name = "payment_session")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PaymentSessionEntity extends BaseEntity {

    @JdbcTypeCode(SqlTypes.JSON)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Object> paymentMethods;
    private String orderId;
    private String sessionId;
    private String intentId;
    private String customerId;
    private String intentStatus;
    private String paymentStatus;
    private String sessionStatus;
    private String sessionUrl;
    private Long amount;
    private String currency;
    private OffsetDateTime completedAt;
    private String email;
    private String username;
    private String message;
    private String notes;
    private String chargeId;
    private String chargeStatus;
    private Boolean isDisputed;
    private Boolean isRefunded;
    private Long amountRefunded;
}
