package com.example.yukka.model.roslina.enums;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoslinaEtykietyFrontend {
    GRUPA("Grupa"),
    PODGRUPA("Podgrupa"),
    FORMA("Forma"),
    SILA_WZROSTU("SilaWzrostu"),
    POKROJ("Pokroj"),
    KOLOR_LISCI("Kolor"),
    KOLOR_KWIATOW("Kolor"),
    ZIMOZIELONOSC("Zimozielonosc"),

    OWOC("Owoc"),
    STANOWISKO("Stanowisko"),
    WILGOTNOSC("Wilgotnosc"),
    ODCZYN("Odczyn"),
    GLEBA("Gleba"),
    WALOR("Walor"),
    ZASTOSOWANIE("Zastosowanie"),
    KWIAT("Kwiat"),
    OKRES_KWITNIENIA("Okres"),
    OKRES_OWOCOWANIA("Okres");


    private final String backendValue;


    public String getBackendValue() {
        return backendValue;
    }

    public static Optional<RoslinaEtykietyFrontend> fromString(String etykieta) {
        String normalized = Normalizer.normalize(etykieta, Normalizer.Form.NFD)
                                      .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                                      .toUpperCase()
                                      .replace("Ł", "L")
                                        .replace("Ż", "Z")
                                        .replace("Ź", "Z")
                                        .replace("Ć", "C")
                                        .replace("Ś", "S")
                                        .replace("Ó", "O")
                                        .replace("Ą", "A")
                                        .replace("Ę", "E")

                                      .replace(" ", "_");
        return Arrays.stream(values())
                     .filter(e -> e.name().equals(normalized))
                     .findFirst();
    }

    public static String toBackend(String etykieta) {
        for (RoslinaEtykietyFrontend value : values()) {
            if (value.getBackendValue().equalsIgnoreCase(etykieta)) {
                return etykieta;
            }
        }
        return fromString(etykieta).map(RoslinaEtykietyFrontend::getBackendValue)
            .orElseThrow(() -> new IllegalArgumentException("Nie ma takiej etykiety: "  + etykieta));
    }


}
