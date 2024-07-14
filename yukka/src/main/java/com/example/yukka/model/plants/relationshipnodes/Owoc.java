package com.example.yukka.model.plants.relationshipnodes;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.plants.Roslina;
@Node
public class Owoc {
    @Id @GeneratedValue
    private Long id;
    @Property("nazwa")
    private String name;

    @Relationship(type="ma_rosline", direction=Relationship.Direction.OUTGOING)
    private List<Roslina> plants;

    public Owoc() {
    }

    public List<Roslina> getPlants() {
        return plants;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
