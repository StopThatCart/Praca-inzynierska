package com.example.yukka;

import java.security.Principal;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.controller.RozmowaPrywatnaController;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatnaResponse;
import com.example.yukka.model.social.service.RozmowaPrywatnaService;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LeMvcTest {
    
    //Jak dasz @Autowired to włącza się security

    private MockMvc mockMvc;
    @Mock
    private Authentication authentication = Mockito.mock(Authentication.class);
    private Principal mockPrincipal = Mockito.mock(Principal.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UzytkownikRepository uzytkownikRepository;
    @Autowired
    private UzytkownikService uzytkownikService;
    @Autowired
    private RozmowaPrywatnaController rozmowaPrywatnaController;
    @Autowired
    private RozmowaPrywatnaService rozmowaPrywatnaService;

    Uzytkownik nadawca = Uzytkownik.builder()
    .uzytId("dawcaIdJakies")
    .nazwa("Na Dawca")
    .email("dawca@email.pl")
   // .labels(Collections.singletonList(ROLE.Admin.toString()))
    .build();
    

    RozmowaPrywatna rozmowa;

    Komentarz k1;
    Komentarz k2;
    Komentarz k3;

    @BeforeAll
    void adddddd() {
        Mockito.when(authentication.getPrincipal()).thenReturn(nadawca);
        Mockito.when(authentication.getName()).thenReturn(nadawca.getNazwa());
        Mockito.when(authentication.getCredentials()).thenReturn(nadawca.getHaslo());


       
       
        
        
        System.out.println("auth: " + authentication.getPrincipal());
    }

    @BeforeEach
    public void setup() {
        nadawca = Uzytkownik.builder()
        .uzytId("dawcaIdJakies")
        .nazwa("Na Dawca")
        .email("dawca@email.pl")
       // .labels(Collections.singletonList(ROLE.Admin.toString()))
        .build();  

        uzytkownikService.addUzytkownik(nadawca);

    }

    @AfterEach
    void cleanUp() {
        if (nadawca != null && nadawca.getEmail() != null) {
            uzytkownikRepository.removeUzytkownik(nadawca.getEmail());
        }
    }

    
    /** 
     * @throws Exception
     */
    @Test
    public void testGetCurrentUser() throws Exception {
        authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(nadawca);
     //   Mockito.when(mockPrincipal.getName()).thenReturn("me");

        System.out.println("auth ale inny: " + authentication.getPrincipal());
        System.out.println("Uzytkownik: " + authentication.getPrincipal().toString());

     //   System.out.println("mockPrincipal.getName(): " + mockPrincipal.getName());

        PageResponse<RozmowaPrywatnaResponse> pageResponse = rozmowaPrywatnaService.findRozmowyPrywatneOfUzytkownik(0, 10, authentication);

        ResponseEntity<PageResponse<RozmowaPrywatnaResponse>> respons = rozmowaPrywatnaController.findRozmowyPrywatneOfUzytkownik(0, 10, authentication);
                assertEquals(HttpStatus.OK, respons.getStatusCode());


        mockMvc = MockMvcBuilders.standaloneSetup(rozmowaPrywatnaController).build();
        mockMvc.perform(MockMvcRequestBuilders
            .get("/rest/neo4j/rozmowy")

                
            .principal(authentication))
            .andExpect(status().isOk());

    }

    @Test
    // We have to write out expectations and the 
      // expectations need to match with actuals
      // When this is run, it imitates and accesses 
      // the web layer and get the output.
    @WithMockUser(username = "admin", roles = {"Uzytkownik"})
    public void testWelcome() throws Exception {
        Mockito.when(authentication.getPrincipal()).thenReturn(nadawca);
        mockMvc = MockMvcBuilders.standaloneSetup(rozmowaPrywatnaController)
        
        .build();
        mockMvc.perform(MockMvcRequestBuilders
            .get("/rest/neo4j/rozmowy")
            .principal(authentication)
            )
                .andExpect(status().isOk());
        Authentication bb = authentication;
      //  Authentication bb = authenticationManager.authenticate(authentication);
        System.out.println("\n\n\nbb no plis: " + bb + "\n\n\n");
        if (bb != null) {
            System.out.println("Authentication name: " + bb.getName());
            System.out.println("Authorities: " + bb.getAuthorities());
            System.out.println("Principal: " + bb.getPrincipal());
        } else {
            System.out.println("Authentication object is null");
        }

    }

}
