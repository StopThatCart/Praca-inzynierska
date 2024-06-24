package com.example.yukka.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.yukka.model.plants.Plant;
import com.example.yukka.repository.PlantRepository;

@Service
public class PlantService {

    @Autowired
    PlantRepository plantRepository;

    public Collection<Plant> getSome(int amount) {
        return plantRepository.getSomePlants(amount);
    }

    public Optional<Plant> getById(Long id) {
        return plantRepository.findById(id);
    }

}
