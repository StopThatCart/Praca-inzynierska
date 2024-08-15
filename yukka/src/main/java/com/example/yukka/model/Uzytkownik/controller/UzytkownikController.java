package com.example.yukka.model.uzytkownik.controller;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rest/neo4j/uzytkownicy")
@RequiredArgsConstructor
public class UzytkownikController {
    @Autowired
    UzytkownikService uzytkownikService;

    // dodawanie użytkowników jest w authorization. Za to dodawanie pracowników będzie u admina
    @GetMapping
    public Collection<Uzytkownik> getAllUsers() {
        return uzytkownikService.getAllUsers();
    }
    
    @GetMapping("/{email}")
    public Optional<Uzytkownik> getByEmail(@PathVariable("email") String email) {
        return uzytkownikService.dawajEmailDeklu(email);
    }

    // TODO: Blokowanie jak użytownik jest zbanowany
    @PatchMapping("/{email}/ban/{ban}")
    public ResponseEntity<Uzytkownik> setBanUzytkownik(@PathVariable("email") String email, 
    @PathVariable("ban") boolean ban, Authentication currentUser) {
        return ResponseEntity.ok(uzytkownikService.setBanUzytkownik(email, currentUser, ban));
    }

    @DeleteMapping("/{email}")
    public void remove(@PathVariable("email") String email, Authentication currentUser) {
        uzytkownikService.remove(email, currentUser);
    }
}
