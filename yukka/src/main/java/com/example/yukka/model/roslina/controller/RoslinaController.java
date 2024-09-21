package com.example.yukka.model.roslina.controller;

import java.util.Optional;
import java.util.Set;

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
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.RoslinaRequest;
import com.example.yukka.model.roslina.RoslinaResponse;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscResponse;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("rosliny")
@Tag(name = "Roslina")
public class RoslinaController {

    @Autowired
    RoslinaService roslinaService;

    @Autowired
    RoslinaRepository roslinaRepository;

    @Autowired
    RoslinaMapper roslinaMapper;

    /*
    @GetMapping(produces="application/json")
    public ResponseEntity<PageResponse<RoslinaResponse>> findAllRosliny(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        return ResponseEntity.ok(roslinaService.findAllRosliny(page, size));
    }
         */

         // Pomijam valid bo tutaj się szuka rośliny i wiele parametrów może być pustych
         // Post bo nie można dawać request
    @PostMapping(value="/szukaj", produces="application/json")
    public ResponseEntity<PageResponse<RoslinaResponse>> findAllRoslinyWithParameters(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestBody RoslinaRequest request) {
        return ResponseEntity.ok(roslinaService.findAllRoslinyWithParameters(page, size, request));
    }
/* 
    @GetMapping
    public Collection<Roslina> getSome() {
        System.out.println("COOOOOOOOOOOOOOOOOOOOOOOO\n\n\n\n\n\n");
        int amount = 1;
        return roslinaService.getSome(amount);
    }
*/
    @GetMapping(value = "/wlasciwosci", produces="application/json")
    public ResponseEntity<Set<WlasciwoscResponse>> getWlasciwosciWithRelations() {
        Set<WlasciwoscResponse> response = roslinaService.getWlasciwosciWithRelations();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{nazwa-lacinska}", produces="application/json")
    public ResponseEntity<RoslinaResponse> findByNazwaLacinska(@PathVariable("nazwa-lacinska") String nazwaLacinska) {
     //   String nazwaLacinska = URLDecoder.decode(encodedNazwaLacinska, StandardCharsets.UTF_8);
        return ResponseEntity.ok(roslinaService.findByNazwaLacinska(nazwaLacinska.toLowerCase()));
    }

    @PostMapping(consumes="application/json", produces="application/json")
    public ResponseEntity<RoslinaResponse> saveRoslina(@Valid @RequestBody RoslinaRequest request,
    Authentication currentUser) {
        Roslina roslina = roslinaService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(roslinaMapper.toRoslinaResponse(roslina));
    }

    @PostMapping(consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<RoslinaResponse> saveRoslina(@Valid @RequestPart("request") RoslinaRequest request, 
    @Parameter() @RequestPart("file") MultipartFile file) {
        Roslina roslina = roslinaService.save(request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(roslinaMapper.toRoslinaResponse(roslina));
    }

    @PutMapping(produces="application/json")
    public ResponseEntity<RoslinaResponse> updateRoslina(@Valid @RequestBody RoslinaRequest request) {
        RoslinaResponse roslina = roslinaService.update(request);
        return ResponseEntity.status(HttpStatus.OK).body(roslina);
    }

    @PutMapping(value = "/{nazwa-lacinska}", consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<byte[]> updateRoslinaObraz(@PathVariable("nazwa-lacinska") String nazwaLacinska,
    @Parameter() @RequestPart("file") MultipartFile file) {
        RoslinaResponse roslina = roslinaService.uploadRoslinaObraz(nazwaLacinska, file);
        return ResponseEntity.status(HttpStatus.OK).body(roslina.getObraz());
    }
    
    
    @DeleteMapping("/{nazwaLacinska}")
    public ResponseEntity<String> deleteRoslina(@PathVariable String nazwaLacinska) {
        Optional<Roslina> roslina = roslinaRepository.findByNazwaLacinska(nazwaLacinska.toLowerCase());

        if (roslina.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono rośliny o nazwie łacińskiej - " + nazwaLacinska);
        }

        roslinaService.deleteByNazwaLacinska(nazwaLacinska.toLowerCase());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usunięto rośline o nazwie łacińskiej - " + nazwaLacinska);
    }

}
