package com.stripe.getways.dto.output;

import java.math.BigDecimal;

public record PaymentRecord(
        String orderId,
        String email,
        String username,
        BigDecimal amount,
        String paymentStatus,
        boolean credited,
        String note,
        String message,
        String receivedAt
) {
    public PaymentRecord(String orderId, String email, String username, BigDecimal amount,
                         String paymentStatus, boolean credited, String note, String message, String receivedAt) {
        this.orderId = orderId;
        this.email = email;
        this.username = username;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.credited = credited;
        this.note = note;
        this.message = message;
        this.receivedAt = receivedAt;
    }
}

