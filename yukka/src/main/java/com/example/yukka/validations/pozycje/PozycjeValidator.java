package com.example.yukka.validations.pozycje;



import com.example.yukka.model.dzialka.Pozycja;
import com.example.yukka.model.dzialka.requests.BaseDzialkaRequest;
import com.example.yukka.model.dzialka.requests.DzialkaRoslinaRequest;
import com.example.yukka.model.dzialka.requests.MoveRoslinaRequest;
import com.example.yukka.validations.ValidWysokosc;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PozycjeValidator implements ConstraintValidator<ValidPozycje, Object> {

    @Override
    public void initialize(ValidPozycje constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object request, ConstraintValidatorContext context) {
        if (request == null) {
            return false;
        }
        System.out.println("Sprawdzam pozycje");
        if (request instanceof MoveRoslinaRequest) {
            return isValidMoveRoslinaRequest((MoveRoslinaRequest) request);
        } else if (request instanceof BaseDzialkaRequest) {
            return isValidDzialkaRoslinaRequest((BaseDzialkaRequest) request);
        }

        return false;
    
    }

    private boolean isValidDzialkaRoslinaRequest(BaseDzialkaRequest request) {
        if (request.getX() == null || request.getY() == null) {
            System.out.println("Pozycja x i y nie może być null");
            return false;
        }
        Pozycja pos = Pozycja.builder().x(request.getX()).y(request.getY()).build();

        return request.getPozycje().contains(pos);
    }

    private boolean isValidMoveRoslinaRequest(MoveRoslinaRequest request) {
        if (request.getXNowy() == null || request.getYNowy() == null) {
            return false;
        }
        Pozycja pos = Pozycja.builder().x(request.getXNowy()).y(request.getYNowy()).build();
        // System.out.println("Pozycja original: " + pos);
        // for (Pozycja p : request.getPozycje()) {
        //     System.out.println("Pozycja: " + p);
        //     System.out.println(p.equals(pos));
        // }
        return request.getPozycje().contains(pos);
    }

}