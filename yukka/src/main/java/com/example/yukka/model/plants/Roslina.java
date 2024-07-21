package com.example.yukka.model.plants;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.plants.relationshipnodes.Forma;
import com.example.yukka.model.plants.relationshipnodes.Gleba;
import com.example.yukka.model.plants.relationshipnodes.Grupa;
import com.example.yukka.model.plants.relationshipnodes.Kolor;
import com.example.yukka.model.plants.relationshipnodes.Kwiat;
import com.example.yukka.model.plants.relationshipnodes.Nagroda;
import com.example.yukka.model.plants.relationshipnodes.Odczyn;
import com.example.yukka.model.plants.relationshipnodes.Okres;
import com.example.yukka.model.plants.relationshipnodes.Owoc;
import com.example.yukka.model.plants.relationshipnodes.Podgrupa;
import com.example.yukka.model.plants.relationshipnodes.Pokroj;
import com.example.yukka.model.plants.relationshipnodes.SilaWzrostu;
import com.example.yukka.model.plants.relationshipnodes.Stanowisko;
import com.example.yukka.model.plants.relationshipnodes.Walor;
import com.example.yukka.model.plants.relationshipnodes.Wilgotnosc;
import com.example.yukka.model.plants.relationshipnodes.Zastowowanie;
import com.example.yukka.model.plants.relationshipnodes.Zimozielonosc;


@Node
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
    private List<Forma> formy;

    @Relationship(type = "ma_glebe", direction = Relationship.Direction.OUTGOING)
    private List<Gleba> gleby;

    @Relationship(type = "ma_grupe", direction = Relationship.Direction.OUTGOING)
    private List<Grupa> grupy;

    @Relationship(type = "ma_kolor_lisci", direction = Relationship.Direction.OUTGOING)
    private List<Kolor> koloryLisci;
    @Relationship(type = "ma_kolor_wiatow", direction = Relationship.Direction.OUTGOING)
    private List<Kolor> koloryKwiatow;

    @Relationship(type = "ma_kwiat", direction = Relationship.Direction.OUTGOING)
    private List<Kwiat> kwiaty;

    @Relationship(type = "ma_nagrode", direction = Relationship.Direction.OUTGOING)
    private List<Nagroda> nagrody;

    @Relationship(type = "ma_odczyn", direction = Relationship.Direction.OUTGOING)
    private List<Odczyn> odczyny;

    @Relationship(type = "ma_okres_kwitnienia", direction = Relationship.Direction.OUTGOING)
    private List<Okres> okresyKwitnienia;
    @Relationship(type = "ma_okres_owocowania", direction = Relationship.Direction.OUTGOING)
    private List<Okres> okresyOwocowania;

    @Relationship(type = "ma_owoc", direction = Relationship.Direction.OUTGOING)
    private List<Owoc> owoce;

    @Relationship(type = "ma_podgrupe", direction = Relationship.Direction.OUTGOING)
    private List<Podgrupa> podgrupa;

    @Relationship(type = "ma_pokroj", direction = Relationship.Direction.OUTGOING)
    private List<Pokroj> pokroje;

    @Relationship(type = "ma_sile_wzrostu", direction = Relationship.Direction.OUTGOING)
    private List<SilaWzrostu> silyWzrostu;

    @Relationship(type = "ma_stanowisko", direction = Relationship.Direction.OUTGOING)
    private List<Stanowisko> stanowiska;

    @Relationship(type = "ma_walor", direction = Relationship.Direction.OUTGOING)
    private List<Walor> walory;

    @Relationship(type = "ma_wilgotnosc", direction = Relationship.Direction.OUTGOING)
    private List<Wilgotnosc> wilgotnosci;

    @Relationship(type = "ma_zastosowanie", direction = Relationship.Direction.OUTGOING)
    private List<Zastowowanie> zastosowania;

    @Relationship(type = "ma_zimozielonosc_lisci", direction = Relationship.Direction.OUTGOING)
    private List<Zimozielonosc> zimozielonosci;

    public Roslina() {
    }

    public Long getId() {
        return id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String name) {
        this.nazwa = name;
    }

    public String getNazwaLacinska() {
        return nazwaLacinska;
    }

    public void setNazwaLacinska(String latinName) {
        this.nazwaLacinska = latinName;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String description) {
        this.opis = description;
    }

    public double getWysokoscMax() {
        return wysokoscMax;
    }

    public void setWysokoscMax(double minHeight) {
        this.wysokoscMax = minHeight;
    }

    public double getWysokoscMin() {
        return wysokoscMin;
    }

    public void setWysokoscMin(double maxHeight) {
        this.wysokoscMin = maxHeight;
    }

    public String getObraz() {
        return obraz;
    }

    public void setObraz(String image) {
        this.obraz = image;
    }

    public List<Forma> getFormy() {
        return formy;
    }

    public List<Gleba> getGleby() {
        return gleby;
    }

    public List<Grupa> getGrupy() {
        return grupy;
    }

    public List<Kolor> getKoloryLisci() {
        return koloryLisci;
    }

    public List<Kolor> getKoloryKwiatow() {
        return koloryKwiatow;
    }

    public List<Kwiat> getKwiaty() {
        return kwiaty;
    }

    public List<Nagroda> getNagrody() {
        return nagrody;
    }

    public List<Odczyn> getOdczyny() {
        return odczyny;
    }

    public List<Okres> getOkresyKwitnienia() {
        return okresyKwitnienia;
    }

    public List<Okres> getOkresyOwocowania() {
        return okresyOwocowania;
    }

    public List<Owoc> getOwoce() {
        return owoce;
    }

    public List<Podgrupa> getPodgrupa() {
        return podgrupa;
    }

    public List<Pokroj> getPokroje() {
        return pokroje;
    }

    public List<SilaWzrostu> getSilyWzrostu() {
        return silyWzrostu;
    }

    public List<Stanowisko> getStanowiska() {
        return stanowiska;
    }

    public List<Walor> getWalory() {
        return walory;
    }

    public List<Wilgotnosc> getWilgotnosci() {
        return wilgotnosci;
    }

    public List<Zastowowanie> getZastosowania() {
        return zastosowania;
    }

    public List<Zimozielonosc> getZimozielonosci() {
        return zimozielonosci;
    }

    
  
}
