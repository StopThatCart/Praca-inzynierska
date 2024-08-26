package com.example.yukka.model.dzialka.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.yukka.model.dzialka.Dzialka;
import com.example.yukka.model.dzialka.DzialkaRoslinaRequest;
import com.example.yukka.model.dzialka.service.DzialkaService;
import com.example.yukka.model.dzialka.service.ZasadzonaNaService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/dzialki")
@Tag(name = "Dzialka")
public class DzialkaController {
@Autowired
    private DzialkaService dzialkaService;

    @Autowired
    private ZasadzonaNaService zasadzonaNaService;


    @GetMapping
    public List<Dzialka> getDzialki(Authentication connectedUser) {
        return dzialkaService.getDzialki(connectedUser);
    }

    @GetMapping("/uzytkownicy/{nazwa}")
    public List<Dzialka> getDzialkiOfUzytkownik(@PathVariable String nazwa, Authentication connectedUser) {
        return dzialkaService.getDzialkiOfUzytkownik(nazwa, connectedUser);
    }

    @GetMapping("/{numer}")
    public Optional<Dzialka> getDzialkaByNumer(@PathVariable int numer, Authentication connectedUser) {
        return dzialkaService.getDzialkaByNumer(numer, connectedUser);
    }

    @GetMapping("/{numer}/uzytkownicy/{nazwa}")
    public Optional<Dzialka> getDzialkaOfUzytkownikByNumer(@PathVariable int numer, 
    @PathVariable String nazwa) {
        return dzialkaService.getDzialkaOfUzytkownikByNumer(numer, nazwa);
    }

    

    /* 
    @DeleteMapping("/{id}")
    public void deleteDzialka(@PathVariable Long id) {
        dzialkaService.deleteDzialka(id);
    }
*/
    @PostMapping("/rosliny")
    public Dzialka saveRoslinaToDzialka(@Valid @RequestBody DzialkaRoslinaRequest request, Authentication connectedUser) {
        return dzialkaService.saveRoslinaToDzialka(request, connectedUser);
    }

    @PatchMapping(value = "/rosliny/obraz", consumes = "multipart/form-data")
    public Dzialka updateRoslinaObrazInDzialka(@Valid @RequestBody DzialkaRoslinaRequest request,
        @Parameter() @RequestPart("file") MultipartFile file,
        Authentication connectedUser) {
        return dzialkaService.updateRoslinaObrazInDzialka(request, file, connectedUser);
    }

    @DeleteMapping("/rosliny")
    public void deleteRoslinaFromDzialka(@Valid @RequestBody DzialkaRoslinaRequest request, Authentication connectedUser) {
        dzialkaService.deleteRoslinaFromDzialka(request, connectedUser);
    }

    @DeleteMapping("/rosliny/obraz")
    public void deleteRoslinaObrazFromDzialka(@Valid @RequestBody DzialkaRoslinaRequest request, Authentication connectedUser) {
        dzialkaService.deleteRoslinaObrazInDzialka(request, connectedUser);
    }
}
