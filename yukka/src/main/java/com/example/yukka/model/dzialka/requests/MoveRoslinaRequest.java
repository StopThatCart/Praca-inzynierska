package com.example.yukka.model.dzialka.requests;

import com.example.yukka.validations.pozycje.ValidPozycje;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)

@AllArgsConstructor
@NoArgsConstructor
@ToString
@ValidPozycje
public class MoveRoslinaRequest extends BaseDzialkaRequest {


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
