package com.example.yukka.resource;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.model.plants.Plant;
import com.example.yukka.service.PlantService;


@RestController
@RequestMapping("/rest/neo4j/plant")
public class PlantResource {

    @Autowired
    PlantService plantService;

    @GetMapping
    public Collection<Plant> getSome() {
        return plantService.getSome(10);
    }
    

}
