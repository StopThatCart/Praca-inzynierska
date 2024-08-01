package com.example.yukka.model.roslina;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.Getter;
import lombok.NoArgsConstructor;



@Node
@NoArgsConstructor
@Getter
public class Roslina {
    @Id @GeneratedValue
    private Long id;
    @Property("nazwa")
    private String nazwa;
    @Property("nazwaLacinska")
    private String nazwaLacinska;
    @Property("opis")
    private String opis;
    @Property("wysokoscMin")
    private double wysokoscMax;
    @Property("wysokoscMax")
    private double wysokoscMin;
    @Property("obraz")
    private String obraz;

    @Relationship(type = "MA_FORME", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> formy;

    @Relationship(type = "MA_GLEBE", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> gleby;

    @Relationship(type = "MA_GRUPE", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> grupy;

    @Relationship(type = "MA_KOLOR_LISCI", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> koloryLisci;

    @Relationship(type = "MA_KOLOR_KWIATOW", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> koloryKwiatow;

    @Relationship(type = "MA_KWIAT", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> kwiaty;

    @Relationship(type = "MA_NAGRODE", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> nagrody;

    @Relationship(type = "MA_ODCZYNY", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> odczyny;

    @Relationship(type = "MA_OKRES_KWITNIENIA", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> okresyKwitnienia;

    @Relationship(type = "MA_OKRES_OWOCOWANIA", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> okresyOwocowania;

    @Relationship(type = "MA_OWOC", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> owoce;

    @Relationship(type = "MA_PODGRUPE", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> podgrupa;

    @Relationship(type = "MA_POKROJ", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> pokroje;

    @Relationship(type = "MA_SILE_WZROSTU", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> silyWzrostu;

    @Relationship(type = "MA_STANOWISKO", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> stanowiska;

    @Relationship(type = "MA_WALOR", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> walory;

    @Relationship(type = "MA_WILGOTNOSC", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> wilgotnosci;

    @Relationship(type = "MA_ZASTOSOWANIE", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> zastosowania;

    @Relationship(type = "MA_ZIMOZIELONOSC_LISCI", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> zimozielonosci;

   
}
