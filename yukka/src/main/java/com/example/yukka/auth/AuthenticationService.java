/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.yukka.auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yukka.auth.email.EmailService;
import com.example.yukka.auth.email.EmailTemplateName;
import com.example.yukka.auth.requests.AuthRequest;
import com.example.yukka.auth.requests.EmailRequest;
import com.example.yukka.auth.requests.HasloRequest;
import com.example.yukka.auth.requests.RegistrationRequest;
import com.example.yukka.handler.exceptions.EntityAlreadyExistsException;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.model.social.CommonMapperService;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.UzytkownikResponse;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;
import com.example.yukka.model.uzytkownik.token.Token;
import com.example.yukka.model.uzytkownik.token.TokenRepository;
import com.example.yukka.security.JwtService;
import com.example.yukka.security.Neo4JAuthenticationProvider;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final UzytkownikRepository uzytkownikRepository;
    private final UzytkownikService uzytkownikService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    //private final AuthenticationManager authenticationManager;
    private final Neo4JAuthenticationProvider neo4jAuthenticationProvider;
    private final CommonMapperService commonMapperService;



    @Value("${uzytkownik.obraz.default.name}")
    private  String defaultAvatarObrazName;

    public void register(RegistrationRequest request)  throws MessagingException {
        log.info("Rejestracja użytkownika: " + request.getNazwa());
        Optional<Uzytkownik> targetUzyt = uzytkownikRepository.checkIfUzytkownikExists(request.getNazwa(), request.getEmail());
        if (targetUzyt.isPresent()) {
            if(targetUzyt.get().isAktywowany()) {
                throw new IllegalArgumentException("Aktywny użytkownik o podanej nazwie lub adresie e-mail już istnieje.");
            } else {
                emailService.sendValidationEmail(targetUzyt.get(), EmailTemplateName.AKTYWACJA_KONTA);
                return;
            }
        }
        System.out.println("\n\n\n Request: " + request.toString() + "\n\n\n");
        Uzytkownik uzyt = Uzytkownik.builder()
                .uzytId(createUzytkownikId())
                .nazwa(request.getNazwa())
                .email(request.getEmail())
                .haslo(passwordEncoder.encode(request.getHaslo()))
                .avatar(defaultAvatarObrazName)
                .build();

        uzytkownikService.addUzytkownik(uzyt);
        emailService.sendValidationEmail(uzyt, EmailTemplateName.AKTYWACJA_KONTA);
    }

    public AuthenticationResponse authenticate(AuthRequest request) {
        log.info("Logowanie użytkownika: " + request.getEmail());

        Authentication auth = neo4jAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getHaslo()));
        // Z poniższym authenticate wywołuje się dwa razy
       // Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getHaslo()));
        
        var claims = new HashMap<String, Object>();
        
        Uzytkownik uzyt = ((Uzytkownik) auth.getPrincipal());
        claims.put("UzytId", uzyt.getUzytId());
        claims.put("Nazwa", uzyt.getUsername());
        claims.put("Email", uzyt.getEmail());
        claims.put("Avatar", uzyt.getAvatar());
        
       // claims.put("authorities", user.getAuthorities()); // To już jest robione w JwtService

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

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        log.info("Aktywacja konta tokenem: " + token);
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono tokenu aktywacyjnego"));
                
        if (savedToken.getDataWalidacji() != null) {
            throw new IllegalArgumentException("Token aktywacyjny został już użyty.");
        } else if (LocalDateTime.now().isAfter(savedToken.getDataWygasniecia())) {
            emailService.sendValidationEmail(savedToken.getUzytkownik(), EmailTemplateName.AKTYWACJA_KONTA);
            throw new IllegalArgumentException("Token aktywacyjny wygasł. Wysłano nowy token na ten adres email.");
        }  

        var uzyt = uzytkownikRepository.findByEmail(savedToken.getUzytkownik().getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika związanego z tokenem aktywacyjnym"));
        if (uzyt.isAktywowany()) {
            throw new IllegalArgumentException("Konto zostało już aktywowane.");
        }
            
        uzyt.setAktywowany(true);
        uzytkownikRepository.activate(uzyt.getEmail());

        tokenRepository.validate(token, LocalDateTime.now());
    }


    public void sendResetHasloEmail(String email) throws MessagingException {
        log.info("Wysyłanie e-maila resetowania hasła do: " + email);
        var uzyt = uzytkownikRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika z podanym adresem email"));
        emailService.sendValidationEmail(uzyt, EmailTemplateName.RESET_HASLO);
    }

    @Transactional
    public void changeHaslo(HasloRequest request) throws MessagingException {
        log.info("Zmiana hasła tokenem: " + request.getToken());
        Token savedToken = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono tokenu aktywacyjnego"));
                
        if (savedToken.getDataWalidacji() != null) {
            throw new IllegalArgumentException("Token aktywacyjny został już użyty.");
        } else if (LocalDateTime.now().isAfter(savedToken.getDataWygasniecia())) {
            emailService.sendValidationEmail(savedToken.getUzytkownik(), EmailTemplateName.RESET_HASLO);
            throw new IllegalArgumentException("Token aktywacyjny wygasł. Wysłano nowy token na ten adres email.");
        }

        var uzyt = uzytkownikRepository.findByEmail(savedToken.getUzytkownik().getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika związanego z tokenem aktywacyjnym"));

        if(passwordEncoder.matches(request.getNoweHaslo(), uzyt.getHaslo())) {
            throw new IllegalArgumentException("Nowe hasło nie może być takie samo jak stare hasło");
        }

        tokenRepository.validate(request.getToken(), LocalDateTime.now());
        uzytkownikRepository.updateHaslo(uzyt.getEmail(), passwordEncoder.encode(request.getNoweHaslo()));

        //tokenRepository.removeToken(uzyt.getEmail(), request.getToken());
    }

    public void changeEmail(String token) throws MessagingException {
        log.info("Zmiana adresu e-mail tokenem: " + token);
        Token savedToken = tokenRepository.findByToken(token, EmailTemplateName.ZMIANA_EMAIL.getName())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono tokenu aktywacyjnego"));
                
        if (savedToken.getDataWalidacji() != null) {
            throw new IllegalArgumentException("Token aktywacyjny został już użyty.");
        } else if (LocalDateTime.now().isAfter(savedToken.getDataWygasniecia())) {
            emailService.sendValidationEmail(savedToken.getUzytkownik(), EmailTemplateName.ZMIANA_EMAIL);
            throw new IllegalArgumentException("Token aktywacyjny wygasł. Wysłano nowy token na ten adres email.");
        }

        var uzyt = uzytkownikRepository.findByEmail(savedToken.getUzytkownik().getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika związanego z tokenem aktywacyjnym"));

        if(uzytkownikRepository.checkIfUzytkownikExists(null, savedToken.getNowyEmail()).isPresent()) {
            throw new EntityAlreadyExistsException("Użytkownik o podanym adresie e-mail już istnieje.");
        }

        tokenRepository.validate(token, LocalDateTime.now());
        uzytkownikRepository.updateEmail(uzyt.getEmail(), savedToken.getNowyEmail());
    }

}
