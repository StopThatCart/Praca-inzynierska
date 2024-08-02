package com.example.yukka.model.roslina;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<Roslina> getById(@PathVariable Integer id) {
        Optional<Roslina> roslina = roslinaService.findById((long) id);

        if (roslina.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(roslina.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> saveRoslina(
            @Valid @RequestBody RoslinaRequest request) {
        Optional<Roslina> roslina = roslinaService.findByNazwaLacinska(request.getNazwaLacinska());
        if (roslina.isPresent()) {
            return ResponseEntity.ok("Roślina o takiej nazwie łacińskiej już istnieje.");
        }

        roslinaService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Roślina została pomyślnie dodana.");
    }
    
    @DeleteMapping("/{nazwaLacinska}")
    public String deleteRoslina(@PathVariable String nazwaLacinska) {
        Optional<Roslina> roslina = roslinaService.findByNazwaLacinska(nazwaLacinska);

        if (roslina.isEmpty()) {
            return "Nie znaleziono rośliny o nazwie łacińskiej - " + nazwaLacinska;
        }

        roslinaService.deleteByNazwaLacinska(nazwaLacinska);;

        return "Usunięto rośline o nazwie łacińskiej - " + nazwaLacinska;
    }
    
    

}
