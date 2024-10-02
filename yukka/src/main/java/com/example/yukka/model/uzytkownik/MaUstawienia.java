package com.example.yukka.model.uzytkownik;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import lombok.Builder;
import lombok.Getter;

@RelationshipProperties
@Getter
@Builder
public class MaUstawienia {
    @Id @GeneratedValue
    private Long id;

    @TargetNode
    private Ustawienia ustawienia;

}
