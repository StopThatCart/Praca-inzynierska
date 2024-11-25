package com.example.yukka.model.social.controller;


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
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.powiadomienie.PowiadomienieDTO;
import com.example.yukka.model.social.powiadomienie.PowiadomienieResponse;
import com.example.yukka.model.social.request.ZgloszenieRequest;
import com.example.yukka.model.social.service.PowiadomienieService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("powiadomienia")
@RequiredArgsConstructor
@Tag(name = "Powiadomienie")
public class PowiadomienieController {
    private final PowiadomienieService powiadomienieService;

    @GetMapping(produces="application/json")
    public ResponseEntity<PageResponse<PowiadomienieResponse>> getPowiadomienia(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
        Authentication connectedUser) {
        return ResponseEntity.ok(powiadomienieService.findPowiadomieniaOfUzytkownik(page, size, connectedUser));
    }

    @GetMapping(value = "/nieprzeczytane/count", produces="application/json")
    public ResponseEntity<Integer> getNieprzeczytaneCountOfUzytkownik(Authentication connectedUser) {
        return ResponseEntity.ok(powiadomienieService.getNieprzeczytaneCountOfUzytkownik(connectedUser));
    }

    @PostMapping(value = "/admin", produces="application/json")
    public ResponseEntity<?> sendSpecjalnePowiadomienieToPracownicy(@RequestBody PowiadomienieDTO powiadomienieDTO) {
        powiadomienieService.addSpecjalnePowiadomienieToPracownicy(powiadomienieDTO);
        return ResponseEntity.ok().build();
    }   

    @PostMapping(value = "/pracownik", produces="application/json")
    public ResponseEntity<?> sendSpecjalnePowiadomienie(@RequestBody PowiadomienieDTO powiadomienieDTO) {
        powiadomienieService.addSpecjalnePowiadomienie(powiadomienieDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/zgloszenie", produces="application/json")
    public ResponseEntity<?> sendZgloszenie(@Valid @RequestBody ZgloszenieRequest request, Authentication connectedUser) {
        powiadomienieService.sendZgloszenie(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{id}/przeczytane", produces="application/json")
    public ResponseEntity<PowiadomienieResponse> setPowiadomieniePrzeczytane(@PathVariable("id") Long id, Authentication connectedUser) {
        return ResponseEntity.ok(powiadomienieService.setPrzeczytane(id, connectedUser));
    }

    @PatchMapping(value = "/przeczytane", produces="application/json")
    public ResponseEntity<?> setAllPowiadomieniaPrzeczytane(Authentication connectedUser) {
        powiadomienieService.setAllPrzeczytane(connectedUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}", produces="application/json")
    public ResponseEntity<?> remove(@PathVariable("id") Long id, Authentication connectedUser) {
        powiadomienieService.remove(id, connectedUser);
        return ResponseEntity.noContent().build();
    }

}
