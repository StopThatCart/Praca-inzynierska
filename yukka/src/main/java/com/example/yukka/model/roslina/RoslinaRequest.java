package com.example.yukka.model.roslina;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscWithRelations;
import com.example.yukka.validations.ValidWysokosc;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@ValidWysokosc
public class RoslinaRequest {

    private String roslinaId;

    @NotEmpty(message = "Nazwa jest wymagana")
    private String nazwa;

    @NotEmpty(message = "Nazwa łacińska jest wymagana")
    private String nazwaLacinska;

    // To nie ma prawa działać jakby co
   // @Default("Ta roślina nie posiada opisu")
    @NotEmpty(message = "Opis jest wymagany.")
    private String opis;

    @JsonIgnore
    @Value("${roslina.obraz.default.name}")
    private String obrazDefault;

    private String obraz;

    @NotNull(message = "wysokość musi być zdefiniowana")
    @Min(value = 0, message = "Wysokość minimalna nie może być mniejsza niż 0")
    private Double wysokoscMin;

    @NotNull(message = "wysokość musi być zdefiniowana")
    @Min(value = 0, message = "Wysokość maksymalna nie może być mniejsza niż 0")
    private Double wysokoscMax;

    @NotNull(message = "Właściwości nie mogą być nullem. Daj puste jak musisz.")
    @Builder.Default
    private List<WlasciwoscWithRelations> wlasciwosci = new ArrayList<>();


    public String getNazwaLacinska() {
        if (nazwaLacinska == null || nazwaLacinska.isEmpty()) {
            return nazwaLacinska;
        }
        return nazwaLacinska.toLowerCase();
    }

    public String getObraz() {
        if (obraz == null || obraz.isEmpty()) {
            return obrazDefault;
        }
        return obraz;
    }

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
        if(wlasciwosci == null) {
            return new ArrayList<>();
        }
        return wlasciwosci.stream()
            .map(w -> Map.of(
                "etykieta", w.getEtykieta(),
                "nazwa", w.getNazwa(),
                "relacja", w.getRelacja()
            ))
            .collect(Collectors.toList());
    }
    
}
