package com.example.yukka.model.roslina;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

@Service
public class RoslinaService {
@Autowired
    private Neo4jClient neo4jClient;
    
    @Autowired
    RoslinaRepository roslinaRepository;

    @Autowired
    RoslinaMapper roslinaMapper;

    public Collection<Roslina> getSome(int amount) {
        System.out.println("BOOOOOOOOOI");
        System.out.println(amount);
        Collection<Roslina> beep = roslinaRepository.getSomePlants(amount);
        Iterable<Roslina> properties = beep;
        for (Roslina property : properties) {
            System.out.println(property.getNazwa());
            System.out.println(property.getGleby());
            
            // TODO
        }

        //return plantRepository.getSomePlants(amount);



        return roslinaRepository.getSomePlants(2);
    }

    public Optional<Roslina> getById(Long id) {
        return roslinaRepository.findById(id);
    }

    public Roslina save(RoslinaRequest request) {
        Roslina pl = roslinaMapper.toRoslina(request);

        System.out.println("\n\n\nROSŁIBASDASD: "+ pl.toString() + "\n\n\n");

        if(roslinaRepository.findRoslinaByLatinName(request.getNazwaLacinska()) != null){
            // TODO: napraw podwójne uruchamianie się testów
            //  throw new IllegalArgumentException("Roślina o takiej nazwie łacińskiej już istnieje.");
        };
       // float mi = (float) request.getWysokoscMin();
        //return roslinaRepository.addPlantWithProperties(pl, request.getWlasciwosci());
        return roslinaRepository.addPlantWithProperties2(
            request.getNazwa(), 
        request.getNazwaLacinska(), 
        request.getOpis(), 
        request.getObraz(), 
        request.getWysokoscMin(), 
        request.getWysokoscMax(), 
        request.getWlasciwosci());
        /* 
        return Roslina roslinaRepository.save(request.getNazwa(), 
        request.getNazwaLacinska(), 
        request.getOpis(), 
        request.getWysokoscMin(), 
        request.getWysokoscMax(), 
        request.getWlasciwosci());
        */
    }

    public Roslina save2(Roslina request) {
        return roslinaRepository.save(request);
     }

     // Nie działa lol
     public Roslina save3(RoslinaRequest request) {
        Roslina pl = roslinaMapper.toRoslina(request);
        StringBuilder cypherQuery = new StringBuilder();

        cypherQuery.append("UNWIND $roslina AS ros\n")
                   .append("MERGE (p:Roslina {nazwa: ros.nazwa, nazwaLacinska: ros.nazwaLacinska, opis: ros.opis, obraz: COALESCE(ros.obraz, 'default_plant.jpg'), wysokoscMin: ros.wysokoscMin, wysokoscMax: ros.wysokoscMax})\n")
                   .append("WITH p, $properties AS properties\n")
                   .append("UNWIND properties AS property\n");

        for (int i = 0; i < request.getWlasciwosci().size(); i++) {
            cypherQuery.append("MATCH (w:").append(request.getWlasciwosci().get(i).get("etykieta"))
                       .append(" {nazwa: property.nazwa})\n")
                       .append("MERGE (p)-[:").append(request.getWlasciwosci().get(i).get("relacja"))
                       .append("]->(w)\n");
        }

        cypherQuery.append("WITH p\n")
                   .append("MATCH path=(p)-[r]->(w:Wlasciwosc)\n")
                   .append("RETURN p, collect(nodes(path)) AS nodes, collect(relationships(path)) AS relus");

        // Wykonanie zapytania Cypher
        return neo4jClient.query(cypherQuery.toString())
                          .bind(pl).to("roslina")
                          .bind(request.getWlasciwosci()).to("properties")
                          .fetchAs(Roslina.class)
                          .mappedBy((typeSystem, record) -> {
                              // Mapowanie danych
                              return new Roslina(); // Załaduj i mapuj dane zgodnie z potrzebami
                          })
                          .one()
                          .orElse(null);

    }
}
