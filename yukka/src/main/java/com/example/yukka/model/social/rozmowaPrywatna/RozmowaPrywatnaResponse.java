package com.example.yukka.model.social.rozmowaPrywatna;

import java.util.List;

import com.example.yukka.model.social.komentarz.KomentarzSimpleResponse;
import com.example.yukka.model.uzytkownik.UzytkownikResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RozmowaPrywatnaResponse {
    private Long id;
    private boolean aktywna;
    private String nadawca;
    private List<UzytkownikResponse> uzytkownicy;
    private List<KomentarzSimpleResponse> komentarze;
    private int liczbaWiadomosci;
    private String ostatnioAktualizowana;
}