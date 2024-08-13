package com.example.yukka.model.social.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.social.RozmowaPrywatna;



public interface RozmowaPrywatnaRepository extends Neo4jRepository<RozmowaPrywatna, Long> {

 
    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        MATCH (uzyt)-[JEST_W_ROZMOWIE]->(priv:RozmowaPrywatna)
        OPTIONAL MATCH (priv)-[:MA_KOMENTARZ]->(kom:Komentarz)
        RETURN priv, collect(kom) AS komentarze
        """)
    Optional<RozmowaPrywatna> findRozmowaPrywatnaByEmail(@Param("email") String email);

    @Query("""
        MATCH (uzyt1:Uzytkownik{email: $email1})
        MATCH (uzyt2:Uzytkownik{email: $email2})
        WITH uzyt1, uzyt2
        CREATE (uzyt1)-[:JEST_W_ROZMOWIE]->
               (priv:RozmowaPrywatna{emaile: [uzyt1.email, uzyt2.email], data_utworzenia: localdatetime()})
               <-[JEST_W_ROZMOWIE]-(uzyt2)
        """)
    void addRozmowaPrywatna(@Param("email1") String email1, @Param("email2") String email2);

    @Query("""
        MATCH (priv:RozmowaPrywatna)
        WHERE all(email IN $emaile WHERE email IN priv.emaile) 
        OPTIONAL MATCH (priv)-[:MA_KOMENTARZ]->(komentarz:Komentarz)

        WITH priv, komentarz, collect(komentarz) AS komentarze
        UNWIND komentarze AS kom
        DETACH DELETE kom
        DETACH DELETE priv
        """)
    void deleteRozmowaPrywatna(@Param("emaile") List<String> emaile);

    @Query("""
        MATCH (u:RozmowaPrywatna) 
        DETACH DELETE u 
        """)
    void clearRozmowyPrywatne();

}
