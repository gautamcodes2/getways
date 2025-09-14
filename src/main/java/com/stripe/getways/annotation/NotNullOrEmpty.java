package com.stripe.getways.annotation;

import com.stripe.getways.annotation.validator.NotNullOrEmptyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotNullOrEmptyValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullOrEmpty {
    String message() default "Field must not be null, empty, or blank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
