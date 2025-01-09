package com.example.yukka.model.social.controller;

import java.util.Collections;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.example.yukka.auth.authorities.ROLE;
import com.example.yukka.auth.requests.UsunKontoRequest;
import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.models.komentarz.Komentarz;
import com.example.yukka.model.social.models.komentarz.KomentarzResponse;
import com.example.yukka.model.social.models.komentarz.controller.KomentarzController;
import com.example.yukka.model.social.models.rozmowaPrywatna.RozmowaPrywatnaResponse;
import com.example.yukka.model.social.models.rozmowaPrywatna.controller.RozmowaPrywatnaController;
import com.example.yukka.model.social.models.rozmowaPrywatna.controller.RozmowaPrywatnaService;
import com.example.yukka.model.social.requests.KomentarzRequest;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;

import lombok.extern.slf4j.Slf4j;
//@WebMvcTest(RozmowaPrywatnaController.class)
@Testcontainers
@SpringBootTest
@Slf4j
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RozmowaPrywatnaControllerTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private KomentarzController komentarzController;
    @Autowired
    private UzytkownikService uzytkownikService;
    @Autowired
    private UzytkownikRepository uzytkownikRepository;
    @Autowired
    private RozmowaPrywatnaService rozmowaPrywatnaService;
    @Autowired
    private RozmowaPrywatnaController rozmowaPrywatnaController;

    Authentication senderAuth;
    Uzytkownik nadawca;

    Authentication receiverAuth;
    Uzytkownik odbiorca;

    KomentarzResponse komentarzResponse;

    RozmowaPrywatnaResponse rozmowa;

    Komentarz k1;
    Komentarz k2;
    Komentarz k3;

    @BeforeAll
    public void setup() {
        senderAuth = Mockito.mock(Authentication.class);
        receiverAuth = Mockito.mock(Authentication.class);

        nadawca = Uzytkownik.builder()
        .uzytId(uzytkownikService.createUzytkownikId())
        .nazwa("Na Dawca")
        .email("dawca@email.pl")
        .haslo(passwordEncoder.encode("haslo12345678"))
        .aktywowany(true)
        .labels(Collections.singletonList(ROLE.Uzytkownik.toString()))
        .build();  
       
        Mockito.when(senderAuth.getPrincipal()).thenReturn(nadawca);

        odbiorca = Uzytkownik.builder()
        .uzytId(uzytkownikService.createUzytkownikId())
        .nazwa("Oden Biorca")
        .email("biorca@email.pl")
        .haslo(passwordEncoder.encode("haslo12345678"))
        .aktywowany(true)
        .build();

        Mockito.when(receiverAuth.getPrincipal()).thenReturn(odbiorca);

        uzytkownikService.addUzytkownik(nadawca);
        uzytkownikService.addUzytkownik(odbiorca);

        rozmowa = RozmowaPrywatnaResponse.builder().build();

        k1 = Komentarz.builder().komentarzId("komId1").opis("Wiadomość od nadawcy").build();
		    k2 = Komentarz.builder().komentarzId("komId2").opis("Wiadomość od odbiorcy").build();
        k3 = Komentarz.builder().komentarzId("komId3").opis("Wiadomość kolejna").build();


        nadawca = uzytkownikRepository.findByEmail(nadawca.getEmail()).get();
        odbiorca = uzytkownikRepository.findByEmail(odbiorca.getEmail()).get();

     //   RozmowaPrywatna rozmowa1 = rozmowaPrywatnaService.inviteToRozmowaPrywatnaNoPunjabi(katarzyna.getNazwa(), piotr);
    }


    @AfterAll
    public void afterAll() {
        log.info("Usuwanie konta testowego...");
        UsunKontoRequest request = UsunKontoRequest.builder().haslo("haslo12345678").build();

        try {
            uzytkownikService.removeSelf(request, senderAuth);
        } catch (IllegalArgumentException e) {
            log.error("Błąd podczas usuwania konta testowego: {}", e.getMessage());
        }

        try {
            uzytkownikService.removeSelf(request, receiverAuth);
        } catch (IllegalArgumentException e) {
            log.error("Błąd podczas usuwania konta testowego: {}", e.getMessage());
        }
    }

    @Test
    @Order(1)
    void testInviteToRozmowaPrywatna() {
      assertNotNull(odbiorca.getNazwa());

      ResponseEntity<?> response = rozmowaPrywatnaController.inviteToRozmowaPrywatna(odbiorca.getNazwa(), senderAuth);
      assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    @Test
    @Order(2)
    void testRejectRozmowaPrywatna() {
      assertNotNull(odbiorca.getNazwa());

      try {
        rozmowa = rozmowaPrywatnaService.findRozmowaPrywatnaByNazwa(odbiorca.getNazwa(), senderAuth);
      } catch (Exception e) {
        log.error("Nie znaleziono testowej rozmowy. Tworzenie nowego zaproszenia...");
        testInviteToRozmowaPrywatna();
      }

      assertNotNull(nadawca.getNazwa());
      
      ResponseEntity<?> response = rozmowaPrywatnaController.rejectRozmowaPrywatna(nadawca.getNazwa(), receiverAuth);
      assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    @Order(3)
    void testAcceptRozmowaPrywatna() {
      assertNotNull(odbiorca.getNazwa());

      try {
        rozmowa = rozmowaPrywatnaService.findRozmowaPrywatnaByNazwa(odbiorca.getNazwa(), senderAuth);
      } catch (Exception e) {
        log.error("Nie znaleziono testowej rozmowy. Tworzenie nowego zaproszenia...");
        testInviteToRozmowaPrywatna();
      }

      assertNotNull(nadawca.getNazwa());

      ResponseEntity<?> response = rozmowaPrywatnaController.acceptRozmowaPrywatna(nadawca.getNazwa(), receiverAuth);
      assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    @Order(4)
    void testFindRozmowyPrywatneOfUzytkownik() {
      ResponseEntity<PageResponse<RozmowaPrywatnaResponse>> response = rozmowaPrywatnaController.findRozmowyPrywatneOfUzytkownik(0, 10, senderAuth);
      assertEquals(HttpStatus.OK, response.getStatusCode());

      PageResponse<RozmowaPrywatnaResponse> body = response.getBody();
      assertNotNull(body);
    }

    @Test
    @Order(5)
    void testFindRozmowaPrywatnaByNazwa() {
      assertNotNull(odbiorca.getNazwa());

      ResponseEntity<RozmowaPrywatnaResponse> response = rozmowaPrywatnaController.findRozmowaPrywatnaByNazwa(odbiorca.getNazwa(), senderAuth);
      assertEquals(HttpStatus.OK, response.getStatusCode());

      RozmowaPrywatnaResponse body = response.getBody();
      assertNotNull(body);
      assertTrue(body.getUzytkownicy().stream().anyMatch(u -> u.getNazwa().equals(odbiorca.getNazwa())));
      assertTrue(body.getUzytkownicy().stream().anyMatch(u -> u.getNazwa().equals(nadawca.getNazwa())));

      rozmowa = body;
    }

    @Test
    @Order(6)
    void testAddKomentarzToWiadomoscPrywatna() {
      assertNotNull(nadawca.getNazwa());
      assertNotNull(odbiorca.getNazwa());

      KomentarzRequest request = KomentarzRequest.builder()
      .opis("Testowa wiadomość")
      .targetId(odbiorca.getNazwa())
      .build();

      ResponseEntity<KomentarzResponse> response = rozmowaPrywatnaController.addKomentarzToWiadomoscPrywatna(request, null, senderAuth);
      assertEquals(HttpStatus.CREATED, response.getStatusCode());

      KomentarzResponse body = response.getBody();
      assertNotNull(body);
      assertEquals(request.getOpis(), body.getOpis());
    }


}
