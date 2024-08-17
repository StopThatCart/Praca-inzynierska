package com.example.yukka.model.roslina;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.yukka.authorities.ROLE;
import com.example.yukka.model.roslina.controller.RoslinaController;
import com.example.yukka.model.roslina.controller.RoslinaService;
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
//@AutoConfigureDataNeo4j
@Testcontainers
@Slf4j
//@TestInstance(TestInstance.Lifecycle.PER_METHOD)
//@TestMethodOrder(OrderAnnotation.class)
//@ContextConfiguration
public class RoslinaControllerTest {

    @Autowired
    private RoslinaController roslinaController;
    @Autowired
    private RoslinaService roslinaService;
    @Autowired
    private RoslinaMapper roslinaMapper;

    Authentication mockAuth;
    Uzytkownik uzyt;

    Roslina roslina;
    //private Long roslinaId;
    private final String roslinaNazwa = "Na pewno takiej nazwy nie ma";
    private final String nazwaLacinska = "Nomen Latinum certe nullum est";
    private final String roslinaOpis = "To jest dramat.";
    private final String roslinaObraz = "tilia_henryana.jpg";
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
        Wlasciwosc formaDrzewo = new Wlasciwosc(Collections.singletonList("Forma"), "TAKA TESTOWA");

        Wlasciwosc glebaPrzecietna = new Wlasciwosc(Collections.singletonList("Gleba"),"TEŻ TESTOWA");
        Wlasciwosc glebaProchniczna = new Wlasciwosc(Collections.singletonList("Gleba"),"próchniczna");
        Wlasciwosc glebaGliniasta = new Wlasciwosc(Collections.singletonList("Gleba"),"gliniasta");

        Wlasciwosc grupaLisciaste = new Wlasciwosc(Collections.singletonList("Grupa"), "liściaste");

        Wlasciwosc kolorLisciCiemnozielone = new Wlasciwosc(Collections.singletonList("Kolor"),"ciemnozielone");
        Wlasciwosc kolorKwiatowKremowy = new Wlasciwosc(Collections.singletonList("Kolor"),"kremowe");

        Wlasciwosc kwiatPojedynczy = new Wlasciwosc(Collections.singletonList("Kwiat"),"pojedyncze");
        Wlasciwosc kwiatPachnace = new Wlasciwosc(Collections.singletonList("Kwiat"),"pachnące");

        Wlasciwosc okresKwitnieniaWrzesien = new Wlasciwosc(Collections.singletonList("Okres"),"wrzesień");
        Wlasciwosc okresOwocowaniaPazdziernik = new Wlasciwosc(Collections.singletonList("Okres"),"październik");
        Wlasciwosc okresOwocowaniaListopad = new Wlasciwosc(Collections.singletonList("Okres"),"listopad");

        Wlasciwosc owocBrazowy = new Wlasciwosc(Collections.singletonList("Owoc"),"brązowe");

        Wlasciwosc podgrupaLiscisteDrzewa = new Wlasciwosc(Collections.singletonList("Podgrupa"),"liściaste drzewa");

        Wlasciwosc pokrojDrzewiastyRozlozysty = new Wlasciwosc(Collections.singletonList("Pokroj"),"drzewiasty rozłożysty");
        Wlasciwosc pokrojSzerokostoszkowy = new Wlasciwosc(Collections.singletonList("Pokroj"),"szerokostożkowy");

        Wlasciwosc silaWzrostuTypowa = new Wlasciwosc(Collections.singletonList("Silawzrostu"),"wzrost typowy dla gatunku");
        Wlasciwosc stanowiskoSloneczne = new Wlasciwosc(Collections.singletonList("Stanowisko"),"stanowisko słoneczne");

        Wlasciwosc walorPachnaceKwiaty = new Wlasciwosc(Collections.singletonList("Walor"),"pachnące kwiaty");
        Wlasciwosc walorRoslinaMiododajna = new Wlasciwosc(Collections.singletonList("Walor"),"roślina miododajna");
        // Resztę właściwości zostawia się pustą.

        Roslina lipaHenryego = Roslina.builder()
            //.id(12345678L)
            .nazwa(roslinaNazwa)
            .nazwaLacinska(nazwaLacinska)
            .opis(roslinaOpis)
            .wysokoscMin(wysokoscMin)
            .wysokoscMax(wysokoscMax)
            .obraz(roslinaObraz)
            .formy(new HashSet<>(Arrays.asList(formaDrzewo)))
            .gleby(new HashSet<>(Arrays.asList(glebaPrzecietna, glebaProchniczna, glebaGliniasta)))
            .grupy(new HashSet<>(Arrays.asList(grupaLisciaste)))
            .koloryLisci(new HashSet<>(Arrays.asList(kolorLisciCiemnozielone)))
            .koloryKwiatow(new HashSet<>(Arrays.asList(kolorKwiatowKremowy)))
            .kwiaty(new HashSet<>(Arrays.asList(kwiatPojedynczy, kwiatPachnace)))
            .nagrody(Collections.emptySet())
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

        System.out.println("\n\n\nUSUWANKOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n\n\n");
        roslinaService.deleteByNazwaLacinska(nazwaLacinska);
    }

    @Test
  //  @WithMockUser(username = "admin", roles = {"ADMIN"})
    //@Order(1)
    void testRoslinaRequestInvalidDataAccessResourceUsageException(){
        

        Roslina emptyRoslina2 = Roslina.builder().build();
        RoslinaRequest emptyRoslinaRequest2 = roslinaMapper.toRoslinaRequest(emptyRoslina2);
        assertThrows(InvalidDataAccessResourceUsageException.class, () -> {
            roslinaController.saveRoslina(emptyRoslinaRequest2, mockAuth);
        });
    }

    @Test
    void testSaveRoslinaWithoutRelations() {
        String lacinskaNazwa2 = "Jeśli taka łacińska nazwa się znajdzie to będę bardzo zdziwiony";
        Roslina roslinaWithoutRelations = Roslina.builder()
            .nazwa(roslinaNazwa)
            .nazwaLacinska(lacinskaNazwa2)
            .opis(roslinaOpis)
            .wysokoscMin(wysokoscMin)
            .wysokoscMax(wysokoscMax)
            .obraz(roslinaObraz)
            .build();

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

        roslinaService.deleteByNazwaLacinska(lacinskaNazwa2);
    }

    @Test
   // @Order(2)
    void testSaveRoslina() {
        RoslinaRequest roslinaRequest = roslinaMapper.toRoslinaRequest(roslina);
        ResponseEntity<String> response = roslinaController.saveRoslina(roslinaRequest, mockAuth);
       
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
      //  Roslina roslina2 = roslinaController.saveRoslina(roslinaRequest).getBody();
        Roslina roslina2 = roslinaService.findByNazwaLacinska(nazwaLacinska).get();
        // Assert
        
        Assertions.assertThat(roslina2).isNotNull();
        Assertions.assertThat(roslina2.getId()).isNotNull();
        Assertions.assertThat(roslina2.getNazwa()).isEqualTo(roslina.getNazwa());
        Assertions.assertThat(roslina2.getNazwaLacinska()).isEqualTo(roslina.getNazwaLacinska());
        Assertions.assertThat(roslina2.getOpis()).isEqualTo(roslina.getOpis());
        Assertions.assertThat(roslina2.getWysokoscMin()).isEqualTo(roslina.getWysokoscMin());
        Assertions.assertThat(roslina2.getWysokoscMax()).isEqualTo(roslina.getWysokoscMax());
        Assertions.assertThat(roslina2.getFormy()).isEqualTo(roslina.getFormy());

       // System.out.println("\n\n\nroslina2 gleby: "+ roslina2.getGleby().toString() + " ||| " + roslina.getGleby().toString() + "    :roslina]\n\n\n");
        
        Assertions.assertThat(roslina2.areWlasciwosciEqual(roslina2.getFormy(), roslina.getFormy())).isTrue();
        Assertions.assertThat(roslina2.areWlasciwosciEqual(roslina2.getGleby(), roslina.getGleby())).isTrue();
        Assertions.assertThat(roslina2.areWlasciwosciEqual(roslina2.getKoloryLisci(), roslina.getKoloryLisci())).isTrue();
        Assertions.assertThat(roslina2.areWlasciwosciEqual(roslina2.getKoloryKwiatow(), roslina.getKoloryKwiatow())).isTrue();
        // Reszty nie trzeba bo nie ma zbytniej różnicy

        System.out.println("\n\n\nZakończono test dodawania roślin.\n\n\n");
    }

    @Test
   // @Order(3)
    void testUpdateRoslina() {
        RoslinaRequest roslinaRequestOld = roslinaMapper.toRoslinaRequest(roslina);
        roslinaService.save(roslinaRequestOld);

        String nazwa2 = "Zmieniona nazwa";
        Set<Wlasciwosc> grupa2 = new HashSet<>(Arrays.asList(new Wlasciwosc(Collections.singletonList("Grupa"), "owocowe")));
        Wlasciwosc owoc22 = new Wlasciwosc(Collections.singletonList("Owoc"),"rzułte");

        // Zmiana nazwy
        roslina.setNazwa(nazwa2);

        // Usunięcie właściwości
        roslina.setFormy(Collections.emptySet());

        // Zamiana właściwości
        roslina.setGrupy(grupa2);

        // Dodanie właściwości
        Set<Wlasciwosc> owoceOld = roslina.getOwoce();
        Set<Wlasciwosc> owoceNew = new HashSet<>(owoceOld);  // Zamiana na modyfikowalną listę
        owoceNew.addAll(Arrays.asList(owoc22));
        roslina.setOwoce(owoceNew);  // Ustawienie zaktualizowanej listy

        RoslinaRequest roslinaRequest = roslinaMapper.toRoslinaRequest(roslina);
        ResponseEntity<String> response = roslinaController.updateRoslina(roslinaRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Roslina roslina2 = roslinaService.findByNazwaLacinska(nazwaLacinska).get();
        
        Assertions.assertThat(roslina2).isNotNull();
        Assertions.assertThat(roslina2.getId()).isNotNull();

        // Ta część jest ważna
        Assertions.assertThat(roslina2.getNazwa()).isEqualTo(nazwa2);
        
        Assertions.assertThat(roslina2.areWlasciwosciEqual(roslina2.getFormy(), Collections.emptySet())).isTrue();
        Assertions.assertThat(roslina2.areWlasciwosciEqual(roslina2.getGrupy(), grupa2)).isTrue();

        //System.out.println("\n\n\n + OwoceOld: " + owoceOld + " Roslina: " + roslina2.getOwoce() + " ||| " + owoceNew + " :OwoceNew\n\n\n");

        Assertions.assertThat(roslina2.areWlasciwosciEqual(roslina2.getOwoce(), owoceNew)).isTrue();

        System.out.println("\n\n\nZakończono test auktualizacji rośliny.\n\n\n");
    }

    @Test
   // @Order(4) 
    void testDeleteRoslina() {
        RoslinaRequest roslinaRequest = roslinaMapper.toRoslinaRequest(roslina);
        ResponseEntity<String> response = roslinaController.saveRoslina(roslinaRequest, mockAuth);
    
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Roslina roslina2 = roslinaService.findByNazwaLacinska(nazwaLacinska).get();

        roslinaController.deleteRoslina(roslina2.getNazwaLacinska());

        Assertions.assertThat(roslinaService.findByNazwaLacinska(nazwaLacinska)).isEmpty();

        System.out.println("\n\n\nZakończono test usuwania roślin.\n\n\n");
    }
}
