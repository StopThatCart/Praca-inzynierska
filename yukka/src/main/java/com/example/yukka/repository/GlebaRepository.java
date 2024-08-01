package com.example.yukka.repository;

import java.util.Collection;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.example.yukka.model.roslina.relationshipnodes.Gleba;

public interface GlebaRepository extends Neo4jRepository<Gleba, Long> {
    @Query("MATCH (s:Gleba) RETURN s")
    Collection<Gleba> getAllSoils();
}
