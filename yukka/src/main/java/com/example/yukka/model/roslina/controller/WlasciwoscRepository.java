package com.example.yukka.model.roslina.controller;

import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscKatalogResponse;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscResponse;

/**
 * Interfejs WlasciwoscRepository rozszerza Neo4jRepository i zapewnia
 * metody do operacji na encjach typu Wlasciwosc w bazie danych Neo4j.
 * 
 * <p>Interfejs ten definiuje niestandardowe zapytania do pobierania
 * właściwości z relacjami, które nie są powiązane z użytkownikiem.</p>
 * 
 */
public interface WlasciwoscRepository extends Neo4jRepository<Wlasciwosc, Long> {

    @Query("""
        MATCH (w:Wlasciwosc) WHERE NOT w:UzytkownikWlasciwosc
        WITH w, [lbl IN labels(w) WHERE lbl <> 'Wlasciwosc'] AS lbls
        UNWIND lbls AS lbl
        WITH DISTINCT lbl, collect(w.nazwa) AS nazwy
        RETURN lbl AS etykieta, nazwy
    """)
    Set<WlasciwoscResponse> getWlasciwosciWithRelacje();

    @Query("""
            WITH $roslina.__properties__ AS rp
            MATCH (roslina:Roslina) WHERE NOT roslina:UzytkownikRoslina
                AND (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
                AND (rp.nazwaLacinska IS NULL OR roslina.nazwaLacinska CONTAINS toLower(rp.nazwaLacinska))
                AND (rp.wysokoscMin IS NULL OR roslina.wysokoscMin >= rp.wysokoscMin)
                AND (rp.wysokoscMax IS NULL OR roslina.wysokoscMax <= rp.wysokoscMax)
        
            WITH roslina, $formy AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_FORME]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $gleby AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_GLEBE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $grupy AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_GRUPE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $koloryLisci AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_KOLOR_LISCI]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $koloryKwiatow AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_KOLOR_KWIATOW]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $kwiaty AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_KWIAT]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $odczyny AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_ODCZYN]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $okresyKwitnienia AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_OKRES_KWITNIENIA]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $okresyOwocowania AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_OKRES_OWOCOWANIA]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $owoce AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_OWOC]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $podgrupa AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_PODGRUPE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $pokroje AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_POKROJ]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $silyWzrostu AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_SILE_WZROSTU]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $stanowiska AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_STANOWISKO]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $walory AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_WALOR]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $wilgotnosci AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_WILGOTNOSC]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $zastosowania AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_ZASTOSOWANIE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $zimozielonosci AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_ZIMOZIELONOSC_LISCI]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            MATCH (roslina)-[]->(w:Wlasciwosc) WHERE NOT w:UzytkownikWlasciwosc
            WITH w, [lbl IN labels(w) WHERE lbl <> 'Wlasciwosc'] AS lbls, roslina
            UNWIND lbls AS lbl
            WITH DISTINCT lbl, w.nazwa AS nazwa, count(DISTINCT roslina) AS liczbaRoslin
            RETURN lbl AS etykieta, collect({nazwa: nazwa, liczbaRoslin: liczbaRoslin}) AS nazwyLiczbaRoslin
            """)
        Set<WlasciwoscKatalogResponse> getWlasciwosciCountFromQuery(
            @Param("roslina") Roslina roslina, 
            @Param("formy") Set<Wlasciwosc> formy,
            @Param("gleby") Set<Wlasciwosc> gleby,
            @Param("grupy") Set<Wlasciwosc> grupy,
            @Param("koloryLisci") Set<Wlasciwosc> koloryLisci,
            @Param("koloryKwiatow") Set<Wlasciwosc> koloryKwiatow,
            @Param("kwiaty") Set<Wlasciwosc> kwiaty,
            @Param("odczyny") Set<Wlasciwosc> odczyny,
            @Param("okresyKwitnienia") Set<Wlasciwosc> okresyKwitnienia,
            @Param("okresyOwocowania") Set<Wlasciwosc> okresyOwocowania,
            @Param("owoce") Set<Wlasciwosc> owoce,
            @Param("podgrupa") Set<Wlasciwosc> podgrupa,
            @Param("pokroje") Set<Wlasciwosc> pokroje,
            @Param("silyWzrostu") Set<Wlasciwosc> silyWzrostu,
            @Param("stanowiska") Set<Wlasciwosc> stanowiska,
            @Param("walory") Set<Wlasciwosc> walory,
            @Param("wilgotnosci") Set<Wlasciwosc> wilgotnosci,
            @Param("zastosowania") Set<Wlasciwosc> zastosowania,
            @Param("zimozielonosci") Set<Wlasciwosc> zimozielonosci
        );



        @Query("""
            WITH $roslina.__properties__ AS rp
            MATCH (roslina:UzytkownikRoslina)-[:STWORZONA_PRZEZ]->(uzyt:Uzytkownik{nazwa: $uzytkownikNazwa})
                WHERE (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
                AND (rp.wysokoscMin IS NULL OR roslina.wysokoscMin >= rp.wysokoscMin)
                AND (rp.wysokoscMax IS NULL OR roslina.wysokoscMax <= rp.wysokoscMax)
        
            WITH roslina, $formy AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_FORME]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $gleby AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_GLEBE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $grupy AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_GRUPE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $koloryLisci AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_KOLOR_LISCI]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $koloryKwiatow AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_KOLOR_KWIATOW]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $kwiaty AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_KWIAT]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $odczyny AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_ODCZYN]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $okresyKwitnienia AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_OKRES_KWITNIENIA]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $okresyOwocowania AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_OKRES_OWOCOWANIA]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $owoce AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_OWOC]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $podgrupa AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_PODGRUPE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $pokroje AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_POKROJ]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $silyWzrostu AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_SILE_WZROSTU]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $stanowiska AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_STANOWISKO]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $walory AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_WALOR]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $wilgotnosci AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_WILGOTNOSC]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $zastosowania AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_ZASTOSOWANIE]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $zimozielonosci AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_ZIMOZIELONOSC_LISCI]->(:Wlasciwosc {nazwa: wezel.__properties__.nazwa})
            })
        
            MATCH (roslina)-[]->(w:Wlasciwosc)
            WITH w, [lbl IN labels(w) WHERE lbl <> 'Wlasciwosc'] AS lbls, roslina
            UNWIND lbls AS lbl
            WITH DISTINCT lbl, w.nazwa AS nazwa, count(DISTINCT roslina) AS liczbaRoslin
            RETURN lbl AS etykieta, collect({nazwa: nazwa, liczbaRoslin: liczbaRoslin}) AS nazwyLiczbaRoslin
            """)
        Set<WlasciwoscKatalogResponse> getUzytkownikWlasciwosciCountFromQuery(
            @Param("uzytkownikNazwa") String uzytkownikNazwa,
            @Param("roslina") Roslina roslina, 
            @Param("formy") Set<Wlasciwosc> formy,
            @Param("gleby") Set<Wlasciwosc> gleby,
            @Param("grupy") Set<Wlasciwosc> grupy,
            @Param("koloryLisci") Set<Wlasciwosc> koloryLisci,
            @Param("koloryKwiatow") Set<Wlasciwosc> koloryKwiatow,
            @Param("kwiaty") Set<Wlasciwosc> kwiaty,
            @Param("odczyny") Set<Wlasciwosc> odczyny,
            @Param("okresyKwitnienia") Set<Wlasciwosc> okresyKwitnienia,
            @Param("okresyOwocowania") Set<Wlasciwosc> okresyOwocowania,
            @Param("owoce") Set<Wlasciwosc> owoce,
            @Param("podgrupa") Set<Wlasciwosc> podgrupa,
            @Param("pokroje") Set<Wlasciwosc> pokroje,
            @Param("silyWzrostu") Set<Wlasciwosc> silyWzrostu,
            @Param("stanowiska") Set<Wlasciwosc> stanowiska,
            @Param("walory") Set<Wlasciwosc> walory,
            @Param("wilgotnosci") Set<Wlasciwosc> wilgotnosci,
            @Param("zastosowania") Set<Wlasciwosc> zastosowania,
            @Param("zimozielonosci") Set<Wlasciwosc> zimozielonosci
        );

}
