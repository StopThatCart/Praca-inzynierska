package com.example.yukka.model.dzialka.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.model.dzialka.Dzialka;
import com.example.yukka.model.dzialka.DzialkaRoslinaRequest;
import com.example.yukka.model.dzialka.service.DzialkaService;
import com.example.yukka.model.dzialka.service.ZasadzonaNaService;

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


    @GetMapping("/{numer}/uzytkownicy/{nazwa}")
    public Optional<Dzialka> getDzialkaOfUzytkownikByNumer(@PathVariable int numer, 
    @PathVariable String nazwa, Authentication connectedUser) {
        return dzialkaService.getDzialkaOfUzytkownikByNumer(numer, nazwa, connectedUser);
    }

    @GetMapping("/{numer}")
    public Optional<Dzialka> getDzialkaByNumer(@PathVariable int numer, Authentication connectedUser) {
        return dzialkaService.getDzialkaByNumer(numer, connectedUser);
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

    @DeleteMapping("/rosliny/{id}")
    public void deleteRoslinaFromDzialka(@PathVariable Long id) {
        zasadzonaNaService.deleteZasadzonaNa(id);
    }
}
