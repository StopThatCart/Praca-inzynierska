package com.example.yukka.model.uzytkownik.token;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends Neo4jRepository<Token, Long> {
    
    @Query("""
        MATCH (t:Token {token: $token})-[r:WALIDUJE]->(u:Uzytkownik)
        RETURN t, r, u
            """)
    Optional<Token> findByToken(String token);

    @Query("""
        MATCH (t:Token)-[r:WALIDUJE]->(u:Uzytkownik {email: $email})
        RETURN t, r, u
            """)
    Optional<Token> findByUzytkownikEmail(String email);

    @Query("""
        MATCH (u:Uzytkownik {email: $email})
        WITH u
        CREATE (t:Token {token: $token})
        Set t.token = $token,
            t.dataUtworzenia = $dataUtworzenia,
            t.dataWygasniecia = $dataWygasniecia,
            t.dataWalidacji = $dataWalidacji
        WITH t, u
        MERGE (t)-[r:WALIDUJE]->(u)
        RETURN t, r, u
            """)
    Token add(
        @Param("email") String email, @Param("token") String token, 
        @Param("dataUtworzenia") LocalDateTime dataUtworzenia,
        @Param("dataWygasniecia") LocalDateTime dataWygasniecia,
        @Param("dataWalidacji") LocalDateTime dataWalidacji);


    @Query("""
        MATCH (t:Token {token: $token})-[r:WALIDUJE]->(u:Uzytkownik)
        SET t.dataWalidacji = $dataWalidacji
        RETURN t, r, u
            """)
    Token validate(@Param("token") String token, @Param("dataWalidacji") LocalDateTime dataWalidacji);


    @Query("""
        MATCH (t:Token {token: $token})-[r:WALIDUJE]->(u:Uzytkownik{ email: $email})
        DETACH DELETE t
            """)
    void removeToken(@Param("email") String email, @Param("token") String token);
}
