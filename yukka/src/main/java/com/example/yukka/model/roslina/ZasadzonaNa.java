package com.example.yukka.model.roslina;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@RelationshipProperties
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZasadzonaNa {
    @Id @GeneratedValue
    private Long id;

    @Property("x")
    private int x;

    @Property("y")
    private int y;
    
   // @TargetNode
   // private Dzialka dzialka;

}
