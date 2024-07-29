package com.example.yukka.security.Authentication;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.model.Uzytkownik.UserDetailsServiceImpl;
import com.example.yukka.model.Uzytkownik.Uzytkownik;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
//@Tag(name = "Authentication")     // Swagger lol
public class AuthenticationController {
    private AuthenticationService service;

    
    UserDetailsServiceImpl userDetailsServiceImpl;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
        @RequestBody @Validated RegistrationRequest request
    ) {
        service.register(request);
        return ResponseEntity.accepted().build();
    }
        
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @GetMapping("/users")
    public Collection<Uzytkownik> getAllUsers() {
        return userDetailsServiceImpl.getAllUsers();
    }

    

    
}
