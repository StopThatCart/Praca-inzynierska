package com.example.yukka.model.social.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.repository.KomentarzRepository;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatnaResponse;
import com.example.yukka.model.social.service.RozmowaPrywatnaService;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;

import lombok.extern.slf4j.Slf4j;
//@WebMvcTest(RozmowaPrywatnaController.class)
@Testcontainers
//@SpringBootTest(classes = {TestUzytConfig.class})
@SpringBootTest
@Slf4j
public class RozmowaPrywatnaControllerTest {
   // @MockBean
    private RozmowaPrywatnaService rozmowaPrywatnaService;

   // @InjectMocks
  //  @InjectMocks
    private RozmowaPrywatnaController rozmowaPrywatnaController;
  //  @MockBean
    private KomentarzRepository komentarzRepository;
  //  @MockBean
    private UzytkownikRepository uzytkownikRepository;
 //   @MockBean
    private UzytkownikService uzytkownikService;


   // @MockBean
    AuthenticationManager authenticationManager;

    @Mock
    private MockMvc mockMvc;

    @Mock
    private Authentication senderAuth;
    @Mock
    private Authentication receiverAuth;

    //@Mock
    private Authentication authentication;

    Uzytkownik nadawca;
    Uzytkownik odbiorca;

    RozmowaPrywatna rozmowa;

    Komentarz k1;
    Komentarz k2;
    Komentarz k3;

    @BeforeEach
    public void setup() {

        mockMvc = MockMvcBuilders.standaloneSetup(rozmowaPrywatnaController)
                .apply(springSecurity())
                .build();

      //  SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
      //  securityContext.setAuthentication(authentication);
      //  SecurityContextHolder.setContext(securityContext);


        senderAuth = Mockito.mock(Authentication.class);
        receiverAuth = Mockito.mock(Authentication.class);




       // MockitoAnnotations.openMocks(this);
       // mockMvc = MockMvcBuilders.standaloneSetup(rozmowaPrywatnaController).build();

        nadawca = Uzytkownik.builder()
        .uzytId("dawcaIdJakies")
        .nazwa("Na Dawca")
        .email("dawca@email.pl")
       // .labels(Collections.singletonList(ROLE.Admin.toString()))
        .build();  

        odbiorca = Uzytkownik.builder()
        .uzytId("biorcaIdJakies")
        .nazwa("Oden Biorca")
        .email("biorca@email.pl")
       // .labels(Collections.singletonList(ROLE.Admin.toString()))
        .build();  

        uzytkownikService.addUzytkownik(nadawca);
        uzytkownikService.addUzytkownik(odbiorca);

      //  Mockito.when(senderAuth.getPrincipal()).thenReturn(nadawca);
     //   Mockito.when(receiverAuth.getPrincipal()).thenReturn(odbiorca);
        

        k1 = Komentarz.builder().komentarzId("komId1").opis("Wiadomość od nadawcy").build();
		k2 = Komentarz.builder().komentarzId("komId2").opis("Wiadomość od odbiorcy").build();
        k3 = Komentarz.builder().komentarzId("komId3").opis("Wiadomość kolejna").build();


        uzytkownikService.addUzytkownik(nadawca);
        uzytkownikService.addUzytkownik(odbiorca);

        Uzytkownik nadawca2  = uzytkownikRepository.findByUzytId(nadawca.getUzytId()).get();

        authentication = Mockito.mock(Authentication.class);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(authentication.getPrincipal()).thenReturn(nadawca2);
        

        
        Authentication bb = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("bb: " + bb);

     //   RozmowaPrywatna rozmowa1 = rozmowaPrywatnaService.inviteToRozmowaPrywatnaNoPunjabi(katarzyna.getNazwa(), piotr);

    }

    @AfterEach
    void cleanUp() {
        if (nadawca != null && nadawca.getEmail() != null) {
            uzytkownikRepository.removeUzytkownik(nadawca.getEmail());
        }
        if (odbiorca != null && odbiorca.getEmail() != null) {
            uzytkownikRepository.removeUzytkownik(odbiorca.getEmail());
        };
    }

  //  @Test
    void testFindRozmowyPrywatneOfUzytkownikAsAdmin() throws Exception {
        Authentication bb = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("\n\n\nbb: " + bb + "\n\n\n");

        log.info("bb: " + bb);
        log.info("nadawca: " + nadawca);
       
        PageResponse<RozmowaPrywatnaResponse> pageResponse = new PageResponse<>();
        when(rozmowaPrywatnaService.findRozmowyPrywatneOfUzytkownik(any(Integer.class), any(Integer.class), eq(bb)))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/rozmowy")
                .param("page", "0")
                .param("size", "10")
                .principal(authentication))
                .andExpect(status().isOk());
    }
/*
    @Test
   // @WithMockUser(username = "pracownik", roles = {"Pracownik"})
    void testGetRozmowaPrywatnaAsPracownik() throws Exception {

        String lacinskaNazwa2 = "Jeśli taka łacińska nazwa się znajdzie to będę bardzo zdziwiony";
        Roslina roslinaWithoutRelations = Roslina.builder()
            .nazwa(roslinaNazwa)
            .nazwaLacinska(lacinskaNazwa2)
            .opis(roslinaOpis)
            .wysokoscMin(wysokoscMin)
            .wysokoscMax(wysokoscMax)
            .obraz(roslinaObraz)
            .build();


            rozmowaPrywatnaController.getRozmowaPrywatna(lacinskaNazwa2, senderAuth);

        RoslinaRequest emptyRoslinaRequest = roslinaMapper.toRoslinaRequest(roslinaWithoutRelations);
        ResponseEntity<String> response = roslinaController.saveRoslina(emptyRoslinaRequest, mockAuth);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Roslina roslina2 = roslinaService.findByNazwaLacinska(lacinskaNazwa2).get();
        // Assert
            
        Assertions.assertThat(roslina2).isNotNull();
        Assertions.assertThat(roslina2.getId()).isNotNull();
        Assertions.assertThat(roslina2.getNazwa()).isEqualTo(roslinaWithoutRelations.getNazwa());
        Assertions.assertThat(roslina2.getNazwaLacinska()).isEqualTo(roslinaWithoutRelations.getNazwaLacinska());
        Assertions.assertThat(roslina2.getOpis()).isEqualTo(roslinaWithoutRelations.getOpis());
        Assertions.assertThat(roslina2.getWysokoscMin()).isEqualTo(roslinaWithoutRelations.getWysokoscMin());
        Assertions.assertThat(roslina2.getWysokoscMax()).isEqualTo(roslinaWithoutRelations.getWysokoscMax());

        Assertions.assertThat(roslina2.getFormy()).isEmpty();
        // Reszty nie trzeba bo to w sumie to samo

        roslinaService.deleteByNazwaLacinska(lacinskaNazwa2)



        RozmowaPrywatnaResponse response = new RozmowaPrywatnaResponse();
        when(rozmowaPrywatnaService.findRozmowaPrywatna(eq("receiverId"), eq(senderAuth)))
                .thenReturn(response);

        mockMvc.perform(get("/rozmowy/receiverId")
                .principal(senderAuth))
                .andExpect(status().isOk());
    }

    
     
     */

   // @Test
   // @WithMockUser(username = "uzytkownik", roles = {"Uzytkownik"})
    void testInviteToRozmowaPrywatnaAsUzytkownik() throws Exception {
        rozmowaPrywatnaController.inviteToRozmowaPrywatna(odbiorca.getUzytId(), senderAuth);
        
        RozmowaPrywatna rozmowa1 = rozmowaPrywatnaService.inviteToRozmowaPrywatna(odbiorca.getUzytId(), senderAuth);
        RozmowaPrywatna rozmowa = new RozmowaPrywatna();
        when(rozmowaPrywatnaService.inviteToRozmowaPrywatna(odbiorca.getUzytId(), senderAuth))
                .thenReturn(rozmowa);

        mockMvc.perform(post("/rozmowy/{odbiorca-uzyt-id}")
                .principal(senderAuth))
                .andExpect(status().isCreated());
    }
    /*
    @Test
    @WithMockUser(username = "uzytkownik", roles = {"Uzytkownik"})
    void testAcceptRozmowaPrywatnaAsUzytkownik() throws Exception {
        RozmowaPrywatna rozmowa = new RozmowaPrywatna();
        when(rozmowaPrywatnaService.acceptRozmowaPrywatna(eq("senderId"), eq(receiverAuth)))
                .thenReturn(rozmowa);

        mockMvc.perform(put("/rozmowy/senderId/accept")
                .principal(receiverAuth))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "uzytkownik", roles = {"Uzytkownik"})
    void testRejectRozmowaPrywatnaAsUzytkownik() throws Exception {
        // No need to mock return value as the method returns void
        mockMvc.perform(put("/rozmowy/senderId/reject")
                .principal(receiverAuth))
                .andExpect(status().isOk());
    }

    

     */
    
}
