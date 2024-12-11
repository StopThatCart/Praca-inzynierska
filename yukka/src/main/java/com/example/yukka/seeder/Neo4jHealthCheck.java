package com.example.yukka.seeder;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Neo4jHealthCheck {

    @Value("${spring.data.neo4j.database}")
    private String dbName;
    @Value("${spring.neo4j.uri}")
    private String uri;
    @Value("${spring.neo4j.authentication.username}")
    private String username;
    @Value("${spring.neo4j.authentication.password}")
    private String password;

    @Value("${spring.data.neo4j.max-retries}")
    private int maxRetries;

    @Value("${spring.data.neo4j.time-after-drop-create}")
    private int timeBetweenDBDrops;


    /**
     * Metoda służy do usunięcia i ponownego utworzenia bazy danych o podanej nazwie.
     *
     * <ul>
     *   <li><strong>databaseName</strong> - nazwa bazy danych, która ma zostać usunięta i utworzona na nowo</li>
     * </ul>
     *
     * Metoda wykonuje następujące kroki:
     * <ul>
     *   <li>Łączy się z bazą danych Neo4j przy użyciu podanych poświadczeń</li>
     *   <li>Wykonuje zapytanie do usunięcia bazy danych, jeśli istnieje</li>
     *   <li>Wykonuje zapytanie do utworzenia nowej bazy danych z odpowiednimi ograniczeniami</li>
     *   <li>Czeka przez określony czas po wykonaniu operacji</li>
     * </ul>
     *
     * W przypadku przerwania wątku podczas oczekiwania, metoda ustawia flagę przerwania wątku i loguje błąd.
     *
     * @param databaseName nazwa bazy danych do usunięcia i ponownego utworzenia
     */
    public void dropAndCreateDatabase(String databaseName) {
        log.info("Czyszczenie bazy danych...");
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        Neo4jClient client = Neo4jClient.create(driver);
        String dropQuery = "DROP DATABASE $databaseName IF EXISTS";
        String createQuery = "CREATE DATABASE $databaseName IF NOT EXISTS";

        client
        .query(dropQuery)
        .in("system")
        .bind(databaseName).to("databaseName")
        .run();

        client
        .query(createQuery)
        .in("system")
        .bind(databaseName).to("databaseName")
        .run();

        try {
            Thread.sleep(timeBetweenDBDrops);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Wątek został zatrzymany podczas resetowania bazy danych", e);
        }
        log.info("Wyczyszczono bazę danych.");
    }

    /**
     * Metoda instalująca ograniczenia w bazie danych Neo4j.
     * Tworzy unikalne ograniczenia dla różnych właściwości węzłów takich jak:
     * - nazwa łacińska rośliny
     * - email użytkownika
     * - nazwa użytkownika
     * - ID rośliny
     * - ID użytkownika
     * - ID posta
     * - ID komentarza
     * 
     * @throws Exception w przypadku błędu podczas instalacji ograniczeń
     */
    @SuppressWarnings("deprecation")
    public void installConstraints() {
        log.info("Instalacja ograniczeń...");
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        String[] constraintQueries = {
            "CREATE CONSTRAINT unikalnaLacinskaNazwa FOR (ros:Roslina) REQUIRE ros.nazwaLacinska IS UNIQUE",
            "CREATE CONSTRAINT unikalnyEmail FOR (u:Uzytkownik) REQUIRE u.email IS UNIQUE",
            "CREATE CONSTRAINT unikalnaNazwaUzytkownika FOR (u:Uzytkownik) REQUIRE u.nazwa IS UNIQUE",
            "CREATE CONSTRAINT unikalneRoslinaId FOR (ros:Roslina) REQUIRE ros.roslinaId IS UNIQUE",
            "CREATE CONSTRAINT unikalneUzytkownikId FOR (u:Uzytkownik) REQUIRE u.uzytId IS UNIQUE",
            "CREATE CONSTRAINT unikalnePostId FOR (p:Post) REQUIRE p.postId IS UNIQUE",
            "CREATE CONSTRAINT unikalneKomentarzId FOR (k:Komentarz) REQUIRE k.komentarzId IS UNIQUE",
        };

        try (Session session = driver.session(SessionConfig.forDatabase("test"))) {
            session.writeTransaction(tx -> {
                for (String query : constraintQueries) {
                    tx.run(query);
                }
                return null;
            });
        } catch (Exception e) {
            log.error("Błąd podczas instalacji ograniczeń", e);
        } finally {
            driver.close();
        }
    }

    /**
     * Sprawdza, czy baza danych jest wypełniona.
     *
     * @return <ul>
     *            <li><strong>true</strong> - jeśli baza danych zawiera co najmniej jeden węzeł</li>
     *            <li><strong>false</strong> - jeśli baza danych jest pusta</li>
     *         </ul>
     */
    public boolean checkIfDatabaseIsPopulated() {
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        Neo4jClient client = Neo4jClient.create(driver);
        
        String checkQuery = "MATCH (n) RETURN COUNT(n) > 0 AS isNotEmpty";

        return client
            .query(checkQuery)
            .in(dbName)
            .fetchAs(Boolean.class)
            .mappedBy((typeSystem, record) -> record.get("isNotEmpty").asBoolean())
            .one()
            .orElse(false);
    }

    /**
     * Sprawdza dostępność bazy danych Neo4j.
     * 
     * <ul>
     *   <li><strong>attempts</strong>: Liczba prób sprawdzenia dostępności bazy danych.</li>
     *   <li><strong>maxRetries</strong>: Maksymalna liczba prób sprawdzenia dostępności bazy danych.</li>
     *   <li><strong>timeBetweenDBDrops</strong>: Czas oczekiwania między próbami sprawdzenia dostępności bazy danych.</li>
     * </ul>
     * 
     * @return <code>true</code> jeśli baza danych jest dostępna, <code>false</code> w przeciwnym razie.
     */
    public boolean isItActuallyAvailable() {
        int attempts = 0;
        while (attempts < maxRetries) {
            if (checkDatabase()) {
                return true;
            }
            try {
                Thread.sleep(timeBetweenDBDrops);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Wątek został zatrzymany podczas sprawdzania dostępności bazy danych", e);
                return false;
            }
            attempts++;
        }
        log.error("Nie udało się nawiązać połączenia z bazą danych Neo4j po {} próbach.", maxRetries);
        return false;
    }


    /**
     * Sprawdza połączenie z bazą danych Neo4j.
     *
     * @return <ul>
     *   <li><strong>true</strong> - jeśli połączenie z bazą danych zostało nawiązane pomyślnie</li>
     *   <li><strong>false</strong> - jeśli wystąpił błąd podczas nawiązywania połączenia</li>
     * </ul>
     */
    public boolean checkDatabase() {
        Driver driver = null;
        Neo4jClient client = null;
        try {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
            client = Neo4jClient.create(driver);
            client.query("RETURN 1")
            .in(dbName)
            .run();

            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (driver != null) {
                driver.close();
            }
        }
    }
}
