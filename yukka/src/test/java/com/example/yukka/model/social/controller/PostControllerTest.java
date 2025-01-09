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
import com.example.yukka.common.PageResponse;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.model.social.models.ocenil.OcenaResponse;
import com.example.yukka.model.social.models.post.PostResponse;
import com.example.yukka.model.social.models.post.controller.PostController;
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
public class PostControllerTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PostController postController;
    @Autowired
    private UzytkownikService uzytkownikService;
    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    Authentication mockAuth;
    Uzytkownik uzyt;

    PostResponse postResponse;

    @BeforeAll
    public void setUp() {
        mockAuth = Mockito.mock(Authentication.class);

        uzyt = Uzytkownik.builder()
        .uzytId(uzytkownikService.createUzytkownikId())
        .nazwa("Postownik pospolity")
        .email("postownik@email.pl")
        .haslo(passwordEncoder.encode("haslo12345678"))
        .aktywowany(true)
        .labels(Collections.singletonList(ROLE.Uzytkownik.toString()))
        .build();  
       
        Mockito.when(mockAuth.getPrincipal()).thenReturn(uzyt);

        postResponse = PostResponse.builder().build();

        afterAll();

        uzytkownikService.addUzytkownik(uzyt);

        uzyt = uzytkownikRepository.findByEmail(uzyt.getEmail()).get();
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
    }
    
    @Test
    @Order(1)
    void testFindAllPosty() {
        ResponseEntity<?> response = postController.findAllPosty(0, 12, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(2)
    void testAddPost() {
        PostRequest request = PostRequest.builder()
        .tytul("Testowy post")
        .opis("Testowy opis")
        .build();

        ResponseEntity<?> response = postController.addPost(request, null, mockAuth);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        PostResponse body = (PostResponse) response.getBody();
        assertNotNull(body);
        assertEquals(request.getTytul(), body.getTytul());
        assertEquals(request.getOpis(), body.getOpis());
        postResponse = body;
    }

    @Test
    @Order(3)
    void testGetPostByPostId() {
        assertNotNull(postResponse.getPostId());
        ResponseEntity<?> response = postController.findPostById(postResponse.getPostId());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        PostResponse body = (PostResponse) response.getBody();
        assertNotNull(body);
        assertEquals(postResponse.getPostId(), body.getPostId());
        assertEquals(postResponse.getTytul(), body.getTytul());
        assertEquals(postResponse.getOpis(), body.getOpis());
    }

    @Test
    @Order(4)
    void testAddOcenaToPost() {
        assertNotNull(postResponse.getPostId());

        boolean ocena = false;        
        OcenaRequest request = OcenaRequest.builder()
        .ocenialnyId(postResponse.getPostId()).lubi(ocena)
        .build();

        ResponseEntity<?> response = postController.addOcenaToPost(request, mockAuth);
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
    @Order(5)
    void testFindAllPostyByUzytkownik() {
        assertNotNull(postResponse.getPostId());
        assertNotNull(uzyt.getNazwa());

        ResponseEntity<PageResponse<PostResponse>> response = postController
            .findAllPostyByUzytkownik(0, 12, uzyt.getNazwa(), mockAuth);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        PageResponse<PostResponse> body =  response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getContent().size());
    }


    @Test
    @Order(6)
    void testRemovePost() {
        assertNotNull(postResponse.getPostId());
        assertNotNull(uzyt.getNazwa());

        ResponseEntity<?> response = postController.removePost(postResponse.getPostId(), mockAuth);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            postController.findPostById(postResponse.getPostId());
        });
        assertNotNull(exception);
    }

}
