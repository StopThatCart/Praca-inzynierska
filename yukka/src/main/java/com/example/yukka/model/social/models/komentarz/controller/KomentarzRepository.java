package com.example.yukka.model.social.models.komentarz.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.social.models.komentarz.Komentarz;

import io.micrometer.common.lang.NonNull;

/**
 * Repozytorium dla operacji na encji Komentarz w bazie danych Neo4j.
 * 
 * <p>Interfejs ten rozszerza Neo4jRepository i zapewnia metody do wykonywania
 * zapytań Cypher na bazie danych Neo4j. Metody te umożliwiają sprawdzanie,
 * pobieranie, zapisywanie oraz aktualizowanie danych dotyczących komentarzy
 * i powiązanych z nimi użytkowników, postów oraz rozmów prywatnych.</p>
 * 
 * <p>Metody w tym repozytorium wykorzystują adnotacje @Query do definiowania
 * zapytań Cypher, które są wykonywane na bazie danych. Parametry zapytań
 * są przekazywane za pomocą adnotacji @Param.</p>
 * 
 * <p>Przykładowe operacje obejmują:</p>
 * <ul>
 *   <li>Pobieranie wszystkich komentarzy.</li>
 *   <li>Wyszukiwanie komentarza na podstawie jego identyfikatora.</li>
 *   <li>Wyszukiwanie komentarzy użytkownika na podstawie jego nazwy lub adresu email.</li>
 *   <li>Dodawanie oceny do komentarza.</li>
 *   <li>Usuwanie oceny z komentarza.</li>
 *   <li>Dodawanie odpowiedzi do komentarza w poście.</li>
 *   <li>Dodawanie komentarza do postu.</li>
 *   <li>Dodawanie komentarza do rozmowy prywatnej.</li>
 *   <li>Aktualizowanie treści komentarza.</li>
 *   <li>Usuwanie komentarza oraz jego odpowiedzi.</li>
 * </ul>
 * 
 * <p>Repozytorium to jest częścią aplikacji społecznościowej, umożliwiającej
 * użytkownikom interakcję poprzez komentarze, oceny oraz prywatne wiadomości.</p>
 */
public interface KomentarzRepository extends Neo4jRepository<Komentarz, Long> {

    @SuppressWarnings("null")
    @Override
    @Query("""
            MATCH (kom:Komentarz)
            RETURN kom
            """)
    @NonNull List<Komentarz> findAll();

    @Query("""
            MATCH (kom:Komentarz{uuid: $uuid})<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik)
            OPTIONAL MATCH (rozmowa:RozmowaPrywatna)-[r3:MA_WIADOMOSC]->(kom)
            OPTIONAL MATCH path = (:Uzytkownik)-[:MA_POST]->(:Post)-[:MA_KOMENTARZ]->(kom)
            OPTIONAL MATCH path2 = (:Uzytkownik)-[:MA_POST]->(:Post)<-[:JEST_W_POSCIE]-(kom)
            OPTIONAL MATCH path3 = (:Uzytkownik)-[:OCENIL]->(kom)
            RETURN kom, r1, uzyt, rozmowa, r3, 
            collect(nodes(path)) as pathNodes, collect(relationships(path)) as pathRels,
            collect(nodes(path2)) as pathNodes2, collect(relationships(path2)) as pathRels2,
            collect(nodes(path3)), collect(relationships(path3))
            """)
    Optional<Komentarz> findKomentarzByUUID(String uuid);


    @Query("""
        MATCH (kom:Komentarz{uuid: $uuid})<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik)
        OPTIONAL MATCH path = (post:Post)-[:MA_KOMENTARZ]->(kom:Komentarz)
                          <-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
                          <-[:SKOMENTOWAL]-(uzytkownik:Uzytkownik)
        OPTIONAL MATCH path2 = (kom)<-[:OCENIL]-(:Uzytkownik)
        RETURN kom, r1, uzyt, collect(nodes(path)), collect(relationships(path)),
                collect(nodes(path2)), collect(relationships(path2))
        """)
    Optional<Komentarz> findKomentarzWithOdpowiedziByUUID(String uuid);

    
    @Query("""
            MATCH (komentarz:Komentarz)<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik{email: $email})
            RETURN komentarz
            ORDER BY komentarz.dataUtworzenia DESC
            LIMIT 1
            """)
    Optional<Komentarz> findNewestKomentarzOfUzytkownik(String email);


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
    Page<Komentarz> findKomentarzeOfUzytkownik(String nazwa, Pageable pageable);

    @Query("""
            MATCH (uzyt:Uzytkownik{email: $email})
            MATCH (kom:Komentarz{uuid: $uuid})
            MERGE (uzyt)-[relu:OCENIL]->(kom)
            ON CREATE SET relu.lubi = $ocena
            ON MATCH SET relu.lubi = $ocena

            WITH kom
            OPTIONAL MATCH path2 = (kom)<-[:OCENIL]-(:Uzytkownik)

            RETURN kom, collect(nodes(path2)), collect(relationships(path2))
            """)
    Komentarz addOcenaToKomentarz(String email, String uuid, boolean ocena);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        MATCH (kom2:Komentarz{uuid: $uuid})
        WITH uzyt, kom2, $kom.__properties__ AS pt 
        CREATE (uzyt)-[:SKOMENTOWAL]->
                (kom:Komentarz{uuid: randomUUID(), opis: pt.opis, edytowany: false,
                obraz: pt.obraz, dataUtworzenia: $time})
                -[:ODPOWIEDZIAL]->(kom2)

        WITH kom, kom2
        OPTIONAL MATCH (kom2)-[:ODPOWIEDZIAL*0..]->(kom1:Komentarz)<-[:MA_KOMENTARZ]-(post:Post)

        WITH kom, post
        CREATE (kom)-[:JEST_W_POSCIE]->(post)

        RETURN kom
        """)
    Komentarz addOdpowiedzToKomentarzInPost(String email, Komentarz kom, String uuid, @Param("time") LocalDateTime time);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        MATCH (post:Post{uuid: $postUUID})
        WITH uzyt, post, $kom.__properties__ AS pt 
        CREATE (uzyt)-[:SKOMENTOWAL]->
                (kom:Komentarz{uuid: randomUUID(), opis: pt.opis, edytowany: false, 
                obraz: pt.obraz, dataUtworzenia: $time})
                <-[:MA_KOMENTARZ]-(post)
        CREATE (kom)-[:JEST_W_POSCIE]->(post)
    
        RETURN kom
        """)
    Komentarz addKomentarzToPost(String email, String postUUID, Komentarz kom, @Param("time") LocalDateTime time);

    @Query("""
        MATCH (uzyt1:Uzytkownik{nazwa: $nadawca})-[:JEST_W_ROZMOWIE]->(priv:RozmowaPrywatna)<-[:JEST_W_ROZMOWIE]-(uzyt2:Uzytkownik{nazwa: $odbiorca})
        WITH uzyt1, priv, $kom.__properties__ as pt
        CREATE (uzyt1)-[:SKOMENTOWAL]->
                (kom:Komentarz{uuid: randomUUID(), opis: pt.opis, edytowany: false,
                obraz: pt.obraz, dataUtworzenia: $time})
                <-[:MA_WIADOMOSC]-(priv)
        WITH priv, kom
        SET priv.ostatnioAktualizowana = $time

        RETURN kom
        """)
    Komentarz addKomentarzToRozmowaPrywatna(String nadawca, String odbiorca, 
        @Param("kom") Komentarz kom,
        @Param("time") LocalDateTime time);

    @Query("""
        MATCH (komentarz:Komentarz{ uuid: $uuid })
        SET komentarz.opis = $opis, komentarz.edytowany = true
        RETURN komentarz
        """)
    Optional<Komentarz> updateKomentarz(String uuid, String opis);

    // UWAGA: TYLKO DO TESTÓW
    @Query("""
        MATCH (u:Komentarz) DETACH DELETE u 
        """)
    void clearKomentarze();

    @Query("""
        MATCH (oceniany:Uzytkownik)-[:SKOMENTOWAL]->(kom:Komentarz{uuid: $uuid})
        OPTIONAL MATCH (kom)<-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)<-[:SKOMENTOWAL]-(uzyt:Uzytkownik)
        DETACH DELETE odpowiedz
        """)
    void removeKomentarz(String uuid);

}