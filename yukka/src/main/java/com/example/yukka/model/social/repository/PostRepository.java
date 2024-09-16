package com.example.yukka.model.social.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.social.post.Post;

import jakarta.annotation.Nonnull;



public interface PostRepository extends Neo4jRepository<Post, Long> {

    @Override
    @Query("""
        MATCH (post:Post)
        RETURN post
        """)
    @Nonnull List<Post> findAll();
    @Query("""
        MATCH (post:Post {postId: $postId})
        MATCH (autor:Uzytkownik)-[r1:MA_POST]->(post)
        OPTIONAL MATCH path = (post)-[:MA_KOMENTARZ]->
                              (kom:Komentarz)
                              <-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
                              <-[:SKOMENTOWAL]-(uzytkownik:Uzytkownik)
        RETURN post, r1, autor, collect(nodes(path)), collect(relationships(path))
        """)
    Optional<Post> findPostByPostIdButWithPath(@Param("postId") String postId);
     
    @Query("""
        MATCH (post:Post{postId: $postId})<-[r1:MA_POST]-(autor:Uzytkownik)
        OPTIONAL MATCH (post)-[:MA_KOMENTARZ]->(kom:Komentarz)
        OPTIONAL MATCH (kom)<-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
        OPTIONAL MATCH (uzytkownik)-[:SKOMENTOWAL]->(odpowiedz)

        WITH post, r1, autor, collect(DISTINCT odpowiedz) AS komentarze, collect(DISTINCT uzytkownik) AS uzytkownicy
        RETURN post, r1, autor, komentarze, uzytkownicy
        """)
    Optional<Post> findPostByPostId(@Param("postId") String postId);


    @Query("""
        MATCH (kom2:Komentarz {komentarzId: $komentarzId})
        WITH kom2
        OPTIONAL MATCH (kom2)-[:ODPOWIEDZIAL*0..]->(kom1:Komentarz)<-[:MA_KOMENTARZ]-(post:Post)
        RETURN post
        """)
    Optional<Post> findPostByKomentarzOdpowiedzId(@Param("komentarzId") String komentarzId);


        @Query("""
            MATCH (post:Post)<-[r1:MA_POST]-(uzyt:Uzytkownik{email: $email})
            RETURN post
            ORDER BY post.dataUtworzenia DESC
            LIMIT 1
            """)
    Optional<Post> findNewestPostOfUzytkownik(@Param("email") String email);


    @Query(value = """
        MATCH path = (post:Post)<-[:MA_POST]-(:Uzytkownik)
        RETURN post, collect(nodes(path)), collect(relationships(path)) 
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
       countQuery = """
        MATCH (post:Post)<-[:MA_POST]-(:Uzytkownik)
        RETURN count(post)
        """)
    Page<Post> findAllPosts(Pageable pageable);

    @Query(value = """
        MATCH path = (post:Post)<-[:MA_POST]-(uzyt:Uzytkownik{nazwa: $nazwa})
        RETURN post, collect(nodes(path)), collect(relationships(path)) 
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
            """,
            countQuery = """
        MATCH (post:Post)<-[:MA_POST]-(uzyt:Uzytkownik{nazwa: $nazwa})
        RETURN count(post)
        """)
    Page<Post> findAllPostyByUzytkownik(@Param("nazwa") String nazwa, Pageable pageable);


    @Query("""
        MATCH path = (post:Post)<-[:MA_POST]-(uzyt:Uzytkownik{nazwa: $nazwa})
        RETURN count(post)
            """)
    Integer findAllPostyCountOfUzytkownik(@Param("nazwa") String nazwa);

    @Query("""
            MATCH (uzyt:Uzytkownik{email: $email})
            MATCH (post:Post{postId: $postId})
            MERGE (uzyt)-[relu:OCENIL]->(post)
            ON CREATE SET relu.lubi = $ocena
            ON MATCH SET relu.lubi = $ocena

            WITH post
            MATCH (post)<-[r:OCENIL]-(uzyt)
            WITH post, COUNT(CASE WHEN r.lubi = true THEN 1 ELSE NULL END) AS ocenyLubi,
            COUNT(CASE WHEN r.lubi = false THEN 1 ELSE NULL END) AS ocenyNieLubi
            SET post.ocenyLubi = ocenyLubi, post.ocenyNieLubi = ocenyNieLubi

            WITH post
            OPTIONAL MATCH (oceniany:Uzytkownik)-[:MA_POST]->(post)<-[r2:OCENIL]-(uzyt:Uzytkownik)
                WHERE oceniany <> uzyt
            WITH post, oceniany,  
                COUNT(CASE WHEN r2.lubi = true THEN 1 ELSE NULL END) AS ocenyPozytywne,
                COUNT(CASE WHEN r2.lubi = false THEN 1 ELSE NULL END) AS ocenyNegatywne
            SET oceniany.postyOcenyPozytywne = ocenyPozytywne, 
                oceniany.postyOcenyNegatywne = ocenyNegatywne

            WITH post
            MATCH (autor:Uzytkownik)-[r1:MA_POST]->(post)
            RETURN post, r1, autor
            """)
    Post addOcenaToPost(@Param("email") String email, @Param("postId") String postId, @Param("ocena") boolean ocena);


    @Query("""
        MATCH (post:Post{postId: $postId})
        OPTIONAL MATCH (oceniany:Uzytkownik)-[:MA_POST]->(post)<-[r2:OCENIL]-(uzyt:Uzytkownik)
            WHERE oceniany <> uzyt
        WITH post, oceniany,  
            COUNT(CASE WHEN r2.lubi = true THEN 1 ELSE NULL END) AS ocenyPozytywne,
            COUNT(CASE WHEN r2.lubi = false THEN 1 ELSE NULL END) AS ocenyNegatywne
        SET oceniany.postyOcenyPozytywne = ocenyPozytywne, 
            oceniany.postyOcenyNegatywne = ocenyNegatywne
        """)
    void updateOcenyCountOfPost(@Param("postId") String postId);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})-[relu:OCENIL]->(post:Post{postId: $postId})
        DELETE relu

        WITH post
        MATCH (post)<-[r:OCENIL]-(uzyt)
        WITH post, COUNT(CASE WHEN r.lubi = true THEN 1 ELSE NULL END) AS ocenyLubi,
        COUNT(CASE WHEN r.lubi = false THEN 1 ELSE NULL END) AS ocenyNieLubi
        SET post.ocenyLubi = ocenyLubi, post.ocenyNieLubi = ocenyNieLubi

        WITH post
        OPTIONAL MATCH (oceniany:Uzytkownik)-[:MA_POST]->(post)<-[r2:OCENIL]-(uzyt:Uzytkownik)
            WHERE oceniany <> uzyt
        WITH post, oceniany,  
            COUNT(CASE WHEN r2.lubi = true THEN 1 ELSE NULL END) AS ocenyPozytywne,
            COUNT(CASE WHEN r2.lubi = false THEN 1 ELSE NULL END) AS ocenyNegatywne
        SET oceniany.postyOcenyPozytywne = ocenyPozytywne, 
            oceniany.postyOcenyNegatywne = ocenyNegatywne

        """)
    void removeOcenaFromPost(@Param("email") String email, @Param("postId") String postId);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        WITH uzyt, $post.__properties__ AS pt 
        CREATE (uzyt)-[relu:MA_POST]->(post:Post{postId: pt.postId, 
                                        tytul: pt.tytul, opis: pt.opis, 
                                        ocenyLubi: 0, ocenyNieLubi: 0, liczbaKomentarzy: 0,
                                        obraz: COALESCE(pt.obraz, null), dataUtworzenia: $time
                                        })
        RETURN post
        """)
    Optional<Post> addPost(@Param("email") String email, @Param("post") Post post, @Param("time") LocalDateTime time);

    @Query("""
        MATCH (post:Post{postId: $postId})
        SET post.obraz = $obraz
        """)
    void updatePostObraz(@Param("postId") String postId, @Param("obraz") String obraz);

    // Po wykonaiu tej funkcji trzeba robić update ocen posta oraz komentarzy
    // Ewentualnie porzucić te atrybuty i tylko zwracać liczbę ocen/komentarzy w countach
    @Query("""
        MATCH (post:Post {postId: $postId})
        OPTIONAL MATCH (post)<-[:JEST_W_POSCIE]-(komentarz:Komentarz)

        WITH post, komentarz
        DETACH DELETE komentarz, post
        """)
    void deletePost(@Param("postId") String postId);

    @Query("""
        MATCH (post:Post {postId: $postId})
        OPTIONAL MATCH (post)<-[:JEST_W_POSCIE]-(komentarz:Komentarz)
        DETACH DELETE komentarz

        WITH post
        DETACH DELETE post
        """)
    void deletePostButBetter(@Param("postId") String postId);

    // Nie używaj
    /* 
    @Query("""
        MATCH (post:Post{postId: $postId})
        OPTIONAL MATCH (post)<-[:MA_POST]-(uzyt:Uzytkownik)
        OPTIONAL MATCH (post)-[:MA_KOMENTARZ]->()<-[:SKOMENTOWAL]-(uzyt2:Uzytkownik)
        OPTIONAL MATCH (post)<-[:OCENIL]-(uzyt3:Uzytkownik)
        WITH post, COLLECT(DISTINCT uzyt) + COLLECT(DISTINCT uzyt2) + COLLECT(DISTINCT uzyt3) AS uzytkownicy
        RETURN uzytkownicy
        """)
    List<Uzytkownik> getConnectedUzytkownicyFromPost(@Param("postId") String postId);

*/
    @Query("""
        MATCH (u:Post) 
        DETACH DELETE u 
        """)
    void clearPosts();

}
