package com.example.yukka.model.roslina.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.roslina.Roslina;

public interface RoslinaRepository extends Neo4jRepository<Roslina, Long> {

    @Query("""
        MATCH (ros:Roslina{nazwaLacinska: $latinName})
        OPTIONAL MATCH path=(ros)-[r]-(:Wlasciwosc)
        RETURN ros, collect(nodes(path)), collect(relationships(path))
    """)
    Optional<Roslina> findByNazwaLacinska(@Param("latinName") String latinName);


    @Query(value = """
        MATCH (roslina:Roslina)
        RETURN roslina
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
       countQuery = """
        MATCH (roslina:Roslina)
        RETURN count(roslina)
        """)
    Page<Roslina> findAllRosliny(Pageable pageable);

    // TODO: Sprawdź jak działa na frontendzie
    @Query(value = """
        WITH $roslina.__properties__ AS rp 
        MATCH (roslina:Roslina)
        OPTIONAL MATCH (roslina)-[rel]->(relatedNode)
        WHERE (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
            AND (rp.nazwaLacinska IS NULL OR roslina.nazwaLacinska CONTAINS rp.nazwaLacinska)
            AND (rp.wysokoscMin IS NULL OR roslina.wysokoscMin >= rp.wysokoscMin)
            AND (rp.wysokoscMax IS NULL OR roslina.wysokoscMax <= rp.wysokoscMax)
            AND (size($relatLump) = 0 OR 
                    ANY(relat IN $relatLump WHERE 
                        (relatedNode.nazwa = relat.nazwa) AND
                        (type(rel) = relat.relacja) AND
                        (labels(relatedNode) = [relat.labels, 'Wlasciwosc'])
                    )
        )
        RETURN DISTINCT roslina
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
       countQuery = """
        WITH $roslina.__properties__ AS rp 
        MATCH (roslina:Roslina)
        OPTIONAL MATCH (roslina)-[rel]->(relatedNode)
        WHERE (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
            AND (rp.nazwaLacinska IS NULL OR roslina.nazwaLacinska CONTAINS rp.nazwaLacinska)
            AND (rp.wysokoscMin IS NULL OR roslina.wysokoscMin >= rp.wysokoscMin)
            AND (rp.wysokoscMax IS NULL OR roslina.wysokoscMax <= rp.wysokoscMax)
            AND (size($relatLump) = 0 OR 
                    ANY(relat IN $relatLump WHERE 
                        (relatedNode.nazwa = relat.nazwa) AND
                        (type(rel) = relat.relacja) AND
                        (labels(relatedNode) = [relat.labels, 'Wlasciwosc'])
                    )
        )
        RETURN count(DISTINCT roslina)
        """)
    Page<Roslina> findAllRoslinyWithParameters(Pageable pageable, @Param("roslina") Roslina roslina);

    // Do wyrzucenia
    @Query("MATCH path=(p:Roslina)-[r]->(g:Wlasciwosc) RETURN p, collect(nodes(path)), collect(relationships(path)) LIMIT $amount")
    Collection<Roslina> getSomePlants(@Param("amount") int amount);
    // Do wyrzucenia
    @Query("MATCH (r:Roslina)-[:MA_GLEBE]->(g:Gleba) WHERE r.name = $name RETURN r, collect(g) as glebus")
    Roslina findRoslinaWithGleba(@Param("name") String name);

    @Query("""
        MERGE (p:Roslina {nazwa: $name, nazwaLacinska: $latinName, opis: $description, 
        obraz: COALESCE($imageFilename, 'default_plant.jpg'), wysokoscMin: $heightMin, wysokoscMax: $heightMax}) 

        WITH p, $relatLump AS relatLump UNWIND relatLump AS relat

        WITH p, relat, [ relat.labels, 'Wlasciwosc' ] AS labels
        CALL apoc.merge.node(labels, {nazwa: relat.nazwa}) YIELD node AS w
        WITH p, w, relat
        CALL apoc.merge.relationship(p, relat.relacja, {}, {}, w) YIELD rel
        MERGE (w)-[:MA_ROSLINE]->(p)

        WITH p OPTIONAL MATCH path=(p)-[r]->(w)
        RETURN p, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relus
    """)
    Roslina addRoslina(
        @Param("name") String name,@Param("latinName") String latinName, 
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
           MATCH (p:Roslina{nazwaLacinska: $latinName})
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
           WITH p, relat, [ relat.labels, 'Wlasciwosc' ] AS labels
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
        MATCH (p:Roslina{nazwaLacinska: $latinName})
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
            MATCH (p:Roslina{nazwaLacinska: $latinName})
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
    
    @Query("MATCH (p:Roslina{nazwaLacinska: $latinName}) DETACH DELETE p")
    void deleteByNazwaLacinska(@Param("latinName") String latinName);

}
