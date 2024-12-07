package com.example.yukka.model.dzialka.requests;

import com.example.yukka.model.dzialka.Pozycja;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Klasa reprezentująca żądanie przeniesienia rośliny na nową pozycję.
 * 
 * <ul>
 * <li><strong>numerDzialkiNowy</strong>: Może być null, jeśli działka nie jest zmieniana.</li>
 * <li><strong>xNowy</strong>: Nowa pozycja x rośliny. Musi być >= 0 i <= 19 oraz nie może być null.</li>
 * <li><strong>yNowy</strong>: Nowa pozycja y rośliny. Musi być >= 0 i <= 19 oraz nie może być null.</li>
 * </ul>
 * 
 * <p>Metoda <strong>isValidMoveRoslinaRequestCheck</strong> sprawdza, czy nowa pozycja rośliny znajduje się w przydzielonych kafelkach.</p>
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

    @JsonIgnore
    @AssertTrue(message = "Nowa pozycja rośliny musi być w przydzielonych kafelkach")
    private boolean isValidMoveRoslinaRequestCheck() {
        if (this.xNowy == null || this.yNowy == null) {
            return false;
        }
        Pozycja pos = Pozycja.builder().x(this.xNowy).y(this.yNowy).build();

        return this.pozycje.contains(pos);
    }

}
