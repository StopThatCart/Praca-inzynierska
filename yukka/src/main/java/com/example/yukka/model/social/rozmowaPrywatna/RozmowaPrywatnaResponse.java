package com.example.yukka.model.social.rozmowaPrywatna;

import java.util.List;

import com.example.yukka.model.social.komentarz.KomentarzSimpleResponse;
import com.example.yukka.model.uzytkownik.UzytkownikResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Reprezentuje odpowiedź na rozmowę prywatną.
 * 
 * <ul>
 * <li><strong>id</strong>: Unikalny identyfikator rozmowy prywatnej.</li>
 * <li><strong>aktywna</strong>: Status aktywności rozmowy.</li>
 * <li><strong>nadawca</strong>: Nazwa nadawcy rozmowy.</li>
 * <li><strong>uzytkownicy</strong>: Lista użytkowników biorących udział w rozmowie.</li>
 * <li><strong>komentarze</strong>: Lista komentarzy w rozmowie.</li>
 * <li><strong>liczbaWiadomosci</strong>: Liczba wiadomości w rozmowie.</li>
 * <li><strong>ostatnioAktualizowana</strong>: Data ostatniej aktualizacji rozmowy.</li>
 * </ul>
 */
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