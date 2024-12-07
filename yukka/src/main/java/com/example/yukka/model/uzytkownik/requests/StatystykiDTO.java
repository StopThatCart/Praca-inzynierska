package com.example.yukka.model.uzytkownik.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * Klasa StatystykiDTO reprezentuje dane statystyczne użytkownika.
 * 
 * <ul>
 *   <li><strong>posty</strong>: Liczba postów użytkownika.</li>
 *   <li><strong>komentarze</strong>: Liczba komentarzy użytkownika.</li>
 *   <li><strong>rosliny</strong>: Liczba roślin dodanych przez użytkownika.</li>
 *   <li><strong>komentarzeOcenyPozytywne</strong>: Liczba pozytywnych ocen komentarzy użytkownika.</li>
 *   <li><strong>komentarzeOcenyNegatywne</strong>: Liczba negatywnych ocen komentarzy użytkownika.</li>
 *   <li><strong>postyOcenyPozytywne</strong>: Liczba pozytywnych ocen postów użytkownika.</li>
 *   <li><strong>postyOcenyNegatywne</strong>: Liczba negatywnych ocen postów użytkownika.</li>
 * </ul>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StatystykiDTO {

    private Integer posty;
    private Integer komentarze;
    private Integer rosliny;


    private Integer komentarzeOcenyPozytywne;
    private Integer komentarzeOcenyNegatywne;
    private Integer postyOcenyPozytywne;
    private Integer postyOcenyNegatywne;
}
