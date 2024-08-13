package com.example.yukka.model.post.controller;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.post.Komentarz;



public interface KomentarzRepository extends Neo4jRepository<Komentarz, Long> {

    @Query("""
            MATCH (uzyt:Uzytkownik{email: $email})
            MATCH (kom:Komentarz{komentarz_id: $komentarz_id})
            MERGE (uzyt)-[relu:OCENIL{lubi: $ocena}]->(kom)

            WITH kom
            MATCH (kom)<-[r:OCENIL]-(uzyt)
            WITH kom, COUNT(CASE WHEN r.lubi = true THEN 1 ELSE NULL END) AS oceny_lubi,
            COUNT(CASE WHEN r.lubi = false THEN 1 ELSE NULL END) AS oceny_nie_lubi
            SET kom.oceny_lubi = oceny_lubi, kom.oceny_nie_lubi = oceny_nie_lubi
            """)
    void addOcenaToKomentarz(@Param("email") String email, @Param("komentarz_id") String komentarz_id, @Param("ocena") boolean ocena);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})-[relu:OCENIL]->(kom:Komentarz{komentarz_id: $komentarz_id})
        DELETE relu

        WITH kom
        MATCH (kom)<-[r:OCENIL]-(uzyt)
        WITH kom, COUNT(CASE WHEN r.lubi = true THEN 1 ELSE NULL END) AS oceny_lubi,
        COUNT(CASE WHEN r.lubi = false THEN 1 ELSE NULL END) AS oceny_nie_lubi
        SET kom.oceny_lubi = oceny_lubi, kom.oceny_nie_lubi = oceny_nie_lubi
        """)
    void removeOcenaFromKomentarz(@Param("email") String email, @Param("komentarz_id") String komentarz_id, @Param("ocena") boolean ocena);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        MATCH (kom2:Komentarz{komentarz_id: $target_komentarz_id})
        WITH uzyt, kom2, $kom.__properties__ AS pt 
        CREATE (uzyt)-[:SKOMENTOWAL]->
                (kom:Komentarz{komentarz_id: pt.komentarz_id, opis: pt.opis, 
                oceny_lubi: 0, oceny_nie_lubi: 0, 
                obraz: pt.obraz, data_utworzenia: localdatetime()})
                -[:ODPOWIEDZIAL]->(kom2)
        """)
    void addKomentarzToKomentarz(@Param("email") String email, @Param("kom") Komentarz kom, @Param("target_komentarz_id") String targetKomentarzId);


    // UWAGA: Nie wiem, czy działa
    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        MATCH (post:Post{post_id: $target_post_id})
        WITH uzyt, post, $kom.__properties__ AS pt 
        CREATE (uzyt)-[:SKOMENTOWAL]->
                (kom:Komentarz{komentarz_id: pt.komentarz_id, opis: pt.opis, 
                oceny_lubi: 0, oceny_nie_lubi: 0, obraz: pt.obraz, data_utworzenia: localdatetime()})
                <-[:MA_KOMENTARZ]-(post)
        """)
    void addKomentarzToPost(@Param("email") String email, @Param("target_post_id") String targetPostId, @Param("kom") Komentarz kom);

    @Query("""
        MATCH (post:Post{post_id: $target_post_id})-[:MA_KOMENTARZ]->(komentarz:Komentarz)
        OPTIONAL MATCH (komentarz)<-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
        WITH post, komentarz, collect(odpowiedz) AS odpowiedzi
        WITH post, sum(size(odpowiedzi)) AS liczbaOdpowiedzi
        SET post.liczba_komentarzy = liczbaOdpowiedzi
        """)
    void updateKomentarzeCountInPost(@Param("target_post_id") String targetPostId);


    // TODO: Komentarze na profilowe, ogród, galerię, może jeszcze rośliny


    // UWAGA: To prawdopodobnie nie będzie używane, gdyż komentarze powinno się usuwać tylko wtedy, kiedy usuwa się cały post/ogród/obraz
    /* 
    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})-[:SKOMENTOWAL]->(kom:Komentarz{komentarz_id: $komentarz_id})<-[:MA_KOMENTARZ]-(post:Post{post_id: $targetPostId})
        DETACH DELETE kom

        WITH post
        CALL apoc.path.expand(post, 'MA_KOMENTARZ', 'Komentarz') YIELD node
        WITH post, COLLECT(DISTINCT node) AS komentarze
        SET post.komentarze = SIZE(komentarze)

        """)
    void removeKomentarzFromPost(@Param("email") String email, @Param("targetPostId") String targetPostId, @Param("komentarz_id") String komentarz_id);
    */

    // UWAGA: TYLKO DO TESTÓW
    @Query("""
        MATCH (u:Komentarz) DETACH DELETE u 
        """)
    void clearKomentarze();

}
