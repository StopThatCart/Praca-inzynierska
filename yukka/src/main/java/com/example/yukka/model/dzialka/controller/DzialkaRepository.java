package com.example.yukka.model.dzialka.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.dzialka.Dzialka;

/**
 * Repozytorium dla operacji na encji Dzialka w bazie danych Neo4j.
 * 
 * <p>Interfejs ten rozszerza Neo4jRepository i zapewnia metody do wykonywania
 * zapytań Cypher na bazie danych Neo4j. Metody te umożliwiają sprawdzanie,
 * pobieranie, zapisywanie oraz aktualizowanie danych dotyczących działek
 * i roślin na nich zasadzonych.</p>
 * 
 * <p>Metody w tym repozytorium wykorzystują adnotacje @Query do definiowania
 * zapytań Cypher, które są wykonywane na bazie danych. Parametry zapytań
 * są przekazywane za pomocą adnotacji @Param.</p>
 * 
 * <p>Przykładowe operacje obejmują:</p>
 * <ul>
 *   <li>Sprawdzanie, czy dane współrzędne są zajęte przez roślinę.</li>
 *   <li>Pobieranie działki na podstawie numeru działki i adresu email użytkownika.</li>
 *   <li>Zapisywanie rośliny na działce.</li>
 *   <li>Aktualizowanie pozycji rośliny na działce.</li>
 *   <li>Usuwanie rośliny z działki.</li>
 * </ul>
 * 
 * <p>Repozytorium to jest częścią aplikacji zarządzającej ogrodami i działkami,
 * umożliwiającej użytkownikom zarządzanie roślinami na ich działkach.</p>
 */
public interface DzialkaRepository extends Neo4jRepository<Dzialka, Long> {
        @Query("""
        MATCH (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki})
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
        Optional<Dzialka> getDzialkaByNumerWithoutRelations(@Param("email") String email, @Param("numerDzialki") int numerDzialki);

        @Query("""
        MATCH path = (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki}) 
        OPTIONAL MATCH (d)<-[r1:ZASADZONA_NA]-(rosliny)-[r]-(w)
        WHERE w:Wlasciwosc OR w:UzytkownikWlasciwosc
        RETURN d, collect(r1), collect(rosliny), collect(r), collect(w), collect(nodes(path)), collect(relationships(path))
        """)
        Optional<Dzialka> getDzialkaByNumer(@Param("email") String email, @Param("numerDzialki") int numerDzialki);

        @Query("""
        MATCH path = (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki}) 
        OPTIONAL MATCH (d)<-[r1:ZASADZONA_NA]-(roslina:Roslina)-[r]-(w:Wlasciwosc)
        WHERE r1.x = $x AND r1.y = $y
        RETURN d, collect(r1), collect(roslina), collect(r), collect(w), collect(nodes(path)), collect(relationships(path))
        """)
        Optional<Dzialka> getRoslinaInDzialka(@Param("email") String email, @Param("numerDzialki") int numerDzialki, int x, int y);

        
        @Query("""
        MATCH path = (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki}) 
        OPTIONAL MATCH (d)<-[r1:ZASADZONA_NA]-(roslina:Roslina{ roslinaId: $roslinaId })-[r]-(w:Wlasciwosc)
        RETURN d, collect(r1), collect(roslina), collect(r), collect(w), collect(nodes(path)), collect(relationships(path))
        """)
        Optional<Dzialka> getRoslinaInDzialka(@Param("email") String email, @Param("numerDzialki") int numerDzialki, String roslinaId);

        @Query("""
        MATCH path = (u:Uzytkownik{nazwa: $nazwa})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka) 
        RETURN d, collect(nodes(path)), collect(relationships(path))
        ORDER BY d.numer
        """)
        List<Dzialka> getDzialkiOfUzytkownikByNazwa(@Param("nazwa") String nazwa);

        @Query("""
        MATCH path = (u:Uzytkownik{nazwa: $nazwa})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka) 
        OPTIONAL MATCH path2 = (d)<-[r:ZASADZONA_NA]-(ros:Roslina)
        
        RETURN d, collect(nodes(path)), collect(relationships(path)), 
                collect(nodes(path2)),collect(relationships(path2))
        ORDER BY d.numer
        """)
        List<Dzialka> getPozycjeInDzialki(@Param("nazwa") String nazwa);

        @Query("""
        MATCH path = (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki})
        SET d.nazwa = $nazwa
        RETURN d
                """)
        Dzialka renameDzialka(@Param("email") String email, @Param("numerDzialki") int numerDzialki, @Param("nazwa") String nazwa);

        @Query("""
        MATCH (u:Uzytkownik{email: $email})-[:MA_OGROD]->
              (:Ogrod)-[:MA_DZIALKE]->
              (d:Dzialka{numer: $numerDzialki})

        MATCH (roslina:Roslina {roslinaId: $roslinaId})

        OPTIONAL MATCH (d)<-[existing:ZASADZONA_NA]-(existingRoslina)
        WHERE   existing.x = $x AND existing.y = $y 
                AND existingRoslina <> roslina
        DELETE existing

        WITH d, roslina
        CREATE (d)<-[r:ZASADZONA_NA {x: $x, y: $y, tabX: $tabX, tabY: $tabY, 
                kolor: $kolor, tekstura: $tekstura, obraz: $obraz, wyswietlanie: $wyswietlanie}]-(roslina)

        WITH d
        MATCH path = (d)<-[zasadzone:ZASADZONA_NA]-(rosliny)
              
        RETURN d, collect(nodes(path)), collect(relationships(path))
        """)
        Dzialka saveRoslinaToDzialka(@Param("email") String email,
        @Param("numerDzialki") int numerDzialki, 
        @Param("x") int x, @Param("y") int y, 
        @Param("tabX") int[] tabX, @Param("tabY") int[] tabY,
        @Param("kolor") String kolor, 
        @Param("tekstura") String tekstura, 
        @Param("wyswietlanie") String wyswietlanie,
        @Param("obraz") String obraz,
        @Param("roslinaId") String roslinaId);

        @Query("""
        MATCH (u:Uzytkownik{email: $email})-[:MA_OGROD]->
                (:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki})
                
        MATCH (d)<-[existing:ZASADZONA_NA {x: $xStary, y: $yStary}]-(existingRoslina)
        SET existing.x = $xNowy, existing.y = $yNowy,
            existing.tabX = $tabX, existing.tabY = $tabY       

        WITH existing

        MATCH path = (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki}) 
        OPTIONAL MATCH (d)<-[r1:ZASADZONA_NA]-(rosliny)-[r]-(w)
        WHERE w:Wlasciwosc OR w:UzytkownikWlasciwosc
        RETURN d, collect(r1), collect(rosliny), collect(r), collect(w), collect(nodes(path)), collect(relationships(path))

        """)
        Dzialka changeRoslinaPozycjaInDzialka(@Param("email") String email, 
        @Param("numerDzialki") int numerDzialki,
        @Param("xStary") int xStary, @Param("yStary") int yStary,
        @Param("xNowy") int xNowy, @Param("yNowy") int yNowy,
        @Param("tabX") int[] tabX, @Param("tabY") int[] tabY);

        @Query("""
        MATCH (u:Uzytkownik{email: $email})-[maOgrod:MA_OGROD]->
              (ogrod:Ogrod)-[:MA_DZIALKE]->(d1:Dzialka{numer: $numerDzialkiStary})

        MATCH (u)-[maOgrod]->(ogrod)-[:MA_DZIALKE]->(d2:Dzialka{numer: $numerDzialkiNowy})
        MATCH (d1)<-[existing:ZASADZONA_NA {x: $xStary, y: $yStary}]-(existingRoslina)

        WITH existing, d1, d2, existingRoslina, 
                existing.obraz AS obrazek, 
                existing.tekstura AS tex, existing.kolor AS klr, 
                existing.wyswietlanie AS wyswietlanie,
                existing.notatka AS notatka
        DELETE existing
  
        CREATE (d2)<-[existing2:ZASADZONA_NA {
                                x: $xNowy, y: $yNowy, 
                                tabX: $tabX, tabY: $tabY, 
                                obraz: obrazek, tekstura: tex, kolor: klr,
                                wyswietlanie: wyswietlanie,
                                notatka: notatka
                                }]-(existingRoslina)
                
        WITH d2
        MATCH path = (d2)<-[zasadzone:ZASADZONA_NA]-(rosliny)
                
        RETURN d2, collect(nodes(path)), collect(relationships(path))
        """)
        Dzialka changeRoslinaPozycjaInDzialka(@Param("email") String email, 
        @Param("numerDzialkiStary") int numerDzialki, @Param("numerDzialkiNowy") int numerDzialkiNowy, 
        @Param("xStary") int xStary, @Param("yStary") int yStary,
        @Param("xNowy") int xNowy, @Param("yNowy") int yNowy,
        @Param("tabX") int[] tabX, @Param("tabY") int[] tabY);


        @Query("""
        MATCH (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki})
                
        MATCH (d)<-[existing:ZASADZONA_NA {x: $x, y: $y}]-(existingRoslina)
        SET existing.notatka = $notatka

        WITH d
        MATCH path = (d)<-[zasadzone:ZASADZONA_NA]-(rosliny)
                
        RETURN d, collect(nodes(path)), collect(relationships(path))
                """)
        Dzialka updateRoslinaNotatkaInDzialka(@Param("email") String email,
                @Param("numerDzialki") int numerDzialki, 
                @Param("x") int x, @Param("y") int y, 
                @Param("notatka") String notatka);
        
        @Query("""
        MATCH (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki})
                
        MATCH (d)<-[existing:ZASADZONA_NA {x: $x, y: $y}]-(existingRoslina)
        SET existing.kolor = $kolor

        WITH d
        MATCH path = (d)<-[zasadzone:ZASADZONA_NA]-(rosliny)
                
        RETURN d, collect(nodes(path)), collect(relationships(path))
                """)
        Dzialka updateRoslinaKolorInDzialka(@Param("email") String email,
                @Param("numerDzialki") int numerDzialki, 
                @Param("x") int x, @Param("y") int y, 
                @Param("kolor") String kolor);

        @Query("""
                MATCH (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki})
                        
                MATCH (d)<-[existing:ZASADZONA_NA {x: $x, y: $y}]-(existingRoslina)
                SET existing.tekstura = $tekstura
        
                WITH d
                MATCH path = (d)<-[zasadzone:ZASADZONA_NA]-(rosliny)
                        
                RETURN d, collect(nodes(path)), collect(relationships(path))
                        """)
        Dzialka updateRoslinaTeksturaInDzialka(@Param("email") String email,
                @Param("numerDzialki") int numerDzialki, 
                @Param("x") int x, @Param("y") int y, 
                @Param("tekstura") String tekstura);
                

        @Query("""
                MATCH (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki})
                        
                MATCH (d)<-[existing:ZASADZONA_NA {x: $x, y: $y}]-(existingRoslina)
                SET existing.wyswietlanie = $wyswietlanie
        
                WITH d
                MATCH path = (d)<-[zasadzone:ZASADZONA_NA]-(rosliny)
                        
                RETURN d, collect(nodes(path)), collect(relationships(path))
                        """)
        Dzialka updateRoslinaWyswietlanieInDzialka(@Param("email") String email,
                @Param("numerDzialki") int numerDzialki, 
                @Param("x") int x, @Param("y") int y, 
                @Param("wyswietlanie") String wyswietlanie);


        @Query("""
        MATCH (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki})
        
        MATCH (d)<-[existing:ZASADZONA_NA {x: $x, y: $y}]-(existingRoslina)
        SET existing.obraz = $obraz

        WITH d
        MATCH path = (d)<-[zasadzone:ZASADZONA_NA]-(rosliny)
        
        RETURN d, collect(nodes(path)), collect(relationships(path))
        """)
        Dzialka updateRoslinaObrazInDzialka(@Param("email") String email,
                @Param("numerDzialki") int numerDzialki, 
                @Param("x") int x, @Param("y") int y, 
                @Param("obraz") String obraz);

        @Query("""
        MATCH (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki})
                
        MATCH (d)<-[existing:ZASADZONA_NA {x: $x, y: $y}]-(existingRoslina)
        SET existing.obraz = null

        WITH d
        MATCH path = (d)<-[zasadzone:ZASADZONA_NA]-(rosliny)
                
        RETURN d, collect(nodes(path)), collect(relationships(path))
        """)
        Dzialka deleteRoslinaObrazInDzialka(@Param("email") String email,
        @Param("numerDzialki") int numerDzialki, 
        @Param("x") int x, @Param("y") int y);


        @Query("""
        MATCH (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki})
                
        MATCH (d)<-[existing:ZASADZONA_NA {x: $x, y: $y}]-(existingRoslina)
        SET existing.tekstura = null

        WITH d
        MATCH path = (d)<-[zasadzone:ZASADZONA_NA]-(rosliny)
                
        RETURN d, collect(nodes(path)), collect(relationships(path))
        """)
        Dzialka deleteRoslinaTeksturaInDzialka(@Param("email") String email,
        @Param("numerDzialki") int numerDzialki, 
        @Param("x") int x, @Param("y") int y);
        

        

        @Query("""
        MATCH (u:Uzytkownik{email: $email})-[:MA_OGROD]->(:Ogrod)-[:MA_DZIALKE]->(d:Dzialka{numer: $numerDzialki})

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
