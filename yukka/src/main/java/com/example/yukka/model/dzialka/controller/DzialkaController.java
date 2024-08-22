package com.example.yukka.model.dzialka.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.model.dzialka.Dzialka;
import com.example.yukka.model.dzialka.ZasadzonaNa;
import com.example.yukka.model.dzialka.service.DzialkaService;
import com.example.yukka.model.dzialka.service.ZasadzonaNaService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/dzialki")
@Tag(name = "Dzialka")
public class DzialkaController {
@Autowired
    private DzialkaService dzialkaService;

    @Autowired
    private ZasadzonaNaService zasadzonaNaService;

    @PostMapping
    public Dzialka createDzialka(@RequestBody Dzialka dzialka) {
        return dzialkaService.saveDzialka(dzialka);
    }

    @GetMapping("/{id}")
    public Optional<Dzialka> getDzialka(@PathVariable Long id) {
        return dzialkaService.getDzialkaById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDzialka(@PathVariable Long id) {
        dzialkaService.deleteDzialka(id);
    }

    @PostMapping("/{dzialkaId}/rosliny")
    public ZasadzonaNa addRoslinaToDzialka(@PathVariable Long dzialkaId, @RequestBody ZasadzonaNa zasadzonaNa) {
        Optional<Dzialka> dzialka = dzialkaService.getDzialkaById(dzialkaId);
        if (dzialka.isPresent()) {
            zasadzonaNa.setDzialka(dzialka.get());
            return zasadzonaNaService.saveZasadzonaNa(zasadzonaNa);
        }
        return null;
    }

    @DeleteMapping("/rosliny/{id}")
    public void deleteRoslinaFromDzialka(@PathVariable Long id) {
        zasadzonaNaService.deleteZasadzonaNa(id);
    }
}
