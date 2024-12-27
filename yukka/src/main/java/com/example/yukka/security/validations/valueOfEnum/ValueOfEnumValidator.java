package com.example.yukka.security.validations.valueOfEnum;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Klasa <strong>ValueOfEnumValidator</strong> implementuje interfejs <strong>ConstraintValidator</strong> 
 * i służy do walidacji, czy wartość należy do określonego wyliczenia (enum).
 * 
 * <ul>
 *   <li><strong>acceptedValues</strong> - lista akceptowanych wartości wyliczenia (enum).</li>
 * </ul>
 * 
 * <strong>Metody:</strong>
 * <ul>
 *   <li><strong>initialize(ValueOfEnum annotation)</strong> - inicjalizuje walidator, pobierając wartości wyliczenia (enum) z adnotacji.</li>
 *   <li><strong>isValid(CharSequence value, ConstraintValidatorContext context)</strong> - sprawdza, czy wartość jest zgodna z akceptowanymi wartościami wyliczenia (enum).</li>
 * </ul>
 */
public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence>  {
    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return acceptedValues.contains(value.toString());
    }
}
