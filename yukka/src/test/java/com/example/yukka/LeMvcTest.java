package com.example.yukka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
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
public class LeMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private Authentication authentication = Mockito.mock(Authentication.class);
    private Principal mockPrincipal = Mockito.mock(Principal.class);


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

      //  SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    //    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
     //   SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);
    //    SecurityContextHolder.setContext(securityContext);

        Mockito.when(authentication.getPrincipal()).thenReturn(nadawca);
        Mockito.when(authentication.getName()).thenReturn(nadawca.getNazwa());
        
        
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

    @Test
    public void testGetCurrentUser() throws Exception {
        authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(nadawca);
     //   Mockito.when(mockPrincipal.getName()).thenReturn("me");

        System.out.println("auth ale inny: " + authentication.getPrincipal());
        System.out.println("Uzytkownik: " + authentication.getPrincipal().toString());

     //   System.out.println("mockPrincipal.getName(): " + mockPrincipal.getName());
//
//        Mockito.when(authentication.getPrincipal()).thenReturn(nadawca);

    //    assertNotNull(authentication);
   //     assertEquals(nadawca, authentication.getPrincipal());
        
      //  System.out.println("auth: " + authentication.getPrincipal());


     //   RequestBuilder requestBuilder = MockMvcRequestBuilders
        //    .get("/rozmowy")
          //  .principal(mockPrincipal);
           // .authentication(authentication)
            //.accept(MediaType.APPLICATION_JSON);
          //  .andExpect(status().isOk());

      //  MvcResult result = mockMvc .perform(requestBuilder).andReturn();

        //MockHttpServletResponse response = result.getResponse();
        //int status = response.getStatus();
        //Assert.assertEquals("response status is wrong", 200, status);


       // PageResponse<RozmowaPrywatnaResponse> pageResponse = new PageResponse<>();
       // when(rozmowaPrywatnaService.findRozmowyPrywatneOfUzytkownik(0, 10, authentication))
        //        .thenReturn(pageResponse);

        PageResponse<RozmowaPrywatnaResponse> pageResponse = rozmowaPrywatnaService.findRozmowyPrywatneOfUzytkownik(0, 10, authentication);

        ResponseEntity<PageResponse<RozmowaPrywatnaResponse>> respons = rozmowaPrywatnaController.findRozmowyPrywatneOfUzytkownik(0, 10, authentication);
                assertEquals(HttpStatus.OK, respons.getStatusCode());
                
    }

    @Test
    // We have to write out expectations and the 
      // expectations need to match with actuals
      // When this is run, it imitates and accesses 
      // the web layer and get the output.
    public void testWelcome() throws Exception {
        Authentication bb = authentication;
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
