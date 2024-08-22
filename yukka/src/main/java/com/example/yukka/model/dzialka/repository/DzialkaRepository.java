package com.example.yukka.model.dzialka.repository;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.dzialka.Dzialka;

public interface DzialkaRepository extends Neo4jRepository<Dzialka, Long> {

    @Query("""
        MATCH (d:Dzialka)<-[r:ZASADZONA_NA {x: $x, y: $y}]-(rosliny)
        RETURN count(r) > 0 AS isOccupied
            """)
    boolean checkIfCoordinatesAreOccupied(@Param("x") int x, @Param("y") int y);


    @Query("""
            MATCH path = (u:Uzytkownik{email: email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numer}) 
            OPTIONAL MATCH (d)<-[r1:ZASADZONA_NA]-(rosliny)
            RETURN d, r1, rosliny, collect(nodes(path)), collect(relationships(path))
            """)
    Optional<Dzialka> getDzialkaByNumer(@Param("numer") int numer, @Param("email") String email);

    @Query("""
            MATCH path = (u:Uzytkownik{email: email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka) 
            RETURN d, collect(nodes(path)), collect(relationships(path))
            """)
    Optional<Dzialka> getDzialkiOfUzytkownik(@Param("email") String email);
}
