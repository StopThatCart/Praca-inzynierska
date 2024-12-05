package com.example.yukka.seeder;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
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


    /* 
    @SuppressWarnings("deprecation")
    public void installTriggers() {
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        try (Session session = driver.session(SessionConfig.forDatabase("system"))) {
            session.writeTransaction(tx -> {
                tx.run("CALL apoc.trigger.install(" +
                        "'" + dbName + "', " +
                        "'setDataUtworzenia', " +
                        "'UNWIND {createdNodes} AS n " +
                        "SET n.dataUtworzenia = localdatetime()', " +
                        "{phase:'after'})");
                return null;
            });
        } catch (Exception e) {
            log.error("Błąd podczas instalacji wyzwalacza", e);
        } finally {
            driver.close();
        }
    }
*/
    public void dropAndCreateDatabase(String databaseName) {
        log.info("Czyszczenie bazy danych...");
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        Neo4jClient client = Neo4jClient.create(driver);
        String dropQuery = "DROP DATABASE " + databaseName +" IF EXISTS";
        String createQuery = "CREATE DATABASE " + databaseName +" IF NOT EXISTS";
        client
        .query(dropQuery)
        .in("system")
        .run();

        client
        .query(createQuery)
        .in("system")
        .run();

        try {
            Thread.sleep(timeBetweenDBDrops);
        } catch (InterruptedException e) {
        }
        log.info("Wyczyszczono bazę danych.");
    }

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
                log.error("Interrupted while waiting for database availability", e);
                return false;
            }
            attempts++;
        }
        log.error("Unable to access the database after {} attempts", maxRetries);
        return false;
    }


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
