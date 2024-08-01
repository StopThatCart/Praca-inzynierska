package com.example.yukka.resource;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.model.roslina.relationshipnodes.Gleba;
import com.example.yukka.service.GlebaService;

@RestController
@RequestMapping("/rest/neo4j/gleba")
public class SoilResource {
    @Autowired
    GlebaService soilService;

    @GetMapping
    public Collection<Gleba> getAllSoils() {
        return soilService.getAllSoils();
    }
}
