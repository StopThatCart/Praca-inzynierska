package com.example.yukka.model.dzialka;

import java.util.Set;

import com.example.yukka.validations.ValidRoslinaIdAlboNazwaLacinska;
import com.example.yukka.validations.pozycje.ValidPozycje;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ValidPozycje
public class MoveRoslinaRequest extends DzialkaRoslinaRequest {


    // Może być null jak nie zmienia się działki
    private Integer numerDzialkiNowy;

    @Min(value = 0, message = "Pozycja nowego x musi być >= 0")
    @Max(value = 19, message = "Pozycja nowego x musi być <= 19")
    @NotNull(message = "Pozycja nowego x jest wymagana")
    private Integer xNowy;

    @Min(value = 0, message = "Pozycja nowego y musi być >= 0")
    @Max(value = 19, message = "Pozycja nowego y musi być <= 19")
    @NotNull(message = "Pozycja nowego y jest wymagana")
    private Integer yNowy;

}
