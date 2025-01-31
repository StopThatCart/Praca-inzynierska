package com.example.yukka.model.roslina.controller;

import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.cecha.Cecha;
import com.example.yukka.model.roslina.cecha.CechaKatalogResponse;
import com.example.yukka.model.roslina.cecha.CechaResponse;

/**
 * Interfejs CechaRepository rozszerza Neo4jRepository i zapewnia
 * metody do operacji na encjach typu Cecha w bazie danych Neo4j.
 * 
 * <p>Interfejs ten definiuje niestandardowe zapytania do pobierania
 * cech z relacjami, które nie są powiązane z użytkownikiem.</p>
 * 
 */
public interface CechaRepository extends Neo4jRepository<Cecha, Long> {

    @Query("""
        MATCH (w:Cecha) WHERE NOT w:CechaWlasna
        WITH w, [lbl IN labels(w) WHERE lbl <> 'Cecha'] AS lbls
        UNWIND lbls AS lbl
        WITH DISTINCT lbl, collect(w.nazwa) AS nazwy
        RETURN lbl AS etykieta, nazwy
    """)
    Set<CechaResponse> getCechyWithRelacje();

    @Query("""
            WITH $roslina.__properties__ AS rp
            MATCH (roslina:Roslina) WHERE NOT roslina:RoslinaWlasna
                AND (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
                AND (rp.nazwaLacinska IS NULL OR roslina.nazwaLacinska CONTAINS toLower(rp.nazwaLacinska))
                AND (rp.wysokoscMin IS NULL OR roslina.wysokoscMin >= rp.wysokoscMin)
                AND (rp.wysokoscMax IS NULL OR roslina.wysokoscMax <= rp.wysokoscMax)
        
            WITH roslina, $formy AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_FORME]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $gleby AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_GLEBE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $grupy AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_GRUPE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $koloryLisci AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_KOLOR_LISCI]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $koloryKwiatow AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_KOLOR_KWIATOW]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $kwiaty AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_KWIAT]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $odczyny AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_ODCZYN]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $okresyKwitnienia AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_OKRES_KWITNIENIA]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $okresyOwocowania AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_OKRES_OWOCOWANIA]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $owoce AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_OWOC]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $podgrupa AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_PODGRUPE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $pokroje AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_POKROJ]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $silyWzrostu AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_SILE_WZROSTU]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $stanowiska AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_STANOWISKO]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $walory AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_WALOR]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $wilgotnosci AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_WILGOTNOSC]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $zastosowania AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_ZASTOSOWANIE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $zimozielonosci AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_ZIMOZIELONOSC_LISCI]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            MATCH (roslina)-[relacja]->(w:Cecha) WHERE NOT w:CechaWlasna
            WITH w, 
            CASE 
                WHEN type(relacja) = 'MA_OKRES_KWITNIENIA' THEN 'OkresKwitnienia'
                WHEN type(relacja) = 'MA_OKRES_OWOCOWANIA' THEN 'OkresOwocowania'
                WHEN type(relacja) = 'MA_KOLOR_LISCI' THEN 'KolorLisci'
                WHEN type(relacja) = 'MA_KOLOR_KWIATOW' THEN 'KolorKwiatow'
                ELSE [lbl IN labels(w) WHERE lbl <> 'Cecha'][0]
            END AS etykieta, 
            roslina
            WITH DISTINCT etykieta, w.nazwa AS nazwa, count(DISTINCT roslina) AS liczbaRoslin
            RETURN etykieta, collect({nazwa: nazwa, liczbaRoslin: liczbaRoslin}) AS nazwyLiczbaRoslin
            """)
        Set<CechaKatalogResponse> getCechyCountFromQuery(
            Roslina roslina, 
            Set<Cecha> formy,
            Set<Cecha> gleby,
            Set<Cecha> grupy,
            Set<Cecha> koloryLisci,
            Set<Cecha> koloryKwiatow,
            Set<Cecha> kwiaty,
            Set<Cecha> odczyny,
            Set<Cecha> okresyKwitnienia,
            Set<Cecha> okresyOwocowania,
            Set<Cecha> owoce,
            Set<Cecha> podgrupa,
            Set<Cecha> pokroje,
            Set<Cecha> silyWzrostu,
            Set<Cecha> stanowiska,
            Set<Cecha> walory,
            Set<Cecha> wilgotnosci,
            Set<Cecha> zastosowania,
            Set<Cecha> zimozielonosci
        );



        @Query("""
            WITH $roslina.__properties__ AS rp
            MATCH (roslina:RoslinaWlasna)-[:STWORZONA_PRZEZ]->(uzyt:Uzytkownik{nazwa: $uzytkownikNazwa})
                WHERE (rp.nazwa IS NULL OR roslina.nazwa CONTAINS rp.nazwa) 
                AND (rp.nazwaLacinska IS NULL OR roslina.nazwaLacinska CONTAINS toLower(rp.nazwaLacinska))
                AND (rp.wysokoscMin IS NULL OR roslina.wysokoscMin >= rp.wysokoscMin)
                AND (rp.wysokoscMax IS NULL OR roslina.wysokoscMax <= rp.wysokoscMax)
        
            WITH roslina, $formy AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_FORME]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $gleby AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_GLEBE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $grupy AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_GRUPE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $koloryLisci AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_KOLOR_LISCI]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $koloryKwiatow AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_KOLOR_KWIATOW]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $kwiaty AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_KWIAT]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $odczyny AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_ODCZYN]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $okresyKwitnienia AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_OKRES_KWITNIENIA]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $okresyOwocowania AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_OKRES_OWOCOWANIA]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $owoce AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_OWOC]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $podgrupa AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_PODGRUPE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $pokroje AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_POKROJ]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $silyWzrostu AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_SILE_WZROSTU]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $stanowiska AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_STANOWISKO]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $walory AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_WALOR]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $wilgotnosci AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_WILGOTNOSC]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $zastosowania AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_ZASTOSOWANIE]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            WITH roslina, $zimozielonosci AS wezly
            WHERE size(wezly) = 0 OR ALL(wezel IN wezly WHERE EXISTS {
                MATCH (roslina)-[:MA_ZIMOZIELONOSC_LISCI]->(:Cecha {nazwa: wezel.__properties__.nazwa})
            })
        
            MATCH (roslina)-[relacja]->(w:Cecha)
            WITH w, 
            CASE 
                WHEN type(relacja) = 'MA_OKRES_KWITNIENIA' THEN 'OkresKwitnienia'
                WHEN type(relacja) = 'MA_OKRES_OWOCOWANIA' THEN 'OkresOwocowania'
                WHEN type(relacja) = 'MA_KOLOR_LISCI' THEN 'KolorLisci'
                WHEN type(relacja) = 'MA_KOLOR_KWIATOW' THEN 'KolorKwiatow'
                ELSE [lbl IN labels(w) WHERE lbl <> 'Cecha'][0]
            END AS etykieta, 
            roslina
            WITH DISTINCT etykieta, w.nazwa AS nazwa, count(DISTINCT roslina) AS liczbaRoslin
            RETURN etykieta, collect({nazwa: nazwa, liczbaRoslin: liczbaRoslin}) AS nazwyLiczbaRoslin
            """)
        Set<CechaKatalogResponse> getCechyWlasneCountFromQuery(
            String uzytkownikNazwa,
            Roslina roslina, 
            Set<Cecha> formy,
            Set<Cecha> gleby,
            Set<Cecha> grupy,
            Set<Cecha> koloryLisci,
            Set<Cecha> koloryKwiatow,
            Set<Cecha> kwiaty,
            Set<Cecha> odczyny,
            Set<Cecha> okresyKwitnienia,
            Set<Cecha> okresyOwocowania,
            Set<Cecha> owoce,
            Set<Cecha> podgrupa,
            Set<Cecha> pokroje,
            Set<Cecha> silyWzrostu,
            Set<Cecha> stanowiska,
            Set<Cecha> walory,
            Set<Cecha> wilgotnosci,
            Set<Cecha> zastosowania,
            Set<Cecha> zimozielonosci
        );

}
