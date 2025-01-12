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
 * Repozytorium dla operacji na encji Roslina w bazie danych Neo4j.
 * 
 * <p>Interfejs ten rozszerza Neo4jRepository i zapewnia metody do wykonywania
 * zapytań Cypher na bazie danych Neo4j. Metody te umożliwiają sprawdzanie,
 * pobieranie, zapisywanie oraz aktualizowanie danych dotyczących roślin.</p>
 * 
 * <p>Metody w tym repozytorium wykorzystują adnotacje @Query do definiowania
 * zapytań Cypher, które są wykonywane na bazie danych. Parametry zapytań
 * są przekazywane za pomocą adnotacji @Param.</p>
 * 
 * <p>Przykładowe operacje obejmują:</p>
 * <ul>
 *   <li>Sprawdzanie, czy roślina o podanej nazwie łacińskiej istnieje.</li>
 *   <li>Pobieranie rośliny na podstawie nazwy łacińskiej wraz z jej cechami.</li>
 *   <li>Pobieranie rośliny na podstawie jej identyfikatora.</li>
 *   <li>Pobieranie wszystkich roślin z możliwością paginacji.</li>
 *   <li>Dodawanie nowej rośliny do bazy danych.</li>
 *   <li>Aktualizowanie danych rośliny, w tym jej relacji z innymi węzłami.</li>
 *   <li>Usuwanie rośliny na podstawie nazwy łacińskiej lub identyfikatora.</li>
 *   <li>Usuwanie niepowiązanych węzłów cech.</li>
 * </ul>
 * 
 * <p>Repozytorium to jest częścią aplikacji zarządzającej danymi roślin,
 * umożliwiającej użytkownikom zarządzanie informacjami o roślinach.</p>
 */
public interface RoslinaRepository extends Neo4jRepository<Roslina, Long> {

    @Query("""
        MATCH (roslina:Roslina{nazwaLacinska: toLower($latinName)}) WHERE NOT roslina:RoslinaWlasna
        RETURN count(roslina) > 0
    """)
    boolean checkIfRoslinaWithNazwaLacinskaExists(String latinName);

    @Query("""
        MATCH (roslina:Roslina{nazwaLacinska: toLower($latinName)}) WHERE NOT roslina:RoslinaWlasna
        OPTIONAL MATCH path=(roslina)-[r]->(w:Cecha)
        RETURN roslina, collect(nodes(path)), collect(relationships(path))
    """)
    Optional<Roslina> findByNazwaLacinskaWithCechy(String latinName);

    @Query("""
        MATCH (roslina:Roslina {uuid: $uuid})
        OPTIONAL MATCH (roslina)-[r1:STWORZONA_PRZEZ]->(u:Uzytkownik)-[r2:MA_USTAWIENIA]->(ust:Ustawienia)
        OPTIONAL MATCH path=(roslina)-[rels]->(w:Cecha)

        RETURN roslina, r1, u, r2, ust, collect(nodes(path)), collect(relationships(path))
    """)
    Optional<Roslina> findByUUID(String uuid);

    @Query("""
        MATCH (roslina:Roslina{nazwaLacinska: toLower($latinName)}) WHERE NOT roslina:RoslinaWlasna
        RETURN roslina
    """)
    Optional<Roslina> findByNazwaLacinska(String latinName);


    
    @Query("""
        MATCH (roslina:Roslina) WHERE NOT roslina:RoslinaWlasna
        WITH COUNT(roslina) AS totalCount, COLLECT(roslina) AS rosliny
        WITH rosliny[toInteger(rand() * totalCount)] AS randomRoslina
        RETURN randomRoslina
    """)
    Optional<Roslina> getRandomRoslina();

    @Query(value = """
        MATCH (roslina:Roslina) WHERE NOT roslina:RoslinaWlasna
        OPTIONAL MATCH path=(roslina)-[r]->(w:Cecha)
        RETURN roslina, collect(nodes(path)), collect(relationships(path))
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
       countQuery = """
        MATCH (roslina:Roslina) WHERE NOT roslina:RoslinaWlasna
        RETURN count(roslina)
        """)
    Page<Roslina> findAllRosliny(Pageable pageable);

    @Query(value = """
        WITH $roslina.__properties__ AS rp
        MATCH (roslina:Roslina) WHERE NOT roslina:RoslinaWlasna
            AND (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
            AND (rp.nazwaLacinska IS NULL OR roslina.nazwaLacinska CONTAINS toLower(rp.nazwaLacinska))
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
        MATCH (roslina:Roslina) WHERE NOT roslina:RoslinaWlasna
            AND (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
            AND (rp.nazwaLacinska IS NULL OR roslina.nazwaLacinska CONTAINS toLower(rp.nazwaLacinska))
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
    Page<Roslina> findAllRoslinyWithParameters(
        Roslina roslina, 
        Set<Cecha> formy,
        Set<Cecha> gleby,
        Set<Cecha> grupy,
        Set<Cecha> koloryLisci,
        Set<Cecha> koloryKwiatow,
        Set<Cecha> kwiaty,
        Set<Cecha> odczyny,
        Set<Cecha> okresyKwitnienia,
        Set<Cecha> okresyOwocowania,
        Set<Cecha> owoce,
        Set<Cecha> podgrupa,
        Set<Cecha> pokroje,
        Set<Cecha> silyWzrostu,
        Set<Cecha> stanowiska,
        Set<Cecha> walory,
        Set<Cecha> wilgotnosci,
        Set<Cecha> zastosowania,
        Set<Cecha> zimozielonosci,
        Pageable pageable);

    @Query("""
        MERGE (p:Roslina {uuid: randomUUID(), nazwa: $name, nazwaLacinska: toLower($latinName), opis: $description, 
        obraz: COALESCE($obraz, 'default_plant.jpg'), wysokoscMin: $heightMin, wysokoscMax: $heightMax}) 

        WITH p, $relatLump AS relatLump UNWIND relatLump AS relat

        WITH p, relat, [ relat.etykieta, 'Cecha' ] AS labels
        CALL apoc.merge.node(labels, {nazwa: relat.nazwa}) YIELD node AS w
        WITH p, w, relat
        CALL apoc.merge.relationship(p, relat.relacja, {}, {}, w) YIELD rel

        WITH p OPTIONAL MATCH path=(p)-[r]->(w)
        RETURN p, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relus
    """)
    Roslina addRoslina(
        String name, String latinName, 
        String description, 
        String obraz, 
        Double heightMin, Double heightMax, 
        @Param("relatLump") List<Map<String, String>> relatLump
    );

    @Query("""
        WITH $roslina.__properties__ AS rp 
        MERGE (p:Roslina {
        uuid: randomUUID(), 
        nazwa: rp.nazwa, 
        nazwaLacinska: toLower(rp.nazwaLacinska), 
        opis: rp.opis, 
        obraz: COALESCE(rp.obraz, 'default_plant.jpg'), 
        wysokoscMin: rp.wysokoscMin, 
        wysokoscMax: rp.wysokoscMax
        }) 

        WITH p OPTIONAL MATCH path=(p)-[r]->(w)
        RETURN p, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relus
    """)
    Roslina addRoslina(Roslina roslina);

// To niestety trzeba zapamiętać bo mi trochę zajęło
// Czas wykonania testu: 162ms, 179ms
    @Query("""
        MATCH (p:Roslina{nazwaLacinska: toLower($latinName)}) WHERE NOT p:RoslinaWlasna
        SET  p.nazwa = $name, 
            p.nazwaLacinska = toLower($nowaNazwaLacinska),
            p.opis = $description,
            p.wysokoscMin = $heightMin, p.wysokoscMax = $heightMax
        WITH p, $relatLump AS relatLump UNWIND relatLump AS relat

        WITH p, relat, [relat.etykieta, 'Cecha'] AS labels
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
        String name,
        String latinName,
        String description,
        Double heightMin,
        Double heightMax,
        String nowaNazwaLacinska,
        @Param("relatLump") List<Map<String, String>> relatLump
    );


    @Query("""
        MATCH (p:Roslina{nazwaLacinska: toLower($latinName)}) WHERE NOT p:RoslinaWlasna
        SET p.nazwa = $name,
            p.nazwaLacinska = toLower($nowaNazwaLacinska),
            p.opis = $description,
           
            p.wysokoscMin = $heightMin, p.wysokoscMax = $heightMax
        RETURN p
    """)
    Roslina updateRoslina(
   String name,
    String latinName,
    String description,
    Double heightMin,
    Double heightMax,
    String nowaNazwaLacinska
    );

    @Query("""
        MATCH (p:Roslina{uuid: $uuid})
        SET  p.obraz = $obraz
        RETURN p
    """)
    Roslina updateRoslinaObraz(String uuid, String obraz);

    @Query("""
            MATCH (p:Roslina{nazwaLacinska: toLower($latinName)}) WHERE NOT p:RoslinaWlasna
            DETACH DELETE p

            WITH p 
            MATCH (cecha:Cecha) 
            WHERE NOT (cecha)--()
            DELETE cecha
            """)
    void deleteByNazwaLacinska(String latinName);

    @Query("""
            MATCH (p:Roslina{uuid: $uuid})
            DETACH DELETE p

            WITH p 
            MATCH (cecha:Cecha) WHERE NOT (cecha)--()
            DELETE cecha
            """)
    void deleteByUUID(String uuid);

    @Query("""
        MATCH (cecha:Cecha) 
        WHERE NOT (cecha)--()
        DELETE cecha
    """)
    void removeLeftoverCechy();

}
