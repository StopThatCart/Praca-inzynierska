package com.example.yukka.model.uzytkownik.token;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repozytorium dla operacji na encji Token w bazie danych Neo4j.
 * 
 * <p>Interfejs ten rozszerza Neo4jRepository i zapewnia metody do wykonywania
 * zapytań Cypher na bazie danych Neo4j. Metody te umożliwiają sprawdzanie,
 * pobieranie, zapisywanie oraz aktualizowanie danych dotyczących tokenów
 * i użytkowników powiązanych z tymi tokenami.</p>
 * 
 * <p>Metody w tym repozytorium wykorzystują adnotacje @Query do definiowania
 * zapytań Cypher, które są wykonywane na bazie danych. Parametry zapytań
 * są przekazywane za pomocą adnotacji @Param.</p>
 * 
 * <p>Przykładowe operacje obejmują:</p>
 * <ul>
 *   <li>Wyszukiwanie tokenu na podstawie jego wartości.</li>
 *   <li>Wyszukiwanie tokenu na podstawie jego wartości i typu.</li>
 *   <li>Wyszukiwanie tokenu na podstawie adresu email użytkownika i typu tokenu.</li>
 *   <li>Dodawanie nowego tokenu i powiązanie go z użytkownikiem.</li>
 *   <li>Walidowanie tokenu poprzez ustawienie daty walidacji i wyczyszczenie nowego adresu email.</li>
 *   <li>Usuwanie tokenu powiązanego z użytkownikiem na podstawie adresu email i wartości tokenu.</li>
 * </ul>
 * 
 * <p>Repozytorium to jest częścią aplikacji zarządzającej użytkownikami i ich tokenami,
 * umożliwiającej zarządzanie procesami uwierzytelniania i autoryzacji.</p>
 */
public interface TokenRepository extends Neo4jRepository<Token, Long> {
    
    @Query("""
        MATCH (t:Token {token: $token})-[r:WALIDUJE]->(u:Uzytkownik)
        RETURN t, r, u
            """)
    Optional<Token> findByToken(String token);

    @Query("""
        MATCH (t:Token {token: $token, typ: $typ })-[r:WALIDUJE]->(u:Uzytkownik)
        RETURN t, r, u
            """)
    Optional<Token> findByToken(String token, String typ);

    @Query("""
        MATCH (t:Token{typ: $typ})-[r:WALIDUJE]->(u:Uzytkownik {email: $email})
        RETURN t, r, u
            """)
    Optional<Token> findByUzytkownikEmail(String email, String typ);

    @Query("""
        MATCH (u:Uzytkownik {email: $email})
        WITH u
        CREATE (t:Token {
            token: $token, 
            typ: $typ, 
            nowyEmail: $nowyEmail,
            dataUtworzenia: $dataUtworzenia, 
            dataWygasniecia: $dataWygasniecia, 
            dataWalidacji: $dataWalidacji
            })

        WITH t, u
        MERGE (t)-[r:WALIDUJE]->(u)
        RETURN t, r, u
            """)
    Token add(
        @Param("email") String email, @Param("token") String token, 
        @Param("typ") String typ,
        @Param("nowyEmail") String nowyEmail,
        @Param("dataUtworzenia") LocalDateTime dataUtworzenia,
        @Param("dataWygasniecia") LocalDateTime dataWygasniecia,
        @Param("dataWalidacji") LocalDateTime dataWalidacji);


    @Query("""
        MATCH (t:Token {token: $token})-[r:WALIDUJE]->(u:Uzytkownik)
        SET t.dataWalidacji = $dataWalidacji,
            t.nowyEmail = null
        RETURN t, r, u
            """)
    Token validate(@Param("token") String token, @Param("dataWalidacji") LocalDateTime dataWalidacji);


    @Query("""
        MATCH (t:Token {token: $token})-[r:WALIDUJE]->(u:Uzytkownik{ email: $email})
        DETACH DELETE t
            """)
    void removeToken(@Param("email") String email, @Param("token") String token);
}
