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

    @PostMapping(value = "/szukaj", produces="application/json")
    public ResponseEntity<PageResponse<RoslinaResponse>> findAllRoslinyOfUzytkownik(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @RequestBody UzytkownikRoslinaRequest request,
            @RequestParam(name = "uzytkownik-nazwa", required = false) String uzytkownikNazwa,
            Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikRoslinaService.findRoslinyOfUzytkownik(page, size, request, uzytkownikNazwa, connectedUser));
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> saveRoslina(@Valid @RequestPart("request") UzytkownikRoslinaRequest request, 
    @Parameter() @RequestPart(value = "file", required = false) MultipartFile file,
    Authentication currentUser) {
        uzytkownikRoslinaService.save(request, file, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("Roślina została pomyślnie dodana.");
    }

    @PatchMapping
    public ResponseEntity<String> updateRoslina(@Valid @RequestBody UzytkownikRoslinaRequest request, Authentication connectedUser) {
        Optional<Roslina> roslina = uzytkownikRoslinaService.findByRoslinaId(request.getRoslinaId());
        if (roslina.isEmpty()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Roślina o takiej nazwie łacińskiej już istnieje.");
        }
        uzytkownikRoslinaService.update(request, connectedUser);
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
    
}
