package com.example.yukka.model.roslina.wlasciwosc;

import java.util.List;

import org.springframework.data.neo4j.core.schema.DynamicLabels;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.roslina.Roslina;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Node
@Getter
@Setter
@NoArgsConstructor
public class Wlasciwosc {

    @DynamicLabels
    private List<String> labels;

    @Id @GeneratedValue
    private Long id;
    @Property("nazwa")
    private String nazwa;

    @Relationship(type="MA_ROSLINE", direction=Relationship.Direction.OUTGOING)
    private List<Roslina> plants;

    // Na razie nieużywane
    /* 
    public List<Roslina> getPlants() {
        return plants;
    }
    */

    public Wlasciwosc(List<String> labels, String nazwa) {
        this.labels = labels;
        this.nazwa = nazwa;
    }
     

      
    public String getLabels() {
        if(labels.isEmpty()) return null;
        return  labels.get(0);
    }
    


}
