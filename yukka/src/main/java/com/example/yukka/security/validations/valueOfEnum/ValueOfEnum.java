package com.example.yukka.security.validations.valueOfEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Adnotacja do walidacji, która sprawdza, czy wartość jest jedną z wartości określonego typu wyliczeniowego.
 *
 * <ul>
 *   <li><strong>enumClass</strong>: Klasa wyliczeniowa, której wartości mają być sprawdzane.</li>
 *   <li><strong>message</strong>: Wiadomość błędu, która ma być zwrócona, jeśli wartość nie jest jedną z wartości wyliczeniowych. Domyślnie "must be any of enum {enumClass}".</li>
 *   <li><strong>groups</strong>: Grupy walidacyjne, do których należy ta adnotacja.</li>
 *   <li><strong>payload</strong>: Dodatkowe informacje o ładunku dla adnotacji walidacyjnych.</li>
 * </ul>
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValueOfEnumValidator.class)
public @interface ValueOfEnum {
    Class<? extends Enum<?>> enumClass();
    String message() default "must be any of enum {enumClass}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
