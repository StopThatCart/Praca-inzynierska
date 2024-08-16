package com.example.yukka.model.social.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.social.komentarz.Komentarz;



public interface KomentarzRepository extends Neo4jRepository<Komentarz, Long> {

    
    @Query("""
            MATCH (kom:Komentarz{komentarzId: $komentarzId})<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik)
            OPTIONAL MATCH path = (:Post)-[:MA_KOMENTARZ]->(kom)
            RETURN kom, r1, uzyt, collect(nodes(path)) as pathNodes, collect(relationships(path)) as pathRels
            """)
    Optional<Komentarz> findKomentarzByKomentarzId(@Param("komentarzId") String komentarzId);

    @Query("""
        MATCH (kom:Komentarz{komentarzId: $komentarzId})<-[:SKOMENTOWAL]-(:Uzytkownik)
        OPTIONAL MATCH path = (post:Post)-[:MA_KOMENTARZ]->(kom:Komentarz)
                          <-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
                          <-[:SKOMENTOWAL]-(uzytkownik:Uzytkownik)
        RETURN kom, collect(nodes(path)) collect(relationships(path))
        """)
    Optional<Komentarz> findKomentarzWithOdpowiedziByKomentarzId(@Param("komentarzId") String komentarzId);

    
    @Query("""
            MATCH (komentarz:Komentarz)<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik{email: $email})
            RETURN kom
            ORDER BY komentarz.dataUtworzenia DESC
            LIMIT 1
            """)
    Optional<Komentarz> findNewestKomentarzOfUzytkownik(@Param("email") String email);


    @Query(value = """
        MATCH (komentarz:Komentarz)<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik{email: $email})
        RETURN komentarz, r1, uzyt
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
        countQuery = """
        MATCH (komentarz:Komentarz)<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik{email: $email})
        RETURN count(komentarz)
    """)
    Page<Komentarz> findKomentarzeOfUzytkownik(Pageable pageable, @Param("email") String email);

    @Query("""
            MATCH (uzyt:Uzytkownik{email: $email})
            MATCH (kom:Komentarz{komentarzId: $komentarzId})
            MERGE (uzyt)-[relu:OCENIL{lubi: $ocena}]->(kom)

            WITH kom
            MATCH (kom)<-[r:OCENIL]-(uzyt)
            WITH kom COUNT(CASE WHEN r.lubi = true THEN 1 ELSE NULL END) AS ocenyLubi,
            COUNT(CASE WHEN r.lubi = false THEN 1 ELSE NULL END) AS ocenyNielubi
            SET kom.ocenyLubi = ocenyLubi, kom.ocenyNielubi = ocenyNielubi
            RETURN kom
            """)
    Komentarz addOcenaToKomentarz(@Param("email") String email, @Param("komentarzId") String komentarzId, @Param("ocena") boolean ocena);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})-[relu:OCENIL]->(kom:Komentarz{komentarzId: $komentarzId})
        DELETE relu

        WITH kom
        MATCH (kom)<-[r:OCENIL]-(uzyt)
        WITH kom, COUNT(CASE WHEN r.lubi = true THEN 1 ELSE NULL END) AS ocenyLubi,
        COUNT(CASE WHEN r.lubi = false THEN 1 ELSE NULL END) AS ocenyNielubi
        SET kom.ocenyLubi = ocenyLubi, kom.ocenyNielubi = ocenyNielubi
        """)
    void removeOcenaFromKomentarz(@Param("email") String email, @Param("komentarzId") String komentarzId, @Param("ocena") boolean ocena);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        MATCH (kom2:Komentarz{komentarzId: $komentarzId})
        WITH uzyt, kom2, $kom.__properties__ AS pt 
        CREATE (uzyt)-[:SKOMENTOWAL]->
                (kom:Komentarz{komentarzId: pt.komentarzId, opis: pt.opis, 
                ocenyLubi: 0, ocenyNielubi: 0, 
                obraz: pt.obraz, dataUtworzenia: localdatetime()})
                -[:ODPOWIEDZIAL]->(kom2)
        RETURN kom
        """)
    Komentarz addKomentarzToKomentarz(@Param("email") String email, @Param("kom") Komentarz kom, @Param("komentarzId") String targetKomentarzId);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        MATCH (post:Post{postId: $postId})
        WITH uzyt, post, $kom.__properties__ AS pt 
        CREATE (uzyt)-[:SKOMENTOWAL]->
                (kom:Komentarz{komentarzId: pt.komentarzId, opis: pt.opis, 
                ocenyLubi: 0, ocenyNielubi: 0, obraz: pt.obraz, dataUtworzenia: localdatetime()})
                <-[:MA_KOMENTARZ]-(post)
        RETURN kom
        """)
    Komentarz addKomentarzToPost(@Param("email") String email, @Param("postId") String postId, @Param("kom") Komentarz kom);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        MATCH (uzyt)-[JEST_W_ROZMOWIE]->(priv:RozmowaPrywatna)
        WITH uzyt, priv, $kom.__properies__ as pt
        CREATE (uzyt)-[:SKOMENTOWAL]->
                (kom:Komentarz{komentarzId: pt.komentarzId, opis: pt.opis, 
                ocenyLubi: null, ocenyNielubi: null, obraz: pt.obraz, dataUtworzenia: localdatetime()})
                <-[:MA_KOMENTARZ]-(priv)
        RETURN kom
        """)
    Komentarz addKomentarzToRozmowaPrywatna(@Param("email") String email, @Param("kom") Komentarz kom);

    // Jakimś cudem działa z i bez odpowiedzi. Czary.
    @Query("""
        MATCH (post:Post{postId: $postId})-[:MA_KOMENTARZ]->(komentarz:Komentarz)
        OPTIONAL MATCH (komentarz)<-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
        WITH post, komentarz, collect(odpowiedz) AS odpowiedzi
        WITH post, sum(size(odpowiedzi)) AS liczbaOdpowiedzi
        SET post.liczba_komentarzy = liczbaOdpowiedzi
        """)
    void updateKomentarzeCountInPost(@Param("postId") String postId);


    @Query("""
        MATCH (:Uzytkownik{email:email})-[:SKOMENTOWAL]->(komentarz:Komentarz{$kom.__properies__.komentarzId})
        SET komentarz.opis = $kom.__properies__.opis 
        RETURN komentarz
        """)
    Komentarz updateKomentarz(@Param("email") String email, @Param("postId") String postId, @Param("kom") Komentarz kom);


    // TODO: Komentarze na profilowe, ogród, galerię, może jeszcze rośliny


    // UWAGA: To prawdopodobnie nie będzie używane, gdyż komentarze powinno się usuwać tylko wtedy, kiedy usuwa się cały post/ogród/obraz
    /* 
    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})-[:SKOMENTOWAL]->(kom:Komentarz{komentarzId: $komentarzId})<-[:MA_KOMENTARZ]-(post:Post{postId: $postId})
        DETACH DELETE kom

        WITH post
        CALL apoc.path.expand(post, 'MA_KOMENTARZ', 'Komentarz') YIELD node
        WITH post, COLLECT(DISTINCT node) AS komentarze
        SET post.komentarze = SIZE(komentarze)

        """)
    void removeKomentarzFromPost(@Param("email") String email, @Param("postId") String postId, @Param("komentarzId") String komentarzId);
    */

    // UWAGA: TYLKO DO TESTÓW
    @Query("""
        MATCH (u:Komentarz) DETACH DELETE u 
        """)
    void clearKomentarze();

    // UWAGA: Nie będzie działać dla wiadomości prywatnych. Chyba.
    @Query("""
        MATCH (kom:Komentarz{komentarzId: $komentarzId})
        OPTIONAL MATCH (kom)<-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
        DETACH DELETE odpowiedz
        """)
    void removeKomentarz(@Param("komentarzId") String komentarzId);

}
