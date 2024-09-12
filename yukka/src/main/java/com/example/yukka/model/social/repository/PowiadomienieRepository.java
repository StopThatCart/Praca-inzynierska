package com.example.yukka.model.social.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.social.powiadomienie.Powiadomienie;

public interface PowiadomienieRepository extends Neo4jRepository<Powiadomienie, Long> {

    @Query(value = """
        MATCH path=(powiadomienie:Powiadomienie)-[:POWIADAMIA]->(uzyt:Uzytkownik{nazwa: $nazwa})
        WHERE id(powiadomienie) = $id
        SET powiadomienie.przeczytane = true
        RETURN powiadomienie
        """)
    Optional<Powiadomienie> setPrzeczytane(@Param("nazwa") String nazwa, @Param("id") Long id);

    @Query(value = """
        MATCH path=(powiadomienie:Powiadomienie)-[:POWIADAMIA]->(uzyt:Uzytkownik{email: $email})

        RETURN  powiadomienie, collect(nodes(path)), collect(relationships(path))
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
        countQuery = """
        MATCH (powiadomienie:Powiadomienie)-[:POWIADAMIA]->(uzyt:Uzytkownik{email: $email})
        RETURN count(powiadomienie)
        """)
    Page<Powiadomienie> findPowiadomieniaOfUzytkownik(@Param("email") String email, Pageable pageable);


    // Niestety z jakiegos powowdu przypisanie całego $powiadomienie.__properties__  
    // SETem nie działa, więc trzeba przypisywać każde pole osobno
    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        WITH uzyt,  $powiadomienie.__properties__ as pp
        CREATE (pow:Powiadomienie{  przeczytane: false,
                                    typ:  pp.typ, odnosnik: pp.odnosnik, 
                                    tytul: pp.tytul, uzytkownikNazwa: pp.uzytkownikNazwa, 
                                    opis: pp.opis, avatar: pp.avatar, 
                                    nazwyRoslin: pp.nazwyRoslin, iloscPolubien: pp.iloscPolubien, 
                                    data: pp.data, dataUtworzenia: pp.dataUtworzenia}
                )-[r1:POWIADAMIA]->(uzyt)
        RETURN pow, r1, uzyt
        """)
    Powiadomienie addPowiadomienieToUzytkownik(@Param("email") String email, @Param("powiadomienie") Powiadomienie powiadomienie);

    @Query("""
        MATCH (uzyt:Uzytkownik)
        CREATE (pow:Powiadomienie{  przeczytane: false,    
                                    typ:  pp.typ, odnosnik: pp.odnosnik, 
                                    tytul: pp.tytul, uzytkownikNazwa: pp.uzytkownikNazwa, 
                                    opis: pp.opis, avatar: pp.avatar, 
                                    nazwyRoslin: pp.nazwyRoslin, iloscPolubien: pp.iloscPolubien, 
                                    data: pp.data, dataUtworzenia: pp.dataUtworzenia}
                )-[r1:POWIADAMIA]->(uzyt)
        RETURN pow
        """)
    Powiadomienie addGlobalCustomPowiadomienie(@Param("powiadomienie") Powiadomienie powiadomienie);

    @Query("""
        MATCH (uzyt:Uzytkownik)
        WHERE uzyt:Pracownik
        CREATE (pow:Powiadomienie{  przeczytane: false,
                                    typ:  pp.typ, odnosnik: pp.odnosnik, 
                                    tytul: pp.tytul, uzytkownikNazwa: pp.uzytkownikNazwa, 
                                    opis: pp.opis, avatar: pp.avatar, 
                                    nazwyRoslin: pp.nazwyRoslin, iloscPolubien: pp.iloscPolubien, 
                                    data: pp.data, dataUtworzenia: pp.dataUtworzenia}
                )-[r1:POWIADAMIA]->(uzyt)
        RETURN pow
        """)
    Powiadomienie addCustomPowiadomienieToPracownicy(@Param("powiadomienie") Powiadomienie powiadomienie);

}
