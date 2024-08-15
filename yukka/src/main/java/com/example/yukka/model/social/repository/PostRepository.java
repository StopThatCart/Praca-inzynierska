package com.example.yukka.model.social.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.social.Ocenil;
import com.example.yukka.model.social.post.Post;



public interface PostRepository extends Neo4jRepository<Post, Long> {
    @Query("""
        MATCH (post:Post {postId: $postId})
        OPTIONAL MATCH path = (:Uzytkownik)-[:MA_POST]->(post)-[:MA_KOMENTARZ]->
                              (kom:Komentarz)
                              <-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
                              <-[:SKOMENTOWAL]-(uzytkownik:Uzytkownik)
        RETURN post, collect(nodes(path)), collect(relationships(path))
        """)
    Optional<Post> findPostByPostIdButWithPath(@Param("postId") String postId);
     
    @Query("""
        MATCH (post:Post{postId: $postId})<-[:MA_POST]-(:Uzytkownik)
        OPTIONAL MATCH (post)-[:MA_KOMENTARZ]->(kom:Komentarz)
        OPTIONAL MATCH (kom)<-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
        OPTIONAL MATCH (uzytkownik)-[:SKOMENTOWAL]->(odpowiedz)

        WITH post, collect(DISTINCT odpowiedz) AS komentarze, collect(DISTINCT uzytkownik) AS uzytkownicy
        RETURN post, komentarze, uzytkownicy
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
        RETURN post, collect(nodes(path)), collect(relationships(path)) 
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
       countQuery = """
        MATCH (post:Post)
        RETURN count(post)
        """)
    Page<Post> findAllPosts(Pageable pageable);

    @Query(value = """
        MATCH path = (post:Post)<-[:MA_POST]-(uzyt:Uzytkownik{email: $email})
        RETURN post, collect(nodes(path)), collect(relationships(path)) 
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
            """,
            countQuery = """
        MATCH (post:Post)<-[:MA_POST]-(uzyt:Uzytkownik{email: $email})
        RETURN count(post)
        """)
    Page<Post> findAllPostyByUzytkownik(Pageable pageable, @Param("email") String email);

    @Query("""
            MATCH (uzyt:Uzytkownik{email: $email})
            MATCH (post:Post{postId: $postId})
            MERGE (uzyt)-[relu:OCENIL{lubi: $ocena}]->(post)

            WITH post, relu
            MATCH (post)<-[r:OCENIL]-(uzyt)
            WITH post, relu, COUNT(CASE WHEN r.lubi = true THEN 1 ELSE NULL END) AS ocenyLubi,
            COUNT(CASE WHEN r.lubi = false THEN 1 ELSE NULL END) AS ocenyNieLubi
            SET post.ocenyLubi = ocenyLubi, post.ocenyNieLubi = ocenyNieLubi
            RETURN relu
            """)
    Ocenil addOcenaToPost(@Param("email") String email, @Param("postId") String postId, @Param("ocena") boolean ocena);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})-[relu:OCENIL]->(post:Post{postId: $postId})
        DELETE relu

        WITH post
        MATCH (post)<-[r:OCENIL]-(uzyt)
        WITH post, COUNT(CASE WHEN r.lubi = true THEN 1 ELSE NULL END) AS ocenyLubi,
        COUNT(CASE WHEN r.lubi = false THEN 1 ELSE NULL END) AS ocenyNieLubi
        SET post.ocenyLubi = ocenyLubi, post.ocenyNieLubi = ocenyNieLubi

        """)
    void removeOcenaFromPost(@Param("email") String email, @Param("postId") String postId, @Param("ocena") boolean ocena);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        WITH uzyt, $post.__properties__ AS pt 
        CREATE (uzyt)-[relu:MA_POST]->(post:Post{postId: pt.postId, 
                                        tytul: pt.tytul, opis: pt.opis, 
                                        ocenyLubi: 0, ocenyNieLubi: 0, 
                                        obraz: COALESCE(pt.obraz, null), dataUtworzenia: localdatetime()})
        RETURN post
        """)
    Optional<Post> addPost(@Param("email") String email, @Param("post") Post post);

    @Query("""
        MATCH (post:Post{postId: $postId})
        SET post.obraz = $obraz
        """)
    void updatePostObraz(@Param("postId") String postId, @Param("obraz") String obraz);

    @Query("""
        MATCH (post:Post {postId: $postId})
    
        OPTIONAL MATCH (post)-[:MA_KOMENTARZ]->(komentarz:Komentarz)
        OPTIONAL MATCH (komentarz)<-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
            
        WITH post,komentarz, collect(odpowiedz) AS odpowiedzi
        UNWIND odpowiedzi AS odp
        DETACH DELETE odp

        WITH post, komentarz
        OPTIONAL MATCH (komentarz)<-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
        DETACH DELETE komentarz
        DETACH DELETE post
        """)
    void deletePost(@Param("postId") String postId);

    @Query("""
        MATCH (u:Post) 
        DETACH DELETE u 
        """)
    void clearPosts();

}
