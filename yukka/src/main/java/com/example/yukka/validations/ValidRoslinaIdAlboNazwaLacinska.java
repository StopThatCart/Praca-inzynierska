package com.example.yukka.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { RoslinaIdAlboNazwaLacinskaValidator.class })
public @interface ValidRoslinaIdAlboNazwaLacinska {
    String message() default "Przynajmniej jedno z pól roślinaId i nazwaLacinska musi być wypełnione";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
