package com.example.yukka.model.roslina;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import com.example.yukka.model.roslina.enums.RoslinaEtykietyFrontend;
import com.example.yukka.model.roslina.enums.RoslinaRelacje;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscWithRelations;
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



@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
// Z rośliną stworzoną przez użytkownika jest większa swawolka
public class UzytkownikRoslinaRequest {
    private String roslinaId;

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

    
    /** 
     * @return boolean
     */
    @JsonIgnore
    @AssertTrue(message = "Wysokość minimalna nie może być większa niż wysokości maksymalnej")
    private boolean isValidWysokosc() {
        return this.wysokoscMin <= this.wysokoscMax;
    }

    @NotNull(message = "Właściwości nie mogą być nullem. Daj puste jak musisz.")
    @Size(max = 100, message = "Lista właściwości nie może przekraczać 100 elementów.")
    @Builder.Default
    private List<WlasciwoscWithRelations> wlasciwosci = new ArrayList<>();

    @AssertTrue(message = "Nazwy właściwości muszą zawierać od 1 do 100 znaków")
    public boolean areWlasciwosciEmpty() {
        if (wlasciwosci == null || wlasciwosci.isEmpty()) {
            return true;
        }
        for (WlasciwoscWithRelations wlasciwosc : wlasciwosci) {
            String nazwa = wlasciwosc.getNazwa();
            if (nazwa != null && !nazwa.trim().isEmpty() && nazwa.length() <= 100) {
                return false;
            }
        }
        return true;
    }

    @JsonIgnore
    public List<Map<String, String>> getWlasciwosciAsMap() {
        if(wlasciwosci == null) {
            return new ArrayList<>();
        }
        return wlasciwosci.stream()
            .map(w -> Map.of(
                "etykieta", RoslinaEtykietyFrontend.toBackend(w.getEtykieta()),
                "nazwa", w.getNazwa(),
                "relacja", RoslinaRelacje.valueOf(w.getRelacja().toUpperCase()).toString()
            ))
            .collect(Collectors.toList());
    }


    @AssertTrue(message = "W liście właściwości znajdują się niepoprawne etykiety")
    @JsonIgnore
    public boolean isValidEtykiety() {
        if (wlasciwosci == null || wlasciwosci.isEmpty()) {
            return true;
        }
        for (WlasciwoscWithRelations wlasciwosc : wlasciwosci) {
            if (!RoslinaEtykietyFrontend.fromString(wlasciwosc.getEtykieta()).isPresent()) {
                return false;
            }
        }
        return true;
    }

}
