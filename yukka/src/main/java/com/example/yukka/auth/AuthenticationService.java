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
import com.example.yukka.handler.EntityNotFoundException;
import com.example.yukka.model.uzytkownik.Uzytkownik;
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
class AuthenticationService {
    private final UzytkownikRepository uzytkownikRepository;
    private final UzytkownikService uzytkownikService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    //private final AuthenticationManager authenticationManager;
    private final Neo4JAuthenticationProvider neo4jAuthenticationProvider;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    @Value("${uzytkownik.obraz.default.name}")
    private  String defaultAvatarObrazName;

    public void register(RegistrationRequest request)  throws MessagingException {
        log.info("Rejestracja użytkownika: " + request.getNazwa());
        Optional<Uzytkownik> targetUzyt = uzytkownikRepository.checkIfUzytkownikExists(request.getNazwa(), request.getEmail());
        if (targetUzyt.isPresent()) {
            if(targetUzyt.get().isAktywowany()) {
                throw new IllegalArgumentException("Aktywny użytkownik o podanej nazwie lub adresie e-mail już istnieje.");
            } else {
                sendValidationEmail(targetUzyt.get());
                return;
            }
        }
        //var userRole = ROLE.Uzytkownik.toString();
        System.out.println("\n\n\n Request: " + request.toString() + "\n\n\n");
        Uzytkownik uzyt = Uzytkownik.builder()
                .uzytId(createUzytkownikId())
                .nazwa(request.getNazwa())
                .email(request.getEmail())
                .haslo(passwordEncoder.encode(request.getHaslo()))
                .avatar(defaultAvatarObrazName)
                //.dataUtworzenia(LocalDateTime.now())
                // .banned(false)
                 //.labels(List.of(userRole))
                .build();
       // Ustawienia ust = Ustawienia.builder().build();

        uzytkownikService.addUzytkownik(uzyt);

        sendValidationEmail(uzyt);
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
                
        if (LocalDateTime.now().isAfter(savedToken.getDataWygasniecia())) {
            sendValidationEmail(savedToken.getUzytkownik());
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
        //savedToken.setDataWalidacji(LocalDateTime.now());
        //tokenRepository.save(savedToken);
    }


    private String generateAndSaveActivationToken(Uzytkownik uzyt) {
        log.info("Generowanie i zapis tokena aktywacyjnego dla użytkownika: " + uzyt.getEmail());
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .dataUtworzenia(LocalDateTime.now())
                .dataWygasniecia(LocalDateTime.now().plusMinutes(15))
                .build();

        Optional<Token> presentToken = tokenRepository.findByUzytkownikEmail(uzyt.getEmail());
        if(presentToken.isPresent()) {
            tokenRepository.removeToken(uzyt.getEmail(), presentToken.get().getToken());
        }

        tokenRepository.add(uzyt.getEmail(), token.getToken(), token.getDataUtworzenia(), token.getDataWygasniecia(),
        token.getDataWalidacji());

        return generatedToken;
    }

    private void sendValidationEmail(Uzytkownik uzyt) throws MessagingException {
        log.info("Wysyłanie e-maila aktywacyjnego do: " + uzyt.getEmail());
        var newToken = generateAndSaveActivationToken(uzyt);

        emailService.sendEmail(
                uzyt.getEmail(),
                uzyt.getNazwa(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Aktywacja konta"
                );
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        if (tokenRepository.findByToken(codeBuilder.toString()).isPresent()) {
            return generateActivationCode(length);
        }

        return codeBuilder.toString();
    }

}
