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
