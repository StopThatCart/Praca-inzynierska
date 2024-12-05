package com.example.yukka.model.social.models;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@RelationshipProperties
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OcenilReverse {
    @Id @GeneratedValue
    private Long id;

    @Property("lubi")
    private boolean lubi;

    @TargetNode
    private Uzytkownik uzytkownik;
}
