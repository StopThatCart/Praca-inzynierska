package com.example.yukka.model.ogrod.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.ogrod.Ogrod;


/**
 * Interfejs OgrodRepository rozszerza Neo4jRepository i zapewnia metody do operacji na encjach Ogrod.
 * 
 * <p>Interfejs ten zawiera niestandardowe zapytania do bazy danych Neo4j, które umożliwiają wyszukiwanie,
 * pobieranie oraz aktualizowanie danych dotyczących ogrodów i powiązanych z nimi encji.</p>
 * 
 * <p>Metody w tym interfejsie wykorzystują adnotację @Query do definiowania zapytań Cypher, które są wykonywane
 * na bazie danych Neo4j.</p>
 * 
 * <p>Interfejs ten jest częścią warstwy dostępu do danych aplikacji i jest używany do komunikacji z bazą danych
 * w celu wykonywania operacji CRUD na encjach Ogrod.</p>
 */
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
        RETURN ogrod
        """)
    Optional<Ogrod> setOgrodNazwa(@Param("uzytkownikNazwa") String uzytkownikNazwa, @Param("ogrodNazwa") String ogrodNazwa);


}
