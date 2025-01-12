package com.example.yukka.model.dzialka.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.yukka.auth.authorities.ROLE;
import com.example.yukka.auth.requests.UsunKontoRequest;
import com.example.yukka.common.FileResponse;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.model.dzialka.Pozycja;
import com.example.yukka.model.dzialka.enums.Wyswietlanie;
import com.example.yukka.model.dzialka.requests.BaseDzialkaRequest;
import com.example.yukka.model.dzialka.requests.DzialkaRoslinaRequest;
import com.example.yukka.model.dzialka.requests.MoveRoslinaRequest;
import com.example.yukka.model.dzialka.response.DzialkaResponse;
import com.example.yukka.model.dzialka.response.ZasadzonaRoslinaResponse;
import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.controller.RoslinaRepository;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;

import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@Testcontainers
@Slf4j
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DzialkaControllerTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DzialkaController controller;
    @Autowired
    private UzytkownikService uzytkownikService;
    @Autowired
    private UzytkownikRepository uzytkownikRepository;
    @Autowired
    private RoslinaRepository roslinaRepository;

    Authentication mockAuth;
    Uzytkownik uzyt;

    int numerDzialki = 1;
    ZasadzonaRoslinaResponse zasadzonaRoslinaResponse;

    @BeforeAll
    public void setUp() {
        mockAuth = Mockito.mock(Authentication.class);

        uzyt = Uzytkownik.builder()
        .uuid(uzytkownikService.createUzytkownikId())
        .nazwa("Działacz Działkujący")
        .email("dzialacz@email.pl")
        .haslo(passwordEncoder.encode("haslo12345678"))
        .aktywowany(true)
        .labels(Collections.singletonList(ROLE.Uzytkownik.toString()))
        .build();  
       
        Mockito.when(mockAuth.getPrincipal()).thenReturn(uzyt);

        zasadzonaRoslinaResponse = ZasadzonaRoslinaResponse.builder().build();

        afterAll();

        uzytkownikService.addUzytkownik(uzyt);
        uzyt = uzytkownikRepository.findByEmail(uzyt.getEmail()).get();

        System.out.println(uzyt.getUuid());
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
    void testGetDzialkaOfUzytkownikByNumer() {
        ResponseEntity<?> response = controller.getDzialkaOfUzytkownikByNumer(numerDzialki, uzyt.getNazwa(), mockAuth);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(2)
    void testGetPozycjeInDzialki() {
        ResponseEntity<?> response = controller.getPozycjeInDzialki(mockAuth);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @Order(3)
    void testRenameDzialka() {
        String nowaNazwa = "Nowa nazwa działki";
        ResponseEntity<?> response = controller.renameDzialka(numerDzialki, nowaNazwa, mockAuth);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        ResponseEntity<DzialkaResponse> dzialkaResponse = controller.getDzialkaOfUzytkownikByNumer(numerDzialki, uzyt.getNazwa(), mockAuth);
        DzialkaResponse body = dzialkaResponse.getBody();

        assertNotNull(body);
        assertEquals(nowaNazwa, body.getNazwa());
    }

    @Test
    @Order(4)
    void testSaveRoslinaToDzialka() {
        Roslina roslina = roslinaRepository.getRandomRoslina()
        .orElseThrow(() -> new IllegalArgumentException("Brak roślin w bazie danych"));


        DzialkaRoslinaRequest request = DzialkaRoslinaRequest.builder()
        .numerDzialki(numerDzialki).x(1).y(1)
		.pozycje(Set.of(
			Pozycja.builder().x(1).y(1).build(), 
			Pozycja.builder().x(1).y(2).build(), 
			Pozycja.builder().x(2).y(1).build(),
			Pozycja.builder().x(2).y(2).build()
			))
		.kolor("#ebf06c")
		.wyswietlanie(Wyswietlanie.TEKSTURA_KOLOR.toString())
		.roslinaUUID(roslina.getUuid())
		.build();

        zasadzonaRoslinaResponse.setX(request.getX());
        zasadzonaRoslinaResponse.setY(request.getY());
        zasadzonaRoslinaResponse.setKolor(request.getKolor());
        zasadzonaRoslinaResponse.setWyswietlanie(request.getWyswietlanie());
        zasadzonaRoslinaResponse.setPozycje(request.getPozycje());
        
        ResponseEntity<DzialkaResponse> response = controller.saveRoslinaToDzialka(request, null, null, mockAuth);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    @Order(5)
    void testGetRoslinaInDzialka() {
        ResponseEntity<ZasadzonaRoslinaResponse> response = controller
            .getRoslinaInDzialka(
                numerDzialki, 
                zasadzonaRoslinaResponse.getX(), 
                zasadzonaRoslinaResponse.getY(), 
                mockAuth
            );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ZasadzonaRoslinaResponse body = response.getBody();

        assertNotNull(body);
        assertNotNull(body.getRoslina());
        assertNotNull(body.getRoslina().getUuid());

        assertEquals(zasadzonaRoslinaResponse.getX(), body.getX());
        assertEquals(zasadzonaRoslinaResponse.getY(), body.getY());
        assertEquals(zasadzonaRoslinaResponse.getKolor(), body.getKolor());
        assertEquals(zasadzonaRoslinaResponse.getWyswietlanie(), body.getWyswietlanie());
        assertEquals(zasadzonaRoslinaResponse.getPozycje(), body.getPozycje());

        zasadzonaRoslinaResponse = body;
    }

    @Test
    @Order(6)
    void testGetRoslinaInDzialkaByUUID() {
        assertNotNull(zasadzonaRoslinaResponse);
        assertNotNull(zasadzonaRoslinaResponse.getRoslina());
        assertNotNull(zasadzonaRoslinaResponse.getRoslina().getUuid());

        ResponseEntity<ZasadzonaRoslinaResponse> response = controller
            .getRoslinaInDzialkaByUUID(
                numerDzialki, 
                zasadzonaRoslinaResponse.getRoslina().getUuid(), 
                mockAuth
            );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ZasadzonaRoslinaResponse body = response.getBody();

        assertNotNull(body);
        assertNotNull(body.getRoslina());
        assertNotNull(body.getRoslina().getUuid());
        
        assertEquals(zasadzonaRoslinaResponse.getX(), body.getX());
        assertEquals(zasadzonaRoslinaResponse.getY(), body.getY());
        assertEquals(zasadzonaRoslinaResponse.getKolor(), body.getKolor());
        assertEquals(zasadzonaRoslinaResponse.getWyswietlanie(), body.getWyswietlanie());
        assertEquals(zasadzonaRoslinaResponse.getPozycje(), body.getPozycje());
        assertEquals(zasadzonaRoslinaResponse.getRoslina().getUuid(), body.getRoslina().getUuid());
    }

    @Test
    @Order(7)
    void testUpdateRoslinaKolorInDzialka() {
        assertNotNull(zasadzonaRoslinaResponse);
        assertNotNull(zasadzonaRoslinaResponse.getRoslina());
        assertNotNull(zasadzonaRoslinaResponse.getRoslina().getUuid());

        String nowyKolor = "#ff0000";

        DzialkaRoslinaRequest request = DzialkaRoslinaRequest.builder()
        .numerDzialki(numerDzialki).x(zasadzonaRoslinaResponse.getX()).y(zasadzonaRoslinaResponse.getY())
        .pozycje(zasadzonaRoslinaResponse.getPozycje())
        .kolor(nowyKolor)
        .wyswietlanie(zasadzonaRoslinaResponse.getWyswietlanie())
        .build();
        
        ResponseEntity<?> response = controller.updateRoslinaKolorInDzialka(request, mockAuth);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        ResponseEntity<ZasadzonaRoslinaResponse> response2 = controller
        .getRoslinaInDzialkaByUUID(numerDzialki, zasadzonaRoslinaResponse.getRoslina().getUuid(), mockAuth);
        
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        ZasadzonaRoslinaResponse body = response2.getBody();

        assertNotNull(body);
        assertNotNull(body.getRoslina());
        assertNotNull(body.getRoslina().getUuid());
        assertEquals(zasadzonaRoslinaResponse.getRoslina().getUuid(), body.getRoslina().getUuid());

        assertEquals(nowyKolor, body.getKolor());
    }

    @Test
    @Order(8)
    void testUpdateRoslinaNotatkaInDzialka() {
        assertNotNull(zasadzonaRoslinaResponse);
        assertNotNull(zasadzonaRoslinaResponse.getRoslina());
        assertNotNull(zasadzonaRoslinaResponse.getRoslina().getUuid());

        String nowaNotatka = "Nowa notatka";

        DzialkaRoslinaRequest request = DzialkaRoslinaRequest.builder()
        .numerDzialki(numerDzialki).x(zasadzonaRoslinaResponse.getX()).y(zasadzonaRoslinaResponse.getY())
        .pozycje(zasadzonaRoslinaResponse.getPozycje())
        .kolor(zasadzonaRoslinaResponse.getKolor())
        .wyswietlanie(zasadzonaRoslinaResponse.getWyswietlanie())
        .notatka(nowaNotatka)
        .build();

        ResponseEntity<?> response = controller.updateRoslinaNotatkaInDzialka(request, mockAuth);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        ResponseEntity<ZasadzonaRoslinaResponse> response2 = controller
        .getRoslinaInDzialkaByUUID(numerDzialki, zasadzonaRoslinaResponse.getRoslina().getUuid(), mockAuth);
        
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        ZasadzonaRoslinaResponse body = response2.getBody();

        assertNotNull(body);
        assertNotNull(body.getRoslina());
        assertNotNull(body.getRoslina().getUuid());
        assertEquals(zasadzonaRoslinaResponse.getRoslina().getUuid(), body.getRoslina().getUuid());
        
        assertEquals(nowaNotatka, body.getNotatka());
    }

    @Test
    @Order(9)
    void testUpdateRoslinaWyswietlanieInDzialka() {
        assertNotNull(zasadzonaRoslinaResponse);
        assertNotNull(zasadzonaRoslinaResponse.getRoslina());
        assertNotNull(zasadzonaRoslinaResponse.getRoslina().getUuid());


        String noweWyswietlanie = Wyswietlanie.KOLOR.toString();

        DzialkaRoslinaRequest request = DzialkaRoslinaRequest.builder()
        .numerDzialki(numerDzialki).x(zasadzonaRoslinaResponse.getX()).y(zasadzonaRoslinaResponse.getY())
        .pozycje(zasadzonaRoslinaResponse.getPozycje())
        .kolor(zasadzonaRoslinaResponse.getKolor())
        .wyswietlanie(noweWyswietlanie)
        .build();

        ResponseEntity<?> response = controller.updateRoslinaWyswietlanieInDzialka(request, mockAuth);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        ResponseEntity<ZasadzonaRoslinaResponse> response2 = controller
        .getRoslinaInDzialkaByUUID(numerDzialki, zasadzonaRoslinaResponse.getRoslina().getUuid(), mockAuth);
        
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        ZasadzonaRoslinaResponse body = response2.getBody();

        assertNotNull(body);
        assertNotNull(body.getRoslina());
        assertNotNull(body.getRoslina().getUuid());
        assertEquals(zasadzonaRoslinaResponse.getRoslina().getUuid(), body.getRoslina().getUuid());
        
        assertEquals(noweWyswietlanie, body.getWyswietlanie());
    }

    @Test
    @Order(10)
    void testUpdateRoslinaObrazInDzialka() {
        assertNotNull(zasadzonaRoslinaResponse);
        assertNotNull(zasadzonaRoslinaResponse.getRoslina());
        assertNotNull(zasadzonaRoslinaResponse.getRoslina().getUuid());

        DzialkaRoslinaRequest request = DzialkaRoslinaRequest.builder()
        .numerDzialki(numerDzialki).x(zasadzonaRoslinaResponse.getX()).y(zasadzonaRoslinaResponse.getY())
        .pozycje(zasadzonaRoslinaResponse.getPozycje())
        .kolor(zasadzonaRoslinaResponse.getKolor())
        .wyswietlanie(zasadzonaRoslinaResponse.getWyswietlanie())
        .build();

        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", baos);
        } catch (IOException ex) {
        }
        byte[] imageBytes = baos.toByteArray();

        MockMultipartFile obraz = new MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpeg",
            imageBytes
        );

        MockMultipartFile tekstura = new MockMultipartFile(
            "file",
            "test2.jpg",
            "image/jpeg",
            imageBytes
        );

        ResponseEntity<FileResponse> response = controller.updateRoslinaObrazInDzialka(request, obraz, tekstura, mockAuth);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());  

        ResponseEntity<ZasadzonaRoslinaResponse> response2 = controller
        .getRoslinaInDzialkaByUUID(numerDzialki, zasadzonaRoslinaResponse.getRoslina().getUuid(), mockAuth);
        
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        ZasadzonaRoslinaResponse body = response2.getBody();

        assertNotNull(body);
        assertNotNull(body.getRoslina());
        assertNotNull(body.getRoslina().getUuid());
        assertEquals(zasadzonaRoslinaResponse.getRoslina().getUuid(), body.getRoslina().getUuid());
        
        assertNotNull(body.getObraz());
        assertNotNull(body.getTekstura());
    }

    @Test
    @Order(11)
    void testDeleteRoslinaObrazFromDzialka() {
        assertNotNull(zasadzonaRoslinaResponse);
        assertNotNull(zasadzonaRoslinaResponse.getRoslina());
        assertNotNull(zasadzonaRoslinaResponse.getRoslina().getUuid());

        BaseDzialkaRequest request = BaseDzialkaRequest.builder()
        .numerDzialki(numerDzialki).x(zasadzonaRoslinaResponse.getX()).y(zasadzonaRoslinaResponse.getY())
        .pozycje(zasadzonaRoslinaResponse.getPozycje())
        .build();  

        ResponseEntity<String> response = controller.deleteRoslinaObrazFromDzialka(request, mockAuth);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());  

        ResponseEntity<ZasadzonaRoslinaResponse> response2 = controller
        .getRoslinaInDzialkaByUUID(numerDzialki, zasadzonaRoslinaResponse.getRoslina().getUuid(), mockAuth);
        
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        ZasadzonaRoslinaResponse body = response2.getBody();

        assertNotNull(body);
        assertNotNull(body.getRoslina());
        assertNotNull(body.getRoslina().getUuid());
        assertEquals(zasadzonaRoslinaResponse.getRoslina().getUuid(), body.getRoslina().getUuid());
        
        assertNull(body.getObraz());
    }

    @Test
    @Order(12)
    void testDeleteRoslinaTeksturaFromDzialka() {
        assertNotNull(zasadzonaRoslinaResponse);
        assertNotNull(zasadzonaRoslinaResponse.getRoslina());
        assertNotNull(zasadzonaRoslinaResponse.getRoslina().getUuid());

        BaseDzialkaRequest request = BaseDzialkaRequest.builder()
        .numerDzialki(numerDzialki).x(zasadzonaRoslinaResponse.getX()).y(zasadzonaRoslinaResponse.getY())
        .pozycje(zasadzonaRoslinaResponse.getPozycje())
        .build();  

        ResponseEntity<String> response = controller.deleteRoslinaTeksturaFromDzialka(request, mockAuth);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());  

        ResponseEntity<ZasadzonaRoslinaResponse> response2 = controller
        .getRoslinaInDzialkaByUUID(numerDzialki, zasadzonaRoslinaResponse.getRoslina().getUuid(), mockAuth);
        
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        ZasadzonaRoslinaResponse body = response2.getBody();

        assertNotNull(body);
        assertNotNull(body.getRoslina());
        assertNotNull(body.getRoslina().getUuid());
        assertEquals(zasadzonaRoslinaResponse.getRoslina().getUuid(), body.getRoslina().getUuid());
        
        assertNull(body.getTekstura());
    }

    @Test
    @Order(13)
    void testUpdateRoslinaPozycjaInDzialka() {
        assertNotNull(zasadzonaRoslinaResponse);
        assertNotNull(zasadzonaRoslinaResponse.getRoslina());
        assertNotNull(zasadzonaRoslinaResponse.getRoslina().getUuid());

        Set<Pozycja> nowePozycje = Set.of(
            Pozycja.builder().x(2).y(2).build(), 
            Pozycja.builder().x(2).y(3).build(), 
            Pozycja.builder().x(3).y(2).build(),
            Pozycja.builder().x(3).y(3).build()
        );

        int numerDzialkiNowy = 3;

        MoveRoslinaRequest moveRequest = MoveRoslinaRequest.builder()
        .numerDzialki(numerDzialki)
        .numerDzialkiNowy(numerDzialkiNowy)
        .x(zasadzonaRoslinaResponse.getX()).y(zasadzonaRoslinaResponse.getY())
        .xNowy(2).yNowy(2)
        .pozycje(nowePozycje)
        .build();

        ResponseEntity<?> response = controller.updateRoslinaPozycjaInDzialka(moveRequest, mockAuth);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        ResponseEntity<ZasadzonaRoslinaResponse> response2 = controller
        .getRoslinaInDzialkaByUUID(numerDzialkiNowy, zasadzonaRoslinaResponse.getRoslina().getUuid(), mockAuth);
        
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        ZasadzonaRoslinaResponse body = response2.getBody();

        assertNotNull(body);
        assertNotNull(body.getRoslina());
        assertNotNull(body.getRoslina().getUuid());
        assertEquals(zasadzonaRoslinaResponse.getRoslina().getUuid(), body.getRoslina().getUuid());

        assertEquals(moveRequest.getXNowy(), body.getX());
        assertEquals(moveRequest.getYNowy(), body.getY());
        assertEquals(moveRequest.getPozycje(), body.getPozycje());

        zasadzonaRoslinaResponse = body;
        numerDzialki = numerDzialkiNowy;
    }


    @Test
    @Order(14)
    void testDeleteRoslinaFromDzialka() {
        assertNotNull(zasadzonaRoslinaResponse);
        assertNotNull(zasadzonaRoslinaResponse.getRoslina());
        assertNotNull(zasadzonaRoslinaResponse.getRoslina().getUuid());

        BaseDzialkaRequest request = BaseDzialkaRequest.builder()
        .numerDzialki(numerDzialki).x(zasadzonaRoslinaResponse.getX()).y(zasadzonaRoslinaResponse.getY())
        .pozycje(zasadzonaRoslinaResponse.getPozycje())
        .build();  

        ResponseEntity<String> response = controller.deleteRoslinaFromDzialka(request, mockAuth);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()); 
        
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            controller.getRoslinaInDzialkaByUUID(numerDzialki, zasadzonaRoslinaResponse.getRoslina().getUuid(), mockAuth);
        });
        assertNotNull(exception);
    }
}
