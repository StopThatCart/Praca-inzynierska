package com.example.yukka.model.ogrod;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.dzialka.Dzialka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Node
public class Ogrod {

    @Id @GeneratedValue
    private Long id;

    @Relationship(type = "MA_DZIALKE", direction = Relationship.Direction.OUTGOING)
    private List<Dzialka> dzialki;

}
