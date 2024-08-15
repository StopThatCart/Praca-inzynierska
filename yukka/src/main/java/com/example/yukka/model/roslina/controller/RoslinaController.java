package com.example.yukka.model.roslina.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.RoslinaRequest;
import com.example.yukka.model.roslina.RoslinaResponse;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/rest/neo4j/rosliny")
@Tag(name = "Post")
public class RoslinaController {

    @Autowired
    RoslinaService roslinaService;


    @GetMapping
    public ResponseEntity<PageResponse<RoslinaResponse>> findAllPosty(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(roslinaService.findAllRosliny(page, size));
    }
/* 
    @GetMapping
    public Collection<Roslina> getSome() {
        System.out.println("COOOOOOOOOOOOOOOOOOOOOOOO\n\n\n\n\n\n");
        int amount = 1;
        return roslinaService.getSome(amount);
    }
*/
    @GetMapping("/{id}")
    public ResponseEntity<Roslina> getById(@PathVariable Integer id) {
        Optional<Roslina> roslina = roslinaService.findById((long) id);

        if (roslina.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(roslina.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> saveRoslina(@Valid @RequestBody RoslinaRequest request) {
        Optional<Roslina> roslina = roslinaService.findByNazwaLacinska(request.getNazwaLacinska());
        if (roslina.isPresent()) {
            System.out.println("\n\n\nBITCH IS NOT PRESENT\n\n\n");
            return ResponseEntity.ok("Roślina o takiej nazwie łacińskiej już istnieje.");
        }

        roslinaService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Roślina została pomyślnie dodana.");
    }

    @PutMapping
    public ResponseEntity<String> updateRoslina(@Valid @RequestBody RoslinaRequest request) {
        Optional<Roslina> roslina = roslinaService.findByNazwaLacinska(request.getNazwaLacinska());
        if (roslina.isEmpty()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Roślina o takiej nazwie łacińskiej już istnieje.");
        }
        roslinaService.update(request);
        return ResponseEntity.status(HttpStatus.OK).body("Roślina została pomyślnie zaktualizowana.");
    }
    
    
    @DeleteMapping("/{nazwaLacinska}")
    public ResponseEntity<String> deleteRoslina(@PathVariable String nazwaLacinska) {
        Optional<Roslina> roslina = roslinaService.findByNazwaLacinska(nazwaLacinska);

        if (roslina.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono rośliny o nazwie łacińskiej - " + nazwaLacinska);
        }

        roslinaService.deleteByNazwaLacinska(nazwaLacinska);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usunięto rośline o nazwie łacińskiej - " + nazwaLacinska);
    }
    

    // TODO: Przetestować to jak już będzie podstawowy panel. Dodatkowo obsługa usuwania starego obrazu po zmianie obrazu
    @PostMapping(value = "/obraz/{nazwaLacinska}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCoverPicture(
            @PathVariable("nazwaLacinska") String nazwaLacinska, 
            @Parameter() @RequestPart("file") 
            MultipartFile file, 
            Authentication connectedUser) {
        roslinaService.uploadRoslinaObraz(file, connectedUser, nazwaLacinska);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    

}
