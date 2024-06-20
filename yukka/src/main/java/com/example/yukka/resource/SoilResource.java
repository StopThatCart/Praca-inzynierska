package com.example.yukka.resource;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.model.plants.relationshipnodes.Soil;
import com.example.yukka.service.SoilService;

@RestController
@RequestMapping("/rest/neo4j/soil")
public class SoilResource {
    @Autowired
    SoilService soilService;

    @GetMapping
    public Collection<Soil> getAllSoils() {
        return soilService.getAllSoils();
    }
}
