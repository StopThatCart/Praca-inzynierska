package com.example.yukka.repository;

import java.util.Collection;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.plants.Plant;

public interface PlantRepository extends Neo4jRepository<Plant, Long> {

    @Query("MATCH (p:Plant) RETURN p LIMIT $amount")
    Collection<Plant> getSomePlants(@Param("amount") int amount);

}
