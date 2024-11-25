package com.example.yukka.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.auth.requests.AuthRequest;
import com.example.yukka.auth.requests.HasloRequest;
import com.example.yukka.auth.requests.RegistrationRequest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
    private final AuthenticationService service;

    @PostMapping(value = "/register", produces="application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody 
             @Valid
            RegistrationRequest request) throws MessagingException {
        //return ResponseEntity.ok().build();
        service.register(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/aktywacja-konta")
    public void confirm(@RequestParam String token) throws MessagingException {
        service.activateAccount(token);
    }

    @PostMapping(value = "/login", produces="application/json")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping(value = "/refresh-token", produces="application/json")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestParam String token) {
        return ResponseEntity.ok(service.refreshToken(token));
    }

    @PostMapping(value = "/zmiana-hasla-email/{email}", produces="application/json")
    public ResponseEntity<?> zmianaHaslaEmail(@PathVariable("email") String email) throws MessagingException {
        service.sendResetHasloEmail(email);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/zmiana-hasla", produces="application/json")
    public ResponseEntity<?> zmianaHasla(@Valid @RequestBody HasloRequest request) throws MessagingException {
        service.changeHaslo(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/zmiana-email", produces="application/json")
    public ResponseEntity<?> zmianaEmail(@RequestParam String token) throws MessagingException {
        service.changeEmail(token);
        return ResponseEntity.accepted().build();
    }


}
