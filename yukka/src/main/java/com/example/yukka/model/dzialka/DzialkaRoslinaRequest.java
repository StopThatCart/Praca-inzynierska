package com.example.yukka.model.dzialka;
import java.util.List;

import com.example.yukka.validations.ValidRoslinaIdAlboNazwaLacinska;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @Min(value = 1, message = "Numer działki musi być > 0")
    @Max(value = 10, message = "Numer działki musi być <= 10")
    private int numerDzialki;

    @Min(value = 0, message = "Pozycja x musi być >= 0")
    @Max(value = 19, message = "Pozycja x musi być <= 19")
    private int x;

    @Min(value = 0, message = "Pozycja y musi być >= 0")
    @Max(value = 19, message = "Pozycja y musi być <= 19")
    private int y;

    // @NotEmpty(message = "Kafelki x są wymagane")
    // private int[] tabX;

    // @NotEmpty(message = "Kafelki y są wymagane")
    // private int[] tabY;

    @NotEmpty(message = "Pozycje są wymagane")
    List<Pozycja> pozycje;

    @JsonIgnore
    public int[] getPozycjeX() {
        int[] tabX = new int[pozycje.size()];

        for (int i = 0; i < pozycje.size(); i++) {
            Pozycja p = pozycje.get(i);
            tabX[i] = p.getX();
        }

        return tabX;
    }

    @JsonIgnore
    public int[] getPozycjeY() {
        int[] tabY = new int[pozycje.size()];

        for (int i = 0; i < pozycje.size(); i++) {
            Pozycja p = pozycje.get(i);
            tabY[i] = p.getY();
        }

        return tabY;
    }


    @AssertTrue(message = "Ilość kafelków x i y musi być z przedziału [1, 20] i muszą mieć taką samą długość")
    private boolean tabsWithinRange() {
        int size = this.pozycje.size();
        return size >= 1 && size <= 20;
    }

    @AssertTrue(message = "W kafelkach x i y nie mogą się powtarzać współrzędne")
    private boolean noRepeatCombinations() {
        for (int i = 0; i < this.pozycje.size(); i++) {
            for (int j = i + 1; j < this.pozycje.size(); j++) {
                if (this.pozycje.get(i).getX() == this.pozycje.get(j).getX() &&
                    this.pozycje.get(i).getY() == this.pozycje.get(j).getY()) {
                    return false;
                }
            }
        }
        return true;
    }


    // Null jak nie dopina się żadnego obrazu
    private String obraz;

}
