/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.yukka.Auth;

import java.util.HashMap;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.yukka.Authorities.ROLE;
import com.example.yukka.model.Uzytkownik.Uzytkownik;
import com.example.yukka.model.Uzytkownik.UzytkownikRepository;
import com.example.yukka.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class AuthenticationService {
    private final UzytkownikRepository uzytkownikRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

//    @Value("${application.mailing.frontend.activation-url}")
//    private String activationUrl;

    public void register(RegistrationRequest request) {
        if (uzytkownikRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Użytkownik o podanym adresie e-mail już istnieje.");
        }
        var userRole = ROLE.Uzytkownik.toString();

        var user = Uzytkownik.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                 .banned(false)
                 .labels(List.of(userRole))
                .build();
        uzytkownikRepository.addNewUzytkownik(user);
        //sendValidationEmail(user);
    }

    public AuthenticationResponse authenticate(AuthRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = ((Uzytkownik) auth.getPrincipal());
        claims.put("Name", user.getName());
       // claims.put("authorities", user.getAuthorities()); // To już jest robione w JwtService

        /* */
        var jwtToken = jwtService.generateToken(claims, (Uzytkownik) auth.getPrincipal());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
