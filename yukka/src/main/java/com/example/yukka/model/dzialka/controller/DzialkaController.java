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

import com.example.yukka.model.dzialka.DzialkaResponse;
import com.example.yukka.model.dzialka.DzialkaRoslinaRequest;
import com.example.yukka.model.dzialka.MoveRoslinaRequest;
import com.example.yukka.model.dzialka.service.DzialkaService;
import com.example.yukka.model.dzialka.service.ZasadzonaNaService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("rest/neo4j/dzialki")
@Tag(name = "Dzialka")
public class DzialkaController {
@Autowired
    private DzialkaService dzialkaService;

    @Autowired
    private ZasadzonaNaService zasadzonaNaService;


    @GetMapping
    public ResponseEntity<List<DzialkaResponse>> getDzialki(Authentication connectedUser) {
        return ResponseEntity.ok(dzialkaService.getDzialki(connectedUser));
    }

    @GetMapping("/uzytkownicy/{nazwa}")
    public ResponseEntity<List<DzialkaResponse>> getDzialkiOfUzytkownik(@PathVariable String nazwa, Authentication connectedUser) {
        return ResponseEntity.ok(dzialkaService.getDzialkiOfUzytkownik(nazwa, connectedUser));
    }

    @GetMapping("/{numer}")
    public ResponseEntity<DzialkaResponse> getDzialkaByNumer(@PathVariable int numer, Authentication connectedUser) {
        return ResponseEntity.ok(dzialkaService.getDzialkaByNumer(numer, connectedUser));
    }

    @GetMapping("/{numer}/uzytkownicy/{nazwa}")
    public ResponseEntity<DzialkaResponse> getDzialkaOfUzytkownikByNumer(@PathVariable int numer, 
    @PathVariable String nazwa) {
        return ResponseEntity.ok(dzialkaService.getDzialkaOfUzytkownikByNumer(numer, nazwa));
    }

    /* 
    @DeleteMapping("/{id}")
    public void deleteDzialka(@PathVariable Long id) {
        dzialkaService.deleteDzialka(id);
    }
*/
    @PostMapping("/rosliny")
    public ResponseEntity<DzialkaResponse> saveRoslinaToDzialka(@Valid @RequestBody DzialkaRoslinaRequest request, Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dzialkaService.saveRoslinaToDzialka(request, connectedUser));
    }

    @PatchMapping(value = "/rosliny/pozycja", consumes = "multipart/form-data")
    public ResponseEntity<DzialkaResponse> updateRoslinaPositionInDzialka(@Valid @RequestBody MoveRoslinaRequest request, 
    Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(dzialkaService.updateRoslinaPositionInDzialka(request, connectedUser));
    }

    @PatchMapping(value = "/rosliny/obraz", consumes = "multipart/form-data")
    public ResponseEntity<DzialkaResponse> updateRoslinaObrazInDzialka(@Valid @RequestBody DzialkaRoslinaRequest request,
        @Parameter() @RequestPart("file") MultipartFile file,
        Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(dzialkaService.updateRoslinaObrazInDzialka(request, file, connectedUser));
    }

    @DeleteMapping("/rosliny")
    public ResponseEntity<String> deleteRoslinaFromDzialka(@Valid @RequestBody DzialkaRoslinaRequest request, Authentication connectedUser) {
        dzialkaService.deleteRoslinaFromDzialka(request, connectedUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/rosliny/obraz")
    public ResponseEntity<String> deleteRoslinaObrazFromDzialka(@Valid @RequestBody DzialkaRoslinaRequest request, Authentication connectedUser) {
        dzialkaService.deleteRoslinaObrazInDzialka(request, connectedUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
