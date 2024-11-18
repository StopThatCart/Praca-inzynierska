package com.example.yukka.model.roslina.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.RoslinaResponse;
import com.example.yukka.model.roslina.UzytkownikRoslinaRequest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


/**
 * Służy do obsługi zapytań związanych z roślinami użytkownika dla samego użytkownika.
 */
@RestController
@RequestMapping("uzytkownikRosliny")
@Tag(name = "UzytkownikRoslina")
@RequiredArgsConstructor
public class UzytkownikRoslinaController {
    private final UzytkownikRoslinaService uzytkownikRoslinaService;
  //  private final UzytkownikRoslinaRepository uzytkownikRoslinaRepository;

    // @GetMapping(value = "/{roslinaId}", produces="application/json")
    // public ResponseEntity<Roslina> findUzytkownikRoslinaByRoslinaId(
    //     @PathVariable("roslinaId") String roslinaId, 
    //         Authentication connectedUser) {
    //     return ResponseEntity.ok(uzytkownikRoslinaService.findByRoslinaId(roslinaId).orElse(null));
    // }

    @GetMapping(produces="application/json")
    public ResponseEntity<PageResponse<RoslinaResponse>> findAllRosliny(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikRoslinaService.findAllRoslinyOfUzytkownik(page, size, connectedUser));
    }

    @PostMapping
    public ResponseEntity<String> saveRoslina(@Valid @RequestBody UzytkownikRoslinaRequest request,
    Authentication currentUser) {
        uzytkownikRoslinaService.save(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("Roślina została pomyślnie dodana.");
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> saveRoslina(@Valid @RequestBody UzytkownikRoslinaRequest request, 
    @Parameter() @RequestPart("file") MultipartFile file,
    Authentication currentUser) {
        uzytkownikRoslinaService.save(request, file, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("Roślina została pomyślnie dodana.");
    }

    @PatchMapping
    public ResponseEntity<String> updateRoslina(@Valid @RequestBody UzytkownikRoslinaRequest request) {
        Optional<Roslina> roslina = uzytkownikRoslinaService.findByRoslinaId(request.getRoslinaId());
        if (roslina.isEmpty()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Roślina o takiej nazwie łacińskiej już istnieje.");
        }
        uzytkownikRoslinaService.update(request);
        return ResponseEntity.status(HttpStatus.OK).body("Roślina została pomyślnie zaktualizowana.");
    }

    @PostMapping(value = "/{roslinaId}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateRoslinaObraz(
            @PathVariable("roslinaId") String roslinaId, 
            @Parameter() @RequestPart("file") 
            MultipartFile file, 
            Authentication connectedUser) {
        uzytkownikRoslinaService.uploadUzytkownikRoslinaObraz(file, connectedUser, roslinaId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    
    // @DeleteMapping("/{roslinaId}")
    // public ResponseEntity<String> deleteRoslina(@PathVariable String roslinaId) {
    //     Optional<Roslina> roslina = uzytkownikRoslinaService.findByRoslinaId(roslinaId);

    //     if (roslina.isEmpty()) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono rośliny o nazwie łacińskiej - " + roslinaId);
    //     }

    //     uzytkownikRoslinaService.deleteByRoslinaId(roslinaId);

    //     return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usunięto rośline o nazwie łacińskiej - " + roslinaId);
    // }
    
}
