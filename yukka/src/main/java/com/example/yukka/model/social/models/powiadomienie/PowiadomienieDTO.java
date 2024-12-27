package com.example.yukka.model.social.models.powiadomienie;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;

import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Reprezentuje Data Transfer Object (DTO) dla powiadomienia.
 * 
 * <ul>
 * <li><strong>id</strong>: Unikalny identyfikator powiadomienia.</li>
 * <li><strong>typ</strong>: Typ powiadomienia.</li>
 * <li><strong>isZgloszenie</strong>: Flaga oznaczająca, czy powiadomienie jest zgłoszeniem.</li>
 * <li><strong>zglaszajacy</strong>: Użytkownik zgłaszający powiadomienie.</li>
 * <li><strong>przeczytane</strong>: Flaga oznaczająca, czy powiadomienie zostało przeczytane.</li>
 * <li><strong>tytul</strong>: Tytuł powiadomienia.</li>
 * <li><strong>odnosnik</strong>: Odnośnik związany z powiadomieniem.</li>
 * <li><strong>opis</strong>: Opis powiadomienia.</li>
 * <li><strong>nazwyRoslin</strong>: Zbiór nazw roślin związanych z powiadomieniem.</li>
 * <li><strong>avatar</strong>: Nazwa pliku z awatarem powiadomienia (domyślnie "default_powiadomienie_avatar.png").</li>
 * <li><strong>uzytkownikNazwa</strong>: Nazwa użytkownika powiązanego z powiadomieniem.</li>
 * <li><strong>zglaszany</strong>: Informacja o zgłaszanym elemencie.</li>
 * <li><strong>iloscPolubien</strong>: Ilość polubień powiadomienia.</li>
 * <li><strong>data</strong>: Data powiązana z powiadomieniem.</li>
 * <li><strong>dataUtworzenia</strong>: Data utworzenia powiadomienia (domyślnie bieżąca data i czas).</li>
 * <li><strong>okres</strong>: Okres związany z powiadomieniem.</li>
 * </ul>
 */
@Getter
@Setter
@Builder
@ToString
// Tak naprawdę to jest DTO, ale nie chce mi się zmieniać nazwy
public class PowiadomienieDTO {
    private Long id;
    private String typ;
    private boolean isZgloszenie;
    private Uzytkownik zglaszajacy;
    private Boolean przeczytane;
    private String tytul;
    private String odnosnik;
    private String opis;
    private Set<String> nazwyRoslin;
    @Builder.Default
    @Value("${powiadomienia.obraz.default.name}")
    private String avatar = "default_powiadomienie_avatar.png";
    private String uzytkownikNazwa;
    private String zglaszany;
    private int iloscPolubien;
    private LocalDateTime data;
    @Builder.Default
    private LocalDateTime dataUtworzenia = LocalDateTime.now();
    private LocalDate okres;

}
