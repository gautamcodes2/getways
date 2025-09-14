package com.stripe.getways.utility;

import com.stripe.getways.dto.input.PaymentRequest;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PaymentRequestInputTestUtil {

    public static PaymentRequest randomInput() {
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        String email = "user" + ThreadLocalRandom.current().nextInt(1000, 9999) + "@gmail.com";
        BigDecimal amount = BigDecimal.valueOf(
                ThreadLocalRandom.current().nextDouble(1.01, 100.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
        String username = "user" + ThreadLocalRandom.current().nextInt(100, 999);
        String notes = "Test order note " + ThreadLocalRandom.current().nextInt(1, 100);

        return new PaymentRequest(orderId, email, amount, username, notes);
    }
}

