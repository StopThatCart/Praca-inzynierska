package com.example.yukka.model.plants;

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
    @Property("nazwa_lacinska")
    private String nazwaLacinska;
    @Property("opis")
    private String opis;
    @Property("wysokosc_min")
    private double wysokoscMax;
    @Property("wysokosc_max")
    private double wysokoscMin;
    @Property("obraz")
    private String obraz;

    @Relationship(type = "ma_forme", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> formy;

    @Relationship(type = "ma_glebe", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> gleby;

    @Relationship(type = "ma_grupe", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> grupy;

    @Relationship(type = "ma_kolor_lisci", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> koloryLisci;
    @Relationship(type = "ma_kolor_wiatow", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> koloryKwiatow;

    @Relationship(type = "ma_kwiat", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> kwiaty;

    @Relationship(type = "ma_nagrode", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> nagrody;

    @Relationship(type = "ma_odczyn", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> odczyny;

    @Relationship(type = "ma_okres_kwitnienia", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> okresyKwitnienia;
    @Relationship(type = "ma_okres_owocowania", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> okresyOwocowania;

    @Relationship(type = "ma_owoc", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> owoce;

    @Relationship(type = "ma_podgrupe", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> podgrupa;

    @Relationship(type = "ma_pokroj", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> pokroje;

    @Relationship(type = "ma_sile_wzrostu", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> silyWzrostu;

    @Relationship(type = "ma_stanowisko", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> stanowiska;

    @Relationship(type = "ma_walor", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> walory;

    @Relationship(type = "ma_wilgotnosc", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> wilgotnosci;

    @Relationship(type = "ma_zastosowanie", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> zastosowania;

    @Relationship(type = "ma_zimozielonosc_lisci", direction = Relationship.Direction.OUTGOING)
    private List<Wlasciwosc> zimozielonosci;

   
}
