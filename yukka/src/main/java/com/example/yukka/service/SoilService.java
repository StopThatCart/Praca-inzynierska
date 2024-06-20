package com.example.yukka.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.yukka.model.Soil;
import com.example.yukka.repository.SoilRepository;

@Service
public class SoilService {

    @Autowired
    SoilRepository soilRepository;

    public Collection<Soil> getAllSoils() {
        return soilRepository.getAllSoils();
    }

}
