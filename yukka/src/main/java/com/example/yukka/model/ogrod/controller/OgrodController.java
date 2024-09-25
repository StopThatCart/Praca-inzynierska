
package com.example.yukka.model.ogrod.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.model.dzialka.DzialkaResponse;
import com.example.yukka.model.ogrod.OgrodResponse;
import com.example.yukka.model.ogrod.service.OgrodService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("ogrody")
@Tag(name = "Ogród")
@RequiredArgsConstructor
public class OgrodController {
    private final OgrodService ogrodService;

    @GetMapping(value = "/{uzytkownik-nazwa}", produces="application/json")
    public ResponseEntity<OgrodResponse> getDzialki(@PathVariable("uzytkownik-nazwa") String uzytkownikNazwa, 
    Authentication connectedUser) {
        return ResponseEntity.ok(ogrodService.getOgrodOfUzytkownik(uzytkownikNazwa, connectedUser));
    }

    @PatchMapping(value = "/{ogrod-nazwa}", produces="application/json")
    public ResponseEntity<String> setOgrodNazwa(@PathVariable("ogrod-nazwa") String ogrodNazwa, 
    Authentication connectedUser) {
        return ResponseEntity.ok(ogrodService.setOgrodNazwa(ogrodNazwa, connectedUser));
    }

}
