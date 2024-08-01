package com.example.yukka.model.roslina;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/rest/neo4j/roslina")
public class RoslinaController {

    @Autowired
    RoslinaService roslinaService;

    @GetMapping
    public Collection<Roslina> getSome() {
        System.out.println("COOOOOOOOOOOOOOOOOOOOOOOO\n\n\n\n\n\n");
        int amount = 1;
        return roslinaService.getSome(amount);
    }

    @GetMapping("/{id}")
    public Optional<Roslina> getById(@PathVariable Integer id) {
        return roslinaService.getById((long) id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Roslina saveRoslina(
            @Valid @RequestBody RoslinaRequest request) {
        return roslinaService.save(request);
    }
    

}
