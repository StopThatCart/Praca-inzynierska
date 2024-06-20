package com.example.yukka.repository;

import java.util.Collection;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.example.yukka.model.plants.relationshipnodes.Soil;

public interface SoilRepository extends Neo4jRepository<Soil, Long> {
    @Query("MATCH (s:Soil) RETURN s")
    Collection<Soil> getAllSoils();
}
