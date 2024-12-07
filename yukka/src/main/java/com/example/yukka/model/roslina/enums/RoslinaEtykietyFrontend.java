package com.example.yukka.model.roslina.enums;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

/**
 * Enum RoslinaEtykietyFrontend reprezentuje etykiety używane w aplikacji frontendowej dla roślin.
 * Każda etykieta ma odpowiadającą jej wartość backendową.
 * 
 * <ul>
 * <li><strong>GRUPA</strong> - Grupa</li>
 * <li><strong>PODGRUPA</strong> - Podgrupa</li>
 * <li><strong>FORMA</strong> - Forma</li>
 * <li><strong>SILA_WZROSTU</strong> - SilaWzrostu</li>
 * <li><strong>POKROJ</strong> - Pokroj</li>
 * <li><strong>KOLOR_LISCI</strong> - Kolor</li>
 * <li><strong>KOLOR_KWIATOW</strong> - Kolor</li>
 * <li><strong>ZIMOZIELONOSC</strong> - Zimozielonosc</li>
 * <li><strong>OWOC</strong> - Owoc</li>
 * <li><strong>STANOWISKO</strong> - Stanowisko</li>
 * <li><strong>WILGOTNOSC</strong> - Wilgotnosc</li>
 * <li><strong>ODCZYN</strong> - Odczyn</li>
 * <li><strong>GLEBA</strong> - Gleba</li>
 * <li><strong>WALOR</strong> - Walor</li>
 * <li><strong>ZASTOSOWANIE</strong> - Zastosowanie</li>
 * <li><strong>KWIAT</strong> - Kwiat</li>
 * <li><strong>OKRES_KWITNIENIA</strong> - Okres</li>
 * <li><strong>OKRES_OWOCOWANIA</strong> - Okres</li>
 * </ul>
 * 
 * Metody:
 * <ul>
 * <li><strong>getBackendValue</strong> - Zwraca wartość backendową etykiety.</li>
 * <li><strong>fromString</strong> - Zwraca opcjonalną wartość enum na podstawie podanego ciągu znaków.</li>
 * <li><strong>toBackend</strong> - Zwraca wartość backendową na podstawie podanego ciągu znaków lub rzuca wyjątek, jeśli etykieta nie istnieje.</li>
 * </ul>
 */
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
