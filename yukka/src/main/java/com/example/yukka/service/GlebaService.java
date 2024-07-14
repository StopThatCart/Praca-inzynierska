package com.example.yukka.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.yukka.model.plants.relationshipnodes.Gleba;
import com.example.yukka.repository.GlebaRepository;

@Service
public class GlebaService {

    @Autowired
    GlebaRepository soilRepository;

    public Collection<Gleba> getAllSoils() {
        return soilRepository.getAllSoils();
    }

}
