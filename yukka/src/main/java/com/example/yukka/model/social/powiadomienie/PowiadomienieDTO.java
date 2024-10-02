package com.example.yukka.model.social.powiadomienie;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Builder
@ToString
// Tak naprawdę to jest DTO, ale nie chce mi się zmieniać nazwy
public class PowiadomienieDTO {
    private Long id;
    private String typ;
    private Boolean przeczytane;
    private String tytul;
    private String odnosnik;
    private String opis;
    private Set<String> nazwyRoslin;
    @Builder.Default
    @Value("${powiadomienia.obraz.default.name}")
    private String avatar = "default_powiadomienie_avatar.png";
    private String uzytkownikNazwa;
    private int iloscPolubien;
    private LocalDateTime data;
    @Builder.Default
    private LocalDateTime dataUtworzenia = LocalDateTime.now();

}
