package com.example.yukka.model.social.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;

/**
 * Repozytorium dla operacji na encji RozmowaPrywatna w bazie danych Neo4j.
 * 
 * <p>Interfejs ten rozszerza Neo4jRepository i zapewnia metody do wykonywania
 * zapytań Cypher na bazie danych Neo4j. Metody te umożliwiają sprawdzanie,
 * pobieranie, zapisywanie oraz aktualizowanie danych dotyczących prywatnych rozmów
 * między użytkownikami.</p>
 * 
 * <p>Metody w tym repozytorium wykorzystują adnotacje @Query do definiowania
 * zapytań Cypher, które są wykonywane na bazie danych. Parametry zapytań
 * są przekazywane za pomocą adnotacji @Param.</p>
 * 
 * <p>Przykładowe operacje obejmują:</p>
 * <ul>
 *   <li>Wyszukiwanie prywatnej rozmowy na podstawie identyfikatorów użytkowników.</li>
 *   <li>Wyszukiwanie prywatnej rozmowy na podstawie nazw użytkowników.</li>
 *   <li>Wyszukiwanie prywatnej rozmowy wraz z komentarzami.</li>
 *   <li>Wyszukiwanie prywatnej rozmowy na podstawie identyfikatora rozmowy.</li>
 *   <li>Wyszukiwanie prywatnych rozmów użytkownika na podstawie jego adresu email.</li>
 *   <li>Zapraszanie użytkownika do prywatnej rozmowy.</li>
 *   <li>Akceptowanie zaproszenia do prywatnej rozmowy.</li>
 *   <li>Odrzucanie zaproszenia do prywatnej rozmowy.</li>
 *   <li>Usuwanie prywatnej rozmowy wraz z powiązanymi komentarzami.</li>
 *   <li>Czyszczenie wszystkich prywatnych rozmów i powiązanych komentarzy.</li>
 * </ul>
 * 
 * <p>Repozytorium to jest częścią aplikacji społecznościowej, umożliwiającej użytkownikom
 * prowadzenie prywatnych rozmów oraz zarządzanie nimi.</p>
 */
public interface RozmowaPrywatnaRepository extends Neo4jRepository<RozmowaPrywatna, Long> {
   @Query("""
    MATCH path = (uzyt1:Uzytkownik{uzytId: $odbiorca})-[:JEST_W_ROZMOWIE]->(priv:RozmowaPrywatna)<-[:JEST_W_ROZMOWIE]-(uzyt2:Uzytkownik{uzytId: $nadawca})
    RETURN priv, collect(nodes(path)), collect(relationships(path))
       """
       )
    Optional<RozmowaPrywatna> findRozmowaPrywatnaByUzytId(@Param("odbiorca") String odbiorca, @Param("nadawca") String nadawca);

    @Query("""
    MATCH path = (uzyt1:Uzytkownik{nazwa: $odbiorca})-[:JEST_W_ROZMOWIE]->(priv:RozmowaPrywatna)<-[:JEST_W_ROZMOWIE]-(uzyt2:Uzytkownik{nazwa: $nadawca})
    RETURN priv, collect(nodes(path)), collect(relationships(path))
       """
       )
    Optional<RozmowaPrywatna> findRozmowaPrywatnaByNazwa(@Param("odbiorca") String odbiorca, @Param("nadawca") String nadawca);

    
   @Query("""
    MATCH path = (uzyt1:Uzytkownik{uzytId: $nadawcaId})-[:JEST_W_ROZMOWIE]->
                (priv:RozmowaPrywatna)
                <-[:JEST_W_ROZMOWIE]-(uzyt2:Uzytkownik{uzytId: $odbiorcaId})
    OPTIONAL MATCH komPath = (priv)-[r1:MA_WIADOMOSC]->(kom:Komentarz)<-[:SKOMENTOWAL]-(diff:Uzytkownik)
    WITH priv, path, komPath, kom
    ORDER BY kom.dataUtworzenia
    RETURN priv, collect(nodes(path)), collect(relationships(path)), 
                collect(nodes(komPath)), collect(relationships(komPath))
       """
       )
    Optional<RozmowaPrywatna> findRozmowaPrywatnaWithKomentarze(@Param("odbiorcaId") String odbiorca, @Param("nadawcaId") String nadawca);


    @Query("""
    MATCH path = (priv:RozmowaPrywatna)<-[:JEST_W_ROZMOWIE]-(uzyt:Uzytkownik)
                WHERE id(priv) = $id
    OPTIONAL MATCH komPath = (priv)-[r1:MA_WIADOMOSC]->(kom:Komentarz)
    RETURN priv, collect(nodes(path)), collect(relationships(path)), 
                collect(nodes(komPath)), collect(relationships(komPath))
       """
       )
    Optional<RozmowaPrywatna> findRozmowaPrywatnaById(@Param("id") Long id);


    @Query(value ="""
        MATCH path = (uzyt:Uzytkownik{email: $email})-[:JEST_W_ROZMOWIE]->(priv:RozmowaPrywatna)<-[:JEST_W_ROZMOWIE]-(uzyt2:Uzytkownik)
        RETURN priv, collect(nodes(path)), collect(relationships(path))
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
           """,
           countQuery = """
        MATCH path = (uzyt:Uzytkownik{email: $email})-[:JEST_W_ROZMOWIE]->(priv:RozmowaPrywatna)<-[:JEST_W_ROZMOWIE]-(uzyt2:Uzytkownik)
        RETURN count(priv)
            """)
    Page<RozmowaPrywatna> findRozmowyPrywatneOfUzytkownik(@Param("email") String email, Pageable pageable);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        MATCH (uzyt)-[:JEST_W_ROZMOWIE]->(priv:RozmowaPrywatna)
        OPTIONAL MATCH (priv)-[:MA_WIADOMOSC]->(kom:Komentarz)
        RETURN priv, collect(kom) AS komentarze
        """)
    List<RozmowaPrywatna> findRozmowyPrywatneByEmail(@Param("email") String email);

    @Query("""
        MATCH (uzyt1:Uzytkownik{uzytId: $nadawcaId})
        MATCH (uzyt2:Uzytkownik{uzytId: $odbiorcaId})
        WITH uzyt1, uzyt2
        MERGE (uzyt1)-[:JEST_W_ROZMOWIE]->
               (priv:RozmowaPrywatna{aktywna: false, nadawca: uzyt1.uzytId, 
               dataUtworzenia: $time, 
               ostatnioAktualizowana: $time})
               <-[:JEST_W_ROZMOWIE]-(uzyt2)
        RETURN priv
        """)
    RozmowaPrywatna inviteToRozmowaPrywatna(@Param("nadawcaId") String nadawca, @Param("odbiorcaId") String odbiorca,
     @Param("time") LocalDateTime localdatetime);

    @Query("""
        MATCH (uzyt1:Uzytkownik{uzytId: $odbiorcaId})-[:JEST_W_ROZMOWIE]->
                (priv:RozmowaPrywatna)
                <-[:JEST_W_ROZMOWIE]-(uzyt2:Uzytkownik{uzytId: $nadawcaId})
        WHERE  priv.aktywna = false
        SET    priv.aktywna = true
        WITH   priv
        REMOVE priv.nadawca
        RETURN priv
        """
        )
    RozmowaPrywatna acceptRozmowaPrywatna(@Param("nadawcaId") String nadawcaId, 
                                        @Param("odbiorcaId") String odbiorcaId);

    @Query("""
        MATCH (odbiorca:Uzytkownik{uzytId: $odbiorcaId})-[:JEST_W_ROZMOWIE]->
                (priv:RozmowaPrywatna)
                <-[:JEST_W_ROZMOWIE]-(nadawca:Uzytkownik{uzytId: $nadawcaId})
        WHERE priv.aktywna = false AND priv.nadawca <> odbiorca.uzytId
        DETACH DELETE priv
        """
        )
    void rejectRozmowaPrywatna(@Param("nadawcaId") String nadawcaId, 
                                        @Param("odbiorcaId") String odbiorcaId,
                                        @Param("rozmowa") RozmowaPrywatna rozmowa);

    // To pójdzie do admina potem
    @Query("""
        MATCH  (uzyt1:Uzytkownik{uzytId: $uczestnik1})-[:JEST_W_ROZMOWIE]->
                (priv:RozmowaPrywatna)
                <-[:JEST_W_ROZMOWIE]-(uzyt2:Uzytkownik{uzytId: $uczestnik2})
        OPTIONAL MATCH (priv)-[:MA_WIADOMOSC]->(komentarz:Komentarz)

        WITH priv, komentarz, collect(komentarz) AS komentarze
        UNWIND komentarze AS kom
        DETACH DELETE kom
        DETACH DELETE priv
        """)
    void deleteRozmowaPrywatna(@Param("uczestnik1") String uczestnik1, @Param("uczestnik2") String uczestnik2);

    // funkcja seedowania, spokojnie.
    @Query("""
        MATCH (roz:RozmowaPrywatna)
        OPTIONAL MATCH (roz)-[:MA_WIADOMOSC]->(kom:Komentarz) 
        DETACH DELETE roz, kom
        """)
    void clearRozmowyPrywatne();
}
