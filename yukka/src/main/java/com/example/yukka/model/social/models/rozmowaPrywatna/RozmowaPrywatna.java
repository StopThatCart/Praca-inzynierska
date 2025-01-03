package com.example.yukka.model.social.models.rozmowaPrywatna;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.social.models.komentarz.Komentarz;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Klasa reprezentująca prywatną rozmowę.
 * 
 * <ul>
 * <li><strong>id</strong>: Unikalny identyfikator rozmowy.</li>
 * <li><strong>aktywna</strong>: Flaga określająca, czy rozmowa jest aktywna.</li>
 * <li><strong>nadawca</strong>: Nazwa nadawcy rozmowy.</li>
 * <li><strong>dataUtworzenia</strong>: Data utworzenia rozmowy.</li>
 * <li><strong>ostatnioAktualizowana</strong>: Data ostatniej aktualizacji rozmowy.</li>
 * <li><strong>uzytkownicy</strong>: Lista użytkowników biorących udział w rozmowie.</li>
 * <li><strong>wiadomosci</strong>: Lista wiadomości w rozmowie.</li>
 * </ul>
 */
@Node
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RozmowaPrywatna {
    @Id @GeneratedValue
    private Long id;

    @Property(name = "aktywna")
    private boolean aktywna;

    @Property(name = "nadawca")
    private String nadawca;

    @CreatedDate
    @Property(name = "dataUtworzenia")
    private LocalDateTime dataUtworzenia;

    @LastModifiedDate
    @Property(name = "ostatnioAktualizowana")
    private LocalDateTime ostatnioAktualizowana;

    @Relationship(type = "JEST_W_ROZMOWIE", direction = Relationship.Direction.INCOMING)
    private List<Uzytkownik> uzytkownicy;

    @ToString.Exclude
    @Relationship(type = "MA_WIADOMOSC", direction = Relationship.Direction.OUTGOING)
    private List<Komentarz> wiadomosci;

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        List<String> nazwyUzytkownikow = uzytkownicy.stream()
            .map(Uzytkownik::getNazwa)
            .collect(Collectors.toList());
        return "RozmowaPrywatna{" +
                "id=" + id +
                ", jestAktywna=" + aktywna +
                ", nadawca=" + nadawca +
                ", liczbaWiadomosci=" + wiadomosci.size() +
                ", dataUtworzenia=" + dataUtworzenia +
                ", ostatnioAktualizowana=" + ostatnioAktualizowana +
                ", uzytkownicy=" + nazwyUzytkownikow +
                ", wiadomosci=" + wiadomosci.size() +
                '}';
    }

}
