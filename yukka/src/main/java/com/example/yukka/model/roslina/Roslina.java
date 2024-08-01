package com.example.yukka.model.roslina;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Node
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Roslina {
    @Id @GeneratedValue
    private long id;
    @Property("nazwa")
    private String nazwa;
    @Property("nazwaLacinska")
    private String nazwaLacinska;
    @Property("opis")
    private String opis;
    @Property("wysokoscMin")
    private Double wysokoscMin;
    @Property("wysokoscMax")
    private Double wysokoscMax;
    
    @Property("obraz")
    private String obraz;

    @Relationship(type = "MA_FORME", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> formy = new ArrayList<>();

    @Relationship(type = "MA_GLEBE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> gleby = new ArrayList<>();

    @Relationship(type = "MA_GRUPE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> grupy = new ArrayList<>();

    @Relationship(type = "MA_KOLOR_LISCI", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> koloryLisci = new ArrayList<>();

    @Relationship(type = "MA_KOLOR_KWIATOW", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> koloryKwiatow = new ArrayList<>();

    @Relationship(type = "MA_KWIAT", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> kwiaty = new ArrayList<>();

    @Relationship(type = "MA_NAGRODE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> nagrody = new ArrayList<>();

    @Relationship(type = "MA_ODCZYNY", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> odczyny = new ArrayList<>();

    @Relationship(type = "MA_OKRES_KWITNIENIA", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> okresyKwitnienia = new ArrayList<>();

    @Relationship(type = "MA_OKRES_OWOCOWANIA", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> okresyOwocowania = new ArrayList<>();

    @Relationship(type = "MA_OWOC", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> owoce = new ArrayList<>();

    @Relationship(type = "MA_PODGRUPE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> podgrupa = new ArrayList<>();

    @Relationship(type = "MA_POKROJ", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> pokroje = new ArrayList<>();

    @Relationship(type = "MA_SILE_WZROSTU", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> silyWzrostu = new ArrayList<>();

    @Relationship(type = "MA_STANOWISKO", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> stanowiska = new ArrayList<>();

    @Relationship(type = "MA_WALOR", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> walory = new ArrayList<>();

    @Relationship(type = "MA_WILGOTNOSC", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> wilgotnosci = new ArrayList<>();

    @Relationship(type = "MA_ZASTOSOWANIE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> zastosowania = new ArrayList<>();

    @Relationship(type = "MA_ZIMOZIELONOSC_LISCI", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Wlasciwosc> zimozielonosci = new ArrayList<>();

   
}
