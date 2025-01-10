package com.example.yukka.model.roslina;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Klasa RoslinaRequest reprezentuje żądanie dotyczące rośliny.
 * <ul>
 *   <li><strong>nazwaLacinska</strong> - Łacińska nazwa rośliny. Jest wymagana i nie może być pusta.</li>
 * </ul>
 * 
 * Metody:
 * <ul>
 *   <li><strong>getNazwaLacinska()</strong> - Zwraca łacińską nazwę rośliny w małych literach, jeśli jest ustawiona.</li>
 * </ul>
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoslinaRequest extends RoslinaWlasnaRequest{
    @NotEmpty(message = "Nazwa łacińska jest wymagana")
    @Size(min = 1, max = 255, message = "Nazwa łacińska musi zawierać od 1 do 255 znaków")
    private String nazwaLacinska;

    public String getNazwaLacinska() {
        if (nazwaLacinska == null || nazwaLacinska.isEmpty()) {
            return nazwaLacinska;
        }
        return nazwaLacinska.toLowerCase();
    }
    
}
