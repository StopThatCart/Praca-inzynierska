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
        MATCH (post:Post {postId: $postId})
        MATCH (autor:Uzytkownik)-[r1:MA_POST]->(post)
        OPTIONAL MATCH path = (post)-[:MA_KOMENTARZ]->
                              (kom:Komentarz)
                              <-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
                              <-[:SKOMENTOWAL]-(uzytkownik:Uzytkownik)
        OPTIONAL MATCH path2 = (post)<-[:OCENIL]-(oceniajacy:Uzytkownik) WHERE oceniajacy <> autor
        OPTIONAL MATCH path3 = (post)<-[:JEST_W_POSCIE]-(:Komentarz)

        OPTIONAL MATCH oceniajacyKomentarze = (kom)<-[:OCENIL]-(oceniajacyKom:Uzytkownik)
        OPTIONAL MATCH oceniajacyOdpowiedzi = (odpowiedz)<-[:OCENIL]-(oceniajacyOdp:Uzytkownik)

        RETURN post, r1, autor, collect(nodes(path)), collect(relationships(path)),
                collect(nodes(path2)), collect(relationships(path2)),
                collect(nodes(path3)), collect(relationships(path3)),
                collect(nodes(oceniajacyKomentarze)),  collect(relationships(oceniajacyKomentarze)),
                collect(nodes(oceniajacyOdpowiedzi)),  collect(relationships(oceniajacyOdpowiedzi))
        """)
    Optional<Post> findPostByPostId(@Param("postId") String postId);


    @Query("""
        MATCH (post:Post)<-[r1:MA_POST]-(uzyt:Uzytkownik{email: $email})
        RETURN post
        ORDER BY post.dataUtworzenia DESC
        LIMIT 1
        """)
    Optional<Post> findNewestPostOfUzytkownik(@Param("email") String email);


    @Query(value = """
        MATCH path = (post:Post)<-[:MA_POST]-(:Uzytkownik)
        OPTIONAL MATCH path2 = (post)-[:OCENIL]-(:Uzytkownik)
        OPTIONAL MATCH path3 = (post)<-[:JEST_W_POSCIE]-(kom:Komentarz)

        WHERE $szukaj IS NULL OR post.tytul CONTAINS $szukaj OR post.opis CONTAINS $szukaj
        RETURN post, collect(nodes(path)), collect(relationships(path)),
                collect(nodes(path2)), collect(relationships(path2)),
                collect(nodes(path3)), collect(relationships(path3))
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
       countQuery = """
        MATCH (post:Post)<-[:MA_POST]-(:Uzytkownik)

        WHERE $szukaj IS NULL OR post.tytul CONTAINS $szukaj OR post.opis CONTAINS $szukaj
        RETURN count(post)
        """)
    Page<Post> findAllPosts(@Param("szukaj") String szukaj, Pageable pageable);

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
    Page<Post> findAllPostyByUzytkownik(@Param("nazwa") String nazwa, Pageable pageable);

    @Query("""
            MATCH (uzyt:Uzytkownik{email: $email})
            MATCH (post:Post{postId: $postId})
            MERGE (uzyt)-[relu:OCENIL]->(post)
            ON CREATE SET relu.lubi = $ocena
            ON MATCH SET relu.lubi = $ocena

            WITH post
            MATCH (autor:Uzytkownik)-[r1:MA_POST]->(post)
            RETURN post, r1, autor
            """)
    Post addOcenaToPost(@Param("email") String email, @Param("postId") String postId, @Param("ocena") boolean ocena);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})-[relu:OCENIL]->(post:Post{postId: $postId})
        DELETE relu
        """)
    void removeOcenaFromPost(@Param("email") String email, @Param("postId") String postId);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        WITH uzyt, $post.__properties__ AS pt 
        CREATE (uzyt)-[relu:MA_POST]->(post:Post{postId: pt.postId, 
                                        tytul: pt.tytul, opis: pt.opis, 
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


    @Query("""
        MATCH (u:Post) 
        DETACH DELETE u 
        """)
    void clearPosts();

}
