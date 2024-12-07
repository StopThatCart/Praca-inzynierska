package com.example.yukka.model.roslina.controller;

import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwosciRodzaje;

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
    Set<WlasciwosciRodzaje> getWlasciwosciWithRelacje();

}
