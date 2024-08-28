package com.example.yukka.model.roslina;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscWithRelations;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
//@ValidWysokosc
// Z rośliną stworzoną przez użytkownika jest większa swawolka
public class UzytkownikRoslinaRequest {

    @NotEmpty(message = "ID rośliny jest wymagane")
    private String roslinaId;

    @NotEmpty(message = "Nazwa jest wymagana")
    private String nazwa;

    // To nie ma prawa działać jakby co
    private String opis;

    @NotNull(message = "Nazwa obrazu jest wymagana.")
    @Builder.Default
    private String obraz = "default.jpg";

    private Double wysokoscMin;
    private Double wysokoscMax;

    private List<WlasciwoscWithRelations> wlasciwosci;

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
                "etykieta", w.getEtykieta(),
                "nazwa", w.getNazwa(),
                "relacja", w.getRelacja()
            ))
            .collect(Collectors.toList());
    }
}
