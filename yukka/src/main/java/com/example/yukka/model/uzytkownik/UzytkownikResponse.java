package com.example.yukka.model.uzytkownik;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Reprezentuje odpowiedź użytkownika.
 * 
 * <ul>
 * <li><strong>id</strong>: Unikalny identyfikator użytkownika.</li>
 * <li><strong>uzytId</strong>: Identyfikator użytkownika.</li>
 * <li><strong>labels</strong>: Lista etykiet przypisanych do użytkownika.</li>
 * <li><strong>nazwa</strong>: Nazwa użytkownika.</li>
 * <li><strong>email</strong>: Adres email użytkownika.</li>
 * <li><strong>avatar</strong>: Awatar użytkownika w postaci tablicy bajtów.</li>
 * <li><strong>dataUtworzenia</strong>: Data utworzenia użytkownika.</li>
 * <li><strong>aktywowany</strong>: Informacja, czy użytkownik jest aktywowany.</li>
 * <li><strong>ban</strong>: Informacja, czy użytkownik jest zbanowany.</li>
 * <li><strong>banDo</strong>: Data do kiedy użytkownik jest zbanowany.</li>
 * <li><strong>imie</strong>: Imię użytkownika.</li>
 * <li><strong>miasto</strong>: Miasto użytkownika.</li>
 * <li><strong>miejsceZamieszkania</strong>: Miejsce zamieszkania użytkownika.</li>
 * <li><strong>opis</strong>: Opis użytkownika.</li>
 * <li><strong>ustawienia</strong>: Ustawienia użytkownika.</li>
 * <li><strong>blokowaniUzytkownicy</strong>: Zestaw użytkowników zablokowanych przez tego użytkownika.</li>
 * <li><strong>blokujacyUzytkownicy</strong>: Zestaw użytkowników, którzy zablokowali tego użytkownika.</li>
 * </ul>
 */
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
    
    private boolean aktywowany;
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
