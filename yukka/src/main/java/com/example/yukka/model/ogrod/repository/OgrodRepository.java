package com.example.yukka.model.ogrod.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.ogrod.Ogrod;
import com.example.yukka.model.social.post.Post;

public interface OgrodRepository  extends Neo4jRepository<Ogrod, Long> {

        @Query(value = """
        MATCH path = (ust:Ustawienia)<-[:MA_USTAWIENIA]-(u:Uzytkownik)-[:MA_OGROD]->(ogrod:Ogrod)-[r1:MA_DZIALKE]->(d:Dzialka) 
        WHERE ust.ogrod_pokaz AND ($szukaj IS NULL OR ogrod.nazwa CONTAINS $szukaj OR u.nazwa CONTAINS $szukaj)

        OPTIONAL MATCH path2 = (ogrod)-[r1]->(d)<-[r2:ZASADZONA_NA]-(rosliny)

        RETURN ogrod, collect(nodes(path)), collect(relationships(path)),
        collect(nodes(path2)), collect(relationships(path2))
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
       countQuery = """
        MATCH path = (ust:Ustawienia)<-[:MA_USTAWIENIA]-(u:Uzytkownik)-[:MA_OGROD]->(ogrod:Ogrod)-[r1:MA_DZIALKE]->(d:Dzialka) 
        WHERE ust.ogrod_pokaz AND ($szukaj IS NULL OR ogrod.nazwa CONTAINS $szukaj OR u.nazwa CONTAINS $szukaj)
        RETURN count(ogrod)
        """)
    Page<Ogrod> findAllOgrody(@Param("szukaj") String szukaj, Pageable pageable);


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
