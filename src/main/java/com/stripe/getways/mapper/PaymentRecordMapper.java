package com.stripe.getways.mapper;

import com.stripe.getways.dto.output.PaymentRecord;
import com.stripe.getways.model.PaymentSessionEntity;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class PaymentRecordMapper {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private PaymentRecordMapper() {
        // prevent instantiation
    }

    public static PaymentRecord toDto(PaymentSessionEntity entity) {
        if (entity == null) {
            return null;
        }

        return new PaymentRecord(
                        entity.getOrderId(),
                        entity.getEmail(),
                        entity.getUsername(),
                        BigDecimal.valueOf(entity.getAmount()).movePointLeft(2),
                        entity.getPaymentStatus(),
                        false,
                        entity.getNotes(),
                        entity.getMessage(),
                        entity.getCreatedAt().toString());
    }
}

