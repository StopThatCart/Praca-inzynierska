package com.example.yukka.model.dzialka;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Min(value = 0, message = "Pozycja starego x musi być >= 0")
    @Max(value = 19, message = "Pozycja starego x musi być <= 19")
    private int xStary;

    @NotEmpty(message = "Pozycja y jest wymagana")
    @Min(value = 0, message = "Pozycja starego y musi być >= 0")
    @Max(value = 19, message = "Pozycja starego y musi być <= 19")
    private int yStary;

    @NotEmpty(message = "Nowa pozycja x jest wymagana")
    @Min(value = 0, message = "Pozycja nowego x musi być >= 0")
    @Max(value = 19, message = "Pozycja nowego x musi być <= 19")
    private int xNowy;

    @NotEmpty(message = "Nowa pozycja y jest wymagana")
    @Min(value = 0, message = "Pozycja nowego y musi być >= 0")
    @Max(value = 19, message = "Pozycja nowego y musi być <= 19")
    private int yNowy;


    // @NotEmpty(message = "Kafelki x są wymagane")
    // private int[] tabX;

    // @NotEmpty(message = "Kafelki y są wymagane")
    // private int[] tabY;

    @NotEmpty(message = "Pozycje są wymagane")
    private List<Pozycja> pozycje;

    @AssertTrue(message = "Ilość kafelków x i y musi być z przedziału [1, 20]")
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


}
