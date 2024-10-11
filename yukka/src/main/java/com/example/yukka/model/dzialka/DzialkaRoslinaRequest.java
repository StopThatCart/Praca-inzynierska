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
public class DzialkaRoslinaRequest {
    private String roslinaId;
    private String nazwaLacinska;

    @NotEmpty(message = "Co...?")
    @Min(value = 1, message = "Numer działki musi być > 0")
    @Max(value = 10, message = "Numer działki musi być <= 10")
    private int numerDzialki;

    @NotEmpty(message = "Pozycja x jest wymagana")
    @Min(value = 1, message = "Pozycja x musi być > 0")
    @Max(value = 20, message = "Pozycja x musi być <= 20")
    private int x;

    @NotEmpty(message = "Pozycja y jest wymagana")
    @Min(value = 1, message = "Pozycja y musi być > 0")
    @Max(value = 20, message = "Pozycja y musi być <= 20")
    private int y;

    @NotEmpty(message = "Kafelki x są wymagane")
    private int[] tabX;

    @NotEmpty(message = "Kafelki y są wymagane")
    private int[] tabY;


    @AssertTrue(message = "Ilość kafelków x i y musi być z przedziału [1, 20] i muszą mieć taką samą długość")
    private boolean tabsWithinRange() {
        boolean xLen = this.tabX.length >= 1 && this.tabX.length <= 20;
        boolean yLen = this.tabY.length >= 1 && this.tabY.length <= 20;

        return xLen && yLen && this.tabX.length == this.tabY.length;
    }

    @AssertTrue(message = "W kafelkach x i y nie mogą się powtarzać współrzędne")
    private boolean noRepeatCombinations() {
        for (int i = 0; i < this.tabX.length; i++) {
            for (int j = i + 1; j < this.tabX.length; j++) {
                if (this.tabX[i] == this.tabX[j] && this.tabY[i] == this.tabY[j]) {
                    return false;
                }
            }
        }
        return true;
    }


    // Null jak nie dopina się żadnego obrazu
    private String obraz;

}
