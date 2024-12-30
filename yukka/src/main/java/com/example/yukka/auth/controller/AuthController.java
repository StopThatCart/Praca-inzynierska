package com.example.yukka.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.auth.AuthenticationResponse;
import com.example.yukka.auth.requests.AuthRequest;
import com.example.yukka.auth.requests.HasloRequest;
import com.example.yukka.auth.requests.RegistrationRequest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * AuthController obsługuje punkty końcowe związane z uwierzytelnianiem, takie jak rejestracja, logowanie,
 * aktywacja konta, resetowanie hasła i zmiana adresu e-mail.
 * 
 * <ul>
 * <li><strong>POST /api/auth/register</strong>: Rejestruje nowego użytkownika.</li>
 * <li><strong>GET /api/auth/aktywacja-konta</strong>: Aktywuje konto użytkownika za pomocą tokena.</li>
 * <li><strong>POST /api/auth/login</strong>: Uwierzytelnia użytkownika i zwraca token.</li>
 * <li><strong>POST /api/auth/refresh-token</strong>: Odświeża token uwierzytelniający.</li>
 * <li><strong>POST /api/auth/zmiana-hasla-email/{email}</strong>: Wysyła e-mail do resetowania hasła.</li>
 * <li><strong>POST /api/auth/zmiana-hasla</strong>: Zmienia hasło użytkownika.</li>
 * <li><strong>POST /api/auth/zmiana-email</strong>: Zmienia adres e-mail użytkownika za pomocą tokena.</li>
 * </ul>
 * 
 * Adnotacje:
 * <ul>
 * <li><strong>@RestController</strong>: Wskazuje, że ta klasa jest kontrolerem REST.</li>
 * <li><strong>@RequestMapping("api/auth")</strong>: Mapuje żądania do /api/auth.</li>
 * <li><strong>@RequiredArgsConstructor</strong>: Generuje konstruktor z wymaganymi argumentami.</li>
 * <li><strong>@Tag(name = "Authentication")</strong>: Oznacza ten kontroler dla dokumentacji API.</li>
 * </ul>
 * 
 * Zależności:
 * <ul>
 * <li><strong>AuthenticationService</strong>: Usługa do obsługi logiki uwierzytelniania.</li>
 * </ul>
 */
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
    private final AuthenticationService service;

    

    /**
     * Obsługuje rejestrację nowego użytkownika.
     *
     * @param request żądanie rejestracji zawierające szczegóły użytkownika
     * @return ResponseEntity wskazujący status procesu rejestracji
     * @throws MessagingException jeśli wystąpi błąd podczas wysyłania e-maila potwierdzającego
     */
    @PostMapping(value = "/register", produces="application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        //return ResponseEntity.ok().build();
        service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
    *  Obsługuję aktywację konta użytkownika za pomocą tokena.
    * @param token token aktywacyjny wysłany na e-mail użytkownika
    * @return ResponseEntity wskazujący, czy konto zostało aktywowane
    * @throws MessagingException jeśli wystąpi błąd podczas wysyłania e-maila aktywacyjnego
    */
    @GetMapping(value = "/aktywacja-konta")
    public ResponseEntity<?> confirm(@RequestParam String token) throws MessagingException {
        service.activateAccount(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * Obsługuje uwierzytelnianie użytkownika i zwraca token dostępu.
     *
     * @param request żądanie uwierzytelnienia zawierające adres e-mail i hasło
     * @return ResponseEntity zwracający token dostępu
     */
    @PostMapping(value = "/login", produces="application/json")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    /**
     * Obsługuje odświeżanie tokena dostępu.
     *
     * @param refreshToken nagłówek X-Refresh-Token do odświeżenia
     * @return ResponseEntity zwracający odświeżony token dostępu
     */
    @PostMapping(value = "/refresh-token", produces="application/json")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(service.refreshToken(refreshToken));
    }


    /**
     * Obsługuje żądanie zmiany hasła użytkownika.
     *
     * @param email adres e-mail użytkownika, dla którego ma zostać wysłany e-mail resetujący hasło
     * @throws MessagingException jeśli wystąpi błąd podczas wysyłania e-maila potwierdzającego
     */
    @PostMapping(value = "/zmiana-hasla-email/{email}", produces="application/json")
    public ResponseEntity<?> sendResetPasswordEmail(@PathVariable("email") String email) throws MessagingException {
        service.sendResetPasswordEmail(email);
        return ResponseEntity.accepted().build();
    }

    /**
     * Obsługuje zmianę hasła użytkownika.
     *
     * @param request żądanie zmiany hasła zawierające nowe hasło i token
     * @return ResponseEntity wskazujący status procesu zmiany hasła
     * @throws MessagingException jeśli wystąpi błąd podczas wysyłania e-maila potwierdzającego
     */
    @PostMapping(value = "/zmiana-hasla", produces="application/json")
    public ResponseEntity<?> changePassword(@Valid @RequestBody HasloRequest request) throws MessagingException {
        service.changePassword(request);
        return ResponseEntity.accepted().build();
    }

    /**
     * Obsługuje zmianę adresu e-mail użytkownika.
     *
     * @param token token zmiany adresu e-mail
     * @return ResponseEntity wskazujący status procesu zmiany adresu e-mail
     * @throws MessagingException jeśli wystąpi błąd podczas wysyłania e-maila potwierdzającego
     */
    @PostMapping(value = "/zmiana-email", produces="application/json")
    public ResponseEntity<?> changeEmail(@RequestParam String token) throws MessagingException {
        service.changeEmail(token);
        return ResponseEntity.accepted().build();
    }
}
