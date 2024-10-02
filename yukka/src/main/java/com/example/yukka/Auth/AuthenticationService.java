/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.yukka.auth;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;
import com.example.yukka.security.JwtService;
import com.example.yukka.security.Neo4JAuthenticationProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
class AuthenticationService {
    private final UzytkownikRepository uzytkownikRepository;
    private final UzytkownikService uzytkownikService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Neo4JAuthenticationProvider neo4jAuthenticationProvider;

//    @Value("${application.mailing.frontend.activation-url}")
//    private String activationUrl;

    @Value("${uzytkownik.obraz.default.name}")
    private  String defaultAvatarObrazName;

    public void register(RegistrationRequest request) {
        if (uzytkownikRepository.checkIfUzytkownikExists(request.getNazwa(), request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Użytkownik o podanej nazwie lub adresie e-mail już istnieje.");
        }
        //var userRole = ROLE.Uzytkownik.toString();
        System.out.println("\n\n\n Request: " + request.toString() + "\n\n\n");
        //System.out.println("\n\n\n Request: " + request.getNazwa() + "\n\n\n");
        Uzytkownik user = Uzytkownik.builder()
                .uzytId(createUzytkownikId())
                .nazwa(request.getNazwa())
                .email(request.getEmail())
                .haslo(passwordEncoder.encode(request.getHaslo()))
                .avatar(defaultAvatarObrazName)
              //  .createdDate(LocalDateTime.now())
                // .banned(false)
                 //.labels(List.of(userRole))
                .build();
        
       // Ustawienia ust = Ustawienia.builder().build();

        uzytkownikService.addUzytkownik(user);

    
        //uzytkownikRepository.addNewUzytkownik(user);
        //sendValidationEmail(user);
    }

    public AuthenticationResponse authenticate(AuthRequest request) {
        
        System.out.println("\n\n\n SAAAAAAAA: " + request.toString() + "\n\n\n");
        /* 
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getHaslo())
        );*/
        Authentication auth = neo4jAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getHaslo()));
        // Z poniższym authenticate wywołuje się dwa razy
       // Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getHaslo()));

        System.out.println("Auth: " + auth.toString());
        
        var claims = new HashMap<String, Object>();

        Uzytkownik uzyt = ((Uzytkownik) auth.getPrincipal());
        claims.put("UzytId", uzyt.getUzytId());
        claims.put("Nazwa", uzyt.getUsername());
        claims.put("Email", uzyt.getEmail());
        claims.put("Avatar", uzyt.getAvatar());
        
       // claims.put("authorities", user.getAuthorities()); // To już jest robione w JwtService

        /* */
        var jwtToken = jwtService.generateToken(claims, (Uzytkownik) auth.getPrincipal());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse refreshToken(String token) {
    if (jwtService.isTokenExpired(token)) {
        throw new IllegalArgumentException("Token JWT wygasł.");
    }
    String username = jwtService.extractUsername(token);
    UserDetails userDetails = uzytkownikService.loadUserByUsername(username);
    String newToken = jwtService.generateToken(userDetails);
    return AuthenticationResponse.builder()
            .token(newToken)
            .build();
    }

    String createUzytkownikId() {
        String resultId = UUID.randomUUID().toString();
        do { 
            Optional<Uzytkownik> uzyt = uzytkownikRepository.findByUzytId(resultId);
            if(uzyt.isEmpty()){
                break;
            }
            resultId = UUID.randomUUID().toString();
        } while (true);
        return resultId;
    }

}
