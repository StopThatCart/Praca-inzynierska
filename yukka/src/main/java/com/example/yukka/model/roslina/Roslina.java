package com.example.yukka.model.roslina;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.dzialka.ZasadzonaNa;
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.Builder;

@Node
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString
@SuppressWarnings("all")
public class Roslina {
    @Id @GeneratedValue
    private long id;

    @Property("roslinaId")
    private String roslinaId;
    @Property("nazwa")
    private String nazwa;
    @Property("nazwaLacinska")
    private String nazwaLacinska;
    @Property("opis")
    private String opis;
    @Property("wysokoscMin")
    @Builder.Default
    private Double wysokoscMin = 0.0;
    @Property("wysokoscMax")
    @Builder.Default
    private Double wysokoscMax = 0.0;
    
    @Property("obraz")
    @Builder.Default
    private String obraz ="default_plant.jpg";


    // Ogrod
    @JsonIgnore
    @Relationship(type = "ZASADZONA_NA", direction = Relationship.Direction.OUTGOING)
    private List<ZasadzonaNa> dzialki;
	
	@JsonIgnore
	@Relationship(type = "STWORZONA_PRZEZ", direction = Relationship.Direction.OUTGOING)
    private Uzytkownik uzytkownik;

    // Wlasciwosci

    @Relationship(type = "MA_FORME", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> formy = new HashSet<>();

    @Relationship(type = "MA_GLEBE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> gleby = new HashSet<>();

    @Relationship(type = "MA_GRUPE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> grupy = new HashSet<>();

    @Relationship(type = "MA_KOLOR_LISCI", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> koloryLisci = new HashSet<>();

    @Relationship(type = "MA_KOLOR_KWIATOW", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> koloryKwiatow = new HashSet<>();

    @Relationship(type = "MA_KWIAT", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> kwiaty = new HashSet<>();

    @Relationship(type = "MA_NAGRODE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> nagrody = new HashSet<>();

    @Relationship(type = "MA_ODCZYNY", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> odczyny = new HashSet<>();

    @Relationship(type = "MA_OKRES_KWITNIENIA", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> okresyKwitnienia = new HashSet<>();

    @Relationship(type = "MA_OKRES_OWOCOWANIA", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> okresyOwocowania = new HashSet<>();

    @Relationship(type = "MA_OWOC", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> owoce = new HashSet<>();

    @Relationship(type = "MA_PODGRUPE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> podgrupa = new HashSet<>();

    @Relationship(type = "MA_POKROJ", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> pokroje = new HashSet<>();

    @Relationship(type = "MA_SILE_WZROSTU", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> silyWzrostu = new HashSet<>();

    @Relationship(type = "MA_STANOWISKO", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> stanowiska = new HashSet<>();

    @Relationship(type = "MA_WALOR", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> walory = new HashSet<>();

    @Relationship(type = "MA_WILGOTNOSC", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> wilgotnosci = new HashSet<>();

    @Relationship(type = "MA_ZASTOSOWANIE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> zastosowania = new HashSet<>();

    @Relationship(type = "MA_ZIMOZIELONOSC_LISCI", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> zimozielonosci = new HashSet<>();

    @JsonIgnore
    boolean areWlasciwosciEqual(Set<Wlasciwosc> set1, Set<Wlasciwosc> set2) {
        return extractNazwy(set1).equals(extractNazwy(set2));
    }

    @JsonIgnore
    private Set<String> extractNazwy(Set<Wlasciwosc> wlasciwosci) {
        return wlasciwosci.stream()
                          .map(Wlasciwosc::getNazwa)
                          .collect(Collectors.toSet());
    }

    // Może się przydać
    /* 
    public void setWlasciwosci(Set<Wlasciwosc> wlasciwosci, RoslinaRelacje relacja) {
        switch (relacja) {
            case MA_FORME -> setFormy(wlasciwosci);
            case MA_GLEBE -> setGleby(wlasciwosci);
            case MA_GRUPE -> setGrupy(wlasciwosci);
            case MA_KOLOR_LISCI -> setKoloryLisci(wlasciwosci);
            case MA_KOLOR_KWIATOW -> setKoloryKwiatow(wlasciwosci);
            case MA_KWIAT -> setKwiaty(wlasciwosci);
            case MA_NAGRODE -> setNagrody(wlasciwosci);
            case MA_ODCZYNY -> setOdczyny(wlasciwosci);
            case MA_OKRES_KWITNIENIA -> setOkresyKwitnienia(wlasciwosci);
            case MA_OKRES_OWOCOWANIA -> setOkresyOwocowania(wlasciwosci);
            case MA_OWOC -> setOwoce(wlasciwosci);
            case MA_PODGRUPE -> setPodgrupa(wlasciwosci);
            case MA_POKROJ -> setPokroje(wlasciwosci);
            case MA_SILE_WZROSTU -> setSilyWzrostu(wlasciwosci);
            case MA_STANOWISKO -> setStanowiska(wlasciwosci);
            case MA_WALOR -> setWalory(wlasciwosci);
            case MA_WILGOTNOSC -> setWilgotnosci(wlasciwosci);
            case MA_ZASTOSOWANIE -> setZastosowania(wlasciwosci);
            case MA_ZIMOZIELONOSC_LISCI -> setZimozielonosci(wlasciwosci);
            default -> throw new IllegalArgumentException("Relacja nie występuje w " + RoslinaRelacje.class);
        }
    }
   */
}
