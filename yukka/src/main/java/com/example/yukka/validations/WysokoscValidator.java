package com.example.yukka.validations;

import org.springframework.beans.factory.annotation.Value;

import com.example.yukka.model.roslina.RoslinaRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WysokoscValidator implements ConstraintValidator<ValidWysokosc, RoslinaRequest> {

    @Value("#{T(Float).parseFloat('${roslina.max.height}')}")
    private float wysokoscLimit;

    @Override
    public void initialize(ValidWysokosc constraintAnnotation) {
    }

    @Override
    public boolean isValid(RoslinaRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return false; 
        }
        
        Double min = request.getWysokoscMin();
        Double max = request.getWysokoscMax();
        
        boolean areHeightsValid = min >= 0 && max >= 0 && max >= min;
        boolean areHeightsWithinLimit = min <= wysokoscLimit && max <= wysokoscLimit;
        
        return areHeightsValid && areHeightsWithinLimit;
    }
}