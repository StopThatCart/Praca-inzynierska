/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.yukka.auth;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yukka.auth.email.EmailService;
import com.example.yukka.auth.email.EmailTemplateName;
import com.example.yukka.auth.requests.AuthRequest;
import com.example.yukka.auth.requests.HasloRequest;
import com.example.yukka.auth.requests.RegistrationRequest;
import com.example.yukka.handler.exceptions.EntityAlreadyExistsException;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;
import com.example.yukka.model.uzytkownik.token.Token;
import com.example.yukka.model.uzytkownik.token.TokenRepository;
import com.example.yukka.security.Neo4JAuthenticationProvider;
import com.example.yukka.security.jwt.JwtService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Klasa serwisowa do obsługi uwierzytelniania i rejestracji użytkowników.
 * Klasa ta zapewnia metody do rejestracji użytkowników, uwierzytelniania, odświeżania tokenów,
 * aktywacji konta, resetowania hasła oraz zmiany adresu e-mail.
 * 
 * Zależności:
 * <ul>
 *   <li><strong>UzytkownikRepository</strong>: Repozytorium danych użytkowników.</li>
 *   <li><strong>UzytkownikService</strong>: Serwis do operacji związanych z użytkownikami.</li>
 *   <li><strong>PasswordEncoder</strong>: Koder haseł użytkowników.</li>
 *   <li><strong>JwtService</strong>: Serwis do obsługi tokenów JWT.</li>
 *   <li><strong>EmailService</strong>: Serwis do wysyłania e-maili.</li>
 *   <li><strong>TokenRepository</strong>: Repozytorium danych tokenów.</li>
 *   <li><strong>Neo4JAuthenticationProvider</strong>: Niestandardowy dostawca uwierzytelniania.</li>
 * </ul>
 * 
 * Konfiguracja:
 * <ul>
 *   <li><strong>defaultAvatarObrazName</strong>: Domyślna nazwa obrazu awatara dla nowych użytkowników.</li>
 * </ul>
 * 
 * Metody:
 * <ul>
 *   <li><strong>register(RegistrationRequest request)</strong>: Rejestruje nowego użytkownika i wysyła e-mail weryfikacyjny.</li>
 *   <li><strong>authenticate(AuthRequest request)</strong>: Uwierzytelnia użytkownika i zwraca odpowiedź uwierzytelniającą z tokenem JWT.</li>
 *   <li><strong>refreshToken(String token)</strong>: Odświeża wygasły token JWT i zwraca nową odpowiedź uwierzytelniającą.</li>
 *   <li><strong>activateAccount(String token)</strong>: Aktywuje konto użytkownika za pomocą podanego tokena.</li>
 *   <li><strong>sendResetPasswordEmail(String email)</strong>: Wysyła e-mail resetowania hasła do użytkownika.</li>
 *   <li><strong>changePassword(HasloRequest request)</strong>: Zmienia hasło użytkownika za pomocą podanego tokena.</li>
 *   <li><strong>changeEmail(String token)</strong>: Zmienia adres e-mail użytkownika za pomocą podanego tokena.</li>
 * </ul>
 * 
 * Wyjątki:
 * <ul>
 *   <li><strong>IllegalArgumentException</strong>: Rzucany, gdy podano nieprawidłowy argument.</li>
 *   <li><strong>EntityNotFoundException</strong>: Rzucany, gdy nie znaleziono encji w repozytorium.</li>
 *   <li><strong>EntityAlreadyExistsException</strong>: Rzucany, gdy encja już istnieje w repozytorium.</li>
 *   <li><strong>MessagingException</strong>: Rzucany, gdy wystąpił błąd podczas wysyłania e-maila.</li>
 * </ul>
 */
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

    @Value("${uzytkownik.obraz.default.name}")
    private  String defaultAvatarObrazName;

    /**
     * Rejestruje nowego użytkownika na podstawie podanych danych rejestracyjnych.
     *
     * @param request obiekt zawierający dane rejestracyjne użytkownika
     * @throws MessagingException jeśli wystąpi problem z wysłaniem wiadomości e-mail
     * @throws IllegalArgumentException jeśli użytkownik o podanej nazwie lub adresie e-mail już istnieje i jest aktywny
     */
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

        Uzytkownik uzyt = Uzytkownik.builder()
                .uzytId(uzytkownikService.createUzytkownikId())
                .nazwa(request.getNazwa())
                .email(request.getEmail())
                .haslo(passwordEncoder.encode(request.getHaslo()))
                .avatar(defaultAvatarObrazName)
                .build();

        uzytkownikService.addUzytkownik(uzyt);
        emailService.sendValidationEmail(uzyt, EmailTemplateName.AKTYWACJA_KONTA);
    }

    /**
     * Autoryzuje użytkownika na podstawie podanych danych uwierzytelniających.
     *
     * @param request obiekt zawierający dane uwierzytelniające użytkownika (email i hasło)
     * @return obiekt AuthenticationResponse zawierający wygenerowany token JWT
     */
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
        var refreshToken = jwtService.generateRefreshToken(claims, (Uzytkownik) auth.getPrincipal());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Odświeża token JWT, jeśli nie wygasł.
     *
     * @param token token JWT do odświeżenia
     * @return obiekt AuthenticationResponse zawierający nowy token JWT
     * @throws IllegalArgumentException jeśli token JWT wygasł
     */
    public AuthenticationResponse refreshToken(String refreshToken) {
    log.info("Odświeżanie tokena JWT...");

    if (jwtService.isTokenExpired(refreshToken)) {
        log.error("Token JWT wygasł.");
        throw new IllegalArgumentException("Token JWT wygasł.");
    }
    String username = jwtService.extractUsername(refreshToken);
    UserDetails userDetails = uzytkownikService.loadUserByUsername(username);

    var claims = new HashMap<String, Object>();
    Uzytkownik uzyt = ((Uzytkownik) userDetails);

    claims.put("UzytId", uzyt.getUzytId());
    claims.put("Nazwa", uzyt.getUsername());
    claims.put("Email", uzyt.getEmail());
    claims.put("Avatar", uzyt.getAvatar());


    String newToken = jwtService.generateToken(claims, uzyt);
    String newRefreshToken = jwtService.generateRefreshToken(claims, uzyt);
    
    return AuthenticationResponse.builder()
            .token(newToken)
            .refreshToken(newRefreshToken)
            .build();
    }


    /**
     * Aktywuje konto użytkownika za pomocą podanego tokenu.
     *
     * @param token token aktywacyjny
     * @throws MessagingException jeśli wystąpi problem z wysłaniem wiadomości e-mail
     * @throws IllegalArgumentException jeśli token jest nieprawidłowy, wygasł lub konto zostało już aktywowane
     */
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

    /**
     * Wysyła e-mail resetowania hasła do użytkownika.
     *
     * @param email adres e-mail użytkownika
     * @throws MessagingException jeśli wystąpi problem z wysłaniem wiadomości e-mail
     */
    public void sendResetPasswordEmail(String email) throws MessagingException {
        log.info("Wysyłanie e-maila resetowania hasła do: " + email);
        var uzyt = uzytkownikRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika z podanym adresem email"));
        emailService.sendValidationEmail(uzyt, EmailTemplateName.RESET_HASLO);
    }

    /**
     * Zmienia hasło użytkownika za pomocą podanego tokenu.
     *
     * @param request obiekt zawierający token resetowania hasła i nowe hasło
     * @throws MessagingException jeśli wystąpi problem z wysłaniem wiadomości e-mail
     * @throws IllegalArgumentException jeśli token jest nieprawidłowy, wygasł lub hasło jest takie samo jak stare hasło
     */
    @Transactional
    public void changePassword(HasloRequest request) throws MessagingException {
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

    /**
     * Zmienia adres e-mail użytkownika za pomocą podanego tokenu.
     *
     * @param token token aktywacyjny
     * @throws MessagingException jeśli wystąpi problem z wysłaniem wiadomości e-mail
     * @throws IllegalArgumentException jeśli token jest nieprawidłowy, wygasł lub adres e-mail jest już zajęty
     */
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
