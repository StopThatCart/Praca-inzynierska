package com.example.yukka.model.uzytkownik.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.model.social.powiadomienie.Powiadomienie;
import com.example.yukka.model.social.powiadomienie.PowiadomienieDTO;
import com.example.yukka.model.social.service.PowiadomienieService;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.UzytkownikResponse;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rest/neo4j/uzytkownicy")
@RequiredArgsConstructor
@Tag(name = "Uzytkownik")
public class UzytkownikController {
    @Autowired
    UzytkownikService uzytkownikService;
    @Autowired
    PowiadomienieService powiadomienieService;

    // dodawanie użytkowników jest w authorization. Za to dodawanie pracowników będzie u admina
    @GetMapping
    public List<Uzytkownik> findAllUzytkownicy() {
        return uzytkownikService.findAll();
    }
    
    @GetMapping("/{email}")
    public ResponseEntity<UzytkownikResponse> getByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(uzytkownikService.findByEmail(email));
    }

    @PatchMapping(value = "/avatar", consumes = "multipart/form-data")
    public ResponseEntity<Uzytkownik> updateAvatar(@Parameter() @RequestPart("file") MultipartFile file, Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.updateUzytkownikAvatar(file, connectedUser));
    }


    // TODO: Blokowanie jak użytownik jest zbanowany
    @PatchMapping("pracownik/ban/{email}/{ban}")
    public ResponseEntity<Uzytkownik> setBanUzytkownik(@PathVariable("email") String email, 
    @PathVariable("ban") boolean ban, Authentication currentUser) {
        return ResponseEntity.ok(uzytkownikService.setBanUzytkownik(email, currentUser, ban));
    }


    @PostMapping("admin/powiadomienie")
    public ResponseEntity<Powiadomienie> sendSpecjalnePowiadomienieToPracownicy(@RequestBody PowiadomienieDTO powiadomienieDTO) {
        return ResponseEntity.ok(powiadomienieService.addSpecjalnePowiadomienieToPracownicy(powiadomienieDTO));
    }

    @PostMapping("pracownik/powiadomienie")
    public ResponseEntity<Powiadomienie> sendSpecjalnePowiadomienie(@RequestBody PowiadomienieDTO powiadomienieDTO) {
        return ResponseEntity.ok(powiadomienieService.addSpecjalnePowiadomienie(powiadomienieDTO));
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
