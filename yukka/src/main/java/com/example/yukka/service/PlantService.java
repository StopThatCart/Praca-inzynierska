package com.example.yukka.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.yukka.model.Plant;
import com.example.yukka.repository.PlantRepository;

@Service
public class PlantService {

    @Autowired
    PlantRepository plantRepository;

    public Collection<Plant> getSome(int amount) {
        return plantRepository.getSomePlants(amount);
    }

}
