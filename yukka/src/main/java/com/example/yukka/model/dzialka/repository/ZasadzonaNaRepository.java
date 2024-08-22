package com.example.yukka.model.dzialka.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.yukka.model.dzialka.ZasadzonaNa;

public interface ZasadzonaNaRepository extends Neo4jRepository<ZasadzonaNa, Long> {
@Query("""
        MATCH (d:Dzialka)<-[r:ZASADZONA_NA {x: $x, y: $y}]-(n)
        RETURN count(r) > 0 AS isOccupied
            """)
    boolean checkIfCoordinatesAreOccupied(@Param("x") int x, @Param("y") int y);

    // Uwaga: Nietestowanie, i nie wiem czy dziala. Tyczy się wszystkich metod w tym pliku.
    @Query("""
        MATCH (d:Dzialka)<-[r:ZASADZONA_NA]-(n)
        RETURN r.x AS x, r.y AS y
        ORDER BY x, y
            """)
    List<ZasadzonaNa> getOccupiedCoordinates();

    @Query("""
        WITH range(0, 20) AS range_x, range(0, 20) AS range_y
        MATCH (d:Dzialka)
        WITH d, range_x, range_y
        OPTIONAL MATCH (d)<-[r:ZASADZONA_NA]-(n)
        WITH d, range_x, range_y, collect({x: r.x, y: r.y}) AS occupied_coords
        UNWIND range_x AS x
        UNWIND range_y AS y
        WITH x, y, occupied_coords
        WHERE NOT ({x: x, y: y} IN occupied_coords)
        RETURN x, y
        ORDER BY x, y
            """)
    List<ZasadzonaNa> getAvailableCoordinates();
}
