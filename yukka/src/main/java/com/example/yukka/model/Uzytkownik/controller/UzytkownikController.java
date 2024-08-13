package com.example.yukka.model.uzytkownik.controller;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rest/neo4j/uzytkownicy")
@RequiredArgsConstructor
public class UzytkownikController {
    @Autowired
    UzytkownikService userDetailsServiceImpl;

    @GetMapping
    public Collection<Uzytkownik> getAllUsers() {
        return userDetailsServiceImpl.getAllUsers();
    }

    // TODO: szukanie po emailu
    @GetMapping("/email/{email}")
    public Optional<Uzytkownik> getByEmail(@PathVariable("email") String email) {
        return userDetailsServiceImpl.dawajEmailDeklu(email);
    }

    // TODO: ban
    @PutMapping("/ban/{email}")
    public ResponseEntity<String> banUzytkownik(@PathVariable("email") String email, Authentication connectedUser) {
        Uzytkownik uzyt = userDetailsServiceImpl.dawajEmailDeklu(email).get();
        var us = (Uzytkownik) connectedUser.getPrincipal();
        if(uzyt.getEmail().equals(us.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nie można banować samego siebie.");
        }

        if(uzyt.isBan()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Użytkownik jest już zbanowany.");
        }

        if(uzyt.isAdmin() || uzyt.isPracownik()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admini i pracownicy nie mogą być banowani.");
        }

        userDetailsServiceImpl.banUzytkownik(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Użytkownik został pomyślnie zbanowany.");
    }
}
