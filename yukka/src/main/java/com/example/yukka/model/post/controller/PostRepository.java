package com.example.yukka.model.post.controller;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.post.Post;



public interface PostRepository extends Neo4jRepository<Post, Long> {

 

    @Query("""
            MATCH (uzyt:Uzytkownik{email: $email})
            MATCH (post:Post{post_id: $post_id})
            MERGE (uzyt)-[relu:OCENIL{lubi: $ocena}]->(post)

            WITH post
            MATCH (post)<-[r:OCENIL]-(uzyt)
            WITH post, COUNT(CASE WHEN r.lubi = true THEN 1 ELSE NULL END) AS oceny_lubi,
            COUNT(CASE WHEN r.lubi = false THEN 1 ELSE NULL END) AS oceny_nie_lubi
            SET post.oceny_lubi = oceny_lubi, post.oceny_nie_lubi = oceny_nie_lubi
            """)
    void addOcenaToPost(@Param("email") String email, @Param("post_id") String post_id, @Param("ocena") boolean ocena);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})-[relu:OCENIL]->(post:Post{post_id: $post_id})
        DELETE relu

        WITH post
        MATCH (post)<-[r:OCENIL]-(uzyt)
        WITH post, COUNT(CASE WHEN r.lubi = true THEN 1 ELSE NULL END) AS oceny_lubi,
        COUNT(CASE WHEN r.lubi = false THEN 1 ELSE NULL END) AS oceny_nie_lubi
        SET post.oceny_lubi = oceny_lubi, post.oceny_nie_lubi = oceny_nie_lubi

        """)
    void removeOcenaFromPost(@Param("email") String email, @Param("post_id") String post_id, @Param("ocena") boolean ocena);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        WITH uzyt, $post.__properties__ AS pt 
        CREATE (uzyt)-[relu:MA_POST]->(post:Post{post_id: pt.post_id, 
                                        tytul: pt.tytul, opis: pt.opis, 
                                        oceny_lubi: 0, oceny_nie_lubi: 0, 
                                        obraz: COALESCE(pt.obraz, null), data_utworzenia: localdatetime()})
        """)
    void addPost(@Param("email") String email, @Param("post") Post post);

    @Query("""
        MATCH (post:Post {post_id: $post_id})
    
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
    void deletePost(@Param("post_id") String post_id);

    @Query("""
        MATCH (u:Post) 
        DETACH DELETE u 
        WITH u
        MATCH(ust:Ustawienia) DETACH DELETE ust
        """)
    void clearPosts();

}
