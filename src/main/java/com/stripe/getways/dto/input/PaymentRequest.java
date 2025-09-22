package com.stripe.getways.dto.input;

import com.stripe.getways.annotation.NotNullOrEmpty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentRequest {
    @NotNullOrEmpty
    private String orderId;

    @NotNullOrEmpty
    private String email;

    @NotNullOrEmpty
    private BigDecimal amount;

    @NotNullOrEmpty
    private String username;

    private final String notes;

    @NotNullOrEmpty
    private String successUrl;

    @NotNullOrEmpty
    private String failedUrl;

    public PaymentRequest(String orderId, String email, BigDecimal amount, String username, String notes, String successUrl, String failedUrl) {
        this.orderId = orderId;
        this.email = email;
        this.amount = amount;
        this.username = username;
        this.notes = notes;
        this.successUrl = successUrl;
        this.failedUrl = failedUrl;
    }

    public Long getAmount() {
        return amount.multiply(BigDecimal.valueOf(100)).longValueExact();
    }

}

