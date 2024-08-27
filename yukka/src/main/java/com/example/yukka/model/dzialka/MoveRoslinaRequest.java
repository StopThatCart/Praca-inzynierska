package com.example.yukka.model.dzialka;

import com.example.yukka.validations.ValidRoslinaIdAlboNazwaLacinska;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@ValidRoslinaIdAlboNazwaLacinska
public class MoveRoslinaRequest {
    private String roslinaId;
    private String nazwaLacinska;

    @NotEmpty(message = "Co...?")
    @Min(value = 1, message = "Numer działki musi być > 0")
    @Max(value = 10, message = "Numer działki musi być <= 10")
    private int numerDzialkiStary;

    // Może być null jak nie zmienia się działki
    @Min(value = 1, message = "Numer działki musi być > 0")
    @Max(value = 10, message = "Numer działki musi być <= 10")
    private int numerDzialkiNowy;

    @NotEmpty(message = "Pozycja x jest wymagana")
    @Min(value = 0, message = "Pozycja starego x musi być > 0")
    @Max(value = 20, message = "Pozycja starego x musi być <= 20")
    private int xStary;

    @NotEmpty(message = "Pozycja y jest wymagana")
    @Min(value = 1, message = "Pozycja starego y musi być > 0")
    @Max(value = 20, message = "Pozycja starego y musi być <= 20")
    private int yStary;

    @NotEmpty(message = "Nowa pozycja x jest wymagana")
    @Min(value = 1, message = "Pozycja x musi być > 0")
    @Max(value = 20, message = "Pozycja x musi być <= 20")
    private int xNowy;

    @NotEmpty(message = "Nowa pozycja y jest wymagana")
    @Min(value = 1, message = "Pozycja y musi być > 0")
    @Max(value = 20, message = "Pozycja y musi być <= 20")
    private int yNowy;

}
