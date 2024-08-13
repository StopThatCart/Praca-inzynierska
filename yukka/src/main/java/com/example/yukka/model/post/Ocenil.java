package com.example.yukka.model.post;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import lombok.Builder;
import lombok.Getter;

@RelationshipProperties
@Getter
@Builder
public class Ocenil {
    @Id @GeneratedValue
    private Long id;

    @Property("lubi")
    private boolean lubi;

    // Uwaga: Może nie zadziałać
    @TargetNode
    private Oceniany oceniany;

}
