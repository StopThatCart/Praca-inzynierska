package com.example.yukka.model.ogrod.repository;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.ogrod.Ogrod;

public interface OgrodRepository  extends Neo4jRepository<Ogrod, Long> {

    @Query("""
        MATCH path = (u:Uzytkownik{nazwa: $nazwa})-[:MA_OGROD]->(ogrod:Ogrod)-[r1:MA_DZIALKE]->(d:Dzialka) 
        OPTIONAL MATCH path2 = (ogrod)-[r1]->(d)<-[r2:ZASADZONA_NA]-(rosliny)
        
        RETURN ogrod, collect(nodes(path)), collect(relationships(path)),
               collect(nodes(path2)), collect(relationships(path2))
        """)
    Optional<Ogrod> getOgrodOfUzytkownikByNazwa(@Param("nazwa") String nazwa);

    @Query("""
        MATCH path = (u:Uzytkownik{nazwa: $uzytkownikNazwa})-[:MA_OGROD]->(ogrod:Ogrod)
        SET ogrod.nazwa = $ogrodNazwa
        RETURN ogrod, collect(nodes(path)), collect(relationships(path))
        """)
    Optional<Ogrod> setOgrodNazwa(@Param("uzytkownikNazwa") String uzytkownikNazwa, @Param("ogrodNazwa") String ogrodNazwa);


}
