package com.example.yukka.model.dzialka.repository;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.dzialka.Dzialka;

public interface DzialkaRepository extends Neo4jRepository<Dzialka, Long> {

    @Query("""
        MATCH (u:Uzytkownik{email: $email})-[:MA_OGROD]->
                (:Ogrod)-[:MA_DZIALKE]->
                (d:Dzialka{numer: $numerDzialki})
                <-[r:ZASADZONA_NA {x: $x, y: $y}]-(rosliny)
        RETURN count(r) > 0 AS isOccupied
            """)
    boolean checkIfCoordinatesAreOccupied(@Param("email") String email, 
        @Param("numerDzialki") int numerDzialki,
        @Param("x") int x, @Param("y") int y);


    @Query("""
            MATCH path = (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki}) 
            OPTIONAL MATCH (d)<-[r1:ZASADZONA_NA]-(rosliny)
            RETURN d, collect(r1), collect(rosliny), collect(nodes(path)), collect(relationships(path))
            """)
    Optional<Dzialka> getDzialkaByNumer(@Param("email") String email, @Param("numerDzialki") int numerDzialki);

    @Query("""
            MATCH path = (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka) 
            RETURN d, collect(nodes(path)), collect(relationships(path))
            """)
    Optional<Dzialka> getDzialkiOfUzytkownik(@Param("email") String email);


    // Na razie zwykła roślina. Rośliny użytkownika na potem
    // TODO: zapisywanie obrazów dla rośliny z katalogu dodanej do ogrodu przez użytkownika
    @Query("""
        MATCH (u:Uzytkownik{email: $email})-[:MA_OGROD]->
              (:Ogrod)-[:MA_DZIALKE]->
              (d:Dzialka{numer: $numerDzialki})
        MATCH (roslina:Roslina{nazwaLacinska: $nazwaLacinska})

        OPTIONAL MATCH (d)<-[existing:ZASADZONA_NA]-(existingRoslina:Roslina)
        WHERE   existing.x = $x AND existing.y = $y 
                AND existingRoslina.nazwaLacinska <> $nazwaLacinska
        DELETE existing

        WITH d, roslina
        CREATE (d)<-[r:ZASADZONA_NA {x: $x, y: $y, obraz: coalesce($obraz, roslina.obraz)}]-(roslina)

        WITH d
        MATCH path = (d)<-[zasadzone:ZASADZONA_NA]-(rosliny)
              
        RETURN d, collect(nodes(path)), collect(relationships(path))
            """)
    Dzialka saveRoslinaToDzialka(@Param("email") String email,
        @Param("numerDzialki") int numerDzialki, 
        @Param("x") int x, @Param("y") int y, 
        @Param("obraz") String obraz,
        @Param("nazwaLacinska") String nazwaLacinska);

        

        @Query("""
            MATCH (u:Uzytkownik{email: $email})-[:MA_OGROD]->
                  (:Ogrod)-[:MA_DZIALKE]->
                  (d:Dzialka{numer: $numerDzialki})
    
            MATCH (d)<-[existing:ZASADZONA_NA{x: $x, y: $y}]-(existingRoslina:Roslina)
            DELETE existing

            WITH d
            MATCH path = (d)<-[zasadzone:ZASADZONA_NA]-(rosliny)
            
            RETURN d, collect(nodes(path)), collect(relationships(path))
                """)
        Dzialka removeRoslinaFromDzialka(@Param("email") String email,
            @Param("numerDzialki") int numerDzialki, 
            @Param("x") int x, @Param("y") int y);
}
