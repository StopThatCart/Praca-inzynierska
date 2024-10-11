package com.example.yukka.model.dzialka;

import com.example.yukka.validations.ValidRoslinaIdAlboNazwaLacinska;

import jakarta.validation.constraints.AssertTrue;
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


    @NotEmpty(message = "Kafelki x są wymagane")
    private int[] tabX;

    @NotEmpty(message = "Kafelki y są wymagane")
    private int[] tabY;


    @AssertTrue(message = "Ilość kafelków x i y musi być z przedziału [1, 20]")
    private boolean tabsWithinRange() {
        boolean xLen = this.tabX.length >= 1 && this.tabX.length <= 20;
        boolean yLen = this.tabY.length >= 1 && this.tabY.length <= 20;

        return xLen && yLen && this.tabX.length == this.tabY.length;
    }


}
