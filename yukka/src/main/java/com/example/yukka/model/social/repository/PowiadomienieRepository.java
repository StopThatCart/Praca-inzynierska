package com.example.yukka.model.social.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.social.powiadomienie.Powiadomienie;

/**
 * Repozytorium dla operacji na encji Powiadomienie w bazie danych Neo4j.
 * 
 * <p>Interfejs ten rozszerza Neo4jRepository i zapewnia metody do wykonywania
 * zapytań Cypher na bazie danych Neo4j. Metody te umożliwiają sprawdzanie,
 * pobieranie, zapisywanie oraz aktualizowanie danych dotyczących powiadomień
 * użytkowników.</p>
 * 
 * <p>Metody w tym repozytorium wykorzystują adnotacje @Query do definiowania
 * zapytań Cypher, które są wykonywane na bazie danych. Parametry zapytań
 * są przekazywane za pomocą adnotacji @Param.</p>
 * 
 * <p>Przykładowe operacje obejmują:</p>
 * <ul>
 *   <li>Sprawdzanie, czy istnieje powiadomienie o tych samych parametrach.</li>
 *   <li>Sprawdzanie, czy istnieje powiadomienie dotyczące rozmowy.</li>
 *   <li>Aktualizowanie danych powiadomienia.</li>
 *   <li>Oznaczanie powiadomienia jako przeczytane.</li>
 *   <li>Oznaczanie wszystkich powiadomień jako przeczytane.</li>
 *   <li>Pobieranie powiadomień użytkownika z paginacją.</li>
 *   <li>Liczenie nieprzeczytanych powiadomień użytkownika.</li>
 *   <li>Dodawanie powiadomienia do użytkownika.</li>
 *   <li>Dodawanie globalnego powiadomienia.</li>
 *   <li>Dodawanie powiadomienia do pracowników.</li>
 *   <li>Wysyłanie zgłoszenia do pracownika.</li>
 *   <li>Pobieranie najnowszego zgłoszenia użytkownika.</li>
 *   <li>Usuwanie powiadomienia.</li>
 * </ul>
 * 
 * <p>Repozytorium to jest częścią aplikacji zarządzającej powiadomieniami
 * użytkowników, umożliwiającej zarządzanie i monitorowanie powiadomień
 * w systemie.</p>
 */
public interface PowiadomienieRepository extends Neo4jRepository<Powiadomienie, Long> {

    @Query(value = """
        MATCH (powiadomienie:Powiadomienie)
        WHERE id(powiadomienie) = $id
        RETURN powiadomienie
        """)
    Optional<Powiadomienie> findPowiadomienieById(@Param("id") Long id);
    @Query(value = """
        MATCH path=(powiadomienie:Powiadomienie)-[r1:POWIADAMIA]->(uzyt:Uzytkownik{email: $email})
        WHERE powiadomienie.typ = $typ AND powiadomienie.opis = $opis
        RETURN powiadomienie, collect(nodes(path)), collect(relationships(path)) 
        ORDER BY powiadomienie.dataUtworzenia DESC LIMIT 1
        """)
    Optional<Powiadomienie> checkIfSamePowiadomienieExists(@Param("email") String email,
    @Param("typ") String typ,
    @Param("opis") String opis);

    @Query(value = """
        MATCH path=(powiadomienie:Powiadomienie)-[r1:POWIADAMIA]->(uzyt:Uzytkownik{email: $email})
        WHERE powiadomienie.typ = $typ AND powiadomienie.uzytkownikNazwa = $uzytkownikNazwa
        RETURN powiadomienie, collect(nodes(path)), collect(relationships(path)) 
        ORDER BY powiadomienie.dataUtworzenia DESC LIMIT 1
        """)
    Optional<Powiadomienie> checkIfRozmowaPowiadomienieExists(@Param("email") String email,
    @Param("typ") String typ,
    @Param("uzytkownikNazwa") String uzytkownikNazwa);


    @Query(value = """
        MATCH path=(powiadomienie:Powiadomienie)-[r1:POWIADAMIA]->(uzyt:Uzytkownik{email: $email})
        WHERE id(powiadomienie) = $id
        SET powiadomienie.dataUtworzenia = 	$time, powiadomienie.avatar = $avatar, r1.przeczytane = false
        RETURN powiadomienie, collect(nodes(path)), collect(relationships(path))
        """)
    Optional<Powiadomienie> updateData(@Param("email") String email, @Param("id") Long id,
    @Param("avatar") String avatar,
    @Param("time") LocalDateTime localdatetime);

    @Query(value = """
        MATCH path=(powiadomienie:Powiadomienie)-[r1:POWIADAMIA]->(uzyt:Uzytkownik{email: $email})
        WHERE id(powiadomienie) = $id
        SET r1.przeczytane = true
        RETURN powiadomienie, collect(nodes(path)), collect(relationships(path))
        """)
    Optional<Powiadomienie> setPrzeczytane(@Param("email") String email, @Param("id") Long id);

    @Query(value = """
        MATCH path=(powiadomienie:Powiadomienie)-[r1:POWIADAMIA]->(uzyt:Uzytkownik{email: $email})
        SET r1.przeczytane = true
        """)
    void setAllPrzeczytane(@Param("email") String email);

    @Query(value = """
        MATCH path=(powiadomienie:Powiadomienie)-[powiadamia:POWIADAMIA]->(uzyt:Uzytkownik{email: $email})
        WHERE powiadamia.ukryte = false

        OPTIONAL MATCH (zglaszajacy:Uzytkownik)-[r1:ZGLASZA]->(powiadomienie)

        RETURN powiadomienie, zglaszajacy, r1, collect(nodes(path)), collect(relationships(path))
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
                                    opis: pp.opis, avatar: pp.avatar,
                                    dataUtworzenia: pp.dataUtworzenia}
                )-[r1:POWIADAMIA{przeczytane: false, ukryte: false}]->(uzyt)
        RETURN pow, r1, uzyt
        """)
    Powiadomienie addPowiadomienieToUzytkownik(@Param("email") String email, @Param("powiadomienie") Powiadomienie powiadomienie);

    @Query("""
        MATCH (nadawca:Uzytkownik{nazwa: $nazwa})
        WITH nadawca, $powiadomienie.__properties__ as pp
        CREATE (pow:Powiadomienie{      
                                    typ:  pp.typ,
                                    opis: pp.opis, avatar: pp.avatar, 
                                    dataUtworzenia: pp.dataUtworzenia}
                )
        WITH nadawca, pow
        MERGE (nadawca)-[:WYSYLA_POWIADOMIENIE]->(pow)
        WITH pow
        MATCH (uzyt:Uzytkownik)
        CREATE (pow)-[:POWIADAMIA{przeczytane: false, ukryte: false}]->(uzyt)
        """)
    void addGlobalCustomPowiadomienie(@Param("powiadomienie") Powiadomienie powiadomienie, @Param("nazwa") String nazwa);

    @Query("""
        MATCH (nadawca:Uzytkownik{nazwa: $nazwa})
        WITH nadawca, $powiadomienie.__properties__ as pp
        CREATE (pow:Powiadomienie{      
                                    typ:  pp.typ,
                                    opis: pp.opis, avatar: pp.avatar, 
                                    dataUtworzenia: pp.dataUtworzenia}
                )
        WITH nadawca, pow
        MERGE (nadawca)-[:WYSYLA_POWIADOMIENIE]->(pow)
        WITH pow
        MATCH (uzyt:Pracownik)
        CREATE (pow)-[:POWIADAMIA{przeczytane: false, ukryte: false}]->(uzyt)
        """)
    void addCustomPowiadomienieToPracownicy(@Param("powiadomienie") Powiadomienie powiadomienie, @Param("nazwa") String nazwa);

    @Query("""
        MATCH (uzyt:Uzytkownik{nazwa: $nazwa})
        MATCH (prac:Pracownik)
        OPTIONAL MATCH (prac)<-[:POWIADAMIA]-(zetka:Zgloszenie)
        WITH uzyt, prac, $powiadomienie.__properties__ AS pp, COUNT(zetka) AS liczbaZgloszen
        ORDER BY liczbaZgloszen ASC LIMIT 1

        MERGE (uzyt)-[:ZGLASZA]->(pow:Powiadomienie:Zgloszenie{  
                                    typ:  pp.typ, odnosnik: pp.odnosnik, 
                                    opis: pp.opis, avatar: pp.avatar,
                                    dataUtworzenia: pp.dataUtworzenia}
                )-[r1:POWIADAMIA{przeczytane: false, ukryte: false}]->(prac)

        RETURN pow, r1, prac
        """)
    Powiadomienie sendZgloszenieToPracownik(@Param("powiadomienie") Powiadomienie powiadomienie, @Param("nazwa") String nazwa);


    @Query("""
        MATCH (uzyt:Uzytkownik{nazwa: $nazwa})-[r1:ZGLASZA]->(pow:Zgloszenie)
        WHERE r1.ukryte = false
        
        RETURN pow, r1, uzyt
        ORDER BY pow.dataUtworzenia DESC
        LIMIT 1
        """)
    Optional<Powiadomienie> getNajnowszeZgloszenieUzytkownika(@Param("nazwa") String nazwa);


    @Query(value = """
        MATCH path=(powiadomienie:Powiadomienie)-[r1:POWIADAMIA]->(uzyt:Uzytkownik{email: $email})
        WHERE id(powiadomienie) = $id
        SET r1.ukryte = true, r1.przeczytane = true
        """)
    void ukryjPowiadomienie(@Param("email") String email, @Param("id") Long id);



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
    void removePowiadomienie(@Param("email") String email, @Param("id") Long id);

}
