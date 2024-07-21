package com.example.yukka.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.yukka.model.plants.Roslina;
import com.example.yukka.repository.RoslinaRepository;

@Service
public class RoslinaService {

    @Autowired
    RoslinaRepository plantRepository;

    public Collection<Roslina> getSome(int amount) {
        System.out.println("BOOOOOOOOOI");
        System.out.println(amount);
        Collection<Roslina> beep = plantRepository.getSomePlants(amount);
        Iterable<Roslina> properties = beep;
        for (Roslina property : properties) {
            System.out.println(property.getNazwa());
            System.out.println(property.getGleby());
            
            // TODO
        }

        //return plantRepository.getSomePlants(amount);



        return plantRepository.getSomePlants(2);
    }

    public Optional<Roslina> getById(Long id) {
        return plantRepository.findById(id);
    }

}
