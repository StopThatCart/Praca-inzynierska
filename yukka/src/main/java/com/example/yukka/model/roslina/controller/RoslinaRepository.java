package com.example.yukka.model.roslina.controller;

import java.util.Collection;
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

public interface RoslinaRepository extends Neo4jRepository<Roslina, Long> {

    @Query("""
        MATCH (roslina:Roslina{nazwaLacinska: $latinName}) WHERE NOT roslina:UzytkownikRoslina
        RETURN count(roslina) > 0
    """)
    boolean checkIfRoslinaWithNazwaLacinskaExists(@Param("latinName") String latinName);

    @Query("""
        MATCH (roslina:Roslina{nazwaLacinska: $latinName}) WHERE NOT roslina:UzytkownikRoslina
        OPTIONAL MATCH path=(roslina)-[r]->(w:Wlasciwosc)
        RETURN roslina, collect(nodes(path)), collect(relationships(path))
    """)
    Optional<Roslina> findByNazwaLacinskaWithWlasciwosci(@Param("latinName") String latinName);

    @Query("""
        MATCH (roslina:Roslina{nazwaLacinska: $latinName}) WHERE NOT roslina:UzytkownikRoslina
        RETURN roslina
    """)
    Optional<Roslina> findByNazwaLacinska(@Param("latinName") String latinName);

    // path na razie został dodany dla testów.
    // TODO: Wyszukiwanie albo na backendzie albo na frontendzie
    // Jednak backend.

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

    // relatLump nie działa. Poza tym zaktualizuj potem count

    /*
     * 
     * """
        WITH $roslina.__properties__ AS rp 
        MATCH (roslina:Roslina) WHERE NOT roslina:UzytkownikRoslina
            AND (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
            AND (rp.nazwaLacinska IS NULL OR roslina.nazwaLacinska CONTAINS rp.nazwaLacinska)
            AND (rp.wysokoscMin IS NULL OR roslina.wysokoscMin >= rp.wysokoscMin)
            AND (rp.wysokoscMax IS NULL OR roslina.wysokoscMax <= rp.wysokoscMax)
        MATCH (roslina)-[rel]->(relatedNode)
        WHERE  (size($relatLump) = 0 OR 
                    ANY(relat IN $relatLump WHERE 
                        (relatedNode.nazwa = relat.nazwa) AND
                        (type(rel) = relat.relacja) AND
                        ANY(label IN labels(relatedNode) WHERE label = relat.etykieta)
                    )
        )
        RETURN DISTINCT roslina
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """
     * 
     * 
     */

     // To jest okropne, ale nie mogłem znaleźć optymalnego sposobu na to, by porównywało właściwości węzła rośliny.
     // Dodatkowo, byłoby więcej problemów z dynamicznymi etykietami bo Neo4j na to nie pozwala, a APOC jest mocno nieczytelny
    @Query(value = """
        WITH $roslina.__properties__ AS rp
        MATCH (roslina:Roslina) WHERE NOT roslina:UzytkownikRoslina
            AND (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
            AND (rp.nazwaLacinska IS NULL OR roslina.nazwaLacinska CONTAINS rp.nazwaLacinska)
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

        WITH roslina, $nagrody AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_NAGRODE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
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
        MATCH (roslina:Roslina) WHERE NOT roslina:UzytkownikRoslina
            AND (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
            AND (rp.nazwaLacinska IS NULL OR roslina.nazwaLacinska CONTAINS rp.nazwaLacinska)
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

        WITH roslina, $nagrody AS wezly
        WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
            MATCH (roslina)-[:MA_NAGRODE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
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
    Page<Roslina> findAllRoslinyWithParameters(
        @Param("roslina") Roslina roslina, 
        @Param("formy") Set<Wlasciwosc> formy,
        @Param("gleby") Set<Wlasciwosc> gleby,
        @Param("grupy") Set<Wlasciwosc> grupy,
        @Param("koloryLisci") Set<Wlasciwosc> koloryLisci,
        @Param("koloryKwiatow") Set<Wlasciwosc> koloryKwiatow,
        @Param("kwiaty") Set<Wlasciwosc> kwiaty,
        @Param("nagrody") Set<Wlasciwosc> nagrody,
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

    // Do wyrzucenia
    @Query("MATCH path=(p:Roslina)-[r]->(g:Wlasciwosc) WHERE NOT p:UzytkownikRoslina RETURN p, collect(nodes(path)), collect(relationships(path)) LIMIT $amount")
    Collection<Roslina> getSomePlants(@Param("amount") int amount);
    // Do wyrzucenia
    @Query("MATCH (r:Roslina)-[:MA_GLEBE]->(g:Gleba) WHERE NOT r:UzytkownikRoslina AND r.name = $name RETURN r, collect(g) as glebus")
    Roslina findRoslinaWithGleba(@Param("name") String name);

    @Query("""
        MERGE (p:Roslina {nazwa: $name, nazwaLacinska: $latinName, opis: $description, 
        obraz: COALESCE($imageFilename, 'default_plant.jpg'), wysokoscMin: $heightMin, wysokoscMax: $heightMax}) 

        WITH p, $relatLump AS relatLump UNWIND relatLump AS relat

        WITH p, relat, [ relat.etykieta, 'Wlasciwosc' ] AS labels
        CALL apoc.merge.node(labels, {nazwa: relat.nazwa}) YIELD node AS w
        WITH p, w, relat
        CALL apoc.merge.relationship(p, relat.relacja, {}, {}, w) YIELD rel
        MERGE (w)-[:MA_ROSLINE]->(p)

        WITH p OPTIONAL MATCH path=(p)-[r]->(w)
        RETURN p, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relus
    """)
    Roslina addRoslina(
        @Param("name") String name, @Param("latinName") String latinName, 
        @Param("description") String description, @Param("imageFilename") String imageFilename, 
        @Param("heightMin") Double heightMin, @Param("heightMax") Double heightMax, 
        @Param("relatLump") List<Map<String, String>> relatLump
    );

    @Query("""
        WITH $roslina.__properties__ AS rp 
        MERGE (p:Roslina {
        nazwa: rp.nazwa, 
        nazwaLacinska: rp.nazwaLacinska, 
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
           MATCH (p:Roslina{nazwaLacinska: $latinName}) WHERE NOT p:UzytkownikRoslina
           SET  p.nazwa = $name, p.opis = $description,
                p.obraz = COALESCE($imageFilename, 'default_plant.jpg'),
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
           MERGE (w)-[:MA_ROSLINE]->(p)

           WITH p OPTIONAL MATCH path=(p)-[r]->(w)
           RETURN p, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relus
    """)
    Roslina updateRoslina(
        @Param("name") String name,
        @Param("latinName") String latinName,
        @Param("description") String description,
        @Param("imageFilename") String imageFilename,
        @Param("heightMin") Double heightMin,
        @Param("heightMax") Double heightMax,
        @Param("relatLump") List<Map<String, String>> relatLump
    );


    @Query("""
        MATCH (p:Roslina{nazwaLacinska: $latinName}) WHERE NOT p:UzytkownikRoslina
        SET  p.nazwa = $name, p.opis = $description,
            p.obraz = COALESCE($imageFilename, 'default_plant.jpg'),
            p.wysokoscMin = $heightMin, p.wysokoscMax = $heightMax
    """)
    Roslina updateRoslina(
    @Param("name") String name,
    @Param("latinName") String latinName,
    @Param("description") String description,
    @Param("imageFilename") String imageFilename,
    @Param("heightMin") Double heightMin,
    @Param("heightMax") Double heightMax
    );


    // Czas wykonania testu: 462ms, 330ms
    @Query("""
            MATCH (p:Roslina{nazwaLacinska: $latinName}) WHERE NOT p:UzytkownikRoslina
            SET p.nazwa = $name, p.opis = $description,
                p.obraz = COALESCE($imageFilename, 'default_plant.jpg'),
                p.wysokoscMin = $heightMin, p.wysokoscMax = $heightMax

            WITH p
            OPTIONAL MATCH (p)-[r]->(w:Wlasciwosc), (p)<-[r2:MA_ROSLINE]-(w)
            DELETE r, r2

            WITH p, $relatLump AS relatLump
            UNWIND relatLump AS relat
            WITH p, relat, [ relat.labels, 'Wlasciwosc' ] AS labels 
            CALL apoc.merge.node(labels, {nazwa: relat.nazwa}) YIELD node AS w
            WITH p, w, relat
            CALL apoc.merge.relationship(p, relat.relacja, {}, {}, w) YIELD rel
            MERGE (w)-[:MA_ROSLINE]->(p)

            WITH p OPTIONAL MATCH path=(p)-[r]->(w)
            RETURN p, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relus
          """)
    Roslina updateRoslinaRelationshipsButEasierAndSlower(
        @Param("name") String name,
        @Param("latinName") String latinName,
        @Param("description") String description,
        @Param("imageFilename") String imageFilename,
        @Param("heightMin") Double heightMin,
        @Param("heightMax") Double heightMax,
        @Param("relatLump") List<Map<String, String>> relatLump
    );
    
    @Query("MATCH (p:Roslina{nazwaLacinska: $latinName}) WHERE NOT p:UzytkownikRoslina DETACH DELETE p")
    void deleteByNazwaLacinska(@Param("latinName") String latinName);

}
