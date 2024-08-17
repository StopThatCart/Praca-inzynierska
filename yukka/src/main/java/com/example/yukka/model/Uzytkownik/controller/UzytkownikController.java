package com.example.yukka.model.uzytkownik.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.model.social.RozmowaPrywatna;
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
    public Collection<Uzytkownik> findAllUzytkownicy() {
        return uzytkownikService.findAllUzytkownicy();
    }
    
    @GetMapping("/{email}")
    public ResponseEntity<Uzytkownik> getByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(uzytkownikService.findByEmail(email));
    }

    @GetMapping("/rozmowaPrywatna/{otherUzytNazwa}")
    public ResponseEntity<RozmowaPrywatna> findRozmowaPrywatna(@PathVariable("otherUzytNazwa") String otherUzytNazwa, Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.findRozmowaPrywatna(otherUzytNazwa, connectedUser));
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
