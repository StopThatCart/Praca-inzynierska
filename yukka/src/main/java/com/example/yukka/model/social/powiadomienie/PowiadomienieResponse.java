package com.example.yukka.model.social.powiadomienie;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Builder
@ToString
// Tak naprawdę to jest DTO, ale nie chce mi się zmieniać nazwy
public class PowiadomienieResponse {
    private String typ;
    private String tytul;
    private String odnosnik;
    private String opis;
    private List<String> nazwyRoslin;
    @Builder.Default
    private String avatar = "default_avatar.png";
    private String uzytkownikNazwa;
    private int iloscPolubien;
    private LocalDateTime data;
    @Builder.Default
    private LocalDateTime dataUtworzenia = LocalDateTime.now();

}
