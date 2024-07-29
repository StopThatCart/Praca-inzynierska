/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.yukka.security.Authentication;


import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.yukka.model.Uzytkownik.Uzytkownik;
import com.example.yukka.model.Uzytkownik.UzytkownikRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class AuthenticationService {

    private final UzytkownikRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegistrationRequest request) {
        var userCheck =  repository.findByEmail(request.getEmail());
        if (userCheck.isEmpty()) {
            System.out.println("No jest jakiś tam już użytkownik o tym samym adresie email.");
        }

        var user = Uzytkownik.builder()
                        .name(request.getName())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .email(request.getEmail())
                        .banned(false)
                        .labels(List.of("Uzytkownik"))
                        .build();

        repository.save(user);
        // send validation email
    }
}
