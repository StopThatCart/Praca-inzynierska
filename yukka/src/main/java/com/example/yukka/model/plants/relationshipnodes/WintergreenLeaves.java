package com.example.yukka.model.plants.relationshipnodes;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.plants.Plant;
@Node
public class WintergreenLeaves {
    @Id @GeneratedValue
    private Long id;
    @Property("name")
    private String name;

    @Relationship(type="has_plant", direction=Relationship.Direction.OUTGOING)
    private List<Plant> plants;

    public WintergreenLeaves() {
    }

    public List<Plant> getPlants() {
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
