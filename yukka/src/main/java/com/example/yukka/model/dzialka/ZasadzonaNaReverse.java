package com.example.yukka.model.dzialka;

import java.util.ArrayList;
import java.util.List;

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

    @Property("obraz")
    private String obraz;

    @Property("tabX")
    private int[] tabX;

    @Property("tabY")
    private int[] tabY;

    
    @TargetNode
    private Roslina roslina;


    @JsonIgnore
    private List<Pozycja> pozycje;

    @PostLoad
    private void initPozycje() {
        pozycje = new ArrayList<>();
        for (int i = 0; i < tabX.length; i++) {
            pozycje.add(new Pozycja(tabX[i], tabY[i]));
        }
    }

}
