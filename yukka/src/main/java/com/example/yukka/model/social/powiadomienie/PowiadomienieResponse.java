    package com.example.yukka.model.social.powiadomienie;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import org.ocpsoft.prettytime.PrettyTime;

import com.example.yukka.model.uzytkownik.UzytkownikResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reprezentuje odpowiedź powiadomienia.
 * 
 * <ul>
 * <li><strong>id</strong>: Unikalny identyfikator powiadomienia.</li>
 * <li><strong>typ</strong>: Typ powiadomienia.</li>
 * <li><strong>isZgloszenie</strong>: Flaga określająca, czy powiadomienie jest zgłoszeniem.</li>
 * <li><strong>przeczytane</strong>: Flaga określająca, czy powiadomienie zostało przeczytane.</li>
 * <li><strong>tytul</strong>: Tytuł powiadomienia.</li>
 * <li><strong>odnosnik</strong>: Odnośnik związany z powiadomieniem.</li>
 * <li><strong>opis</strong>: Opis powiadomienia.</li>
 * <li><strong>nazwyRoslin</strong>: Zbiór nazw roślin związanych z powiadomieniem.</li>
 * <li><strong>avatar</strong>: Awatar użytkownika powiązanego z powiadomieniem.</li>
 * <li><strong>uzytkownikNazwa</strong>: Nazwa użytkownika powiązanego z powiadomieniem.</li>
 * <li><strong>iloscPolubien</strong>: Ilość polubień powiadomienia.</li>
 * <li><strong>data</strong>: Data powiadomienia.</li>
 * <li><strong>dataUtworzenia</strong>: Data utworzenia powiadomienia w formacie PrettyTime.</li>
 * <li><strong>zglaszajacy</strong>: Informacje o użytkowniku zgłaszającym powiadomienie.</li>
 * </ul>
 */
@Getter
@Setter
@Builder
@ToString
public class PowiadomienieResponse {
    private Long id;
    private String typ;
    private boolean isZgloszenie;
    private Boolean przeczytane;
    private Boolean ukryte;
    private String tytul;
    private String odnosnik;
    private String opis;
    private Set<String> nazwyRoslin;
    private byte[] avatar;
    private String uzytkownikNazwa;
    private int iloscPolubien;
    private LocalDateTime data;
    @Builder.Default
    private String dataUtworzenia = new PrettyTime(Locale.forLanguageTag("pl")).format(new Date());

    private UzytkownikResponse zglaszajacy;

}
