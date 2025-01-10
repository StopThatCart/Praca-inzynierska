package com.example.yukka.model.roslina.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.cecha.Cecha;


/**
 * Interfejs RoslinaWlasnaRepository rozszerza Neo4jRepository i zapewnia metody do zarządzania węzłami typu Roslina w bazie danych Neo4j.
 * 
 * Metody tego interfejsu umożliwiają:
 * - Wyszukiwanie roślin użytkownika na podstawie różnych parametrów.
 * - Dodawanie nowych roślin do bazy danych.
 * - Aktualizowanie istniejących roślin.
 * - Usuwanie roślin z bazy danych.
 * - Usuwanie niepowiązanych węzłów typu CechaWlasna.
 * 
 * Każda metoda jest opatrzona odpowiednią adnotacją @Query, która definiuje zapytanie Cypher do wykonania w bazie danych Neo4j.
 */
/**
 * Repozytorium dla operacji na encji RoslinaWlasna w bazie danych Neo4j.
 * 
 * <p>Interfejs ten rozszerza Neo4jRepository i zapewnia metody do wykonywania
 * zapytań Cypher na bazie danych Neo4j. Metody te umożliwiają sprawdzanie,
 * pobieranie, zapisywanie oraz aktualizowanie danych dotyczących roślin
 * użytkowników.</p>
 * 
 * <p>Metody w tym repozytorium wykorzystują adnotacje @Query do definiowania
 * zapytań Cypher, które są wykonywane na bazie danych. Parametry zapytań
 * są przekazywane za pomocą adnotacji @Param.</p>
 * 
 * <p>Przykładowe operacje obejmują:</p>
 * <ul>
 *   <li>Wyszukiwanie roślin użytkownika na podstawie różnych parametrów.</li>
 *   <li>Pobieranie rośliny użytkownika wraz z jej relacjami.</li>
 *   <li>Dodawanie nowej rośliny użytkownika.</li>
 *   <li>Aktualizowanie danych rośliny użytkownika.</li>
 *   <li>Usuwanie rośliny użytkownika.</li>
 *   <li>Usuwanie niepowiązanych cech użytkownika.</li>
 * </ul>
 * 
 * <p>Repozytorium to jest częścią aplikacji zarządzającej roślinami użytkowników,
 * umożliwiającej użytkownikom zarządzanie ich roślinami w bazie danych Neo4j.</p>
 */
public interface RoslinaWlasnaRepository  extends Neo4jRepository<Roslina, Long> {

         // To jest okropne, ale nie mogłem znaleźć optymalnego sposobu na to, by porównywało cechy węzła rośliny.
     // Dodatkowo, byłoby więcej problemów z dynamicznymi etykietami bo Neo4j na to nie pozwala, a APOC jest mocno nieczytelny
    @Query(value = """
        WITH $roslina.__properties__ AS rp
        MATCH (roslina:RoslinaWlasna)-[:STWORZONA_PRZEZ]->(uzyt:Uzytkownik{nazwa: $uzytkownikNazwa})
            WHERE (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
            AND (rp.wysokoscMin IS NULL OR roslina.wysokoscMin >= rp.wysokoscMin)
            AND (rp.wysokoscMax IS NULL OR roslina.wysokoscMax <= rp.wysokoscMax)

        WITH roslina, $formy AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_FORME]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $gleby AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_GLEBE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $grupy AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_GRUPE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $koloryLisci AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_KOLOR_LISCI]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $koloryKwiatow AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_KOLOR_KWIATOW]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $kwiaty AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_KWIAT]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $odczyny AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_ODCZYN]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $okresyKwitnienia AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_OKRES_KWITNIENIA]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $okresyOwocowania AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_OKRES_OWOCOWANIA]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $owoce AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_OWOC]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $podgrupa AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_PODGRUPE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $pokroje AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_POKROJ]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $silyWzrostu AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_SILE_WZROSTU]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $stanowiska AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_STANOWISKO]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $walory AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_WALOR]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $wilgotnosci AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_WILGOTNOSC]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $zastosowania AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_ZASTOSOWANIE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $zimozielonosci AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_ZIMOZIELONOSC_LISCI]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })
        
        RETURN DISTINCT roslina
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
       countQuery = """
        WITH $roslina.__properties__ AS rp 
        MATCH (roslina:RoslinaWlasna)-[:STWORZONA_PRZEZ]->(uzyt:Uzytkownik{nazwa: $uzytkownikNazwa})
            WHERE (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
            AND (rp.wysokoscMin IS NULL OR roslina.wysokoscMin >= rp.wysokoscMin)
            AND (rp.wysokoscMax IS NULL OR roslina.wysokoscMax <= rp.wysokoscMax)
            
        WITH roslina, $formy AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_FORME]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $gleby AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_GLEBE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $grupy AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_GRUPE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $koloryLisci AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_KOLOR_LISCI]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $koloryKwiatow AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_KOLOR_KWIATOW]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $kwiaty AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_KWIAT]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $odczyny AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_ODCZYN]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $okresyKwitnienia AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_OKRES_KWITNIENIA]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $okresyOwocowania AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_OKRES_OWOCOWANIA]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $owoce AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_OWOC]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $podgrupa AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_PODGRUPE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $pokroje AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_POKROJ]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $silyWzrostu AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_SILE_WZROSTU]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $stanowiska AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_STANOWISKO]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $walory AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_WALOR]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $wilgotnosci AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_WILGOTNOSC]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $zastosowania AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_ZASTOSOWANIE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $zimozielonosci AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_ZIMOZIELONOSC_LISCI]->(:Cecha {nazwa: wezel.__properties__.nazwa})
        })

        RETURN count(DISTINCT roslina)
        """)
    Page<Roslina> findAllRoslinyOfUzytkownikWithParameters(
        @Param("uzytkownikNazwa") String uzytkownikNazwa,
        @Param("roslina") Roslina roslina, 
        @Param("formy") Set<Cecha> formy,
        @Param("gleby") Set<Cecha> gleby,
        @Param("grupy") Set<Cecha> grupy,
        @Param("koloryLisci") Set<Cecha> koloryLisci,
        @Param("koloryKwiatow") Set<Cecha> koloryKwiatow,
        @Param("kwiaty") Set<Cecha> kwiaty,
        @Param("odczyny") Set<Cecha> odczyny,
        @Param("okresyKwitnienia") Set<Cecha> okresyKwitnienia,
        @Param("okresyOwocowania") Set<Cecha> okresyOwocowania,
        @Param("owoce") Set<Cecha> owoce,
        @Param("podgrupa") Set<Cecha> podgrupa,
        @Param("pokroje") Set<Cecha> pokroje,
        @Param("silyWzrostu") Set<Cecha> silyWzrostu,
        @Param("stanowiska") Set<Cecha> stanowiska,
        @Param("walory") Set<Cecha> walory,
        @Param("wilgotnosci") Set<Cecha> wilgotnosci,
        @Param("zastosowania") Set<Cecha> zastosowania,
        @Param("zimozielonosci") Set<Cecha> zimozielonosci,
        Pageable pageable);

    @Query("""
        MATCH (ros:RoslinaWlasna{roslinaId: $roslinaId})
        OPTIONAL MATCH path = (ros)-[r]-(cecha)
        WHERE cecha:Cecha OR cecha:CechaWlasna
        RETURN ros, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relationships
    """)
    Optional<Roslina> findByRoslinaIdWithRelations(@Param("roslinaId") String roslinaId);

    @Query(value = """
        MATCH (roslina:RoslinaWlasna)-[:STWORZONA_PRZEZ]->(uzytkownik:Uzytkownik{nazwa: $nazwa})
        RETURN roslina
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
       countQuery = """
        MATCH (roslina:RoslinaWlasna)-[:STWORZONA_PRZEZ]->(uzytkownik:Uzytkownik{nazwa: $nazwa})
        RETURN count(roslina)
        """)
    Page<Roslina> findRoslinyOfUzytkownik(@Param("nazwa") String nazwa, Pageable pageable);

    @Query("""
        MATCH (roslina:RoslinaWlasna {roslinaId: $roslinaId})-[r1:STWORZONA_PRZEZ]->(uzytkownik:Uzytkownik{nazwa: $nazwa})
        RETURN roslina, r1, uzytkownik
        """)
    Optional<Roslina> findRoslinaOfUzytkownik(@Param("nazwa") String nazwa, String roslinaId);


    @Query("""
        MATCH (uzytkownik:Uzytkownik{uzytId: $uzytId})
        WITH uzytkownik, $roslina.__properties__ AS rp 
        MERGE (p:RoslinaWlasna:Roslina {
        nazwa: rp.nazwa, 
        roslinaId: rp.roslinaId,
        opis: rp.opis, 
        obraz: COALESCE(rp.obraz, 'default_plant.jpg'), 
        wysokoscMin: rp.wysokoscMin, 
        wysokoscMax: rp.wysokoscMax
        })-[:STWORZONA_PRZEZ]->(uzytkownik)

        WITH p OPTIONAL MATCH path=(p)-[r]->(w)
        RETURN p, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relus
    """)
    Roslina addRoslina(@Param("uzytId") String uzytId, @Param("roslina") Roslina roslina);

    @Query("""
        MATCH (uzytkownik:Uzytkownik{uzytId: $uzytId})
        WITH uzytkownik
        MERGE (p:RoslinaWlasna:Roslina {nazwa: $name, roslinaId: $roslinaId, opis: $description, 
        obraz: COALESCE($imageFilename, 'default_plant.jpg'), wysokoscMin: $heightMin, wysokoscMax: $heightMax})
            -[:STWORZONA_PRZEZ]->(uzytkownik)

        WITH p, $relatLump AS relatLump UNWIND relatLump AS relat

        WITH p, relat, [ relat.etykieta, 'CechaWlasna', 'Cecha' ] AS labels
        CALL apoc.merge.node(labels, {nazwa: relat.nazwa}) YIELD node AS w
        WITH p, w, relat
        CALL apoc.merge.relationship(p, relat.relacja, {}, {}, w) YIELD rel

        WITH p OPTIONAL MATCH path=(p)-[r]->(w)
        RETURN p, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relus
    """)
    Roslina addRoslina(@Param("uzytId") String uzytId,
        @Param("name") String name, @Param("roslinaId") String roslinaId, 
        @Param("description") String description, @Param("imageFilename") String imageFilename, 
        @Param("heightMin") Double heightMin, @Param("heightMax") Double heightMax, 
        @Param("relatLump") List<Map<String, String>> relatLump
    );


    @Query("""
           MATCH (p:RoslinaWlasna{roslinaId: $roslinaId})
           SET  p.nazwa = $name, p.opis = $description,
                p.wysokoscMin = $heightMin, p.wysokoscMax = $heightMax
           WITH p, $relatLump AS relatLump UNWIND relatLump AS relat

           WITH p, relat, [relat.etykieta, 'CechaWlasna', 'Cecha'] AS labels
           CALL apoc.merge.node(labels, {nazwa: relat.nazwa}) YIELD node AS w
           WITH p, w, relat
           CALL apoc.merge.relationship(p, relat.relacja, {}, {}, w) YIELD rel

           WITH p, collect(DISTINCT w) AS currentNodes, collect(DISTINCT rel) AS currentRels
           OPTIONAL MATCH (p)-[r]->(w:Cecha)

           WHERE NOT w IN currentNodes
           CALL {
               WITH r
               DELETE r
               RETURN COUNT(*) AS deletedRels
           }
           RETURN p
    """)
    Roslina updateRoslina(
        @Param("roslinaId") String roslinaId,
        @Param("name") String name,
        @Param("description") String description,
        @Param("imageFilename") String imageFilename,
        @Param("heightMin") Double heightMin,
        @Param("heightMax") Double heightMax,
        @Param("relatLump") List<Map<String, String>> relatLump
    );

    @Query("""
        MATCH (p:RoslinaWlasna{roslinaId: $roslinaId})
        SET  p.nazwa = $name, p.opis = $description,
            p.wysokoscMin = $heightMin, p.wysokoscMax = $heightMax
    """)
    Roslina updateRoslina(
    @Param("roslinaId") String roslinaId,
    @Param("name") String name,
    @Param("description") String description,
    @Param("imageFilename") String imageFilename,
    @Param("heightMin") Double heightMin,
    @Param("heightMax") Double heightMax
    );


    @Query("""
            MATCH (p:RoslinaWlasna{roslinaId: $roslinaId}) 
            DETACH DELETE p

            WITH p 
            MATCH (cecha:CechaWlasna) 
            WHERE NOT (cecha)--()
            DELETE cecha
            """)
    void deleteByRoslinaId(@Param("roslinaId") String roslinaId);

    @Query("""
           MATCH (cecha:CechaWlasna) 
           WHERE NOT (cecha)--()
           DELETE cecha
    """)
    void removeLeftoverUzytkownikCechy();

}
