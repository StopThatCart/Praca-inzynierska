/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.yukka.auth;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.yukka.authorities.ROLE;
import com.example.yukka.model.Uzytkownik.Uzytkownik;
import com.example.yukka.model.Uzytkownik.UzytkownikRepository;
import com.example.yukka.security.JwtService;

import org.springframework.web.bind.annotation.RequestBody;

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
        if (uzytkownikRepository.findByNameOrEmail(request.getNazwa(), request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Użytkownik o podanej nazwie lub adresie e-mail już istnieje.");
        }
        //var userRole = ROLE.Uzytkownik.toString();
        System.out.println("\n\n\n Request: " + request.toString() + "\n\n\n");
        //System.out.println("\n\n\n Request: " + request.getNazwa() + "\n\n\n");
        var user = Uzytkownik.builder()
                .nazwa(request.getNazwa())
                .email(request.getEmail())
                .haslo(passwordEncoder.encode(request.getHaslo()))
              //  .createdDate(LocalDateTime.now())
                // .banned(false)
                 //.labels(List.of(userRole))
                .build();
        System.out.println("\n\n\n Request: " + user.toString() + "\n\n\n");
        uzytkownikRepository.addNewUzytkownik(user.getNazwa(), user.getEmail(), user.getHaslo());
        //uzytkownikRepository.addNewUzytkownik(user);
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
        claims.put("Name", user.getNazwa());
       // claims.put("authorities", user.getAuthorities()); // To już jest robione w JwtService

        /* */
        var jwtToken = jwtService.generateToken(claims, (Uzytkownik) auth.getPrincipal());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
