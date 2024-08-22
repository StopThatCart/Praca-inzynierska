package com.example.yukka.model.dzialka;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.uzytkownik.Uzytkownik;

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
public class Dzialka {

    
    @Id @GeneratedValue
    private Long id;

    @Property("numer")
    private int numer;

    @Property("pola")
    private int[][] pola;

    @Property("uzytkownik")
    private Uzytkownik uzytkowni;

    // Zwykla roslina
    @Relationship(type = "ZASADZONA_NA", direction = Relationship.Direction.INCOMING)
    private List<ZasadzonaNa> roslina;

    // Customowa roslina
}
