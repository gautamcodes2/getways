package com.stripe.getways.annotation.validator;

import com.stripe.getways.annotation.NotNullOrEmpty;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;

public class NotNullOrEmptyValidator implements ConstraintValidator<NotNullOrEmpty, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false; // null is invalid
        }

        if (value instanceof String str) {
            return !str.trim().isEmpty(); // non-empty string
        }

        if (value instanceof Collection<?> col) {
            return !col.isEmpty(); // non-empty collection
        }

        if (value instanceof Number) {
            return true; // not null is enough; optional: check >0
        }

        // Other objects → only check null
        return true;
    }
}
