package com.example.yukka.model.social.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.social.komentarz.Komentarz;

import io.micrometer.common.lang.NonNull;



public interface KomentarzRepository extends Neo4jRepository<Komentarz, Long> {

    @SuppressWarnings("null")
    @Override
    @Query("""
            MATCH (kom:Komentarz)
            RETURN kom
            """)
    @NonNull List<Komentarz> findAll();

    @Query("""
            MATCH (kom:Komentarz{komentarzId: $komentarzId})<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik)
            OPTIONAL MATCH (rozmowa:RozmowaPrywatna)-[r3:MA_WIADOMOSC]->(kom)
            OPTIONAL MATCH path = (:Uzytkownik)-[:MA_POST]->(:Post)-[:MA_KOMENTARZ]->(kom)
            OPTIONAL MATCH path2 = (:Uzytkownik)-[:MA_POST]->(:Post)<-[:JEST_W_POSCIE]-(kom)
            OPTIONAL MATCH path3 = (:Uzytkownik)-[:OCENIL]->(kom)
            RETURN kom, r1, uzyt, rozmowa, r3, 
            collect(nodes(path)) as pathNodes, collect(relationships(path)) as pathRels,
            collect(nodes(path2)) as pathNodes2, collect(relationships(path2)) as pathRels2,
            collect(nodes(path3)), collect(relationships(path3))
            """)
    Optional<Komentarz> findKomentarzByKomentarzId(@Param("komentarzId") String komentarzId);


    @Query("""
        MATCH (kom:Komentarz{komentarzId: $komentarzId})<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik)
        OPTIONAL MATCH path = (post:Post)-[:MA_KOMENTARZ]->(kom:Komentarz)
                          <-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
                          <-[:SKOMENTOWAL]-(uzytkownik:Uzytkownik)
        OPTIONAL MATCH path2 = (kom)<-[:OCENIL]-(:Uzytkownik)
        RETURN kom, r1, uzyt, collect(nodes(path)), collect(relationships(path)),
                collect(nodes(path2)), collect(relationships(path2))
        """)
    Optional<Komentarz> findKomentarzWithOdpowiedziByKomentarzId(@Param("komentarzId") String komentarzId);

    
    @Query("""
            MATCH (komentarz:Komentarz)<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik{email: $email})
            RETURN komentarz
            ORDER BY komentarz.dataUtworzenia DESC
            LIMIT 1
            """)
    Optional<Komentarz> findNewestKomentarzOfUzytkownik(@Param("email") String email);


    @Query(value = """
        MATCH path=(komentarz:Komentarz)<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik{nazwa: $nazwa})
        WHERE NOT (komentarz)<-[:MA_WIADOMOSC]-(:RozmowaPrywatna)
        OPTIONAL MATCH (post:Post)<-[r2:JEST_W_POSCIE]-(komentarz)
        OPTIONAL MATCH path2 = (komentarz)<-[:OCENIL]-(:Uzytkownik)

        RETURN  komentarz, r1, post, r2, collect(nodes(path)), collect(relationships(path)),
                collect(nodes(path2)), collect(relationships(path2))
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
        countQuery = """
        MATCH (komentarz:Komentarz)<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik{nazwa: $nazwa})
        WHERE NOT (komentarz)<-[:MA_WIADOMOSC]-(:RozmowaPrywatna)
        RETURN count(komentarz)
    """)
    Page<Komentarz> findKomentarzeOfUzytkownik(@Param("nazwa") String nazwa, Pageable pageable);

    @Query("""
            MATCH (uzyt:Uzytkownik{email: $email})
            MATCH (kom:Komentarz{komentarzId: $komentarzId})
            MERGE (uzyt)-[relu:OCENIL]->(kom)
            ON CREATE SET relu.lubi = $ocena
            ON MATCH SET relu.lubi = $ocena

            RETURN kom
            """)
    Komentarz addOcenaToKomentarz(@Param("email") String email, @Param("komentarzId") String komentarzId, @Param("ocena") boolean ocena);


    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})-[relu:OCENIL]->(kom:Komentarz{komentarzId: $komentarzId})
        DELETE relu
        """)
    void removeOcenaFromKomentarz(@Param("email") String email, @Param("komentarzId") String komentarzId);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        MATCH (kom2:Komentarz{komentarzId: $komentarzId})
        WITH uzyt, kom2, $kom.__properties__ AS pt 
        CREATE (uzyt)-[:SKOMENTOWAL]->
                (kom:Komentarz{komentarzId: pt.komentarzId, opis: pt.opis, edytowany: false,
                obraz: pt.obraz, dataUtworzenia: $time})
                -[:ODPOWIEDZIAL]->(kom2)

        WITH kom, kom2
        OPTIONAL MATCH (kom2)-[:ODPOWIEDZIAL*0..]->(kom1:Komentarz)<-[:MA_KOMENTARZ]-(post:Post)

        WITH kom, post
        CREATE (kom)-[:JEST_W_POSCIE]->(post)

        RETURN kom
        """)
    Komentarz addOdpowiedzToKomentarzInPost(@Param("email") String email, @Param("kom") Komentarz kom, 
    @Param("komentarzId") String targetKomentarzId,
    @Param("time") LocalDateTime time);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        MATCH (post:Post{postId: $postId})
        WITH uzyt, post, $kom.__properties__ AS pt 
        CREATE (uzyt)-[:SKOMENTOWAL]->
                (kom:Komentarz{komentarzId: pt.komentarzId, opis: pt.opis, edytowany: false, 
                obraz: pt.obraz, dataUtworzenia: $time})
                <-[:MA_KOMENTARZ]-(post)
        CREATE (kom)-[:JEST_W_POSCIE]->(post)
    
        RETURN kom
        """)
    Komentarz addKomentarzToPost(@Param("email") String email, @Param("postId") String postId, @Param("kom") Komentarz kom,
        @Param("time") LocalDateTime time);

    @Query("""
        MATCH (uzyt1:Uzytkownik{nazwa: $nadawca})-[:JEST_W_ROZMOWIE]->(priv:RozmowaPrywatna)<-[:JEST_W_ROZMOWIE]-(uzyt2:Uzytkownik{nazwa: $odbiorca})
        WITH uzyt1, priv, $kom.__properties__ as pt
        CREATE (uzyt1)-[:SKOMENTOWAL]->
                (kom:Komentarz{komentarzId: pt.komentarzId, opis: pt.opis, edytowany: false,
                obraz: pt.obraz, dataUtworzenia: $time})
                <-[:MA_WIADOMOSC]-(priv)
        WITH priv, kom
        SET priv.ostatnioAktualizowana = $time

        RETURN kom
        """)
    Komentarz addKomentarzToRozmowaPrywatna(@Param("nadawca") String nadawca, @Param("odbiorca") String odbiorca, 
        @Param("kom") Komentarz kom,
        @Param("time") LocalDateTime time);

    @Query("""
        MATCH (komentarz:Komentarz{ komentarzId:$komentarzId })
        SET komentarz.opis = $opis, komentarz.edytowany = true
        RETURN komentarz
        """)
    Optional<Komentarz> updateKomentarz(@Param("komentarzId") String komentarzId, @Param("opis") String opis);

    // UWAGA: TYLKO DO TESTÓW
    @Query("""
        MATCH (u:Komentarz) DETACH DELETE u 
        """)
    void clearKomentarze();

    @Query("""
        MATCH (oceniany:Uzytkownik)-[:SKOMENTOWAL]->(kom:Komentarz{komentarzId: $komentarzId})
        OPTIONAL MATCH (kom)<-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)<-[:SKOMENTOWAL]-(uzyt:Uzytkownik)
        DETACH DELETE odpowiedz
        """)
    void removeKomentarz(@Param("komentarzId") String komentarzId);

}