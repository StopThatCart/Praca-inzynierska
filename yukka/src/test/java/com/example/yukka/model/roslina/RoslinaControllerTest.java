package com.example.yukka.model.roslina;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.yukka.auth.authorities.ROLE;
import com.example.yukka.model.roslina.cecha.Cecha;
import com.example.yukka.model.roslina.controller.RoslinaController;
import com.example.yukka.model.roslina.controller.RoslinaRepository;
import com.example.yukka.model.roslina.controller.RoslinaService;
import com.example.yukka.model.roslina.enums.RoslinaEtykietyFrontend;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
//@AutoConfigureDataNeo4j
@Testcontainers
@Slf4j
//@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(OrderAnnotation.class)
//@ContextConfiguration

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoslinaControllerTest {

    @Autowired
    private RoslinaController roslinaController;
    @Autowired
    private RoslinaService roslinaService;
    @Autowired
    private RoslinaRepository roslinaRepository;
    @Autowired
    private RoslinaMapper roslinaMapper;

  //  @Mock
    Authentication mockAuth;
    Uzytkownik uzyt;

    Roslina roslina;
    //private Long uuid;
    private final String roslinaNazwa = "Na pewno takiej nazwy nie ma";
    private final String nazwaLacinska = "nomen latinum certe nullum est";
    private final String roslinaOpis = "To jest dramat.";
   // private final String roslinaObraz = "tilia_henryana.jpg";
    private final Double wysokoscMin = 0.5;
    private final Double wysokoscMax = 4.0;

    
    @BeforeAll
    void setUpUzytkownik() {
        mockAuth = Mockito.mock(Authentication.class);

        // Utworzenie użytkownika z rolą ADMIN
        uzyt = Uzytkownik.builder()
        .nazwa("Jan Kowalski")
        .email("jan@email.pl")
        .labels(Collections.singletonList(ROLE.Admin.toString()))
        .build();  
        //new User("admin", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        Mockito.when(mockAuth.getPrincipal()).thenReturn(uzyt);
    }

    @BeforeEach
    void setUp() {
 
        //new User("admin", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        Mockito.when(mockAuth.getPrincipal()).thenReturn(uzyt);


        Cecha formaDrzewo = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.FORMA.getBackendValue()), "TAKA TESTOWA");

        Cecha glebaPrzecietna = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.GLEBA.getBackendValue()), "TEŻ TESTOWA");
        Cecha glebaProchniczna = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.GLEBA.getBackendValue()), "próchniczna");
        Cecha glebaGliniasta = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.GLEBA.getBackendValue()), "gliniasta");
        
        Cecha grupaLisciaste = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.GRUPA.getBackendValue()), "liściaste");
        
        Cecha kolorLisciCiemnozielone = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.KOLOR_LISCI.getBackendValue()), "ciemnozielone");
        Cecha kolorKwiatowKremowy = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.KOLOR_KWIATOW.getBackendValue()), "kremowe");
        
        Cecha kwiatPojedynczy = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.KWIAT.getBackendValue()), "pojedyncze");
        Cecha kwiatPachnace = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.KWIAT.getBackendValue()), "pachnące");
        
        Cecha okresKwitnieniaWrzesien = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.OKRES_KWITNIENIA.getBackendValue()), "wrzesień");
        Cecha okresOwocowaniaPazdziernik = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.OKRES_OWOCOWANIA.getBackendValue()), "październik");
        Cecha okresOwocowaniaListopad = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.OKRES_OWOCOWANIA.getBackendValue()), "listopad");
        
        Cecha owocBrazowy = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.OWOC.getBackendValue()), "brązowe");
        
        Cecha podgrupaLiscisteDrzewa = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.PODGRUPA.getBackendValue()), "liściaste drzewa");
        
        Cecha pokrojDrzewiastyRozlozysty = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.POKROJ.getBackendValue()), "drzewiasty rozłożysty");
        Cecha pokrojSzerokostoszkowy = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.POKROJ.getBackendValue()), "szerokostożkowy");
        
        Cecha silaWzrostuTypowa = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.SILA_WZROSTU.getBackendValue()), "wzrost typowy dla gatunku");
        Cecha stanowiskoSloneczne = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.STANOWISKO.getBackendValue()), "stanowisko słoneczne");
        
        Cecha walorPachnaceKwiaty = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.WALOR.getBackendValue()), "pachnące kwiaty");
        Cecha walorRoslinaMiododajna = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.WALOR.getBackendValue()), "roślina miododajna");
        // Resztę właściwości zostawia się pustą.

        Roslina lipaHenryego = Roslina.builder()
            //.id(12345678L)
            .nazwa(roslinaNazwa)
            .nazwaLacinska(nazwaLacinska)
            .opis(roslinaOpis)
            .wysokoscMin(wysokoscMin)
            .wysokoscMax(wysokoscMax)
         //   .obraz(roslinaObraz)
            .formy(new HashSet<>(Arrays.asList(formaDrzewo)))
            .gleby(new HashSet<>(Arrays.asList(glebaPrzecietna, glebaProchniczna, glebaGliniasta)))
            .grupy(new HashSet<>(Arrays.asList(grupaLisciaste)))
            .koloryLisci(new HashSet<>(Arrays.asList(kolorLisciCiemnozielone)))
            .koloryKwiatow(new HashSet<>(Arrays.asList(kolorKwiatowKremowy)))
            .kwiaty(new HashSet<>(Arrays.asList(kwiatPojedynczy, kwiatPachnace)))
            .odczyny(Collections.emptySet())
            .okresyKwitnienia(new HashSet<>(Arrays.asList(okresKwitnieniaWrzesien)))
            .okresyOwocowania(new HashSet<>(Arrays.asList(okresOwocowaniaPazdziernik, okresOwocowaniaListopad)))
            .owoce(new HashSet<>(Arrays.asList(owocBrazowy)))
            .podgrupa(new HashSet<>(Arrays.asList(podgrupaLiscisteDrzewa)))
            .pokroje(new HashSet<>(Arrays.asList(pokrojDrzewiastyRozlozysty, pokrojSzerokostoszkowy)))
            .silyWzrostu(new HashSet<>(Arrays.asList(silaWzrostuTypowa)))
            .stanowiska(new HashSet<>(Arrays.asList(stanowiskoSloneczne)))
            .walory(new HashSet<>(Arrays.asList(walorPachnaceKwiaty, walorRoslinaMiododajna)))
            .build();   
        roslina = lipaHenryego;

        System.out.println("\nUsuwanie rośliny testowej\n");
        roslinaService.deleteByNazwaLacinska(nazwaLacinska);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Order(1)
    void testRoslinaRequestInvalidDataAccessResourceUsageException(){
        Roslina emptyRoslina2 = Roslina.builder().build();
        RoslinaRequest emptyRoslinaRequest2 = roslinaMapper.toRoslinaRequest(emptyRoslina2);
        Exception exception = assertThrows(NullPointerException.class, () -> {
            roslinaController.saveRoslina(emptyRoslinaRequest2, null, mockAuth);
        });
        assertNotNull(exception);
    }

    @Test
    void testSaveRoslinaWithoutRelations() {
        String lacinskaNazwa2 = "jeśli taka łacińska nazwa się znajdzie to będę bardzo zdziwiony";
        Roslina roslinaWithoutRelations = Roslina.builder()
            .nazwa(roslinaNazwa)
            .nazwaLacinska(lacinskaNazwa2)
            .opis(roslinaOpis)
            .wysokoscMin(wysokoscMin)
            .wysokoscMax(wysokoscMax)
           // .obraz(roslinaObraz)
            .build();

        RoslinaRequest emptyRoslinaRequest = roslinaMapper.toRoslinaRequest(roslinaWithoutRelations);
        ResponseEntity<RoslinaResponse> response = roslinaController.saveRoslina(emptyRoslinaRequest, null, mockAuth);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Roslina roslina2 = roslinaRepository.findByNazwaLacinska(lacinskaNazwa2).get();
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

        roslinaService.deleteByNazwaLacinska(lacinskaNazwa2);
    }

    @Test
    @Order(2)
    void testSaveRoslina() {
        RoslinaRequest roslinaRequest = roslinaMapper.toRoslinaRequest(roslina);
        ResponseEntity<RoslinaResponse> response = roslinaController.saveRoslina(roslinaRequest, null, mockAuth);
       
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
      //  Roslina roslina2 = roslinaController.saveRoslina(roslinaRequest).getBody();
        Roslina roslina2 = roslinaRepository.findByNazwaLacinskaWithCechy(nazwaLacinska).get();
        // Assert
        
        Assertions.assertThat(roslina2).isNotNull();
        Assertions.assertThat(roslina2.getId()).isNotNull();
        Assertions.assertThat(roslina2.getNazwa()).isEqualTo(roslina.getNazwa());
        Assertions.assertThat(roslina2.getNazwaLacinska()).isEqualTo(roslina.getNazwaLacinska());
        Assertions.assertThat(roslina2.getOpis()).isEqualTo(roslina.getOpis());
        Assertions.assertThat(roslina2.getWysokoscMin()).isEqualTo(roslina.getWysokoscMin());
        Assertions.assertThat(roslina2.getWysokoscMax()).isEqualTo(roslina.getWysokoscMax());

        System.out.println("\n\n\nroslina2 gleby: "+ roslina2.getGleby().toString() + " ||| " + roslina.getGleby().toString() + "    :roslina]\n\n\n");
        
        Assertions.assertThat(roslina2.areCechyEqual(roslina2.getFormy(), roslina.getFormy())).isTrue();
        Assertions.assertThat(roslina2.areCechyEqual(roslina2.getGleby(), roslina.getGleby())).isTrue();
        Assertions.assertThat(roslina2.areCechyEqual(roslina2.getKoloryLisci(), roslina.getKoloryLisci())).isTrue();
        Assertions.assertThat(roslina2.areCechyEqual(roslina2.getKoloryKwiatow(), roslina.getKoloryKwiatow())).isTrue();
        // Reszty nie trzeba bo nie ma zbytniej różnicy

        System.out.println("\n\n\nZakończono test dodawania roślin.\n\n\n");
    }

    @Test
    @Order(3)
    void testUpdateRoslina() {
        RoslinaRequest roslinaRequestOld = roslinaMapper.toRoslinaRequest(roslina);
        roslinaService.save(roslinaRequestOld, null);

        String nazwa2 = "Zmieniona nazwa";
        Set<Cecha> grupa2 = new HashSet<>(Arrays.asList(new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.GRUPA.getBackendValue()), "owocowe")));
        Cecha owoc22 = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.OWOC.getBackendValue()),"rzułte");

        // Zmiana nazwy
        roslina.setNazwa(nazwa2);

        // Usunięcie właściwości
        roslina.setFormy(Collections.emptySet());

        // Zamiana właściwości
        roslina.setGrupy(grupa2);

        // Dodanie właściwości
        Set<Cecha> owoceOld = roslina.getOwoce();
        Set<Cecha> owoceNew = new HashSet<>(owoceOld);  // Zamiana na modyfikowalną listę
        owoceNew.addAll(Arrays.asList(owoc22));
        roslina.setOwoce(owoceNew);  // Ustawienie zaktualizowanej listy

        RoslinaRequest roslinaRequest = roslinaMapper.toRoslinaRequest(roslina);
        ResponseEntity<RoslinaResponse> response = roslinaController.updateRoslina(roslinaRequest.getNazwaLacinska(), roslinaRequest);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        Roslina roslina2 = roslinaRepository.findByNazwaLacinskaWithCechy(nazwaLacinska).get();
        
        Assertions.assertThat(roslina2).isNotNull();
        Assertions.assertThat(roslina2.getId()).isNotNull();

        // Ta część jest ważna
        Assertions.assertThat(roslina2.getNazwa()).isEqualTo(nazwa2);
        
        Assertions.assertThat(roslina2.areCechyEqual(roslina2.getFormy(), Collections.emptySet())).isTrue();
        Assertions.assertThat(roslina2.areCechyEqual(roslina2.getGrupy(), grupa2)).isTrue();

        //System.out.println("\n\n\n + OwoceOld: " + owoceOld + " Roslina: " + roslina2.getOwoce() + " ||| " + owoceNew + " :OwoceNew\n\n\n");

        Assertions.assertThat(roslina2.areCechyEqual(roslina2.getOwoce(), owoceNew)).isTrue();

        System.out.println("\n\n\nZakończono test auktualizacji rośliny.\n\n\n");
    }

    @Test
    @Order(4) 
    void testDeleteRoslina() {
        System.out.println("\n\n\nRozpoczęto test usuwania roślin.\n\n\n");
        System.out.println("\n\n\nRoslina: " + roslina + "\n\n\n");
        System.out.println("\n\n\nNazwa: " + nazwaLacinska + "\n\n\n");
        System.out.println("\n\n\n<a[[er]]>: " + roslinaMapper.toRoslinaRequest(roslina) + "\n\n\n");
        
        RoslinaRequest roslinaRequest = roslinaMapper.toRoslinaRequest(roslina);
        ResponseEntity<RoslinaResponse> response = roslinaController.saveRoslina(roslinaRequest, null, mockAuth);
    
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Roslina roslina2 = roslinaRepository.findByNazwaLacinska(nazwaLacinska).get();

        roslinaController.deleteRoslina(roslina2.getUuid(), mockAuth);

        Assertions.assertThat(roslinaRepository.findByNazwaLacinska(nazwaLacinska)).isEmpty();

        System.out.println("\n\n\nZakończono test usuwania roślin.\n\n\n");
    }
}
