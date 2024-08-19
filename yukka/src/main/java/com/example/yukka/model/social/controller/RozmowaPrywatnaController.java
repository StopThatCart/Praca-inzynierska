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
@RequestMapping("/rest/neo4j/rozmowy")
public class RozmowaPrywatnaController {

    @Autowired
    private RozmowaPrywatnaService rozmowaPrywatnaService;
    

    @GetMapping
    public ResponseEntity<PageResponse<RozmowaPrywatnaResponse>> findRozmowyPrywatneOfUzytkownik(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
        Authentication connectedUser) {
        PageResponse<RozmowaPrywatnaResponse> rozmowy = rozmowaPrywatnaService.findRozmowyPrywatneOfUzytkownik(page, size, connectedUser);
        return ResponseEntity.ok(rozmowy);
    }

    @GetMapping("/{odbiorca-uzyt-id}")
    public ResponseEntity<RozmowaPrywatnaResponse> getRozmowaPrywatna(@PathVariable String odbiorcaId, Authentication connectedUser) {
        RozmowaPrywatnaResponse rozmowa = rozmowaPrywatnaService.findRozmowaPrywatna(odbiorcaId, connectedUser);
        return ResponseEntity.ok(rozmowa);
    }

    @PostMapping("/{odbiorca-uzyt-id}")
    public ResponseEntity<RozmowaPrywatna> inviteToRozmowaPrywatna(@PathVariable String odbiorcaId, Authentication connectedUser) {
        RozmowaPrywatna rozmowa = rozmowaPrywatnaService.inviteToRozmowaPrywatna(odbiorcaId, connectedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(rozmowa);
    }

    @PutMapping("/{nadawca-uzyt-id}/accept")
    public ResponseEntity<RozmowaPrywatna> acceptRozmowaPrywatna(@PathVariable String nadawcaId, Authentication connectedUser) {
        RozmowaPrywatna rozmowa = rozmowaPrywatnaService.acceptRozmowaPrywatna(nadawcaId, connectedUser);
        return ResponseEntity.ok(rozmowa);
    }

    @PutMapping("/{nadawca-uzyt-id}/reject")
    public ResponseEntity<RozmowaPrywatna> rejectRozmowaPrywatna(@PathVariable String nadawcaId, Authentication connectedUser) {
        rozmowaPrywatnaService.rejectRozmowaPrywatna(nadawcaId, connectedUser);
        return ResponseEntity.ok().build();
    }
}