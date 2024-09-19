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

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("rozmowy")
@Tag(name = "RozmowaPrywatna")
public class RozmowaPrywatnaController {

    @Autowired
    private RozmowaPrywatnaService rozmowaPrywatnaService;
    

    @GetMapping(produces="application/json")
    public ResponseEntity<PageResponse<RozmowaPrywatnaResponse>> findRozmowyPrywatneOfUzytkownik(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
        Authentication connectedUser) {
        PageResponse<RozmowaPrywatnaResponse> rozmowy = rozmowaPrywatnaService.findRozmowyPrywatneOfUzytkownik(page, size, connectedUser);
        return ResponseEntity.ok(rozmowy);
    }

    // Potem się poprawi
    @GetMapping(value = "/id/{id}", produces="application/json")
    public ResponseEntity<RozmowaPrywatnaResponse> getRozmowaPrywatnaById(@PathVariable("id") Long id, Authentication connectedUser) {
        RozmowaPrywatnaResponse rozmowa = rozmowaPrywatnaService.findRozmowaPrywatnaById(id, connectedUser);
        return ResponseEntity.ok(rozmowa);
    }

    @GetMapping(value = "/{uzytkownikNazwa}", produces="application/json")
    public ResponseEntity<RozmowaPrywatnaResponse> getRozmowaPrywatna(@PathVariable("uzytkownikNazwa") String nazwa, Authentication connectedUser) {
        RozmowaPrywatnaResponse rozmowa = rozmowaPrywatnaService.findRozmowaPrywatnaByNazwa(nazwa, connectedUser);
        return ResponseEntity.ok(rozmowa);
    }

    @PostMapping(value = "/{uzytkownikNazwa}", produces="application/json")
    public ResponseEntity<RozmowaPrywatnaResponse> inviteToRozmowaPrywatna(@PathVariable("uzytkownikNazwa") String nazwa, Authentication connectedUser) {
        RozmowaPrywatnaResponse rozmowa = rozmowaPrywatnaService.inviteToRozmowaPrywatna(nazwa, connectedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(rozmowa);
    }

    @PutMapping(value = "/{uzytkownikNazwa}/accept", produces="application/json")
    public ResponseEntity<RozmowaPrywatna> acceptRozmowaPrywatna(@PathVariable("uzytkownikNazwa") String nazwa, Authentication connectedUser) {
        RozmowaPrywatna rozmowa = rozmowaPrywatnaService.acceptRozmowaPrywatna(nazwa, connectedUser);
        return ResponseEntity.ok(rozmowa);
    }

    @PutMapping(value = "/{uzytkownikNazwa}/reject")
    public ResponseEntity<RozmowaPrywatna> rejectRozmowaPrywatna(@PathVariable("uzytkownikNazwa") String uzytkownikNazwa, Authentication connectedUser) {
        rozmowaPrywatnaService.rejectRozmowaPrywatna(uzytkownikNazwa, connectedUser);
        return ResponseEntity.ok().build();
    }
}