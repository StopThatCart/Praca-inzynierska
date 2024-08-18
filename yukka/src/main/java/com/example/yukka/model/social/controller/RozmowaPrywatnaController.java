package com.example.yukka.model.social.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatnaResponse;
import com.example.yukka.model.social.service.RozmowaPrywatnaService;

@RestController
@RequestMapping("/rozmowy")
public class RozmowaPrywatnaController {

    @Autowired
    private RozmowaPrywatnaService rozmowaPrywatnaService;
    

    @GetMapping
    public ResponseEntity<PageResponse<RozmowaPrywatnaResponse>> findRozmowyPrywatneOfUzytkownik(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
        Authentication authentication) {
        PageResponse<RozmowaPrywatnaResponse> rozmowy = rozmowaPrywatnaService.findRozmowyPrywatneOfUzytkownik(page, size, authentication);
        return ResponseEntity.ok(rozmowy);
    }

    @GetMapping("/{nazwa-odbiorcy}")
    public ResponseEntity<RozmowaPrywatnaResponse> getRozmowaPrywatna(@PathVariable String odbiorca, Authentication authentication) {
        RozmowaPrywatnaResponse rozmowa = rozmowaPrywatnaService.findRozmowaPrywatna(odbiorca, authentication);
        return ResponseEntity.ok(rozmowa);
    }

    @PostMapping("/{nazwa-odbiorcy}")
    public ResponseEntity<RozmowaPrywatna> addRozmowaPrywatna(@PathVariable String odbiorca, Authentication authentication) {
        RozmowaPrywatna rozmowa = rozmowaPrywatnaService.addRozmowaPrywatna(odbiorca, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(rozmowa);
    }

    @PutMapping("/{nazwa-odbiorcy}/accept")
    public ResponseEntity<RozmowaPrywatna> acceptRozmowaPrywatna(@PathVariable String odbiorca, Authentication authentication) {
        RozmowaPrywatna rozmowa = rozmowaPrywatnaService.acceptRozmowaPrywatna(odbiorca, authentication);
        return ResponseEntity.ok(rozmowa);
    }
}