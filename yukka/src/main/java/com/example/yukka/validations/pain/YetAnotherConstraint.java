package com.example.yukka.validations.pain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YetAnotherValidator.class)
public @interface YetAnotherConstraint {
    String message() default "Pozycja rośliny musi być w przydzielonych kafelkach";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}