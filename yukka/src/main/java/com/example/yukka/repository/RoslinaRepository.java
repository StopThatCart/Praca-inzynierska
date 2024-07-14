package com.example.yukka.repository;

import java.util.Collection;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.plants.Roslina;

public interface RoslinaRepository extends Neo4jRepository<Roslina, Long> {

       // @Query("MATCH (p:Roslina) RETURN p LIMIT $amount")
    //@Query("MATCH (p:Roslina) RETURN p LIMIT 3")
    //Collection<Roslina> getSomePlants();
    //@Query("MATCH (p:Roslina)-[r:ma_wlasciwosc]->(g:Gleba) RETURN p, collect(r), collect(g) LIMIT $amount")
    
    @Query("MATCH path=(p:Roslina)-[:ma_wlasciwosc]->(g) RETURN p, collect(nodes(path)), collect(relationships(path)) LIMIT $amount")
    //@Query("MATCH (p:Roslina)-[r1:ma_wlasciwosc]->(g:Gleba), (p)-[r2:ma_wlasciwosc]->(w:Wilgotnosc) RETURN p, collect(g), collect(r1), collect(r2), collect(w) LIMIT $amount")
   // @Query("MATCH (p:Roslina)-[r:ma_wlasciwosc]->(wlasciwosc) RETURN p, collect(r), collect(wlasciwosc) LIMIT $amount")
    Collection<Roslina> getSomePlants(@Param("amount") int amount);
    
    @Query("MATCH (r:Roslina)-[:ma_wlasciwosc]->(g:Gleba) WHERE r.name = $name RETURN r, collect(g) as glebus")
    Roslina findRoslinaWithGleba(@Param("name") String name);

    

}
