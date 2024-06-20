package com.example.yukka.model;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
public class Plant {
    @Id @GeneratedValue
    private Long id;
    @Property("name")
    private String name;
    @Property("latin_name")
    private String latinName;
    @Property("description")
    private String description;
    @Property("min_height")
    private double minHeight;
    @Property("max_height")
    private double maxHeight;
    @Property("image")
    private String image;

    @Relationship(type = "has_soil", direction= Relationship.Direction.OUTGOING)
    private List<Soil> soils;
    
    

    public Plant() {
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

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(double minHeight) {
        this.minHeight = minHeight;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Soil> getSoils() {
        return soils;
    }


    

}
