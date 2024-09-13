package com.example.yukka.model.uzytkownik.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.model.social.service.PowiadomienieService;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.UzytkownikResponse;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("uzytkownicy")
@RequiredArgsConstructor
@Tag(name = "Uzytkownik")
public class UzytkownikController {
    @Autowired
    UzytkownikService uzytkownikService;
    @Autowired
    PowiadomienieService powiadomienieService;

    // dodawanie użytkowników jest w authorization. Za to dodawanie pracowników będzie u admina
    @GetMapping(produces="application/json")
    public List<Uzytkownik> findAllUzytkownicy() {
        return uzytkownikService.findAll();
    }
    
    @GetMapping(value = "/{nazwa}", produces="application/json")
    public ResponseEntity<UzytkownikResponse> findByNazwa(@PathVariable("nazwa") String nazwa) {
        return ResponseEntity.ok(uzytkownikService.findByEmail(nazwa));
    }

    @GetMapping(value = "/{email}", produces="application/json")
    public ResponseEntity<UzytkownikResponse> findByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(uzytkownikService.findByEmail(email));
    }

    @GetMapping(value = "/avatar", produces="application/json")
    public ResponseEntity<UzytkownikResponse> getAvatar(Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.getLoggedInAvatar(connectedUser));
    }

    @PatchMapping(value = "/avatar", consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<UzytkownikResponse> updateAvatar(@Parameter() @RequestPart("file") MultipartFile file, Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.updateUzytkownikAvatar(file, connectedUser));
    }


    @PatchMapping(value = "pracownik/ban/{email}/{ban}", produces="application/json")
    public ResponseEntity<Uzytkownik> setBanUzytkownik(@PathVariable("email") String email, 
    @PathVariable("ban") boolean ban, Authentication currentUser) {
        return ResponseEntity.ok(uzytkownikService.setBanUzytkownik(email, currentUser, ban));
    }

    @DeleteMapping
    public void removeSelf(Authentication currentUser) {
        uzytkownikService.removeSelf(currentUser);
    }

    @DeleteMapping("/{email}")
    public void remove(@PathVariable("email") String email, Authentication currentUser) {
        uzytkownikService.remove(email, currentUser);
    }
}
