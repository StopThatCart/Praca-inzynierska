package com.example.yukka.model.social.controller;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.example.yukka.auth.authorities.ROLE;
import com.example.yukka.auth.requests.UsunKontoRequest;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.model.social.models.komentarz.KomentarzResponse;
import com.example.yukka.model.social.models.komentarz.controller.KomentarzController;
import com.example.yukka.model.social.models.ocenil.OcenaResponse;
import com.example.yukka.model.social.models.post.PostResponse;
import com.example.yukka.model.social.models.post.controller.PostController;
import com.example.yukka.model.social.requests.KomentarzRequest;
import com.example.yukka.model.social.requests.OcenaRequest;
import com.example.yukka.model.social.requests.PostRequest;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;

import lombok.extern.slf4j.Slf4j;

@Testcontainers
@SpringBootTest
@Slf4j
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KomentarzControllerTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PostController postController;
    @Autowired
    private KomentarzController komentarzController;
    @Autowired
    private UzytkownikService uzytkownikService;
    @Autowired
    private UzytkownikRepository uzytkownikRepository;


    Authentication mockAuth;
    Uzytkownik uzyt;

    Authentication otherMockAuth;
    Uzytkownik otherUzyt;

    PostResponse postResponse;
    KomentarzResponse komentarzResponse;

    @BeforeAll
    public void setUp() {
        mockAuth = Mockito.mock(Authentication.class);
        otherMockAuth = Mockito.mock(Authentication.class);

        uzyt = Uzytkownik.builder()
        .uuid(uzytkownikService.createUzytkownikId())
        .nazwa("Postownik komentujący")
        .email("postokom@email.pl")
        .haslo(passwordEncoder.encode("haslo12345678"))
        .aktywowany(true)
        .labels(Collections.singletonList(ROLE.Uzytkownik.toString()))
        .build();  
       
        Mockito.when(mockAuth.getPrincipal()).thenReturn(uzyt);

        otherUzyt = Uzytkownik.builder()
        .uuid(uzytkownikService.createUzytkownikId())
        .nazwa("Krzysztof Koment")
        .email("chris@email.pl")
        .haslo(passwordEncoder.encode("haslo12345678"))
        .aktywowany(true)
        .build();

        Mockito.when(otherMockAuth.getPrincipal()).thenReturn(otherUzyt);

        postResponse = PostResponse.builder().build();
        komentarzResponse = KomentarzResponse.builder().build();

        afterAll();

        uzytkownikService.addUzytkownik(uzyt);
        uzytkownikService.addUzytkownik(otherUzyt);

        uzyt = uzytkownikRepository.findByEmail(uzyt.getEmail()).get();
        otherUzyt = uzytkownikRepository.findByEmail(otherUzyt.getEmail()).get();

        PostRequest request = PostRequest.builder()
        .tytul("Testowy post")
        .opis("Testowy opis")
        .build();

        ResponseEntity<?> response = postController.addPost(request, null, mockAuth);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        postResponse = (PostResponse) response.getBody();
    }

    @AfterAll
    public void afterAll() {
        log.info("Usuwanie konta testowego...");
        UsunKontoRequest request = UsunKontoRequest.builder().haslo("haslo12345678").build();

        try {
            uzytkownikService.removeSelf(request, mockAuth);
            
        } catch (IllegalArgumentException e) {
            log.error("Błąd podczas usuwania konta testowego: {}", e.getMessage());
        }

        try {
            uzytkownikService.removeSelf(request, otherMockAuth);
        } catch (IllegalArgumentException e) {
            log.error("Błąd podczas usuwania konta testowego: {}", e.getMessage());
        }
    }

    @Test
    @Order(1)
    void testAddKomentarzToPost() {
        assertNotNull(postResponse.getUuid());

        KomentarzRequest request = KomentarzRequest.builder()
        .opis("Testowy opis")
        .targetId(postResponse.getUuid())
        .build();

        ResponseEntity<?> response = komentarzController.addKomentarzToPost(request, null, mockAuth);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        KomentarzResponse body = (KomentarzResponse) response.getBody();
        assertNotNull(body);
        assertEquals(request.getOpis(), body.getOpis());
        komentarzResponse = body;
    }

    @Test
    @Order(2)
    void testFindKomentarzById() {
        assertNotNull(komentarzResponse.getUuid());

        ResponseEntity<?> response = komentarzController.findKomentarzByUUID(komentarzResponse.getUuid());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        KomentarzResponse body = (KomentarzResponse) response.getBody();
        assertNotNull(body);
        assertEquals(komentarzResponse.getUuid(), body.getUuid());
        assertEquals(komentarzResponse.getOpis(), body.getOpis());
    }

    @Test
    @Order(3)
    void testAddOdpowiedzToKomentarz() {
        assertNotNull(komentarzResponse.getUuid());

        KomentarzRequest request = KomentarzRequest.builder()
        .opis("Testowa odpowiedź")
        .targetId(komentarzResponse.getUuid())
        .build();

        ResponseEntity<?> response = komentarzController.addOdpowiedzToKomentarz(request, null, otherMockAuth);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        KomentarzResponse body = (KomentarzResponse) response.getBody();
        assertNotNull(body);
        assertEquals(request.getOpis(), body.getOpis());
    }
    
    @Test
    @Order(4)
    void testUpdateKomentarz() {
        assertNotNull(komentarzResponse.getUuid());

        KomentarzRequest request = KomentarzRequest.builder()
        .opis("Zaktualizowany opis")
        .targetId(komentarzResponse.getUuid())
        .build();

        ResponseEntity<?> response = komentarzController.updateKomentarz(request, mockAuth);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        KomentarzResponse body = (KomentarzResponse) response.getBody();
        assertNotNull(body);
        assertEquals(request.getOpis(), body.getOpis());
    }

    @Test
    @Order(5)
    void testFindKomentarzeOfUzytkownik() {
        assertNotNull(uzyt.getUuid());
        assertNotNull(uzyt.getNazwa());

        ResponseEntity<?> response = komentarzController.findKomentarzeOfUzytkownik(0, 12, uzyt.getNazwa(), mockAuth);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(6)
    void testAddOcenaToKomentarz() {
        assertNotNull(komentarzResponse.getUuid());

        boolean ocena = false;
        OcenaRequest request = OcenaRequest.builder()
        .ocenialnyId(komentarzResponse.getUuid())
        .lubi(ocena)
        .build();

        ResponseEntity<?> response = komentarzController.addOcenaToKomentarz(request, otherMockAuth);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        OcenaResponse body = (OcenaResponse) response.getBody();
        assertNotNull(body);

        if (ocena) {
            assertNotEquals(0, body.getOcenyLubi());
        } else {
            assertNotEquals(0, body.getOcenyNieLubi());
        }
    }

    @Test
    @Order(7)
    void testRemoveKomentarz() {
        assertNotNull(komentarzResponse.getUuid());

        ResponseEntity<?> response = komentarzController.removeKomentarz(komentarzResponse.getUuid(), mockAuth);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            komentarzController.findKomentarzByUUID(komentarzResponse.getUuid());
        });
        assertNotNull(exception);

    }

}
