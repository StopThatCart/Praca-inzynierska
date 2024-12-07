package com.example.yukka.auth.requests;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reprezentuje żądanie zbanowania użytkownika.
 * 
 * <p>Ta klasa zawiera niezbędne informacje do zbanowania użytkownika, w tym nazwę użytkownika, 
 * powód bana, czy użytkownik ma być zbanowany oraz datę zakończenia bana.</p>
 * 
 * <p>Adnotacje są używane do wymuszania reguł walidacji na polach:</p>
 * <ul>
 *   <li>{@code @NotEmpty} zapewnia, że nazwa użytkownika i powód są podane.</li>
 *   <li>{@code @NotNull} zapewnia, że status bana i data zakończenia są podane.</li>
 *   <li>{@code @JsonIgnore} zapobiega serializacji metody {@code isBanDataOkay}.</li>
 *   <li>{@code @AssertTrue} zapewnia, że data zakończenia bana jest po obecnej dacie.</li>
 * </ul>
 * 
 */
@Getter
@Setter
@Builder
@ToString
public class BanRequest {
    @NotEmpty(message = "Podaj nazwę użytkownika") 
    String nazwa;

    @NotEmpty(message = "Podaj powód bana")
    String powod;

    @NotNull(message = "Podaj czy użytkownik ma być zbanowany")
    Boolean ban;

    @NotNull(message = "Podaj datę zakończenia bana")
    LocalDate banDo;

    
    /** 
     * @return boolean
     */
    @JsonIgnore
    @AssertTrue(message = "Data zakończenia bana nie może być wcześniejsza niż obecna data")
    public boolean isBanDataOkay() {
        return banDo != null && banDo.isAfter(LocalDate.now());
    }
}
