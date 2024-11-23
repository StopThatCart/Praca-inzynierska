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


public interface UzytkownikRoslinaRepository  extends Neo4jRepository<Roslina, Long> {

         // To jest okropne, ale nie mogłem znaleźć optymalnego sposobu na to, by porównywało właściwości węzła rośliny.
     // Dodatkowo, byłoby więcej problemów z dynamicznymi etykietami bo Neo4j na to nie pozwala, a APOC jest mocno nieczytelny
    @Query(value = """
        WITH $roslina.__properties__ AS rp
        MATCH (roslina:UzytkownikRoslina)
            WHERE (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
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
            MATCH (roslina)-[:MA_ODCZYNY]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
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
        MATCH (roslina:UzytkownikRoslina)
            WHERE (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
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
            MATCH (roslina)-[:MA_ODCZYNY]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
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
    Page<Roslina> findAllRoslinyOfUzytkownikWithParameters(
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
        MATCH (ros:UzytkownikRoslina{roslinaId: $roslinaId})
        OPTIONAL MATCH path = (ros)-[r]-(wlasciwosc)
        WHERE wlasciwosc:Wlasciwosc OR wlasciwosc:UzytkownikWlasciwosc
        RETURN ros, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relationships
    """)
    Optional<Roslina> findByRoslinaIdWithRelations(@Param("roslinaId") String roslinaId);


    // @Query("""
    //     MATCH (ros:UzytkownikRoslina{roslinaId: $roslinaId})
    //     RETURN ros
    // """)
    // Optional<Roslina> findByRoslinaId(@Param("roslinaId") String roslinaId);

    @Query(value = """
        MATCH (roslina:UzytkownikRoslina)-[:STWORZONA_PRZEZ]->(uzytkownik:Uzytkownik{nazwa: $nazwa})
        RETURN roslina
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
       countQuery = """
        MATCH (roslina:UzytkownikRoslina)-[:STWORZONA_PRZEZ]->(uzytkownik:Uzytkownik{nazwa: $nazwa})
        RETURN count(roslina)
        """)
    Page<Roslina> findRoslinyOfUzytkownik(@Param("nazwa") String nazwa, Pageable pageable);

    @Query("""
        MATCH (roslina:UzytkownikRoslina {roslinaId: $roslinaId})-[:STWORZONA_PRZEZ]->(uzytkownik:Uzytkownik{nazwa: $nazwa})
        RETURN roslina
        """)
    Optional<Roslina> findRoslinaOfUzytkownik(@Param("nazwa") String nazwa, String roslinaId);


    @Query("""
        MATCH (uzytkownik:Uzytkownik{uzytId: $uzytId})
        WITH uzytkownik, $roslina.__properties__ AS rp 
        MERGE (p:UzytkownikRoslina:Roslina {
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
        MERGE (p:UzytkownikRoslina:Roslina {nazwa: $name, roslinaId: $roslinaId, opis: $description, 
        obraz: COALESCE($imageFilename, 'default_plant.jpg'), wysokoscMin: $heightMin, wysokoscMax: $heightMax})
            -[:STWORZONA_PRZEZ]->(uzytkownik)

        WITH p, $relatLump AS relatLump UNWIND relatLump AS relat

        WITH p, relat, [ relat.etykieta, 'UzytkownikWlasciwosc', 'Wlasciwosc' ] AS labels
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
           MATCH (p:UzytkownikRoslina{roslinaId: $roslinaId})
           SET  p.nazwa = $name, p.opis = $description,
                p.wysokoscMin = $heightMin, p.wysokoscMax = $heightMax
           WITH p, $relatLump AS relatLump UNWIND relatLump AS relat

           MATCH (p)-[r]->(w:Wlasciwosc)
           WITH p, r, w, collect(relat) AS newRelatLump

           WHERE NOT any(np IN newRelatLump WHERE w.nazwa = np.nazwa AND type(r) = np.relacja)
           DELETE r

           WITH p, newRelatLump AS relatLump
           UNWIND relatLump AS relat
           WITH p, relat, [ relat.etykieta, 'UzytkownikWlasciwosc', 'Wlasciwosc' ] AS labels
           CALL apoc.merge.node(labels, {nazwa: relat.nazwa}) YIELD node AS w
           WITH p, w, relat
           CALL apoc.merge.relationship(p, relat.relacja, {}, {}, w) YIELD rel


           WITH p OPTIONAL MATCH path=(p)-[r]->(w)
           RETURN p, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relus
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
        MATCH (p:UzytkownikRoslina{roslinaId: $roslinaId})
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
        MATCH (p:UzytkownikRoslina{roslinaId: $roslinaId}) 
        SET  p.obraz = $obraz
        RETURN p
    """)
    Roslina updateRoslinaObraz(
    @Param("roslinaId") String roslinaId,
    @Param("obraz") String obraz
    );


    @Query("""
            MATCH (p:UzytkownikRoslina{roslinaId: $roslinaId}) 
            DETACH DELETE p

            WITH p 
            MATCH (wlasciwosc:UzytkownikWlasciwosc) 
            WHERE NOT (wlasciwosc)--()
            DELETE wlasciwosc
            """)
    void deleteByRoslinaId(@Param("roslinaId") String roslinaId);

    @Query("""
           MATCH (wlasciwosc:UzytkownikWlasciwosc) 
           WHERE NOT (wlasciwosc)--()
           DELETE wlasciwosc
    """)
    void removeLeftoverUzytkownikWlasciwosci();

}
