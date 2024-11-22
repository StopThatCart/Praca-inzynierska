package com.example.yukka.model.dzialka;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.PostLoad;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.example.yukka.model.roslina.Roslina;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RelationshipProperties
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZasadzonaNaReverse {
    @RelationshipId 
    @GeneratedValue
    private Long id;

    @Property("x")
    private int x;

    @Property("y")
    private int y;

    @Property("kolor")
    private String kolor;

    @Property("tekstura")
    private String tekstura;

    @Property("wyswietlanie")
    private String wyswietlanie;

    @Property("notatka")
    private String notatka;

    @Property("obraz")
    private String obraz;

    @Property("tabX")
    private int[] tabX;

    @Property("tabY")
    private int[] tabY;

    
    @TargetNode
    private Roslina roslina;


    @JsonIgnore
    private Set<Pozycja> pozycje;

    @PostLoad
    private void initPozycje() {
        pozycje = new HashSet<Pozycja>();
        for (int i = 0; i < tabX.length; i++) {
            pozycje.add(new Pozycja(tabX[i], tabY[i]));
        }
    }


    @JsonIgnore
    public boolean equalsRoslina(Roslina roslina) {
        return this.roslina.getRoslinaId().equals(roslina.getRoslinaId());
    }

    @JsonIgnore
    public boolean equalsRoslina(String roslinaId) {
        Roslina roslina = this.roslina;
        if(roslina == null) {
            return false;
        } 

        return this.roslina.getRoslinaId().equals(roslinaId);
    }

    @JsonIgnore
    public boolean equalsRoslina(ZasadzonaNaReverse zasadzinaNaReverse) {
        Roslina roslina = this.roslina;
        Roslina roslinaWhatever = zasadzinaNaReverse.getRoslina();
        if(roslina == null || roslinaWhatever == null) {
            return false;
        } 
        return this.roslina.getRoslinaId().equals(roslinaWhatever.getRoslinaId());
    }

}
