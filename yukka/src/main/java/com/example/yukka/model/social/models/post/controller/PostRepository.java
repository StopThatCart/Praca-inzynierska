package com.example.yukka.model.social.models.post.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.social.models.post.Post;

import jakarta.annotation.Nonnull;


/**
 * Repozytorium dla operacji na encji Post w bazie danych Neo4j.
 * 
 * <p>Interfejs ten rozszerza Neo4jRepository i zapewnia metody do wykonywania
 * zapytań Cypher na bazie danych Neo4j. Metody te umożliwiają sprawdzanie,
 * pobieranie, zapisywanie oraz aktualizowanie danych dotyczących postów
 * i powiązanych z nimi użytkowników oraz komentarzy.</p>
 * 
 * <p>Metody w tym repozytorium wykorzystują adnotacje @Query do definiowania
 * zapytań Cypher, które są wykonywane na bazie danych. Parametry zapytań
 * są przekazywane za pomocą adnotacji @Param.</p>
 * 
 * <p>Przykładowe operacje obejmują:</p>
 * <ul>
 *   <li>Pobieranie wszystkich postów.</li>
 *   <li>Pobieranie najnowszego postu.</li>
 *   <li>Pobieranie postu na podstawie jego identyfikatora.</li>
 *   <li>Pobieranie najnowszego postu użytkownika na podstawie jego adresu email.</li>
 *   <li>Wyszukiwanie postów z możliwością paginacji i sortowania.</li>
 *   <li>Dodawanie oceny do postu przez użytkownika.</li>
 *   <li>Dodawanie nowego postu przez użytkownika.</li>
 *   <li>Aktualizowanie obrazu postu.</li>
 *   <li>Usuwanie postu wraz z powiązanymi komentarzami.</li>
 *   <li>Usuwanie wszystkich postów.</li>
 * </ul>
 * 
 * <p>Repozytorium to jest częścią aplikacji społecznościowej, umożliwiającej
 * użytkownikom tworzenie, ocenianie oraz komentowanie postów.</p>
 */
public interface PostRepository extends Neo4jRepository<Post, Long> {

    @SuppressWarnings("null")
    @Override
    @Query("""
        MATCH (post:Post)
        RETURN post
        """)
    @Nonnull List<Post> findAll();


    // Używane tylko do seedowania
    @Query("""
        MATCH (post:Post)
        RETURN post
        ORDER BY post.dataUtworzenia DESC
        LIMIT 1
        """)
    Optional<Post> findLatestPost();
    @Query("""
        MATCH (post:Post {uuid: $uuid})
        MATCH (autor:Uzytkownik)-[r1:MA_POST]->(post)
        OPTIONAL MATCH path = (post)-[:MA_KOMENTARZ]->
                              (kom:Komentarz)
                              <-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
                              <-[:SKOMENTOWAL]-(uzytkownik:Uzytkownik)
        OPTIONAL MATCH path2 = (post)<-[:OCENIL]-(oceniajacy:Uzytkownik)
        
        OPTIONAL MATCH path3 = (post)<-[:JEST_W_POSCIE]-(komentarze:Komentarz)
        OPTIONAL MATCH oceniajacyKomentarze = (komentarze)<-[:OCENIL]-(oceniajacyKom:Uzytkownik)

        RETURN post, r1, autor, collect(nodes(path)), collect(relationships(path)),
                collect(nodes(path2)), collect(relationships(path2)),
                collect(nodes(path3)), collect(relationships(path3)),
                collect(nodes(oceniajacyKomentarze)),  collect(relationships(oceniajacyKomentarze))
        """)
    Optional<Post> findPostByUUID(String uuid);


    @Query("""
        MATCH (post:Post {uuid: $uuid})
        MATCH (autor:Uzytkownik)-[r1:MA_POST]->(post)

        RETURN post, r1, autor
        """)
    Optional<Post> findPostByUUIDCheck(String uuid);


    @Query("""
        MATCH (post:Post)<-[r1:MA_POST]-(uzyt:Uzytkownik{email: $email})
        RETURN post
        ORDER BY post.dataUtworzenia DESC
        LIMIT 1
        """)
    Optional<Post> findNewestPostOfUzytkownik(String email);


    @Query(value = """
        MATCH path = (post:Post)<-[:MA_POST]-(:Uzytkownik)
        WHERE $szukaj IS NULL OR toLower(post.tytul) CONTAINS toLower($szukaj) OR toLower(post.opis) CONTAINS toLower($szukaj)

        OPTIONAL MATCH path2 = (post)<-[:OCENIL]-(:Uzytkownik)
        OPTIONAL MATCH path3 = (post)<-[:JEST_W_POSCIE]-(kom:Komentarz)

        RETURN post, collect(nodes(path)), collect(relationships(path)),
                collect(nodes(path2)), collect(relationships(path2)),
                collect(nodes(path3)), collect(relationships(path3))
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
       countQuery = """
        MATCH (post:Post)<-[:MA_POST]-(:Uzytkownik)
        WHERE $szukaj IS NULL OR toLower(post.tytul) CONTAINS toLower($szukaj) OR toLower(post.opis) CONTAINS toLower($szukaj)
        RETURN count(post)
        """)
    Page<Post> findAllPosts(String szukaj, Pageable pageable);

    @Query(value = """
        MATCH path = (post:Post)<-[:MA_POST]-(uzyt:Uzytkownik{nazwa: $nazwa})
        OPTIONAL MATCH path2 = (post)<-[:OCENIL]-(oceniajacy:Uzytkownik)
        OPTIONAL MATCH path3 = (post)<-[:JEST_W_POSCIE]-(:Komentarz)

        
        RETURN post, collect(nodes(path)), collect(relationships(path)),
                collect(nodes(path2)), collect(relationships(path2)),
                collect(nodes(path3)), collect(relationships(path3)) 
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
            """,
            countQuery = """
        MATCH (post:Post)<-[:MA_POST]-(uzyt:Uzytkownik{nazwa: $nazwa})
        RETURN count(post)
        """)
    Page<Post> findAllPostyByUzytkownik(String nazwa, Pageable pageable);

    @Query("""
            MATCH (uzyt:Uzytkownik{email: $email})
            MATCH (post:Post{uuid: $uuid})
            MERGE (uzyt)-[relu:OCENIL]->(post)
            ON CREATE SET relu.lubi = $ocena
            ON MATCH SET relu.lubi = $ocena

            WITH post
            MATCH (autor:Uzytkownik)-[r1:MA_POST]->(post)
            OPTIONAL MATCH path2 = (post)<-[:OCENIL]-(oceniajacy:Uzytkownik)

            RETURN post, r1, autor, collect(nodes(path2)), collect(relationships(path2))
            """)
    Post addOcenaToPost(String email, String uuid, boolean ocena);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        WITH uzyt, $post.__properties__ AS pt 
        CREATE (uzyt)-[relu:MA_POST]->(post:Post{uuid: randomUUID(), 
                                        tytul: pt.tytul, opis: pt.opis, 
                                        obraz: COALESCE(pt.obraz, null), dataUtworzenia: $time
                                        })
        RETURN post
        """)
    Optional<Post> addPost(String email, Post post, @Param("time") LocalDateTime time);

    @Query("""
        MATCH (post:Post{uuid: $uuid})
        SET post.obraz = $obraz
        """)
    void updatePostObraz(String uuid, String obraz);

    @Query("""
        MATCH (post:Post {uuid: $uuid})
        OPTIONAL MATCH (post)<-[:JEST_W_POSCIE]-(komentarz:Komentarz)

        WITH post, komentarz
        DETACH DELETE komentarz, post
        """)
    void deletePost(String uuid);

    @Query("""
        MATCH (post:Post {uuid: $uuid})
        OPTIONAL MATCH (post)<-[:JEST_W_POSCIE]-(komentarz:Komentarz)
        DETACH DELETE komentarz

        WITH post
        DETACH DELETE post
        """)
    void deletePostButBetter(String uuid);


    @Query("""
        MATCH (u:Post) 
        DETACH DELETE u 
        """)
    void clearPosts();

}
