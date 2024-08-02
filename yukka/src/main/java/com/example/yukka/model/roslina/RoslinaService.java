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

    public Optional<Roslina> findById(Long id) {
        return roslinaRepository.findById(id);
    }

    public Optional<Roslina> findByNazwaLacinska(String nazwaLacinska) {
        return roslinaRepository.findRoslinaByLatinName(nazwaLacinska);
    }

    public Roslina save(RoslinaRequest request) {
        Roslina pl = roslinaMapper.toRoslina(request);

      //  System.out.println("\n\n\nWYSOKOŚCI request: "+ request.getWysokoscMin() + " ||| " + request.getWysokoscMax() + "\n");
       // System.out.println("\n\n\nWYSOKOŚCI PL: "+ pl.getWysokoscMin() + " ||| " + pl.getWysokoscMax() + "\n\n\n");

        if(roslinaRepository.findRoslinaByLatinName(request.getNazwaLacinska()) != null){
            // TODO: napraw podwójne uruchamianie się testów
            //  throw new IllegalArgumentException("Roślina o takiej nazwie łacińskiej już istnieje.");
        };
       // float mi = (float) request.getWysokoscMin();
        //return roslinaRepository.addPlantWithProperties(pl, request.getWlasciwosci());
        return roslinaRepository.addPlantWithProperties(
            request.getNazwa(), 
        request.getNazwaLacinska(), 
        request.getOpis(), 
        request.getObraz(), 
        request.getWysokoscMin(), 
        request.getWysokoscMax(), 
       // pl,
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

    // Trzeba ID
    public void delete(Roslina roslina) {
        roslinaRepository.delete(roslina);
    }
    // Usuwanie po ID zajmuje ogromną ilość czasu i wywołuje HeapOverflow, więc lepiej jest użyć UNIQUE atrybutu jak nazwaLacinska
    public void deleteById(Long roslinaId) {
        roslinaRepository.deleteById(roslinaId);
       // roslinaRepository.deleteById(roslinaId);
    }

    public void deleteByNazwaLacinska(String nazwaLacinska) {
        roslinaRepository.deleteByNazwaLacinska(nazwaLacinska);
    }
}
