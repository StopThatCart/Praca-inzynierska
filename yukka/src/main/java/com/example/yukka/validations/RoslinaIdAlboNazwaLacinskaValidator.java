package com.example.yukka.validations;

import java.lang.reflect.Field;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


// Trochę to skomplikowane, ale nie chciałem robić osobnego walidatora dla każdego requesta
public class RoslinaIdAlboNazwaLacinskaValidator implements ConstraintValidator<ValidRoslinaIdAlboNazwaLacinska, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value == null) {
            return false;
        }
        try {
            Field roslinaIdField = value.getClass().getDeclaredField("roslinaId");
            Field nazwaLacinskaField = value.getClass().getDeclaredField("nazwaLacinska");

            roslinaIdField.setAccessible(true);
            nazwaLacinskaField.setAccessible(true);

            String uzytkownikRoslinaId = (String) roslinaIdField.get(value);
            String nazwaLacinskaRosliny = (String) nazwaLacinskaField.get(value);

            return uzytkownikRoslinaId != null || nazwaLacinskaRosliny != null;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }

}
