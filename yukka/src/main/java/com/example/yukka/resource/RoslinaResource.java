package com.example.yukka.resource;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.model.plants.Roslina;
import com.example.yukka.service.RoslinaService;



@RestController
@RequestMapping("/rest/neo4j/roslina")
public class RoslinaResource {

    @Autowired
    RoslinaService plantService;

    @GetMapping
    public Collection<Roslina> getSome() {
        System.out.println("COOOOOOOOOOOOOOOOOOOOOOOO\n\n\n\n\n\n");
        int amount = 2;
        return plantService.getSome(amount);
    }

    @GetMapping("/{id}")
    public Optional<Roslina> getById(@PathVariable Integer id) {
        return plantService.getById((long) id);
    }
    

}
