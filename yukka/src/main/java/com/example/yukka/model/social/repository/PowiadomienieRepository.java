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
        MATCH path=(powiadomienie:Powiadomienie)-[r1:POWIADAMIA]->(uzyt:Uzytkownik{email: $email})
        WHERE id(powiadomienie) = $id
        SET r1.przeczytane = true
        RETURN powiadomienie, collect(nodes(path)), collect(relationships(path))
        """)
    Optional<Powiadomienie> setPrzeczytane(@Param("email") String email, @Param("id") Long id);

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

    @Query(value = """
        MATCH path=(powiadomienie:Powiadomienie)-[r1:POWIADAMIA]->(uzyt:Uzytkownik{email: $email})
        WHERE r1.przeczytane = false
        RETURN COUNT(r1)
        """)
    Integer getNieprzeczytaneCountOfUzytkownik(@Param("email") String email);


    // Niestety z jakiegos powowdu przypisanie całego $powiadomienie.__properties__  
    // SETem nie działa, więc trzeba przypisywać każde pole osobno
    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        WITH uzyt,  $powiadomienie.__properties__ as pp
        CREATE (pow:Powiadomienie{  
                                    typ:  pp.typ, odnosnik: pp.odnosnik, 
                                    tytul: pp.tytul, uzytkownikNazwa: pp.uzytkownikNazwa, 
                                    opis: pp.opis, avatar: pp.avatar, 
                                    nazwyRoslin: pp.nazwyRoslin, iloscPolubien: pp.iloscPolubien, 
                                    data: pp.data, dataUtworzenia: pp.dataUtworzenia}
                )-[r1:POWIADAMIA{przeczytane: false}]->(uzyt)
        RETURN pow, r1, uzyt
        """)
    Powiadomienie addPowiadomienieToUzytkownik(@Param("email") String email, @Param("powiadomienie") Powiadomienie powiadomienie);

    @Query("""
        WITH $powiadomienie.__properties__ as pp
        CREATE (pow:Powiadomienie{      
                                    typ:  pp.typ,
                                    tytul: pp.tytul,  
                                    opis: pp.opis, avatar: pp.avatar, 
                                    dataUtworzenia: pp.dataUtworzenia}
                )
        WITH pow
        MATCH (uzyt:Uzytkownik)
        CREATE (pow)-[:POWIADAMIA{przeczytane: false}]->(uzyt)
        
        """)
    void addGlobalCustomPowiadomienie(@Param("powiadomienie") Powiadomienie powiadomienie);

    @Query("""
        WITH $powiadomienie.__properties__ as pp
        CREATE (pow:Powiadomienie{      
                                    typ:  pp.typ,
                                    tytul: pp.tytul,  
                                    opis: pp.opis, avatar: pp.avatar, 
                                    dataUtworzenia: pp.dataUtworzenia}
                )
        WITH pow
        MATCH (uzyt:Uzytkownik:Pracownik)
        CREATE (pow)-[:POWIADAMIA{przeczytane: false}]->(uzyt)
        """)
    void addCustomPowiadomienieToPracownicy(@Param("powiadomienie") Powiadomienie powiadomienie);




    @Query(value = """
        MATCH path=(powiadomienie:Powiadomienie)-[r1:POWIADAMIA]->(uzyt:Uzytkownik{email: $email})
        WHERE id(powiadomienie) = $id
        DELETE r1

        WITH powiadomienie
        OPTIONAL MATCH (powiadomienie)-[:POWIADAMIA]->(uzyt2:Uzytkownik)
        WITH powiadomienie, count(uzyt2) as uzytkownicy
        WHERE uzytkownicy = 0

        DETACH DELETE powiadomienie
        """)
    void remove(@Param("email") String email, @Param("id") Long id);

}
