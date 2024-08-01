package com.example.yukka.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WysokoscValidator.class)
public @interface ValidWysokosc {
    String message() default "Wysokość maksymalna nie może być mniejsza niż wysokość minimalna";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}