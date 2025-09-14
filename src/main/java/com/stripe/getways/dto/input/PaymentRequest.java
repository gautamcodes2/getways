package com.stripe.getways.dto.input;

import com.stripe.getways.annotation.NotNullOrEmpty;

import java.math.BigDecimal;

public class PaymentRequest {
    @NotNullOrEmpty
    private String orderId;

    @NotNullOrEmpty
    private String email;

    @NotNullOrEmpty
    private BigDecimal amount;

    @NotNullOrEmpty
    private String username;

    private String notes;

    public PaymentRequest(String orderId, String email, BigDecimal amount, String username, String notes) {
        this.orderId = orderId;
        this.email = email;
        this.amount = amount;
        this.username = username;
        this.notes = notes;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getEmail() {
        return email;
    }

    public Long getAmount() {
        return amount.multiply(BigDecimal.valueOf(100)).longValueExact();
    }

    public String getUsername() {
        return username;
    }

    public String getNotes() {
        return notes;
    }
}

