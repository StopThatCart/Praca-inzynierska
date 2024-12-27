package com.example.yukka.model.dzialka.requests;


import com.example.yukka.model.dzialka.Pozycja;
import com.example.yukka.model.enums.Wyswietlanie;
import com.example.yukka.security.validations.valueOfEnum.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


/**
 * Reprezentuje żądanie dotyczące rośliny na działce.
 * 
 * <ul>
 * <li><strong>roslinaId</strong>: Id rośliny, wymagane.</li>
 * <li><strong>kolor</strong>: Kolor w formacie hex, wymagany.</li>
 * <li><strong>wyswietlanie</strong>: Sposób wyświetlania, wymagany.</li>
 * <li><strong>notatka</strong>: Dodatkowa notatka, opcjonalna.</li>
 * <li><strong>tekstura</strong>: Tekstura, opcjonalna.</li>
 * <li><strong>obraz</strong>: Obraz, opcjonalny. Null, jeśli nie dopina się żadnego obrazu.</li>
 * </ul>
 * 
 * <p>Metody:</p>
 * <ul>
 * <li><strong>isValidDzialkaRoslinaRequest</strong>: Sprawdza, czy żądanie jest prawidłowe.</li>
 * <li><strong>isValid</strong>: Sprawdza, czy pozycja rośliny jest w przydzielonych kafelkach.</li>
 * </ul>
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DzialkaRoslinaRequest extends BaseDzialkaRequest {
    @NotEmpty(message = "Id rośliny jest wymagane")
    private String roslinaId;

    @NotEmpty(message = "Kolor jest wymagany")
    @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "Kolor musi być w formacie hex")
    private String kolor;

    @NotEmpty(message = "Wybierz sposób wyświetlania")
    @ValueOfEnum(enumClass = Wyswietlanie.class, message = "Niepoprawny sposób wyświetlania")
    private String wyswietlanie;

    private String notatka;

    private String tekstura;
    // Null jak nie dopina się żadnego obrazu
    private String obraz;


    @JsonIgnore
    public boolean isValidDzialkaRoslinaRequest() {
        if (this.getX() == null || this.getY() == null) {
            return false;
        }
        Pozycja pos = Pozycja.builder().x(this.getX()).y(this.getY()).build();

        return this.getPozycje().contains(pos);
    }

    @JsonIgnore
    @AssertTrue(message = "Pozycja rośliny musi być w przydzielonych kafelkach")
    private boolean isValid() {
        if (this.getX() == null || this.getY() == null) {
            System.out.println("Pozycja x i y nie może być null");
            return false;
        }
        Pozycja pos = Pozycja.builder().x(this.getX()).y(this.getY()).build();

        return this.pozycje.contains(pos);
    }

}
