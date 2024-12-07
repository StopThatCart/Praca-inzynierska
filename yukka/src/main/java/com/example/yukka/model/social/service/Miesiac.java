package com.example.yukka.model.social.service;

/**
 * Enum reprezentujący miesiące w roku.
 * <ul>
 *   <li><strong>STYCZEŃ</strong> - styczeń</li>
 *   <li><strong>LUTY</strong> - luty</li>
 *   <li><strong>MARZEC</strong> - marzec</li>
 *   <li><strong>KWIECIEŃ</strong> - kwiecień</li>
 *   <li><strong>MAJ</strong> - maj</li>
 *   <li><strong>CZERWIEC</strong> - czerwiec</li>
 *   <li><strong>LIPIEC</strong> - lipiec</li>
 *   <li><strong>SIERPIEŃ</strong> - sierpień</li>
 *   <li><strong>WRZESIEŃ</strong> - wrzesień</li>
 *   <li><strong>PAŹDZIERNIK</strong> - październik</li>
 *   <li><strong>LISTOPAD</strong> - listopad</li>
 *   <li><strong>GRUDZIEŃ</strong> - grudzień</li>
 * </ul>
 */
public enum Miesiac {
    STYCZEŃ("styczeń"),
    LUTY("luty"),
    MARZEC("marzec"),
    KWIECIEŃ("kwiecień"),
    MAJ("maj"),
    CZERWIEC("czerwiec"),
    LIPIEC("lipiec"),
    SIERPIEŃ("sierpień"),
    WRZESIEŃ("wrzesień"),
    PAŹDZIERNIK("październik"),
    LISTOPAD("listopad"),
    GRUDZIEŃ("grudzień");

    @SuppressWarnings("unused")
    private final String nazwa;

    Miesiac(String nazwa) {
        this.nazwa = nazwa;
    }
}
