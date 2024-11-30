package com.example.yukka.model.dzialka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.example.yukka.common.FileResponse;
import com.example.yukka.model.dzialka.DzialkaResponse;
import com.example.yukka.model.dzialka.requests.BaseDzialkaRequest;
import com.example.yukka.model.dzialka.requests.DzialkaRoslinaRequest;
import com.example.yukka.model.dzialka.requests.MoveRoslinaRequest;
import com.example.yukka.model.dzialka.service.DzialkaService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("dzialki")
@Tag(name = "Dzialka")
public class DzialkaController {
@Autowired
    private DzialkaService dzialkaService;

    @GetMapping(produces="application/json")
    public ResponseEntity<List<DzialkaResponse>> getDzialki(Authentication connectedUser) {
        return ResponseEntity.ok(dzialkaService.getDzialki(connectedUser));
    }

    @GetMapping(value = "/uzytkownicy/{uzytkownik-nazwa}", produces="application/json")
    public ResponseEntity<List<DzialkaResponse>> getDzialkiOfUzytkownik(@PathVariable("uzytkownik-nazwa") String nazwa, 
    Authentication connectedUser) {
        return ResponseEntity.ok(dzialkaService.getDzialkiOfUzytkownik(nazwa, connectedUser));
    }

    @GetMapping(value = "/pozycje", produces="application/json")
    public ResponseEntity<List<DzialkaResponse>> getPozycjeInDzialki(Authentication connectedUser) {
        return ResponseEntity.ok(dzialkaService.getPozycjeInDzialki(connectedUser));
    }
    
    @GetMapping(value = "/{numer}", produces="application/json")
    public ResponseEntity<DzialkaResponse> getDzialkaByNumer(@PathVariable("numer") int numer, Authentication connectedUser) {
        return ResponseEntity.ok(dzialkaService.getDzialkaByNumer(numer, connectedUser));
    }

    @GetMapping(value = "/{numer}/uzytkownicy/{uzytkownik-nazwa}", produces="application/json")
    public ResponseEntity<DzialkaResponse> getDzialkaOfUzytkownikByNumer(@PathVariable("numer") int numer, 
    @PathVariable("uzytkownik-nazwa") String nazwa, Authentication connectedUser) {
        return ResponseEntity.ok(dzialkaService.getDzialkaOfUzytkownikByNumer(numer, nazwa, connectedUser));
    }

    // @PostMapping(value = "/rosliny", consumes="application/json", produces="application/json")
    // public ResponseEntity<DzialkaResponse> saveRoslinaToDzialka(@Valid @RequestBody DzialkaRoslinaRequest request, 
    // Authentication connectedUser) {
    //     return ResponseEntity.status(HttpStatus.CREATED).body(dzialkaService.saveRoslinaToDzialka(request, null, null, connectedUser));
    // }

    @PostMapping(value = "/rosliny", consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<DzialkaResponse> saveRoslinaToDzialka(@Valid @RequestPart("request") DzialkaRoslinaRequest request, 
    @Parameter() @RequestPart(value = "obraz", required = false) MultipartFile obraz,
    @Parameter() @RequestPart(value = "tekstura", required = false)  MultipartFile tekstura,
     Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dzialkaService.saveRoslinaToDzialka(request, obraz, tekstura, connectedUser));
    }


    @PatchMapping(value = "/rosliny/pozycja", consumes="application/json", produces="application/json")
    public ResponseEntity<DzialkaResponse> updateRoslinaPozycjaInDzialka(@Valid @RequestBody MoveRoslinaRequest request, 
    Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(dzialkaService.updateRoslinaPozycjaInDzialka(request, connectedUser));
    }


    @PatchMapping(value = "/rosliny/kolor", consumes="application/json", produces="application/json")
    public ResponseEntity<DzialkaResponse> updateRoslinaKolorInDzialka(@Valid @RequestBody DzialkaRoslinaRequest request,
        Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(dzialkaService.updateRoslinaKolorInDzialka(request, connectedUser));
    }

    @PatchMapping(value = "/rosliny/obraz", consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<FileResponse> updateRoslinaObrazInDzialka(@Valid@RequestPart("request") DzialkaRoslinaRequest request,
        @Parameter() @RequestPart(value = "obraz", required = false) MultipartFile obraz,
        @Parameter() @RequestPart(value = "tekstura", required = false)  MultipartFile tekstura,
        Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(dzialkaService.updateRoslinaObrazInDzialka(request, obraz, tekstura, connectedUser));
    }

    @PatchMapping(value = "/rosliny/wyswietlanie", consumes = "application/json", produces="application/json")
    public ResponseEntity<DzialkaResponse> updateRoslinaWyswietlanieInDzialka(@Valid @RequestBody DzialkaRoslinaRequest request,
        Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(dzialkaService.updateRoslinaWyswietlanieInDzialka(request, connectedUser));
    }

    @PatchMapping(value = "/rosliny/notatka", consumes = "application/json", produces="application/json")
    public ResponseEntity<DzialkaResponse> updateRoslinaNotatkaInDzialka(@Valid @RequestBody DzialkaRoslinaRequest request,
        Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(dzialkaService.updateRoslinaNotatkaInDzialka(request, connectedUser));
    }

    @DeleteMapping(value = "/rosliny", consumes="application/json", produces="application/json")
    public ResponseEntity<String> deleteRoslinaFromDzialka(@Valid @RequestBody BaseDzialkaRequest request, Authentication connectedUser) {
        dzialkaService.deleteRoslinaFromDzialka(request, connectedUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/rosliny/obraz", consumes="application/json", produces="application/json")
    public ResponseEntity<String> deleteRoslinaObrazFromDzialka(@Valid @RequestBody BaseDzialkaRequest request, Authentication connectedUser) {
        dzialkaService.deleteRoslinaObrazInDzialka(request, connectedUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/rosliny/tekstura", consumes="application/json", produces="application/json")
    public ResponseEntity<String> deleteRoslinaTeksturaFromDzialka(@Valid @RequestBody BaseDzialkaRequest request, Authentication connectedUser) {
        dzialkaService.deleteRoslinaTeksturaInDzialka(request, connectedUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
