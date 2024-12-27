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
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;


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
 *   <li>Pobieranie rośliny na podstawie nazwy łacińskiej wraz z jej właściwościami.</li>
 *   <li>Pobieranie rośliny na podstawie jej identyfikatora.</li>
 *   <li>Pobieranie wszystkich roślin z możliwością paginacji.</li>
 *   <li>Dodawanie nowej rośliny do bazy danych.</li>
 *   <li>Aktualizowanie danych rośliny, w tym jej relacji z innymi węzłami.</li>
 *   <li>Usuwanie rośliny na podstawie nazwy łacińskiej lub identyfikatora.</li>
 *   <li>Usuwanie niepowiązanych węzłów właściwości.</li>
 * </ul>
 * 
 * <p>Repozytorium to jest częścią aplikacji zarządzającej danymi roślin,
 * umożliwiającej użytkownikom zarządzanie informacjami o roślinach.</p>
 */
public interface RoslinaRepository extends Neo4jRepository<Roslina, Long> {

    @Query("""
        MATCH (roslina:Roslina{nazwaLacinska: toLower($latinName)}) WHERE NOT roslina:UzytkownikRoslina
        RETURN count(roslina) > 0
    """)
    boolean checkIfRoslinaWithNazwaLacinskaExists(@Param("latinName") String latinName);

    @Query("""
        MATCH (roslina:Roslina{nazwaLacinska: toLower($latinName)}) WHERE NOT roslina:UzytkownikRoslina
        OPTIONAL MATCH path=(roslina)-[r]->(w:Wlasciwosc)
        RETURN roslina, collect(nodes(path)), collect(relationships(path))
    """)
    Optional<Roslina> findByNazwaLacinskaWithWlasciwosci(@Param("latinName") String latinName);

    @Query("""
        MATCH (roslina:Roslina {roslinaId: $roslinaId})
        OPTIONAL MATCH (roslina)-[r1:STWORZONA_PRZEZ]->(u:Uzytkownik)
        OPTIONAL MATCH path=(roslina)-[rels]->(w:Wlasciwosc)

        RETURN roslina, r1, u, collect(nodes(path)), collect(relationships(path))
    """)
    Optional<Roslina> findByRoslinaId(@Param("roslinaId") String roslinaId);

    @Query("""
        MATCH (roslina:Roslina{nazwaLacinska: toLower($latinName)}) WHERE NOT roslina:UzytkownikRoslina
        RETURN roslina
    """)
    Optional<Roslina> findByNazwaLacinska(@Param("latinName") String latinName);


    
    @Query("""
        MATCH (roslina:Roslina) WHERE NOT roslina:UzytkownikRoslina
        WITH COUNT(roslina) AS totalCount, COLLECT(roslina) AS rosliny
        WITH rosliny[toInteger(rand() * totalCount)] AS randomRoslina
        RETURN randomRoslina
    """)
    Optional<Roslina> getRandomRoslina();

    @Query(value = """
        MATCH (roslina:Roslina) WHERE NOT roslina:UzytkownikRoslina
        OPTIONAL MATCH path=(roslina)-[r]->(w:Wlasciwosc)
        RETURN roslina, collect(nodes(path)), collect(relationships(path))
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
       countQuery = """
        MATCH (roslina:Roslina) WHERE NOT roslina:UzytkownikRoslina
        RETURN count(roslina)
        """)
    Page<Roslina> findAllRosliny(Pageable pageable);

    @Query(value = """
        WITH $roslina.__properties__ AS rp
        MATCH (roslina:Roslina) WHERE NOT roslina:UzytkownikRoslina
            AND (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
            AND (rp.nazwaLacinska IS NULL OR roslina.nazwaLacinska CONTAINS toLower(rp.nazwaLacinska))
            AND (rp.wysokoscMin IS NULL OR roslina.wysokoscMin >= rp.wysokoscMin)
            AND (rp.wysokoscMax IS NULL OR roslina.wysokoscMax <= rp.wysokoscMax)

        WITH roslina, $formy AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_FORME]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $gleby AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_GLEBE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $grupy AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_GRUPE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $koloryLisci AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_KOLOR_LISCI]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $koloryKwiatow AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_KOLOR_KWIATOW]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $kwiaty AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_KWIAT]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $odczyny AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_ODCZYN]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $okresyKwitnienia AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_OKRES_KWITNIENIA]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $okresyOwocowania AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_OKRES_OWOCOWANIA]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $owoce AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_OWOC]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $podgrupa AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_PODGRUPE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $pokroje AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_POKROJ]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $silyWzrostu AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_SILE_WZROSTU]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $stanowiska AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_STANOWISKO]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $walory AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_WALOR]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $wilgotnosci AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_WILGOTNOSC]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $zastosowania AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_ZASTOSOWANIE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $zimozielonosci AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_ZIMOZIELONOSC_LISCI]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })
        
        RETURN DISTINCT roslina
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
       countQuery = """
        WITH $roslina.__properties__ AS rp 
        MATCH (roslina:Roslina) WHERE NOT roslina:UzytkownikRoslina
            AND (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
            AND (rp.nazwaLacinska IS NULL OR roslina.nazwaLacinska CONTAINS toLower(rp.nazwaLacinska))
            AND (rp.wysokoscMin IS NULL OR roslina.wysokoscMin >= rp.wysokoscMin)
            AND (rp.wysokoscMax IS NULL OR roslina.wysokoscMax <= rp.wysokoscMax)
            
        WITH roslina, $formy AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_FORME]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $gleby AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_GLEBE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $grupy AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_GRUPE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $koloryLisci AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_KOLOR_LISCI]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $koloryKwiatow AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_KOLOR_KWIATOW]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $kwiaty AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_KWIAT]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $odczyny AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_ODCZYN]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $okresyKwitnienia AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_OKRES_KWITNIENIA]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $okresyOwocowania AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_OKRES_OWOCOWANIA]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $owoce AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_OWOC]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $podgrupa AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_PODGRUPE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $pokroje AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_POKROJ]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $silyWzrostu AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_SILE_WZROSTU]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $stanowiska AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_STANOWISKO]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $walory AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_WALOR]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $wilgotnosci AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_WILGOTNOSC]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $zastosowania AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_ZASTOSOWANIE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        WITH roslina, $zimozielonosci AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_ZIMOZIELONOSC_LISCI]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
        })

        RETURN count(DISTINCT roslina)
        """)
    Page<Roslina> findAllRoslinyWithParameters(
        @Param("roslina") Roslina roslina, 
        @Param("formy") Set<Wlasciwosc> formy,
        @Param("gleby") Set<Wlasciwosc> gleby,
        @Param("grupy") Set<Wlasciwosc> grupy,
        @Param("koloryLisci") Set<Wlasciwosc> koloryLisci,
        @Param("koloryKwiatow") Set<Wlasciwosc> koloryKwiatow,
        @Param("kwiaty") Set<Wlasciwosc> kwiaty,
        @Param("odczyny") Set<Wlasciwosc> odczyny,
        @Param("okresyKwitnienia") Set<Wlasciwosc> okresyKwitnienia,
        @Param("okresyOwocowania") Set<Wlasciwosc> okresyOwocowania,
        @Param("owoce") Set<Wlasciwosc> owoce,
        @Param("podgrupa") Set<Wlasciwosc> podgrupa,
        @Param("pokroje") Set<Wlasciwosc> pokroje,
        @Param("silyWzrostu") Set<Wlasciwosc> silyWzrostu,
        @Param("stanowiska") Set<Wlasciwosc> stanowiska,
        @Param("walory") Set<Wlasciwosc> walory,
        @Param("wilgotnosci") Set<Wlasciwosc> wilgotnosci,
        @Param("zastosowania") Set<Wlasciwosc> zastosowania,
        @Param("zimozielonosci") Set<Wlasciwosc> zimozielonosci,
        Pageable pageable);

    @Query("""
        MERGE (p:Roslina {roslinaId: $roslinaId, nazwa: $name, nazwaLacinska: toLower($latinName), opis: $description, 
        obraz: COALESCE($obraz, 'default_plant.jpg'), wysokoscMin: $heightMin, wysokoscMax: $heightMax}) 

        WITH p, $relatLump AS relatLump UNWIND relatLump AS relat

        WITH p, relat, [ relat.etykieta, 'Wlasciwosc' ] AS labels
        CALL apoc.merge.node(labels, {nazwa: relat.nazwa}) YIELD node AS w
        WITH p, w, relat
        CALL apoc.merge.relationship(p, relat.relacja, {}, {}, w) YIELD rel

        WITH p OPTIONAL MATCH path=(p)-[r]->(w)
        RETURN p, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relus
    """)
    Roslina addRoslina(
        @Param("roslinaId") String roslinaId,
        @Param("name") String name, @Param("latinName") String latinName, 
        @Param("description") String description, 
        @Param("obraz") String obraz, 
        @Param("heightMin") Double heightMin, @Param("heightMax") Double heightMax, 
        @Param("relatLump") List<Map<String, String>> relatLump
    );

    @Query("""
        WITH $roslina.__properties__ AS rp 
        MERGE (p:Roslina {
        roslinaId: rp.roslinaId, 
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
    Roslina addRoslina(@Param("roslina") Roslina roslina);

// To niestety trzeba zapamiętać bo mi trochę zajęło
// Czas wykonania testu: 162ms, 179ms
    @Query("""
           MATCH (p:Roslina{nazwaLacinska: toLower($latinName)}) WHERE NOT p:UzytkownikRoslina
           SET  p.nazwa = $name, 
                p.nazwaLacinska = toLower($nowaNazwaLacinska),
                p.opis = $description,
                p.wysokoscMin = $heightMin, p.wysokoscMax = $heightMax
           WITH p, $relatLump AS relatLump UNWIND relatLump AS relat

           MATCH (p)-[r]->(w:Wlasciwosc)
           WITH p, r, w, collect(relat) AS newRelatLump

           WHERE NOT any(np IN newRelatLump WHERE w.nazwa = np.nazwa AND type(r) = np.relacja)
           DELETE r

           WITH p, newRelatLump AS relatLump
           UNWIND relatLump AS relat
           WITH p, relat, [ relat.etykieta, 'Wlasciwosc' ] AS labels
           CALL apoc.merge.node(labels, {nazwa: relat.nazwa}) YIELD node AS w
           WITH p, w, relat
           CALL apoc.merge.relationship(p, relat.relacja, {}, {}, w) YIELD rel

           WITH p 
           OPTIONAL MATCH path=(p)-[r]->(w)
           RETURN p, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relus
    """)
    Roslina updateRoslina(
        @Param("name") String name,
        @Param("latinName") String latinName,
        @Param("description") String description,
        @Param("heightMin") Double heightMin,
        @Param("heightMax") Double heightMax,
        @Param("nowaNazwaLacinska") String nowaNazwaLacinska,
        @Param("relatLump") List<Map<String, String>> relatLump
    );


    @Query("""
        MATCH (p:Roslina{nazwaLacinska: toLower($latinName)}) WHERE NOT p:UzytkownikRoslina
        SET p.nazwa = $name,
            p.nazwaLacinska = toLower($nowaNazwaLacinska),
            p.opis = $description,
           
            p.wysokoscMin = $heightMin, p.wysokoscMax = $heightMax
        RETURN p
    """)
    Roslina updateRoslina(
    @Param("name") String name,
    @Param("latinName") String latinName,
    @Param("description") String description,
    @Param("heightMin") Double heightMin,
    @Param("heightMax") Double heightMax,
    @Param("nowaNazwaLacinska") String nowaNazwaLacinska
    );

    @Query("""
        MATCH (p:Roslina{nazwaLacinska: toLower($nazwaLacinska)}) WHERE NOT p:UzytkownikRoslina
        SET  p.obraz = $obraz
        RETURN p
    """)
    Roslina updateRoslinaObraz(
    @Param("nazwaLacinska") String nazwaLacinska,
    @Param("obraz") String obraz
    );

    @Query("""
            MATCH (p:Roslina{nazwaLacinska: toLower($latinName)}) WHERE NOT p:UzytkownikRoslina
            DETACH DELETE p

            WITH p 
            MATCH (wlasciwosc:Wlasciwosc) 
            WHERE NOT (wlasciwosc)--()
            DELETE wlasciwosc
            """)
    void deleteByNazwaLacinska(@Param("latinName") String latinName);

    @Query("""
            MATCH (p:Roslina{roslinaId: $roslinaId})
            DETACH DELETE p

            WITH p 
            MATCH (wlasciwosc:Wlasciwosc) WHERE NOT (wlasciwosc)--()
            DELETE wlasciwosc
            """)
    void deleteByRoslinaId(@Param("roslinaId") String roslinaId);

    @Query("""
        MATCH (wlasciwosc:Wlasciwosc) 
        WHERE NOT (wlasciwosc)--()
        DELETE wlasciwosc
    """)
    void removeLeftoverWlasciwosci();

}
