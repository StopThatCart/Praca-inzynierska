package com.example.yukka.model.roslina.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.roslina.Roslina;


public interface UzytkownikRoslinaRepository  extends Neo4jRepository<Roslina, Long> {
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
        MATCH (roslina:UzytkownikRoslina)-[:STWORZONA_PRZEZ]->(uzytkownik:Uzytkownik{uzytId: $uzytId})
        RETURN roslina
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
       countQuery = """
        MATCH (roslina:UzytkownikRoslina)-[:STWORZONA_PRZEZ]->(uzytkownik:Uzytkownik{uzytId: $uzytId})
        RETURN count(roslina)
        """)
    Page<Roslina> findAllRoslinyOfUzytkownik(@Param("uzytId") String uzytId, Pageable pageable);


    @Query("""
        MATCH (uzytkownik:Uzytkownik{uzytId: $uzytId})
        WITH uzytkownik, $roslina.__properties__ AS rp 
        MERGE (p:UzytkownikRoslina:Roslina {
        nazwa: rp.nazwa, 
        nazwaLacinska: rp.nazwaLacinska, 
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
        MERGE (w)-[:MA_ROSLINE]->(p)

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
                p.obraz = COALESCE($imageFilename, 'default_plant.jpg'),
                p.wysokoscMin = $heightMin, p.wysokoscMax = $heightMax
           WITH p, $relatLump AS relatLump UNWIND relatLump AS relat

           MATCH (p)-[r]->(w:UzytkownikWlasciwosc)
           WITH p, r, w, collect(relat) AS newRelatLump

           WHERE NOT any(np IN newRelatLump WHERE w.nazwa = np.nazwa AND type(r) = np.relacja)
           DELETE r

           WITH p, newRelatLump AS relatLump
           UNWIND relatLump AS relat
           WITH p, relat, [ relat.etykieta, 'UzytkownikWlasciwosc', 'Wlasciwosc' ] AS labels
           CALL apoc.merge.node(labels, {nazwa: relat.nazwa}) YIELD node AS w
           WITH p, w, relat
           CALL apoc.merge.relationship(p, relat.relacja, {}, {}, w) YIELD rel
           MERGE (w)-[:MA_ROSLINE]->(p)

           WITH p OPTIONAL MATCH path=(p)-[r]->(w)
           RETURN p, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relus
    """)
    Roslina updateRoslina(
        @Param("name") String name,
        @Param("roslinaId") String roslinaId,
        @Param("description") String description,
        @Param("imageFilename") String imageFilename,
        @Param("heightMin") Double heightMin,
        @Param("heightMax") Double heightMax,
        @Param("relatLump") List<Map<String, String>> relatLump
    );

    @Query("""
        MATCH (p:UzytkownikRoslina{roslinaId: $roslinaId})
        SET  p.nazwa = $name, p.opis = $description,
            p.obraz = COALESCE($imageFilename, 'default_plant.jpg'),
            p.wysokoscMin = $heightMin, p.wysokoscMax = $heightMax
    """)
    Roslina updateRoslina(
    @Param("name") String name,
    @Param("roslinaId") String roslinaId,
    @Param("description") String description,
    @Param("imageFilename") String imageFilename,
    @Param("heightMin") Double heightMin,
    @Param("heightMax") Double heightMax
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

}
