package com.example.yukka.model.uzytkownik.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.uzytkownik.Ustawienia;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import jakarta.annotation.Nonnull;

/**
 * Repozytorium dla operacji na encji Uzytkownik w bazie danych Neo4j.
 * 
 * <p>Interfejs ten rozszerza Neo4jRepository i zapewnia metody do wykonywania
 * zapytań Cypher na bazie danych Neo4j. Metody te umożliwiają sprawdzanie,
 * pobieranie, zapisywanie oraz aktualizowanie danych dotyczących użytkowników
 * oraz ich powiązań z innymi encjami, takimi jak ustawienia, ogród, działki,
 * posty, komentarze i powiadomienia.</p>
 * 
 * <p>Metody w tym repozytorium wykorzystują adnotacje @Query do definiowania
 * zapytań Cypher, które są wykonywane na bazie danych. Parametry zapytań
 * są przekazywane za pomocą adnotacji @Param.</p>
 * 
 * <p>Przykładowe operacje obejmują:</p>
 * <ul>
 *   <li>Wyszukiwanie użytkowników na podstawie różnych kryteriów, takich jak ID, nazwa, email.</li>
 *   <li>Sprawdzanie, czy użytkownik istnieje na podstawie nazwy lub emaila.</li>
 *   <li>Pobieranie użytkowników zablokowanych przez innego użytkownika.</li>
 *   <li>Dodawanie nowych użytkowników wraz z ich ustawieniami i ogrodem.</li>
 *   <li>Aktualizowanie danych użytkownika, takich jak email, hasło, avatar, profil.</li>
 *   <li>Usuwanie użytkowników oraz powiązanych z nimi danych, takich jak posty, komentarze, rośliny.</li>
 *   <li>Banowanie użytkowników oraz zarządzanie ich stanem aktywacji.</li>
 * </ul>
 * 
 * <p>Repozytorium to jest częścią aplikacji zarządzającej użytkownikami,
 * umożliwiającej zarządzanie ich profilami, interakcjami oraz powiązanymi
 * danymi w kontekście społeczności ogrodniczej.</p>
 */
public interface UzytkownikRepository extends Neo4jRepository<Uzytkownik, Long> {
    @Query("""
        MATCH path = (ustawienia:Ustawienia)<-[:MA_USTAWIENIA]-(u:Uzytkownik{uuid: $uuid})
        RETURN u, collect(NODES(path)), collect(RELATIONSHIPS(path))
        """)
    Optional<Uzytkownik> findByUUID(String uuid);

    @Query("""
            MATCH path = (ustawienia:Ustawienia)<-[:MA_USTAWIENIA]-(u:Uzytkownik{nazwa: $nazwa})
            RETURN u, collect(NODES(path)), collect(RELATIONSHIPS(path))
            """)
    Optional<Uzytkownik> findByNazwa(String nazwa);
    
    @Query("""
        MATCH path = (ustawienia:Ustawienia)<-[:MA_USTAWIENIA]-(u:Uzytkownik{email: $email})
        RETURN u, collect(NODES(path)), collect(RELATIONSHIPS(path))
        """)
    Optional<Uzytkownik> findByEmail(String email);

    @Query("""
        MATCH path = (ustawienia:Ustawienia)<-[:MA_USTAWIENIA]-(u:Uzytkownik)
        WHERE u.nazwa = $nazwa OR u.email = $nazwa
        RETURN u, collect(NODES(path)), collect(RELATIONSHIPS(path))
        """)
    Optional<Uzytkownik> findByNameOrEmail(String nazwa);

    @Query("MATCH (u:Uzytkownik) WHERE (u.nazwa = $nazwa OR u.email = $nazwa) AND u.haslo = $haslo RETURN u")
    Uzytkownik findByLogin(String nazwa, String haslo);

   // List<Uzytkownik> findByLabels(Set<String> labels);


    @SuppressWarnings("null")
    @Override
    @Query("MATCH (u:Uzytkownik) RETURN u")
    @Nonnull List<Uzytkownik> findAll();


        @Query(value = """
        MATCH path = (ustawienia:Ustawienia)<-[:MA_USTAWIENIA]-(uzyt:Uzytkownik)
        WHERE $szukaj IS NULL OR toLower(uzyt.nazwa) CONTAINS toLower($szukaj)
        AND ($aktywowany = true OR uzyt.aktywowany = true)

        RETURN uzyt, collect(nodes(path)), collect(relationships(path))
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
            """,
            countQuery = """
        MATCH (ustawienia:Ustawienia)<-[:MA_USTAWIENIA]-(uzyt:Uzytkownik)
        WHERE $szukaj IS NULL OR toLower(uzyt.nazwa) CONTAINS toLower($szukaj)
        AND ($aktywowany = true OR uzyt.aktywowany = true)

        RETURN count(uzyt)
        """)
    Page<Uzytkownik> findAllUzytkownicy(String szukaj, boolean aktywowany, Pageable pageable);

    @Query("MATCH (u:Uzytkownik) WHERE u.nazwa = $nazwa OR u.email = $email RETURN u")
    Optional<Uzytkownik> checkIfUzytkownikExists(String nazwa, String email);


    @Query("""
        MATCH p = (blokujacy:Uzytkownik{email: $email})-[r:BLOKUJE]->(blokowany:Uzytkownik)
        
        RETURN blokujacy, collect(nodes(p)), collect(relationships(p))
            """)
    Optional<Uzytkownik> getBlokowaniUzytkownicyOfUzytkownik(String email);


    @Query("""
        MATCH p = (:Ustawienia)<-[:MA_USTAWIENIA]-(blokujacy:Uzytkownik{nazwa: $nazwa})
        OPTIONAL MATCH p2 = (blokujacy)-[r:BLOKUJE]->(blokowany:Uzytkownik)-[:MA_USTAWIENIA]->(:Ustawienia)
        
        RETURN blokujacy, 
        collect(nodes(p)), collect(relationships(p)), 
        collect(nodes(p2)), collect(relationships(p2))
            """)
    Optional<Uzytkownik> getBlokowaniAndBlokujacy(String nazwa);

    @Query("""
        MATCH path = (ustawienia:Ustawienia)<-[:MA_USTAWIENIA]-(uzyt:Uzytkownik)-[:MA_OGROD]->(:Ogrod)
            -[:MA_DZIALKE]->(:Dzialka)
            <-[:ZASADZONA_NA]-(rosliny)-[r]-(cecha)
        WHERE (cecha:Cecha OR cecha:CechaWlasna)
                AND (rosliny:Roslina OR rosliny:RoslinaWlasna)
        RETURN uzyt, collect(NODES(path)), collect(RELATIONSHIPS(path))
        """)
    List<Uzytkownik> getUzytkownicyWithRoslinyInDzialki();

    @Query("""
        MATCH (uzyt:Uzytkownik { ban: true })
        RETURN uzyt
        """)
    List<Uzytkownik> getZbanowaniUzytkownicy();


    @Query("""
        MATCH (post:Post{uuid: $uuid})
        OPTIONAL MATCH (post)<-[:MA_POST]-(uzyt:Uzytkownik)
        OPTIONAL MATCH (post)<-[:JEST_W_POSCIE]-(:Komentarz)<-[:SKOMENTOWAL]-(uzyt2:Uzytkownik)
        OPTIONAL MATCH (post)<-[:OCENIL]-(uzyt3:Uzytkownik)
        WITH post, COLLECT(DISTINCT uzyt) + COLLECT(DISTINCT uzyt2) + COLLECT(DISTINCT uzyt3) AS uzytkownicy
        UNWIND uzytkownicy AS uzytkownik
        RETURN DISTINCT uzytkownik
        """)
    List<Uzytkownik> getConnectedUzytkownicyFromPostButBetter(String uuid);

    @Query("""
        MATCH (uzyt:Uzytkownik{nazwa: $nazwa})
        OPTIONAL MATCH (uzyt)-[:MA_POST]->(post:Post)
        RETURN count(post) as posty
                """)
    Integer getPostyCountOfUzytkownik(String nazwa);
    @Query("""
        MATCH (uzyt:Uzytkownik{nazwa: $nazwa})
        OPTIONAL MATCH (uzyt)-[:SKOMENTOWAL]->(kom:Komentarz)
        RETURN count(kom) as komentarze
                """)
    Integer getKomentarzeCountOfUzytkownik(String nazwa);
    @Query("""
        MATCH (uzyt:Uzytkownik{nazwa: $nazwa})
        OPTIONAL MATCH (uzyt)<-[:STWORZONA_PRZEZ]-(ros:RoslinaWlasna)
        RETURN count(ros) as rosliny
                """)
    Integer getRoslinyCountOfUzytkownik(String nazwa);
    @Query("""
        MATCH (oceniany:Uzytkownik{nazwa: $nazwa})
        OPTIONAL MATCH (oceniany)-[:SKOMENTOWAL]->(komentarz:Komentarz)<-[r2:OCENIL]-(uzyt:Uzytkownik)
            WHERE oceniany <> uzyt
        WITH oceniany,  
            COUNT(CASE WHEN r2.lubi = true THEN 1 ELSE NULL END) AS ocenyPozytywne,
            COUNT(CASE WHEN r2.lubi = false THEN 1 ELSE NULL END) AS ocenyNegatywne
        RETURN ocenyPozytywne
                """)
    Integer getKomentarzeOcenyPozytywneOfUzytkownik(String nazwa);


    @Query("""
        MATCH (oceniany:Uzytkownik{nazwa: $nazwa})
        OPTIONAL MATCH (oceniany)-[:SKOMENTOWAL]->(komentarz:Komentarz)<-[r2:OCENIL]-(uzyt:Uzytkownik)
            WHERE oceniany <> uzyt
        WITH oceniany,  
            COUNT(CASE WHEN r2.lubi = true THEN 1 ELSE NULL END) AS ocenyPozytywne,
            COUNT(CASE WHEN r2.lubi = false THEN 1 ELSE NULL END) AS ocenyNegatywne
        RETURN ocenyNegatywne
                """)
    Integer getKomentarzeOcenyNegatywneOfUzytkownik(String nazwa);


    @Query("""
        MATCH (oceniany:Uzytkownik{nazwa: $nazwa})
        OPTIONAL MATCH (oceniany)-[:MA_POST]->(post:Post)<-[r2:OCENIL]-(uzyt:Uzytkownik)
            WHERE oceniany <> uzyt
        WITH oceniany,  
            COUNT(CASE WHEN r2.lubi = true THEN 1 ELSE NULL END) AS ocenyPozytywne,
            COUNT(CASE WHEN r2.lubi = false THEN 1 ELSE NULL END) AS ocenyNegatywne

        RETURN ocenyPozytywne
                """)
    Integer getPostyOcenyPozytywneOfUzytkownik(String nazwa);

    @Query("""
        MATCH (oceniany:Uzytkownik{nazwa: $nazwa})
        OPTIONAL MATCH (oceniany)-[:MA_POST]->(post:Post)<-[r2:OCENIL]-(uzyt:Uzytkownik)
            WHERE oceniany <> uzyt
        WITH oceniany,  
            COUNT(CASE WHEN r2.lubi = true THEN 1 ELSE NULL END) AS ocenyPozytywne,
            COUNT(CASE WHEN r2.lubi = false THEN 1 ELSE NULL END) AS ocenyNegatywne

        RETURN ocenyNegatywne
                """)
    Integer getPostyOcenyNegatywneOfUzytkownik(String nazwa);

    /*
 ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⢤⣄⡤⠤⣤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⢀⣠⠤⠀⡴⠋⠀⠀⠀⠀⠀⠉⠒⢌⠉⠛⣽⡲⣄⡀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⣠⠾⠉⠀⠀⠀⠀⣄⠀⠀⠀⠀⠀⢀⣀⠀⣥⡤⠜⠊⣈⢻⣆⠀⠀⠀⠀⠀
⠀⠀⠀⣠⠾⠁⠔⠨⠂⠀⢀⠘⡜⡦⣀⡴⡆⠛⠒⠙⡴⡀⠘⡆⠀⠀⠛⡙⢷⡀⠀⠀⠀
⠀⠀⡴⠃⠀⠀⠀⠀⢀⣠⡼⠟⡏⡏⠙⣇⢸⡄⠀⠀⢹⠏⠁⢹⡳⣤⠀⠘⡌⣷⠀⠀⠀
⠀⣸⠃⠀⡠⠖⢲⠀⠀⣸⠃⢰⡇⡇⠀⢸⣌⣇⢀⠀⣸⣷⣀⡼⢣⡇⠀⠀⢹⣹⠀⠀⠀
⠀⡏⠀⡜⠁⠀⠁⠀⡰⢃⣴⣷⢟⣿⡟⡲⠟⠻⠊⠙⠃⣼⣿⣻⣾⡇⠀⠀⢸⡿⠀⠀⠀
⠀⡇⠰⡇⠀⢀⡠⠞⡗⢩⡟⢸⡏⠀⢹⡇⠀⠀⠀⠀⠀⢸⣿⠉⢱⣿⠠⢤⣟⠁⠀⠀⠀
⠀⣧⠀⠉⠉⠉⠀⢸⠦⡸⡅⢸⣏⠒⣱⠇⠀⠀⠀⠀⠀⠀⢿⣅⡽⠙⢦⠀⢈⣳⡄⠀⠀
⠀⡟⠀⠀⠀⠀⠀⠘⠀⣘⡌⣀⡉⠉⠁⠀⠀⠀⠀⠈⠀⠀⠀⠀⠀⠁⠀⡸⠛⠜⡷⣠⠀
⢸⠃⠀⠀⠀⠀⣀⡫⣿⣮⡀⠀⠀⠀⠀⠀⢠⠤⠶⡦⡤⠀⠀⠀⠀⠀⢠⠇⡀⠸⣧⣤⡆
⡟⠀⠀⠀⠀⠀⠀⡄⢠⠉⢇⠀⡄⠀⠀⠀⠘⢦⣀⡸⠃⠀⠀⠀⢀⡠⠋⠈⠛⢷⡖⠋⠀
⡇⢀⠀⠀⠀⠀⠀⢇⠀⢕⣺⣿⣅⡀⠀⠀⠀⠀⠀⠀⢀⣠⠤⠒⠉⠀⢠⣄⡶⠋⠀⠀⠀
⠻⢾⣼⣦⣀⠀⡄⠈⠓⢦⣼⣿⣍⠉⠻⣄⠀⢈⠏⠉⣿⣦⡀⠀⢀⣠⠾⠀⠀⠀⠀⠀⠀
⠀⠀⠈⠀⠉⠙⠓⠛⣦⡼⠘⣿⣿⣷⣤⣀⣹⠞⢤⣼⣿⣿⠈⢶⡋⠁⠀⠀⠀⠀⠀⠀⠀
     */


    @Query("""
        CREATE (u:Uzytkownik) SET u += $uzyt.__properties__
        WITH u
        CREATE (ustawienia:Ustawienia) SET ustawienia += $ustawienia.__properties__
        WITH u, ustawienia
        CREATE(u)-[:MA_USTAWIENIA]->(ustawienia)
        WITH u
        CREATE(u)-[:MA_OGROD]->(o:Ogrod{nazwa: "Ogród użytkownika " + u.nazwa})
        WITH u, o
        FOREACH (i IN RANGE(1, 10) |
            CREATE (o)-[:MA_DZIALKE]->(d:Dzialka {
                numer: i, 
                nazwa: "Działka użytkownika " + u.nazwa + " nr. " + i
                })
        )
        RETURN u
            """
            )
    Uzytkownik addUzytkownik(@Param("uzyt") Uzytkownik uzyt, @Param("ustawienia") Ustawienia ustawienia);

    @Query("""
        CREATE (u:Uzytkownik::#{allOf(#roles)}) SET u += $uzyt.__properties__
        WITH u
        CREATE (ustawienia:Ustawienia) SET ustawienia += $ustawienia.__properties__
        WITH u, ustawienia
        CREATE(u)-[:MA_USTAWIENIA]->(ustawienia)
        WITH u
        CREATE(u)-[:MA_OGROD]->(o:Ogrod{nazwa: "Ogród użytkownika " + u.nazwa})
        WITH u, o
        FOREACH (i IN RANGE(1, 10) |
            CREATE (o)-[:MA_DZIALKE]->(d:Dzialka {
                numer: i, 
                nazwa: "Działka użytkownika " + u.nazwa + " nr. " + i
                })
        )
        RETURN u
            """
            )
    Uzytkownik addUzytkownik(@Param("uzyt") Uzytkownik uzyt, String roles, @Param("ustawienia") Ustawienia ustawienia);

  //  @Query("CREATE (u:Uzytkownik:`:#{literal(#rola)}` {nazwa: $nazwa, email: $email, haslo: $haslo, data_utworzenia: localdatetime(), ban: false})")
   // void addNewPracownik(@Param("nazwa") String nazwa, @Param("email") String email, @Param("haslo") String haslo, @Param("rola") String rola);

   @Query("""
    MATCH path = (ustawienia:Ustawienia)<-[:MA_USTAWIENIA]-(uzyt:Uzytkownik{email: $email})
    SET ustawienia += $ustawienia.__properties__
    RETURN uzyt, collect(NODES(path)), collect(RELATIONSHIPS(path))
        """)
    Uzytkownik updateUstawienia(@Param("ustawienia") Ustawienia ustawienia, String email);


    @Query("MATCH (u:Uzytkownik) WHERE u.email = $email SET u.email = $nowyEmail RETURN u")
    Uzytkownik updateEmail(String email, String nowyEmail);

    @Query("MATCH (u:Uzytkownik) WHERE u.email = $email SET u.haslo = $noweHaslo RETURN u")
    Uzytkownik updateHaslo(String email, String noweHaslo);


    @Query("MATCH (u:Uzytkownik) WHERE u.email = $email SET u.avatar = $avatar RETURN u")
    Uzytkownik updateAvatar(String email, String avatar);

    
    @Query("""
        MATCH (u:Uzytkownik) WHERE u.email = $email 
        SET u.imie = $imie, u.miasto = $miasto, 
            u.miejsceZamieszkania = $miejsceZamieszkania, u.opis = $opis
        RETURN u
        """)
    Uzytkownik updateProfil(String email, String imie, 
    String miasto, String miejsceZamieszkania,
    String opis);


    @Query("MATCH (u:Uzytkownik) WHERE u.email = $email SET u.aktywowany = true RETURN u")
    Uzytkownik activate(String email);

    @Query("""
        MATCH (blokowany:Uzytkownik{email: $blokowanyEmail})
        MATCH (blokujacy:Uzytkownik{email: $blokujacyEmail})

        MERGE (blokujacy)-[r:BLOKUJE]->(blokowany)
        
        RETURN COUNT(r) > 0 AS success
            """)
    Boolean zablokujUzyt(String blokowanyEmail, String blokujacyEmail);
    
    @Query("""
        MATCH (blokujacy:Uzytkownik{email: $blokujacyEmail})-[r:BLOKUJE]->
                (blokowany:Uzytkownik{email: $blokowanyEmail})
        DELETE r
        RETURN COUNT(r) > 0 AS success
            """)
    Boolean odblokujUzyt(String blokowanyEmail, String blokujacyEmail);

    @Query("""
       MATCH (u:Uzytkownik) WHERE u.nazwa = $nazwa 
       SET u.ban = $ban, u.banDo = $banDo, u.banPowod = $banPowod
       RETURN COUNT(u) > 0 AS success     
            """)
    Boolean banUzytkownik(String nazwa, boolean ban, LocalDate banDo, String banPowod);

    @Query("""
        MATCH (u:Uzytkownik{email: $email})
        WITH u

        OPTIONAL MATCH (u)-[:MA_USTAWIENIA]->(ust:Ustawienia)
        DETACH DELETE ust

        WITH u
        OPTIONAL MATCH (t:Token)-[]-(u) 
        DETACH DELETE t

        WITH u
        OPTIONAL MATCH (u)-[:JEST_W_ROZMOWIE]->(rozmowa:RozmowaPrywatna)
        OPTIONAL MATCH (rozmowa)-[:MA_WIADOMOSC]->(kom:Komentarz)
        DETACH DELETE kom, rozmowa

        WITH u
        DETACH DELETE u 
        """)
    void removeUzytkownik(String email);

    @Query("""
        MATCH (u:Uzytkownik{email: $email})
        OPTIONAL MATCH (u)-[:MA_POST]->(post:Post)
        OPTIONAL MATCH (post)<-[:JEST_W_POSCIE]-(komentarz:Komentarz)
        DETACH DELETE komentarz

        WITH post
        DETACH DELETE post
        """)
    void removePostyOfUzytkownik(String email);

    @Query("""
        MATCH (u:Uzytkownik{email: $email})
        OPTIONAL MATCH (u)-[:SKOMENTOWAL]->(kom:Komentarz)
        OPTIONAL MATCH (kom)<-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)<-[:SKOMENTOWAL]-(:Uzytkownik)
        DETACH DELETE odpowiedz
        """)
    void removeKomentarzeOfUzytkownik(String email);

    @Query("""
        MATCH (u:Uzytkownik{email: $email})
        OPTIONAL MATCH (u)<-[r1:POWIADAMIA]-(powiadomienie:Powiadomienie)
        DELETE r1

        WITH u
        MATCH (powiadomienie:Powiadomienie) 
        WHERE NOT (powiadomienie)--()
        DELETE powiadomienie
        """)
    void removePowiadomieniaOfUzytkownik(String email);
    @Query("""
        MATCH (u:Uzytkownik{email: $email})
        OPTIONAL MATCH (u)<-[:STWORZONA_PRZEZ]-(roslina:RoslinaWlasna)
        OPTIONAL MATCH (roslina)-[rel]-(wl:CechaWlasna)
        DETACH DELETE roslina

        WITH u
        MATCH (cecha:CechaWlasna) 
        WHERE NOT (cecha)--()
        DELETE cecha
        """)
    void removeRoslinyOfUzytkownik(String email);

    @Query("""
        MATCH (u:Uzytkownik) DETACH DELETE u 
        WITH u

        OPTIONAL MATCH(ust:Ustawienia) DETACH DELETE ust
        WITH u

        OPTIONAL MATCH (t:Token) DETACH DELETE t
        WITH t

        OPTIONAL MATCH (o:Ogrod)
        OPTIONAL MATCH (o)-[:MA_DZIALKE]->(d:Dzialka)
        DETACH DELETE d, o
        WITH d

        MATCH (u:Uzytkownik)
        DETACH DELETE u 
        """)
    void clearUzytkowicy();


    @Query("""
        MATCH (powiadomienie:Powiadomienie)
        DETACH DELETE powiadomienie
        """)
    void clearPowiadomienia();   
    

    @Query("""
        MATCH (roslina:RoslinaWlasna)
        OPTIONAL MATCH (roslina)-[rel]-(wl:CechaWlasna)
        DETACH DELETE roslina, wl
        """)
    void clearRoslinaWlasna(); 



}
