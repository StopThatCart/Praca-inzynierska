package com.example.yukka.model.roslina;
import java.util.List;
import java.util.Map;

import com.example.yukka.validations.ValidWysokosc;

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

    // TODO: Zrobić sprawdzanie OR dla roslinaId i nazwa
    private String roslinaId;

    private String nazwa;

    @NotEmpty(message = "Nazwa jest wymagana")
    private String nazwaLacinska;

    // To nie ma prawa działać jakby co
   // @Default("Ta roślina nie posiada opisu")
    @NotEmpty(message = "Opis jest wymagany.")
    private String opis;

    @NotNull(message = "Nazwa obrazu jest wymagana.")
    @Builder.Default
    private String obraz = "default.jpg";

    @NotEmpty(message = "wysokość musi być zdefiniowana")
    private Double wysokoscMin;

    @NotEmpty(message = "wysokość musi być zdefiniowana")
    private Double wysokoscMax;

    @NotNull(message = "Właściwości nie mogą być nullem. Daj puste jak musisz.")
    private List<Map<String, String>> wlasciwosci;

    public boolean areWlasciwosciEmpty() {
        if (wlasciwosci == null || wlasciwosci.isEmpty()) {
            return true;
        }
        for (Map<String, String> map : wlasciwosci) {
            for (String value : map.values()) {
                if (value != null && !value.trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}
