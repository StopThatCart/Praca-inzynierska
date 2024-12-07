package com.example.yukka.model.roslina.controller;

import java.util.Set;

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
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("rosliny")
@Tag(name = "Roslina")
@RequiredArgsConstructor
public class RoslinaController {

    private final RoslinaService roslinaService;
    private final RoslinaMapper roslinaMapper;

    
    /** 
     * @return ResponseEntity<PageResponse<RoslinaResponse>>
     */
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
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @RequestBody(required = false) RoslinaRequest request) {
        return ResponseEntity.ok(roslinaService.findAllRoslinyWithParameters(page, size, request));
    }

    @GetMapping(value = "/wlasciwosci", produces="application/json")
    public ResponseEntity<Set<WlasciwoscResponse>> getWlasciwosciWithRelations() {
        Set<WlasciwoscResponse> response = roslinaService.getWlasciwosciWithRelations();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/nazwa-lacinska/{nazwa-lacinska}", produces="application/json")
    public ResponseEntity<RoslinaResponse> findByNazwaLacinska(@PathVariable("nazwa-lacinska") String nazwaLacinska) {
        return ResponseEntity.ok(roslinaService.findByNazwaLacinska(nazwaLacinska.toLowerCase()));
    }

    // @GetMapping(value = "/id/{id}", produces="application/json")
    // public ResponseEntity<RoslinaResponse> findById(@PathVariable("id") Long id) {
    //     return ResponseEntity.ok(roslinaService.findById(id));
    // }

    @GetMapping(value = "/roslina-id/{roslina-id}", produces="application/json")
    public ResponseEntity<RoslinaResponse> findByRoslinaId(@PathVariable("roslina-id") String id) {
        return ResponseEntity.ok(roslinaService.findByRoslinaId(id));
    }

    @PostMapping(consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<RoslinaResponse> saveRoslina(@Valid @RequestPart("request") RoslinaRequest request, 
    @Parameter() @RequestPart(value = "file", required = false) MultipartFile file,
    Authentication currentUser) {
        Roslina roslina = roslinaService.save(request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(roslinaMapper.toRoslinaResponse(roslina));
    }

    @PutMapping(value = "/{nazwa-lacinska}", consumes="application/json", produces="application/json")
    public ResponseEntity<RoslinaResponse> updateRoslina(@PathVariable("nazwa-lacinska") String nazwaLacinska,
            @Valid @RequestBody RoslinaRequest request) {
        RoslinaResponse roslina = roslinaService.update(nazwaLacinska, request);
        return ResponseEntity.status(HttpStatus.OK).body(roslina);
    }

    @PutMapping(value = "/{nazwa-lacinska}/obraz", consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<RoslinaResponse> updateRoslinaObraz(
            @PathVariable("nazwa-lacinska") String nazwaLacinska,
            @Parameter() @RequestPart("file") MultipartFile file) {
        RoslinaResponse roslina = roslinaService.uploadRoslinaObraz(nazwaLacinska, file);
        return ResponseEntity.status(HttpStatus.OK).body(roslina);
    }
    
    
    @DeleteMapping("/{roslina-id}")
    public ResponseEntity<String> deleteRoslina(@PathVariable("roslina-id") String roslinaId, Authentication connectedUser) {
        roslinaService.deleteByRoslinaId(roslinaId, connectedUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
