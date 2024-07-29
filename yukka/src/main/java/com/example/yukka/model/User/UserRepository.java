package com.example.yukka.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;


public interface UserRepository extends Neo4jRepository<User, Long> {
    List<User> findByUsername(String name);
    Optional<User> findByEmail(String email);

    List<User> findByLabels(Set<String> labels);

    @Query("MATCH (u:Uzytkownik) RETURN u")
    Collection<User> getAllSoils();
}
