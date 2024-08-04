package com.example.yukka.model.uzytkownik;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rest/neo4j/uzytkownicy")
@RequiredArgsConstructor
public class UzytkownikResource {
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;



    @GetMapping
    public Collection<Uzytkownik> getAllUsers() {
        return userDetailsServiceImpl.getAllUsers();
    }

    // TODO: szukanie po emailu
    @GetMapping("/email")
    public Optional<Uzytkownik> getByEmail() {
        String eo = "jan@email.pl";
        return userDetailsServiceImpl.dawajEmailDeklu(eo);
        //return userDetailsServiceImpl.loadUserByUsername(eo);
    }
}
