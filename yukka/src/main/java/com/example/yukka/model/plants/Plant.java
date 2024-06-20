package com.example.yukka.model.plants;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.plants.relationshipnodes.Appeal;
import com.example.yukka.model.plants.relationshipnodes.Color;
import com.example.yukka.model.plants.relationshipnodes.Flower;
import com.example.yukka.model.plants.relationshipnodes.Form;
import com.example.yukka.model.plants.relationshipnodes.Fruit;
import com.example.yukka.model.plants.relationshipnodes.GrowthStrength;
import com.example.yukka.model.plants.relationshipnodes.Humidity;
import com.example.yukka.model.plants.relationshipnodes.Period;
import com.example.yukka.model.plants.relationshipnodes.Ph;
import com.example.yukka.model.plants.relationshipnodes.Position;
import com.example.yukka.model.plants.relationshipnodes.Shape;
import com.example.yukka.model.plants.relationshipnodes.Subgroup;
import com.example.yukka.model.plants.relationshipnodes.Use;
import com.example.yukka.model.plants.relationshipnodes.WintergreenLeaves;

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

    @Relationship(type = "has_group", direction= Relationship.Direction.OUTGOING)
    private List<Form> groups;

    @Relationship(type = "has_subgroup", direction= Relationship.Direction.OUTGOING)
    private List<Subgroup> subgroups;

    @Relationship(type = "has_form", direction= Relationship.Direction.OUTGOING)
    private List<Form> forms;

    @Relationship(type = "has_growth_strength", direction= Relationship.Direction.OUTGOING)
    private List<GrowthStrength> growthStrengths;

    @Relationship(type = "has_shape", direction= Relationship.Direction.OUTGOING)
    private List<Shape> shapes;

    @Relationship(type = "has_leaves_color", direction= Relationship.Direction.OUTGOING)
    private List<Color> leavesColors;

    @Relationship(type = "has_flower_color", direction= Relationship.Direction.OUTGOING)
    private List<Color> flowerColors;

    @Relationship(type = "has_wintergreen_leaves", direction= Relationship.Direction.OUTGOING)
    private List<WintergreenLeaves> wintergreenLeaves;

    @Relationship(type = "has_fruit", direction= Relationship.Direction.OUTGOING)
    private List<Fruit> fruits;

    @Relationship(type = "has_position", direction= Relationship.Direction.OUTGOING)
    private List<Position> positions;

    @Relationship(type = "has_humidity", direction= Relationship.Direction.OUTGOING)
    private List<Humidity> humidities;

    @Relationship(type = "has_ph", direction= Relationship.Direction.OUTGOING)
    private List<Ph> phs;

    @Relationship(type = "has_soil", direction= Relationship.Direction.OUTGOING)
    private List<Flower> soils;

    @Relationship(type = "has_appeal", direction= Relationship.Direction.OUTGOING)
    private List<Appeal> appeals;

    @Relationship(type = "has_use", direction= Relationship.Direction.OUTGOING)
    private List<Use> uses;

    @Relationship(type = "has_award", direction= Relationship.Direction.OUTGOING)
    private List<Flower> awards;

    @Relationship(type = "has_flower", direction= Relationship.Direction.OUTGOING)
    private List<Flower> flowers;

    @Relationship(type = "has_flowering_period", direction= Relationship.Direction.OUTGOING)
    private List<Period> floweringPeriods;

    @Relationship(type = "has_fruiting_period", direction= Relationship.Direction.OUTGOING)
    private List<Period> fruitingPeriods;

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

    public List<Flower> getSoils() {
        return soils;
    }

    public List<Form> getGroups() {
        return groups;
    }

    public List<Subgroup> getSubgroups() {
        return subgroups;
    }

    public List<Form> getForms() {
        return forms;
    }

    public List<GrowthStrength> getGrowthStrengths() {
        return growthStrengths;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public List<Color> getLeavesColors() {
        return leavesColors;
    }

    public List<Color> getFlowerColors() {
        return flowerColors;
    }

    public List<WintergreenLeaves> getWintergreenLeaves() {
        return wintergreenLeaves;
    }

    public List<Fruit> getFruits() {
        return fruits;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public List<Humidity> getHumidities() {
        return humidities;
    }

    public List<Ph> getPhs() {
        return phs;
    }

    public List<Appeal> getAppeals() {
        return appeals;
    }

    public List<Use> getUses() {
        return uses;
    }

    public List<Flower> getAwards() {
        return awards;
    }

    public List<Flower> getFlowers() {
        return flowers;
    }

    public List<Period> getFloweringPeriods() {
        return floweringPeriods;
    }

    public List<Period> getFruitingPeriods() {
        return fruitingPeriods;
    }

}
