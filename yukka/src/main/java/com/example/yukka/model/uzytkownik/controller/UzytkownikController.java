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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.auth.requests.EmailRequest;
import com.example.yukka.common.FileResponse;
import com.example.yukka.model.social.request.UstawieniaRequest;
import com.example.yukka.model.social.service.PowiadomienieService;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.UzytkownikResponse;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
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
    
    @GetMapping(value = "/nazwa/{nazwa}", produces="application/json")
    public ResponseEntity<UzytkownikResponse> findByNazwa(@PathVariable("nazwa") String nazwa) {
        return ResponseEntity.ok(uzytkownikService.findByNazwa(nazwa));
    }

    @GetMapping(value = "/email/{email}", produces="application/json")
    public ResponseEntity<UzytkownikResponse> findByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(uzytkownikService.findByEmail(email));
    }

    @GetMapping(value = "/avatar", produces="application/json")
    public ResponseEntity<FileResponse> getAvatar(Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.getLoggedInAvatar(connectedUser));
    }

    @GetMapping(value = "/ustawienia", produces="application/json")
    public ResponseEntity<UzytkownikResponse> getUstawienia(Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.getUstawienia(connectedUser));
    }

    @GetMapping(value = "/blokowani", produces="application/json")
    public ResponseEntity<UzytkownikResponse> getBlokowaniAndBlokujacy(Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.getBlokowaniAndBlokujacy(connectedUser));
    }

    @PatchMapping(value = "/ustawienia", consumes="multipart/form-data", produces="application/json")
    public ResponseEntity<UzytkownikResponse> updateUstawienia(@Valid @RequestPart("ustawienia") UstawieniaRequest ustawienia, Authentication connectedUser) {
        System.out.println("Received UstawieniaRequest: " + ustawienia);
        
        return ResponseEntity.ok(uzytkownikService.updateUstawienia(ustawienia, connectedUser));
    }

    @PostMapping(value = "/send-zmiana-email", produces="application/json")
    public ResponseEntity<?> sendZmianaEmail(@Valid @RequestBody EmailRequest request, Authentication currentUser) throws MessagingException {
        uzytkownikService.sendChangeEmail(request, currentUser);
        return ResponseEntity.accepted().build();
    }


    @PatchMapping(value = "/avatar", consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<UzytkownikResponse> updateAvatar(@Parameter() @RequestPart("file") MultipartFile file, Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.updateUzytkownikAvatar(file, connectedUser));
    }

    @PatchMapping(value = "/blok/{nazwa}/{blok}", produces="application/json")
    public ResponseEntity<Boolean> setBlokUzytkownik(@PathVariable("nazwa") String nazwa, 
            @PathVariable("blok") boolean blok, Authentication currentUser) {
        return ResponseEntity.ok(uzytkownikService.setBlokUzytkownik(nazwa, currentUser, blok));
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
