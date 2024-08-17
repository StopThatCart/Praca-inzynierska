package com.example.yukka.model.social.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;



public interface RozmowaPrywatnaRepository extends Neo4jRepository<RozmowaPrywatna, Long> {
   @Query("""
    MATCH path = (uzyt1:Uzytkownik{nazwa: $nazwa1})-[:JEST_W_ROZMOWIE]->(priv:RozmowaPrywatna)<-[:JEST_W_ROZMOWIE]-(uzyt2:Uzytkownik{nazwa: $nazwa2})
    RETURN priv, collect(nodes(path)), collect(relationships(path))
       """
       )
    Optional<RozmowaPrywatna> findRozmowaPrywatna(@Param("nazwa1") String nazwa1, @Param("nazwa2") String nazwa2);

    
   @Query("""
    MATCH path = (uzyt1:Uzytkownik{nazwa: $nazwa1})-[:JEST_W_ROZMOWIE]->
                        (priv:RozmowaPrywatna)
                        <-[:JEST_W_ROZMOWIE]-(uzyt2:Uzytkownik{nazwa: $nazwa2})
    OPTIONAL MATCH (priv)<-[:MA_WIADOMOSC]-(kom:Komentarz)
    RETURN priv, collect(nodes(path)), collect(relationships(path)),  collect(kom) AS komentarze
       """
       )
    Optional<RozmowaPrywatna> findRozmowaPrywatnaWithKomentarze(@Param("nazwa1") String nazwa1, @Param("nazwa2") String nazwa2);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        MATCH (uzyt)-[:JEST_W_ROZMOWIE]->(priv:RozmowaPrywatna)
        OPTIONAL MATCH (priv)-[:MA_WIADOMOSC]->(kom:Komentarz)
        RETURN priv, collect(kom) AS komentarze
        """)
    List<RozmowaPrywatna> findRozmowyPrywatneByEmail(@Param("email") String email);

    @Query("""
        MATCH (uzyt1:Uzytkownik{nazwa: $nazwa1})
        MATCH (uzyt2:Uzytkownik{nazwa: $nazwa2})
        WITH uzyt1, uzyt2
        CREATE (uzyt1)-[:JEST_W_ROZMOWIE]->
               (priv:RozmowaPrywatna{emaile: [uzyt1.email, uzyt2.email], dataUtworzenia: localdatetime()})
               <-[:JEST_W_ROZMOWIE]-(uzyt2)
        """)
    RozmowaPrywatna saveRozmowaPrywatna(@Param("nazwa1") String nazwa1, @Param("nazwa2") String nazwa2);

    @Query("""
        MATCH (priv:RozmowaPrywatna)
        WHERE all(email IN $emaile WHERE email IN priv.emaile) 
        OPTIONAL MATCH (priv)-[:MA_WIADOMOSC]->(komentarz:Komentarz)

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


    @Query("""
        MATCH (uzyt1:Uzytkownik{nazwa: $nazwa1})-[:JEST_W_ROZMOWIE]->
                (priv:RozmowaPrywatna{aktywna: false, zablokowana: false, liczbaWiadomosci: 0, dataUtworzenia: localdatetime()})
                <-[:JEST_W_ROZMOWIE]-(uzyt2:Uzytkownik{nazwa: $nazwa2})
        SET     priv.aktywna = $rozmowa.__properties__.aktywna, 
                priv.zablokowana = $rozmowa.__properties__.zablokowana
        WITH priv
        MATCH (priv)<-[:MA_WIADOMOSC]-(kom:Komentarz)
        WITH priv, count(kom) AS liczbaKom
        SET priv.liczbaWiadomosci = liczbaKom
        RETURN  priv
        """
        )
    RozmowaPrywatna updateRozmowaPrywatna(@Param("nazwa1") String nazwa1, 
                                        @Param("nazwa2") String nazwa2,
                                        @Param("rozmowa") RozmowaPrywatna rozmowa);
}
