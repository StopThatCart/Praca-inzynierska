package com.example.yukka.model.Uzytkownik;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;


public interface UzytkownikRepository extends Neo4jRepository<Uzytkownik, Long> {
    List<Uzytkownik> findByUsername(String name);
    Optional<Uzytkownik> findByEmail(String email);

    List<Uzytkownik> findByLabels(Set<String> labels);

    @Query("MATCH (u:Uzytkownik) RETURN u")
    Collection<Uzytkownik> getAllUsers();
}
