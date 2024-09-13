    package com.example.yukka.model.social.powiadomienie;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class PowiadomienieResponse {
    private Long id;
    private String typ;
    private Boolean przeczytane;
    private String tytul;
    private String odnosnik;
    private String opis;
    private Set<String> nazwyRoslin;
    private byte[] avatar;
    private String uzytkownikNazwa;
    private int iloscPolubien;
    private LocalDateTime data;
    @Builder.Default
    private LocalDateTime dataUtworzenia = LocalDateTime.now();

}
