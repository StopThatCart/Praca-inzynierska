package com.example.yukka.model.roslina;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.yukka.model.roslina.enums.RoslinaEtykietyFrontend;
import com.example.yukka.model.roslina.enums.RoslinaRelacje;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscWithRelations;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@Builder
@ToString
// Z rośliną stworzoną przez użytkownika jest większa swawolka
public class UzytkownikRoslinaRequest {
    private String roslinaId;

    @NotEmpty(message = "Nazwa jest wymagana")
    private String nazwa;

    // To nie ma prawa działać jakby co
    private String opis;

    //@NotNull(message = "Nazwa obrazu jest wymagana.")
    @Builder.Default
    
    private String obraz = "default.jpg";

    @NotNull(message = "wysokość musi być zdefiniowana")
    @Min(value = 0, message = "Wysokość minimalna nie może być mniejsza niż 0")
    private Double wysokoscMin;

    @NotNull(message = "wysokość musi być zdefiniowana")
    @Min(value = 0, message = "Wysokość maksymalna nie może być mniejsza niż 0")
    private Double wysokoscMax;

    @JsonIgnore
    @AssertTrue(message = "Wysokość minimalna nie może być większa niż wysokości maksymalnej")
    private boolean isValidWysokosc() {
        return this.wysokoscMin <= this.wysokoscMax;
    }

    @NotNull(message = "Właściwości nie mogą być nullem. Daj puste jak musisz.")
    @Builder.Default
    private List<WlasciwoscWithRelations> wlasciwosci = new ArrayList<>();

    public boolean areWlasciwosciEmpty() {
        if (wlasciwosci == null || wlasciwosci.isEmpty()) {
            return true;
        }
        for (WlasciwoscWithRelations wlasciwosc : wlasciwosci) {
            if (wlasciwosc.getNazwa() != null && !wlasciwosc.getNazwa().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @JsonIgnore
    public List<Map<String, String>> getWlasciwosciAsMap() {
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
