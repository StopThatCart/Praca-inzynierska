package com.example.yukka.model.dzialka;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.ogrod.Ogrod;
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
public class Dzialka {

    @Id @GeneratedValue
    private Long id;

    @Property("numer")
    private int numer;

  //  @Property("pola")
  //  private int[][] pola;

  //  @Property("uzytkownik")
 //   private Uzytkownik uzytkownik;

    @JsonIgnore
    @Relationship(type = "MA_DZIALKE", direction = Relationship.Direction.INCOMING)
    private Ogrod ogrod;

    // Zwykla roslina
    @Relationship(type = "ZASADZONA_NA", direction = Relationship.Direction.INCOMING)
    private List<ZasadzonaNaReverse> zasadzoneRosliny;
    

    // Customowa roslina


    public ZasadzonaNaReverse getZasadzonaNaByCoordinates(int x, int y) {
        for (ZasadzonaNaReverse zasadzonaNa : zasadzoneRosliny) {
            if (zasadzonaNa.getX() == x && zasadzonaNa.getY() == y) {
                return zasadzonaNa;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        
        return "Dzialka{" +
                "id=" + id +
                ", numer=" + numer +
                ", ogrod=" + ogrod.getNazwa() +
                ", zasadzoneRosliny=" + zasadzoneRosliny.size() +
                '}';
    }
}
