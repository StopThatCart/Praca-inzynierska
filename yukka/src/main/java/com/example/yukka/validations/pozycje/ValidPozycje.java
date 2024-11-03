package com.example.yukka.validations.pozycje;

import jakarta.validation.Payload;

//@Target({ ElementType.TYPE })
//@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = { PozycjeValidator.class })
public @interface ValidPozycje {
    String message() default "Pozycja rośliny musi być w przydzielonych kafelkach";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

