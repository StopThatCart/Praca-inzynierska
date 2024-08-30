package com.example.yukka.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/rest/neo4j/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
    private final AuthenticationService service;

    @PostMapping(value = "/register", produces="application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody 
             @Valid
            RegistrationRequest request) {
        //return ResponseEntity.ok().build();
        service.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/login", produces="application/json")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/test")
    public String confirm() {
        return "No tutaj nie wejdziesz.";
    }

    @GetMapping("/testUnprotected")
    public String confirm2() {
        return "No tutaj wejdziesz.";
    }
}
