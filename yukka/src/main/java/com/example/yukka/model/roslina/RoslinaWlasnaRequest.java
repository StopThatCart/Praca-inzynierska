package com.example.yukka.model.roslina;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import com.example.yukka.model.roslina.cecha.CechaWithRelations;
import com.example.yukka.model.roslina.enums.RoslinaEtykietyFrontend;
import com.example.yukka.model.roslina.enums.RoslinaRelacje;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
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



/**
 * Klasa reprezentująca żądanie użytkownika dotyczące rośliny.
 * <ul>
 * <li><strong>uuid</strong>: Identyfikator rośliny.</li>
 * <li><strong>nazwa</strong>: Nazwa rośliny. Wymagana.</li>
 * <li><strong>opis</strong>: Opis rośliny. Wymagany.</li>
 * <li><strong>obrazDefault</strong>: Domyślna nazwa obrazu rośliny. Ignorowane podczas serializacji JSON.</li>
 * <li><strong>obraz</strong>: Nazwa obrazu rośliny.</li>
 * <li><strong>wysokoscMin</strong>: Minimalna wysokość rośliny. Wymagana. Musi być między 0 a 200.</li>
 * <li><strong>wysokoscMax</strong>: Maksymalna wysokość rośliny. Wymagana. Musi być między 0 a 200.</li>
 * <li><strong>cechy</strong>: Lista cech rośliny. Wymagana. Maksymalnie 100 elementów.</li>
 * </ul>
 * <p>Metody walidacyjne:</p>
 * <ul>
 * <li><strong>isValidWysokosc</strong>: Sprawdza, czy minimalna wysokość nie jest większa niż maksymalna wysokość.</li>
 * <li><strong>areCechyEmpty</strong>: Sprawdza, czy nazwy cech zawierają od 1 do 100 znaków.</li>
 * <li><strong>isValidEtykiety</strong>: Sprawdza, czy etykiety w liście cech są poprawne.</li>
 * </ul>
 * <p>Metody pomocnicze:</p>
 * <ul>
 * <li><strong>getCechyAsMap</strong>: Zwraca listę cech jako mapę.</li>
 * </ul>
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoslinaWlasnaRequest {
    private String uuid;

    @NotEmpty(message = "Nazwa jest wymagana")
    private String nazwa;

    @NotEmpty(message = "Opis jest wymagany.")
    private String opis;

    @JsonIgnore
    @Value("${roslina.obraz.default.name}")
    private String obrazDefault;

    private String obraz;

    @NotNull(message = "wysokość musi być zdefiniowana")
    @Min(value = 0, message = "Wysokość minimalna nie może być mniejsza niż 0")
    @Max(value = 200, message = "Wysokość minimalna nie może być większa niż 200")
    private Double wysokoscMin;

    @NotNull(message = "wysokość musi być zdefiniowana")
    @Min(value = 0, message = "Wysokość maksymalna nie może być mniejsza niż 0")
    @Max(value = 200, message = "Wysokość maksymalna nie może być większa niż 200")
    private Double wysokoscMax;

    
    @JsonIgnore
    @AssertTrue(message = "Wysokość minimalna nie może być większa niż wysokości maksymalnej")
    private boolean isValidWysokosc() {
        return this.wysokoscMin <= this.wysokoscMax;
    }

    @NotNull(message = "Cechy nie mogą być nullem.")
    @Size(max = 100, message = "Lista cech nie może przekraczać 100 elementów.")
    @Builder.Default
    private List<CechaWithRelations> cechy = new ArrayList<>();

    @AssertTrue(message = "Nazwy cech muszą zawierać od 1 do 100 znaków")
    public boolean areCechyEmpty() {
        if (cechy == null || cechy.isEmpty()) {
            return true;
        }
        for (CechaWithRelations cecha : cechy) {
            String nazwa = cecha.getNazwa();
            if (nazwa != null && !nazwa.trim().isEmpty() && nazwa.length() <= 100) {
                return false;
            }
        }
        return true;
    }

    @JsonIgnore
    public List<Map<String, String>> getCechyAsMap() {
        if(cechy == null) {
            return new ArrayList<>();
        }
        return cechy.stream()
            .map(w -> Map.of(
                "etykieta", RoslinaEtykietyFrontend.toBackend(w.getEtykieta()),
                "nazwa", w.getNazwa(),
                "relacja", RoslinaRelacje.valueOf(w.getRelacja().toUpperCase()).toString()
            ))
            .collect(Collectors.toList());
    }


    @AssertTrue(message = "W liście cech znajdują się niepoprawne etykiety")
    @JsonIgnore
    public boolean isValidEtykiety() {
        if (cechy == null || cechy.isEmpty()) {
            return true;
        }
        for (CechaWithRelations cecha : cechy) {
            if (!RoslinaEtykietyFrontend.fromString(cecha.getEtykieta()).isPresent()) {
                return false;
            }
        }
        return true;
    }

}
