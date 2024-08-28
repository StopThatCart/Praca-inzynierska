package com.example.yukka.model.social.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import io.micrometer.common.lang.NonNull;



public interface KomentarzRepository extends Neo4jRepository<Komentarz, Long> {

    @Override
    @Query("""
            MATCH (kom:Komentarz)
            RETURN kom
            """)
    @NonNull List<Komentarz> findAll();

    @Query("""
            MATCH (kom:Komentarz{komentarzId: $komentarzId})<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik)
            OPTIONAL MATCH (rozmowa:RozmowaPrywatna)-[r3:MA_WIADOMOSC]->(kom)
            OPTIONAL MATCH path = (:Post)-[:MA_KOMENTARZ]->(kom)
            RETURN kom, r1, uzyt, rozmowa, r3, collect(nodes(path)) as pathNodes, collect(relationships(path)) as pathRels
            """)
    Optional<Komentarz> findKomentarzByKomentarzId(@Param("komentarzId") String komentarzId);

    @Query("""
        MATCH (kom:Komentarz{komentarzId: $komentarzId})<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik)
        OPTIONAL MATCH path = (post:Post)-[:MA_KOMENTARZ]->(kom:Komentarz)
                          <-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
                          <-[:SKOMENTOWAL]-(uzytkownik:Uzytkownik)
        RETURN kom, r1, uzyt, collect(nodes(path)), collect(relationships(path))
        """)
    Optional<Komentarz> findKomentarzWithOdpowiedziByKomentarzId(@Param("komentarzId") String komentarzId);

    
    @Query("""
            MATCH (komentarz:Komentarz)<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik{email: $email})
            RETURN komentarz
            ORDER BY komentarz.dataUtworzenia DESC
            LIMIT 1
            """)
    Optional<Komentarz> findNewestKomentarzOfUzytkownik(@Param("email") String email);


    @Query(value = """
        MATCH path=(komentarz:Komentarz)<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik{email: $email})
        WHERE NOT (komentarz)<-[:MA_WIADOMOSC]-(:RozmowaPrywatna)
        OPTIONAL MATCH (post:Post)-[r2:MA_KOMENTARZ]->(komentarz)

        RETURN  komentarz, r1, post, r2, collect(nodes(path)), collect(relationships(path))
        :#{orderBy(#pageable)} SKIP $skip LIMIT $limit
        """,
        countQuery = """
        MATCH (komentarz:Komentarz)<-[r1:SKOMENTOWAL]-(uzyt:Uzytkownik{email: $email})
        WHERE NOT (komentarz)<-[:MA_WIADOMOSC]-(:RozmowaPrywatna)
        RETURN count(komentarz)
    """)
    Page<Komentarz> findKomentarzeOfUzytkownik(@Param("email") String email, Pageable pageable);

    @Query("""
            MATCH (uzyt:Uzytkownik{email: $email})
            MATCH (kom:Komentarz{komentarzId: $komentarzId})
            MERGE (uzyt)-[relu:OCENIL]->(kom)
            ON CREATE SET relu.lubi = $ocena
            ON MATCH SET relu.lubi = $ocena

            WITH kom
            MATCH (kom)<-[r:OCENIL]-(:Uzytkownik)
            WITH kom, 
                    COUNT(CASE WHEN r.lubi = true THEN 1 ELSE NULL END) AS ocenyLubi,
                    COUNT(CASE WHEN r.lubi = false THEN 1 ELSE NULL END) AS ocenyNielubi
            SET kom.ocenyLubi = ocenyLubi, kom.ocenyNielubi = ocenyNielubi

            WITH kom
            MATCH (oceniany:Uzytkownik)-[:SKOMENTOWAL]->(kom)
            OPTIONAL MATCH (oceniany)-[:SKOMENTOWAL]->(komentarz:Komentarz)<-[r2:OCENIL]-(uzyt:Uzytkownik)
                WHERE oceniany <> uzyt
            WITH kom, oceniany,  
                COUNT(CASE WHEN r2.lubi = true THEN 1 ELSE NULL END) AS ocenyPozytywne,
                COUNT(CASE WHEN r2.lubi = false THEN 1 ELSE NULL END) AS ocenyNegatywne
            SET oceniany.komentarzeOcenyPozytywne = ocenyPozytywne, 
                oceniany.komentarzeOcenyNegatywne = ocenyNegatywne

            RETURN kom
            """)
    Komentarz addOcenaToKomentarz(@Param("email") String email, @Param("komentarzId") String komentarzId, @Param("ocena") boolean ocena);


    @Query("""
            MATCH (kom:Komentarz{komentarzId: $komentarzId})
            WITH kom
            MATCH (kom)<-[r:OCENIL]-(:Uzytkownik)
            WITH kom, 
                    COUNT(CASE WHEN r.lubi = true THEN 1 ELSE NULL END) AS ocenyLubi,
                    COUNT(CASE WHEN r.lubi = false THEN 1 ELSE NULL END) AS ocenyNielubi
            SET kom.ocenyLubi = ocenyLubi, kom.ocenyNielubi = ocenyNielubi

            WITH kom
            MATCH (oceniany:Uzytkownik)-[:SKOMENTOWAL]->(kom)
            MATCH (oceniany)-[:SKOMENTOWAL]->(komentarz:Komentarz)<-[r2:OCENIL]-(uzyt:Uzytkownik)
                WHERE oceniany <> uzyt
            WITH kom, oceniany,  
                COUNT(CASE WHEN r2.lubi = true THEN 1 ELSE NULL END) AS ocenyPozytywne,
                COUNT(CASE WHEN r2.lubi = false THEN 1 ELSE NULL END) AS ocenyNegatywne
            SET oceniany.komentarzeOcenyPozytywne = ocenyPozytywne, 
                oceniany.komentarzeOcenyNegatywne = ocenyNegatywne

            RETURN kom
            """)
    Komentarz updateUzytkownikKomentarzeOcenyCountButBad(@Param("email") String email, @Param("komentarzId") String komentarzId, @Param("ocena") boolean ocena);

    @Query("""
        MATCH (oceniany:Uzytkownik{uzytId: $uzytId})
        OPTIONAL MATCH (oceniany)-[:SKOMENTOWAL]->(komentarz:Komentarz)<-[r2:OCENIL]-(uzyt:Uzytkownik)
            WHERE oceniany <> uzyt
        WITH oceniany,  
            COUNT(CASE WHEN r2.lubi = true THEN 1 ELSE NULL END) AS ocenyPozytywne,
            COUNT(CASE WHEN r2.lubi = false THEN 1 ELSE NULL END) AS ocenyNegatywne
        SET oceniany.komentarzeOcenyPozytywne = ocenyPozytywne, 
            oceniany.komentarzeOcenyNegatywne = ocenyNegatywne
        RETURN oceniany
        """)
    void updateUzytkownikKomentarzeOcenyCount(@Param("uzytId") String uzytId);

    @Query("""
        UNWIND $uzytkownicy AS uzytkownik
        WITH uzytkownik
        OPTIONAL MATCH (oceniany:Uzytkownik)-[:SKOMENTOWAL]->(komentarz:Komentarz)<-[r2:OCENIL]-(uzyt:Uzytkownik)
            WHERE oceniany <> uzyt AND oceniany.uzytId = uzytkownik.uzytId
        WITH oceniany,  
            COUNT(CASE WHEN r2.lubi = true THEN 1 ELSE NULL END) AS ocenyPozytywne,
            COUNT(CASE WHEN r2.lubi = false THEN 1 ELSE NULL END) AS ocenyNegatywne
        SET oceniany.komentarzeOcenyPozytywne = ocenyPozytywne, 
            oceniany.komentarzeOcenyNegatywne = ocenyNegatywne
        
        """)
    void updateUzytkownikKomentarzeOcenyCount(@Param("uzytkownicy") List<Uzytkownik> uzytkownicy);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})-[relu:OCENIL]->(kom:Komentarz{komentarzId: $komentarzId})
        DELETE relu

        WITH kom
        MATCH (kom)<-[r:OCENIL]-(:Uzytkownik)
        WITH kom, COUNT(CASE WHEN r.lubi = true THEN 1 ELSE NULL END) AS ocenyLubi,
                  COUNT(CASE WHEN r.lubi = false THEN 1 ELSE NULL END) AS ocenyNielubi
        SET kom.ocenyLubi = ocenyLubi, kom.ocenyNielubi = ocenyNielubi

        WITH kom
        MATCH (oceniany:Uzytkownik)-[:SKOMENTOWAL]->(kom)
        OPTIONAL MATCH (oceniany)-[:SKOMENTOWAL]->(komentarz:Komentarz)<-[r2:OCENIL]-(uzyt:Uzytkownik)
            WHERE oceniany <> uzyt
        WITH kom, oceniany,  
            COUNT(CASE WHEN r2.lubi = true THEN 1 ELSE NULL END) AS ocenyPozytywne,
            COUNT(CASE WHEN r2.lubi = false THEN 1 ELSE NULL END) AS ocenyNegatywne
        SET oceniany.komentarzeOcenyPozytywne = ocenyPozytywne, 
            oceniany.komentarzeOcenyNegatywne = ocenyNegatywne

        """)
    void removeOcenaFromKomentarz(@Param("email") String email, @Param("komentarzId") String komentarzId);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        MATCH (kom2:Komentarz{komentarzId: $komentarzId})
        WITH uzyt, kom2, $kom.__properties__ AS pt 
        CREATE (uzyt)-[:SKOMENTOWAL]->
                (kom:Komentarz{komentarzId: pt.komentarzId, opis: pt.opis, edytowany: false,
                ocenyLubi: 0, ocenyNielubi: 0, 
                obraz: pt.obraz, dataUtworzenia: localdatetime()})
                -[:ODPOWIEDZIAL]->(kom2)

        WITH kom, kom2
        OPTIONAL MATCH (kom2)-[:ODPOWIEDZIAL*0..]->(kom1:Komentarz)<-[:MA_KOMENTARZ]-(post:Post)

        WITH kom, post
        CREATE (kom)-[:JEST_W_POSCIE]->(post)

        WITH kom, post
        MATCH (post)-[:MA_KOMENTARZ]->(komentarz:Komentarz)
        OPTIONAL MATCH (komentarz)<-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
        WITH kom, post, komentarz, collect(odpowiedz) AS odpowiedzi
        WITH kom, post, sum(size(odpowiedzi)) AS liczbaOdpowiedzi
        SET post.liczbaKomentarzy = liczbaOdpowiedzi

        RETURN kom
        """)
    Komentarz addOdpowiedzToKomentarzInPost(@Param("email") String email, @Param("kom") Komentarz kom, @Param("komentarzId") String targetKomentarzId);


    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        MATCH (kom2:Komentarz{komentarzId: $komentarzId})
        WITH uzyt, kom2, $kom.__properties__ AS pt 
        CREATE (uzyt)-[:SKOMENTOWAL]->
                (kom:Komentarz{komentarzId: pt.komentarzId, opis: pt.opis, edytowany: false,
                ocenyLubi: 0, ocenyNielubi: 0, 
                obraz: pt.obraz, dataUtworzenia: localdatetime()})
                -[:ODPOWIEDZIAL]->(kom2)
        RETURN kom
        """)
    Komentarz addOdpowiedzToKomentarzButOld(@Param("email") String email, @Param("kom") Komentarz kom, @Param("komentarzId") String targetKomentarzId);

    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})
        MATCH (post:Post{postId: $postId})
        WITH uzyt, post, $kom.__properties__ AS pt 
        CREATE (uzyt)-[:SKOMENTOWAL]->
                (kom:Komentarz{komentarzId: pt.komentarzId, opis: pt.opis, edytowany: false, 
                ocenyLubi: 0, ocenyNielubi: 0, obraz: pt.obraz, dataUtworzenia: localdatetime()})
                <-[:MA_KOMENTARZ]-(post)
        CREATE (kom)-[:JEST_W_POSCIE]->(post)

        WITH kom 

        MATCH (post:Post{postId: $postId})-[:MA_KOMENTARZ]->(komentarz:Komentarz)
        OPTIONAL MATCH (komentarz)<-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
        WITH kom, post, komentarz, collect(odpowiedz) AS odpowiedzi
        WITH kom, post, sum(size(odpowiedzi)) AS liczbaOdpowiedzi
        SET post.liczbaKomentarzy = liczbaOdpowiedzi
    
        RETURN kom
        """)
    Komentarz addKomentarzToPost(@Param("email") String email, @Param("postId") String postId, @Param("kom") Komentarz kom);

    @Query("""
        MATCH (uzyt1:Uzytkownik{nazwa: $nadawca})-[:JEST_W_ROZMOWIE]->(priv:RozmowaPrywatna)<-[:JEST_W_ROZMOWIE]-(uzyt2:Uzytkownik{nazwa: $odbiorca})
        WITH uzyt1, priv, $kom.__properties__ as pt
        CREATE (uzyt1)-[:SKOMENTOWAL]->
                (kom:Komentarz{komentarzId: pt.komentarzId, opis: pt.opis, edytowany: false,
                ocenyLubi: 0, ocenyNielubi: 0, obraz: pt.obraz, dataUtworzenia: localdatetime()})
                <-[:MA_WIADOMOSC]-(priv)
        WITH priv, kom
        SET priv.ostatnioAktualizowane = localdatetime()

        WITH priv, kom
        MATCH (priv)-[:MA_WIADOMOSC]->(komentarze:Komentarz)
        WITH priv, kom, count(komentarze) AS liczbaKom
        SET priv.liczbaWiadomosci = liczbaKom

        RETURN kom
        """)
    Komentarz addKomentarzToRozmowaPrywatna(@Param("nadawca") String nadawca, @Param("odbiorca") String odbiorca, @Param("kom") Komentarz kom);

    // Jakimś cudem działa z i bez odpowiedzi. Czary.
    @Query("""
        MATCH (post:Post{postId: $postId})-[:MA_KOMENTARZ]->(komentarz:Komentarz)
        OPTIONAL MATCH (komentarz)<-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)
        WITH post, komentarz, collect(odpowiedz) AS odpowiedzi
        WITH post, sum(size(odpowiedzi)) AS liczbaOdpowiedzi
        SET post.liczbaKomentarzy = liczbaOdpowiedzi
        """)
    void updateKomentarzeCountInPost(@Param("postId") String postId);

    @Query("""
        MATCH (post:Post{postId: $postId})
        MATCH (reszta:Komentarz)-[r1:JEST_W_POSCIE]->(post)
        WITH post, count(r1) AS liczbaKomentarzy
        SET post.liczbaKomentarzy = liczbaKomentarzy
        """)
    void updateKomentarzeCountInPostButWithFunniNewRelation(@Param("postId") String postId);


    @Query("""
        MATCH (:Uzytkownik{email:email})-[:SKOMENTOWAL]->(komentarz:Komentarz{$kom.__properies__.komentarzId})
        SET komentarz.opis = $kom.__properies__.opis, komentarz.edytowany = true
        RETURN komentarz
        """)
    Komentarz updateKomentarz(@Param("email") String email, @Param("postId") String postId, @Param("kom") Komentarz kom);


    // TODO: Komentarze na profilowe, ogród, galerię, może jeszcze rośliny


    // UWAGA: To prawdopodobnie nie będzie używane, gdyż komentarze powinno się usuwać tylko wtedy, kiedy usuwa się cały post/ogród/obraz
    /* 
    @Query("""
        MATCH (uzyt:Uzytkownik{email: $email})-[:SKOMENTOWAL]->(kom:Komentarz{komentarzId: $komentarzId})<-[:MA_KOMENTARZ]-(post:Post{postId: $postId})
        DETACH DELETE kom

        WITH post
        CALL apoc.path.expand(post, 'MA_KOMENTARZ', 'Komentarz') YIELD node
        WITH post, COLLECT(DISTINCT node) AS komentarze
        SET post.komentarze = SIZE(komentarze)

        """)
    void removeKomentarzFromPost(@Param("email") String email, @Param("postId") String postId, @Param("komentarzId") String komentarzId);
    */

    // UWAGA: TYLKO DO TESTÓW
    @Query("""
        MATCH (u:Komentarz) DETACH DELETE u 
        """)
    void clearKomentarze();

    // UWAGA: Nie będzie działać dla wiadomości prywatnych. Chyba.
    // UWAGA2: Potrzebna będzie aktualizacji liczby komentarzy w poście, jeśli w takim jest. Najlepiej zrób to w service.
    // UWAGA3: Tak samo liczbę ocen pozytywnych i negatywnych dla komentującego i komentowanego.
    @Query("""
        MATCH (oceniany:Uzytkownik)-[:SKOMENTOWAL]->(kom:Komentarz{komentarzId: $komentarzId})
        OPTIONAL MATCH (kom)<-[:ODPOWIEDZIAL*0..]-(odpowiedz:Komentarz)<-[:SKOMENTOWAL]-(uzyt:Uzytkownik)
        DETACH DELETE odpowiedz

        WITH DISTINCT uzyt, odpowiedz
        OPTIONAL MATCH (uzyt)-[:SKOMENTOWAL]->(k:Komentarz)<-[r:OCENIL]-(innyUzyt:Uzytkownik)
        WITH uzyt, innyUzyt, odpowiedz,
            COUNT(CASE WHEN r.lubi = true THEN 1 ELSE NULL END) AS ocenyPozytywne,
            COUNT(CASE WHEN r.lubi = false THEN 1 ELSE NULL END) AS ocenyNegatywne
        SET uzyt.komentarzeOcenyPozytywne = ocenyPozytywne,
            uzyt.komentarzeOcenyPozytywne = ocenyNegatywne

        WITH innyUzyt, odpowiedz
        OPTIONAL MATCH (innyUzyt)-[:SKOMENTOWAL]->(k:Komentarz)<-[r:OCENIL]-(uzyt:Uzytkownik)
            WHERE innyUzyt <> uzyt
        WITH innyUzyt,  odpowiedz,  
            COUNT(CASE WHEN r.lubi = true THEN 1 ELSE NULL END) AS ocenyPozytywne,
            COUNT(CASE WHEN r.lubi = false THEN 1 ELSE NULL END) AS ocenyNegatywne
        SET innyUzyt.komentarzeOcenyPozytywne = ocenyPozytywne, 
            innyUzyt.komentarzeOcenyNegatywne = ocenyNegatywne
        """)
    void removeKomentarz(@Param("komentarzId") String komentarzId);


    @Query("""
        MATCH (post:Post{postId: "1938001c-29d1-4f5f-ad7b-ec4a604d6453"})
        OPTIONAL MATCH (post)<-[:MA_POST]-(uzyt:Uzytkownik)
        OPTIONAL MATCH (post)-[:MA_KOMENTARZ]->()<-[:SKOMENTOWAL]-(uzyt2:Uzytkownik)
        OPTIONAL MATCH (post)<-[:OCENIL]-(uzyt3:Uzytkownik)
        WITH post, COLLECT(DISTINCT uzyt) + COLLECT(DISTINCT uzyt2) + COLLECT(DISTINCT uzyt3) AS uzytkownicy
        RETURN uzytkownicy
        """)
    List<Uzytkownik> getConnectedUzytkownicyFromKomentarz(@Param("komentarzId") String postId);




}


/*
 * 
 * 
 * @Test
    public void testRejectRozmowaPrywatna() throws Exception {
        String nadawcaId = "testId";
        doNothing().when(rozmowaPrywatnaService).rejectRozmowaPrywatna(nadawcaId, receiverAuth);

        mockMvc.perform(put("/nadawca-uzyt-id/reject")
                .principal(authentication))
                .andExpect(status().ok());

        verify(rozmowaPrywatnaService, times(1)).rejectRozmowaPrywatna(nadawcaId, receiverAuth);
    }
 */