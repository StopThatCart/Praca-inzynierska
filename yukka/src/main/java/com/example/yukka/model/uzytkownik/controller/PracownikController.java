package com.example.yukka.model.uzytkownik.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.auth.requests.BanRequest;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("pracownicy")
@RequiredArgsConstructor
@Tag(name = "Pracownik")
public class PracownikController {
    //private final UzytkownikService uzytkownikService;
    private final PracownikService pracownikService;

    @PatchMapping(value = "/ban", consumes="multipart/form-data", produces="application/json")
    public ResponseEntity<Boolean> setBanUzytkownik(@Valid @RequestPart("request") BanRequest request, 
    Authentication currentUser) {
        return ResponseEntity.ok(pracownikService.setBanUzytkownik(request, currentUser));
    }

}
