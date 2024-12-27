package com.example.yukka.auth;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.yukka.auth.email.EmailTemplateName;
import com.example.yukka.auth.requests.AuthRequest;
import com.example.yukka.auth.requests.EmailRequest;
import com.example.yukka.auth.requests.HasloRequest;
import com.example.yukka.auth.requests.RegistrationRequest;
import com.example.yukka.auth.requests.UsunKontoRequest;
import com.example.yukka.authorities.ROLE;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikController;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;
import com.example.yukka.model.uzytkownik.token.Token;
import com.example.yukka.model.uzytkownik.token.TokenRepository;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@Testcontainers
@Slf4j
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthController controller;
    @Autowired
    private UzytkownikController uzytkownikController;
    @Autowired
    private UzytkownikService uzytkownikService;
    @Autowired
    private UzytkownikRepository uzytkownikRepository;
    @Autowired
    private TokenRepository tokenRepository;
    
    Authentication mockAuth;
    Uzytkownik uzyt;
    String email = "roman@email.pl";
    String haslo = "haslo12345678";
    String noweHaslo = "haslo87654321";
    String nowyEmail = "nowy@email.pl";

    @BeforeAll
    public void setUpUzytkownik() {
        mockAuth = Mockito.mock(Authentication.class);

        uzyt = Uzytkownik.builder()
        .nazwa("Roman Rejestrator")
        .email(email)
        .haslo(passwordEncoder.encode(haslo))
        .labels(Collections.singletonList(ROLE.Uzytkownik.toString()))
        .build();  
        Mockito.when(mockAuth.getPrincipal()).thenReturn(uzyt);

        afterAll();
    }

    @AfterAll
    public void afterAll() {
        log.info("Usuwanie konta testowego...");

        Optional<Uzytkownik> uz = uzytkownikRepository.findByEmail(email);
        if (uz.isEmpty()) {
            uz = uzytkownikRepository.findByEmail(nowyEmail);
        }
        if (uz.isEmpty()) {
            return;
        }

        UsunKontoRequest request = UsunKontoRequest.builder().haslo(haslo).build();
        uzyt.setEmail(uz.get().getEmail());
        uzyt.setHaslo(passwordEncoder.encode(haslo));

        try {
            uzytkownikService.removeSelf(request, mockAuth);
        } catch (IllegalArgumentException e) {
            request.setHaslo(noweHaslo);
            uzyt.setHaslo(passwordEncoder.encode(noweHaslo));
            try {
                uzytkownikService.removeSelf(request, mockAuth);
                uzyt.setHaslo(passwordEncoder.encode(haslo));
            } catch (IllegalArgumentException er) {
                log.error("Błąd podczas usuwania konta testowego: {}", er.getMessage());
            }
        }

        uzyt.setEmail(email);
        uzyt.setHaslo(passwordEncoder.encode(haslo));
    }


    @Test
    @Order(1)
    void testRegister() {
        RegistrationRequest request = RegistrationRequest.builder()
        .nazwa(uzyt.getNazwa())
        .email(uzyt.getEmail())
        .haslo(haslo)
        .powtorzHaslo(haslo)
        .build();
        
        try {
            ResponseEntity<?> response = controller.register(request);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        } catch (MessagingException e) {
            log.error("Błąd podczas wysyłania wiadomości email: {}", e.getMessage());
        }
    }

    @Test
    @Order(2)
    void testConfirm() {
        sleep(5000);
        Optional<Token> presentToken = tokenRepository.findByUzytkownikEmail(uzyt.getEmail(), EmailTemplateName.AKTYWACJA_KONTA.getName());
        assertTrue(presentToken.isPresent());

        try {
            ResponseEntity<?> response = controller.confirm(presentToken.get().getToken());
            assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        } catch (MessagingException e) {
            log.error("Błąd podczas wysyłania wiadomości email: {}", e.getMessage());
        }
    }

    @Test
    @Order(3)
    void testLogin() {
        AuthRequest request = AuthRequest.builder()
        .email(uzyt.getEmail())
        .haslo(haslo)
        .build();

        ResponseEntity<AuthenticationResponse> response = controller.login(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        AuthenticationResponse body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.getToken());
        assertNotNull(body.getRefreshToken());
    }


    @Test
    @Order(4)
    void testRefreshToken() {
        AuthRequest request = AuthRequest.builder()
        .email(uzyt.getEmail())
        .haslo(haslo)
        .build();

        ResponseEntity<AuthenticationResponse> response = controller.login(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        AuthenticationResponse body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.getToken());
        assertNotNull(body.getRefreshToken());

        ResponseEntity<AuthenticationResponse> refreshResponse = controller.refreshToken(body.getRefreshToken());
        assertEquals(HttpStatus.OK, refreshResponse.getStatusCode());

        AuthenticationResponse refreshBody = response.getBody();
        assertNotNull(refreshBody);
        assertNotNull(refreshBody.getToken());
        assertNotNull(refreshBody.getRefreshToken());
    }


    @Test
    @Order(5)
    void testSendResetPasswordEmail() {
        try {
            ResponseEntity<?> response = controller.sendResetPasswordEmail(uzyt.getEmail());
            assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        } catch (MessagingException e) {
            log.error("Błąd podczas wysyłania wiadomości email: {}", e.getMessage());
        }
    }

    
    @Test
    @Order(6)
    void testChangePassword() {
        sleep(5000);
        Optional<Token> presentToken = tokenRepository.findByUzytkownikEmail(uzyt.getEmail(), EmailTemplateName.RESET_HASLO.getName());
        assertTrue(presentToken.isPresent());

        HasloRequest request = HasloRequest.builder()
        .noweHaslo(noweHaslo)
        .nowePowtorzHaslo(noweHaslo)
        .token(presentToken.get().getToken())
        .build();

        try {
            ResponseEntity<?> response = controller.changePassword(request);
            assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
            uzyt.setHaslo(noweHaslo);
        } catch (MessagingException e) {
            log.error("Błąd podczas wysyłania wiadomości email: {}", e.getMessage());
        }
        
    }


    @Test
    @Order(7)
    void testChangeEmail() {
        EmailRequest emailRequest = EmailRequest.builder()
        .haslo(noweHaslo)
        .nowyEmail(nowyEmail)
        .build();

        try {
            ResponseEntity<?> emailResponse = uzytkownikController.sendChangeEmail(emailRequest, mockAuth);
            assertEquals(HttpStatus.ACCEPTED, emailResponse.getStatusCode());

            sleep(5000);
            Optional<Token> presentToken = tokenRepository.findByUzytkownikEmail(uzyt.getEmail(), EmailTemplateName.ZMIANA_EMAIL.getName());
            assertTrue(presentToken.isPresent());

            ResponseEntity<?> response = controller.changeEmail(presentToken.get().getToken());
            assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
            uzyt.setEmail(nowyEmail);
        } catch (MessagingException e) {
            log.error("Błąd podczas wysyłania wiadomości email: {}", e.getMessage());
        }

    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log.error("Thread interrupted: {}", e.getMessage());
        }
    }




}
