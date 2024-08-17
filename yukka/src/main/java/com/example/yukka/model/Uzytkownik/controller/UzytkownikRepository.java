package com.example.yukka.model.uzytkownik.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.uzytkownik.Ustawienia;
import com.example.yukka.model.uzytkownik.Uzytkownik;

public interface UzytkownikRepository extends Neo4jRepository<Uzytkownik, Long> {
    @Query("""
            MATCH (u:Uzytkownik{nazwa: $nazwa})
            RETURN u
            """)
    Optional<Uzytkownik> findByNazwa(@Param("nazwa") String nazwa);
    @Query("""
        MATCH (u:Uzytkownik{email: $email})
        RETURN u
        """)
    Optional<Uzytkownik> findByEmail(@Param("email") String email);

    @Query("""
            MATCH (u:Uzytkownik) DETACH DELETE u 
            WITH u
            MATCH(ust:Ustawienia) DETACH DELETE ust
            WITH ust
            MATCH(roz:RozmowaPrywatna)
            OPTIONAL MATCH (roz)-[:MA_WIADOMOSC]->(kom:Komentarz)
            DETACH DELETE roz, kom
            WITH roz
            MATCH (u:Uzytkownik)
            DETACH DELETE u 
            """)
    void clearUzytkowicy();

    @Query("MATCH (u:Uzytkownik) WHERE u.nazwa = $nazwa OR u.email = $email RETURN u")
    Optional<Uzytkownik> checkIfUzytkownikExists(@Param("nazwa") String nazwa, @Param("email") String email);

    @Query("MATCH (u:Uzytkownik) WHERE u.nazwa = $nazwa OR u.email = $nazwa RETURN u")
    Optional<Uzytkownik> findByNameOrEmail(@Param("nazwa") String nameOrEmail);

    @Query("MATCH (u:Uzytkownik) WHERE (u.nazwa = $nazwa OR u.email = $nazwa) AND u.haslo = $haslo RETURN u")
    Uzytkownik findByLogin(@Param("nazwa") String nazwa, @Param("haslo") String haslo);

   // List<Uzytkownik> findByLabels(Set<String> labels);


    @Query("MATCH (u:Uzytkownik) RETURN u")
    Collection<Uzytkownik> findAllUzytkownicy();

    /* 
    @Query("""
        CREATE (u:Uzytkownik {nazwa: $nazwa, 
                              email: $email, haslo: $haslo, 
                              data_utworzenia: localdatetime(), 
                              ban: false
                })-[:MA_USTAWIENIA]->(ust:Ustawienia {
                                    statystyki_profilu: true, galeria_pokaz: true,
                                    galeria_ocena_komentarze: true, ogrod_pokaz: true,
                                    ogrod_ocena_komentarze: true, powiadomienia_komentarze_odpowiedz: true,
                                    powiadomienia_komentarze_ogrod: true, powiadomienia_komentarze_galeria: true,
                                    powiadomienia_ogrod_podlewanie: true, powiadomienia_ogrod_kwitnienie: true,
                                    powiadomienia_ogrod_owocowanie: true
                                    })
            """
            )
    void addNewUzytkownikButBadLikeVeryBad(@Param("nazwa") String nazwa, @Param("email") String email, @Param("haslo") String haslo);
*/
    @Query("""
        CREATE (u:Uzytkownik) SET u += $uzyt.__properties__
        WITH u
        CREATE (ustawienia:Ustawienia) SET ustawienia += $ustawienia.__properties__
        WITH u, ustawienia
        CREATE(u)-[:MA_USTAWIENIA]->(ustawienia)

        RETURN u
            """
            )
    Uzytkownik addUzytkownik(@Param("uzyt") Uzytkownik uzyt, @Param("ustawienia") Ustawienia ustawienia);

    @Query("""
        CREATE (u:Uzytkownik:`:#{allOf(#roles)}`) SET u += $uzyt.__properties__
        WITH u
        CREATE (ustawienia:Ustawienia) SET ustawienia += $ustawienia.__properties__
        WITH u, ustawienia
        CREATE(u)-[:MA_USTAWIENIA]->(ustawienia)
        RETURN u
            """
            )
    Uzytkownik addUzytkownik(@Param("uzyt") Uzytkownik uzyt, List<String> roles, @Param("ustawienia") Ustawienia ustawienia);

  //  @Query("CREATE (u:Uzytkownik:`:#{literal(#rola)}` {nazwa: $nazwa, email: $email, haslo: $haslo, data_utworzenia: localdatetime(), ban: false})")
   // void addNewPracownik(@Param("nazwa") String nazwa, @Param("email") String email, @Param("haslo") String haslo, @Param("rola") String rola);


    @Query("MATCH (u:Uzytkownik) WHERE u.email = $email SET u.ban = $ban RETURN u")
    Uzytkownik banUzytkownik(@Param("email") String email, @Param("ban") boolean ban);


    // TODO: Zmień jak będą kolejne komponenty dodawane
    @Query("""
            MATCH (u:Uzytkownik{email: $email})
            WITH u

            MATCH (u)-[:MA_USTAWIENIA]->(ust:Ustawienia)
            DETACH DELETE ust

            WITH u
            OPTIONAL MATCH (u)-[:JEST_W_ROZMOWIE]->(rozmowa:RozmowaPrywatna)
            OPTIONAL MATCH (rozmowa)-[:MA_WIADOMOSC]->(kom:Komentarz)
            DETACH DELETE kom, rozmowa
            
            WITH u
            DETACH DELETE u 
            """)
    void removeUzytkownik(@Param("email") String email);



}
