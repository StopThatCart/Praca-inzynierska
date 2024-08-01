package com.example.yukka.model.uzytkownik;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;


public interface UzytkownikRepository extends Neo4jRepository<Uzytkownik, Long> {
    List<Uzytkownik> findByUsername(String name);
    Optional<Uzytkownik> findByEmail(String email);

    @Query("MATCH (u:Uzytkownik) WHERE u.nazwa = $nazwa OR u.email = $email RETURN u")
    Optional<Uzytkownik> checkIfUzytkownikExists(@Param("nazwa") String nazwa, @Param("email") String email);

    @Query("MATCH (u:Uzytkownik) WHERE u.nazwa = $nazwa OR u.email = $nazwa RETURN u")
    Uzytkownik findByNameOrEmail(@Param("nazwa") String nameOrEmail);

    @Query("MATCH (u:Uzytkownik) WHERE (u.nazwa = $nazwa OR u.email = $nazwa) AND u.haslo = $haslo RETURN u")
    Uzytkownik findByLogin(@Param("nazwa") String nazwa, @Param("haslo") String haslo);

    List<Uzytkownik> findByLabels(Set<String> labels);

    @Query("MATCH (u:Uzytkownik) RETURN u")
    Collection<Uzytkownik> getAllUsers();

    @Query("CREATE (u:Uzytkownik {nazwa: $nazwa, email: $email, haslo: $haslo, data_utworzenia: localdatetime(), ban: false})")
    void addNewUzytkownik(@Param("nazwa") String nazwa, @Param("email") String email, @Param("haslo") String haslo);

    @Query("CREATE (u:Uzytkownik:Pracownik {nazwa: $nazwa, email: $email, haslo: $haslo, data_utworzenia: localdatetime(), ban: false})")
    void addNewPracownik(@Param("nazwa") String nazwa, @Param("email") String email, @Param("haslo") String haslo);

    @Query("CREATE (u:Uzytkownik:Admin {nazwa: $nazwa, email: $email, haslo: $haslo, data_utworzenia: localdatetime(), ban: false})")
    void addNewAdmin(@Param("nazwa") String nazwa, @Param("email") String email, @Param("haslo") String haslo);

}
