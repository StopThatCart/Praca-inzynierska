package com.example.yukka.model.ogrod;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.dzialka.Dzialka;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @Property("nazwa")
    private String nazwa;

    @JsonIgnore
    @Relationship(type = "MA_OGROD", direction = Relationship.Direction.INCOMING)
    private Uzytkownik uzytkownik;
    @JsonIgnore
    @Relationship(type = "MA_DZIALKE", direction = Relationship.Direction.OUTGOING)
    private List<Dzialka> dzialki;


    @Override
    public String toString() {
        return "Ogrod{" +
                "id=" + id +
                ", nazwa='" + nazwa + '\'' +
                ", uzytkownik=" + uzytkownik.getNazwa() +
                ", dzialki=" + dzialki.size() +
                '}';
    }

}
