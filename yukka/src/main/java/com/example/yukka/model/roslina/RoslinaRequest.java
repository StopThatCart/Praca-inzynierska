package com.example.yukka.model.roslina;

import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ToString
public class RoslinaRequest extends UzytkownikRoslinaRequest{
    @NotEmpty(message = "Nazwa łacińska jest wymagana")
    private String nazwaLacinska;

    public String getNazwaLacinska() {
        if (nazwaLacinska == null || nazwaLacinska.isEmpty()) {
            return nazwaLacinska;
        }
        return nazwaLacinska.toLowerCase();
    }
    
}
