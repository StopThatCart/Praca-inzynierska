package com.example.yukka.model.social.rozmowaPrywatna;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    @Property(name = "zablokowana")
    private boolean zablokowana;

    @Property(name = "liczbaWiadomosci")
    private int liczbaWiadomosci;

    @CreatedDate
    @Property(name = "dataUtworzenia")
    private LocalDateTime dataUtworzenia;

    @Relationship(type = "JEST_W_ROZMOWIE", direction = Relationship.Direction.INCOMING)
    private List<Uzytkownik> uzytkownicy;

    @ToString.Exclude
    @Relationship(type = "MA_WIADOMOSC", direction = Relationship.Direction.OUTGOING)
    private List<Komentarz> wiadomosci;

    @Override
    public String toString() {
        List<String> nazwyUzytkownikow = uzytkownicy.stream()
            .map(Uzytkownik::getNazwa)
            .collect(Collectors.toList());
        return "RozmowaPrywatna{" +
                "id=" + id +
                ", jestAktywna=" + aktywna +
                ", zablokowana=" + zablokowana +
                ", liczbaWiadomosci=" + liczbaWiadomosci +
                ", dataUtworzenia=" + dataUtworzenia +
                ", uzytkownicy=" + nazwyUzytkownikow +
                ", wiadomosci=" + wiadomosci.size() +
                '}';
    }

}
