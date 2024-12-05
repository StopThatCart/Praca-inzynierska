package com.example.yukka.model.uzytkownik;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UzytkownikResponse {
private Long id;
    private String uzytId;
    private List<String> labels;
    private String nazwa;
    private String email;
    private byte[] avatar;
    private LocalDateTime dataUtworzenia;
    
    private boolean ban;
    public LocalDate banDo;

    private String imie;
    private String miasto;
    private String miejsceZamieszkania;
    private String opis;
    
    private Ustawienia ustawienia;
    
    private Set<UzytkownikResponse> blokowaniUzytkownicy;
    private Set<UzytkownikResponse> blokujacyUzytkownicy;

    
}
