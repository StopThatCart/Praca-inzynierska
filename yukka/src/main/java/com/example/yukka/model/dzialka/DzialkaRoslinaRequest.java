package com.example.yukka.model.dzialka;
import com.example.yukka.validations.ValidWysokosc;

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
@ValidWysokosc
public class DzialkaRoslinaRequest {

    private String uzytkownikRoslinaId;

    private String nazwaLacinskaRosliny;

    @NotEmpty(message = "")
    @Min(value = 0, message = "Numer działki musi być >= 0")
    @Max(value = 10, message = "Numer działki musi być <= 10")
    private int numerDzialki;

    @NotEmpty(message = "Pozycja x jest wymagana")
    @Min(value = 0, message = "Pozycja x musi być >= 0")
    @Max(value = 20, message = "Pozycja x musi być <= 20")
    private int x;

    @NotEmpty(message = "Pozycja y jest wymagana")
    @Min(value = 0, message = "Pozycja y musi być >= 0")
    @Max(value = 20, message = "Pozycja y musi być <= 20")
    private int y;

    // Null jak nie dopina się żadnego obrazu
    private String obraz;

}
