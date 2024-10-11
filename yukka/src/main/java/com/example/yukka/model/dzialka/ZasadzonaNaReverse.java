package com.example.yukka.model.dzialka;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.example.yukka.model.roslina.Roslina;

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
    private int[] tabx;

    @Property("tabY")
    private int[] taby;

    
    @TargetNode
    private Roslina roslina;

}
