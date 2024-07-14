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
    private String name;
    @Property("nazwa_lacinska")
    private String latinName;
    @Property("opis")
    private String description;
    @Property("wysokosc_min")
    private double minHeight;
    @Property("wysokosc_max")
    private double maxHeight;
    @Property("obraz")
    private String image;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Forma> formy;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Gleba> gleby;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Grupa> grupa;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Kolor> kolory;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Kwiat> kwiaty;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Nagroda> nagrody;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Odczyn> odczyny;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Okres> okresy;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Owoc> owoce;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Podgrupa> podgrupa;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Pokroj> pokroje;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<SilaWzrostu> silyWzrostu;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Stanowisko> stanowiska;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Walor> walory;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Wilgotnosc> wilgotnosci;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Zastowowanie> zastosowania;

    @Relationship(type = "ma_wlasciwosc", direction = Relationship.Direction.OUTGOING)
    private List<Zimozielonosc> zimozielonosci;

    public Roslina() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(double minHeight) {
        this.minHeight = minHeight;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
  
    public List<Forma> getFormy() {
        return formy;
    }

    public List<Grupa> getGrupa() {
        return grupa;
    }

    public List<Kolor> getKolory() {
        return kolory;
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

    public List<Okres> getOkresy() {
        return okresy;
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

    public List<Zastowowanie> getZastosowania() {
        return zastosowania;
    }

    public List<Zimozielonosc> getZimozielonosci() {
        return zimozielonosci;
    }
    
    public List<Gleba> getGleby() {
        return gleby;
    }

    public List<Wilgotnosc> getWilgotnosci() {
        return wilgotnosci;
    }
}
