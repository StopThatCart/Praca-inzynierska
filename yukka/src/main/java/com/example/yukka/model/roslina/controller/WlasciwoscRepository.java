package com.example.yukka.model.roslina.controller;

import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscWithRelations;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwosciRodzaje;

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
