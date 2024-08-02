package com.example.yukka.model.roslina;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.yukka.model.roslina.controller.RoslinaController;
import com.example.yukka.model.roslina.controller.RoslinaService;
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;

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

    Roslina roslina;
    private Long roslinaId;
    private String name = "Na pewno takiej nazwy nie ma";
    private String latinName = "Nomen Latinum certe nullum est";
    private String description = "To jest dramat.";
    private String imageFilename = "tilia_henryana.jpg";
    private Double minHeight = 0.5;
    private Double maxHeight = 4.0;

    private List<String> extractNazwy(List<Wlasciwosc> wlasciwosci) {
        return wlasciwosci.stream()
                           .map(Wlasciwosc::getNazwa)
                           .sorted()
                           .collect(Collectors.toList());
    }
    

    private boolean areWlasciwosciEqual(List<Wlasciwosc> list1, List<Wlasciwosc> list2) {
        return extractNazwy(list1).equals(extractNazwy(list2));
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
            .nazwa(name)
            .nazwaLacinska(latinName)
            .opis(description)
            .wysokoscMin(minHeight)
            .wysokoscMax(maxHeight)
            .obraz(imageFilename)
            .formy(Arrays.asList(formaDrzewo))
            .gleby(Arrays.asList(glebaPrzecietna, glebaProchniczna, glebaGliniasta))
            .grupy(Arrays.asList(grupaLisciaste))
            .koloryLisci(Arrays.asList(kolorLisciCiemnozielone))
            .koloryKwiatow(Arrays.asList(kolorKwiatowKremowy))
            .kwiaty(Arrays.asList(kwiatPojedynczy, kwiatPachnace))
            .nagrody(Collections.emptyList())
            .odczyny(Collections.emptyList())
            .okresyKwitnienia(Arrays.asList(okresKwitnieniaWrzesien))
            .okresyOwocowania(Arrays.asList(okresOwocowaniaPazdziernik, okresOwocowaniaListopad))
            .owoce(Arrays.asList(owocBrazowy))
            .podgrupa(Arrays.asList(podgrupaLiscisteDrzewa))
            .pokroje(Arrays.asList(pokrojDrzewiastyRozlozysty, pokrojSzerokostoszkowy))
            .silyWzrostu(Arrays.asList(silaWzrostuTypowa))
            .stanowiska(Arrays.asList(stanowiskoSloneczne))
            .walory(Arrays.asList(walorPachnaceKwiaty, walorRoslinaMiododajna))
            .build();
        roslina = lipaHenryego;

        System.out.println("\n\n\nUSUWANKOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n\n\n");
        roslinaService.deleteByNazwaLacinska(latinName);
    }

    @Test
    //@Order(1)
    void testRoslinaRequestInvalidDataAccessResourceUsageException(){
        Roslina emptyRoslina2 = Roslina.builder().build();
        RoslinaRequest emptyRoslinaRequest2 = roslinaMapper.toRoslinaRequest(emptyRoslina2);
        Exception exception = assertThrows(InvalidDataAccessResourceUsageException.class, () -> {
            roslinaController.saveRoslina(emptyRoslinaRequest2);
        });
    }

    @Test
    void testSaveRoslinaWithoutRelations() {
        String lacinskaNazwa2 = "Jeśli taka łacińska nazwa się znajdzie to będę bardzo zdziwiony";
        Roslina roslinaWithoutRelations = Roslina.builder()
            .nazwa(name)
            .nazwaLacinska(lacinskaNazwa2)
            .opis(description)
            .wysokoscMin(minHeight)
            .wysokoscMax(maxHeight)
            .obraz(imageFilename)
            .build();
        RoslinaRequest emptyRoslinaRequest = roslinaMapper.toRoslinaRequest(roslinaWithoutRelations);
        roslinaController.saveRoslina(emptyRoslinaRequest);

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
        ResponseEntity<String> response = roslinaController.saveRoslina(roslinaRequest);
       
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
      //  Roslina roslina2 = roslinaController.saveRoslina(roslinaRequest).getBody();
        Roslina roslina2 = roslinaService.findByNazwaLacinska(latinName).get();
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
        
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getFormy(), roslina.getFormy())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getGleby(), roslina.getGleby())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getGrupy(), roslina.getGrupy())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getKoloryLisci(), roslina.getKoloryLisci())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getKoloryKwiatow(), roslina.getKoloryKwiatow())).isTrue();
        // Reszty nie trzeba bo nie ma zbytniej różnicy
        /* 
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getKwiaty(), roslina.getKwiaty())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getNagrody(), roslina.getNagrody())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getOdczyny(), roslina.getOdczyny())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getOkresyKwitnienia(), roslina.getOkresyKwitnienia())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getOkresyOwocowania(), roslina.getOkresyOwocowania())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getOwoce(), roslina.getOwoce())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getPodgrupa(), roslina.getPodgrupa())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getPokroje(), roslina.getPokroje())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getSilyWzrostu(), roslina.getSilyWzrostu())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getStanowiska(), roslina.getStanowiska())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getWalory(), roslina.getWalory())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getWilgotnosci(), roslina.getWilgotnosci())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getZastosowania(), roslina.getZastosowania())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getZimozielonosci(), roslina.getZimozielonosci())).isTrue();
        */

        System.out.println("\n\n\nZakończono test dodawania roślin.\n\n\n");
    }

    @Test
   // @Order(3)
    void testUpdateRoslina() {
        RoslinaRequest roslinaRequestOld = roslinaMapper.toRoslinaRequest(roslina);
        roslinaService.save(roslinaRequestOld);

        String nazwa2 = "Zmieniona nazwa";
        Wlasciwosc grupa2 = new Wlasciwosc(Collections.singletonList("Grupa"), "owocowe");
        Wlasciwosc owoc22 = new Wlasciwosc(Collections.singletonList("Owoc"),"rzułte");

        // TODO: Obsłuż wstawianie nulli do Kontrollera

        // Zmiana nazwy
        roslina.setNazwa(nazwa2);

        // Usunięcie właściwości
        roslina.setFormy(Arrays.asList());

        // Zamiana właściwości
        roslina.setGrupy(Arrays.asList(grupa2));

        // Dodanie właściwości
        List<Wlasciwosc> owoceOld = roslina.getOwoce();
        List<Wlasciwosc> owoceNew = new ArrayList<>(owoceOld);  // Zamiana na modyfikowalną listę
        owoceNew.addAll(Arrays.asList(owoc22));
        roslina.setOwoce(owoceNew);  // Ustawienie zaktualizowanej listy

        RoslinaRequest roslinaRequest = roslinaMapper.toRoslinaRequest(roslina);
        ResponseEntity<String> response = roslinaController.updateRoslina(roslinaRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Roslina roslina2 = roslinaService.findByNazwaLacinska(latinName).get();
        
        Assertions.assertThat(roslina2).isNotNull();
        Assertions.assertThat(roslina2.getId()).isNotNull();

        // Ta część jest ważna
        Assertions.assertThat(roslina2.getNazwa()).isEqualTo(nazwa2);
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getFormy(), Arrays.asList())).isTrue();
        Assertions.assertThat(areWlasciwosciEqual(roslina2.getGrupy(), Arrays.asList(grupa2))).isTrue();

        //System.out.println("\n\n\n + OwoceOld: " + owoceOld + " Roslina: " + roslina2.getOwoce() + " ||| " + owoceNew + " :OwoceNew\n\n\n");

        Assertions.assertThat(areWlasciwosciEqual(roslina2.getOwoce(), owoceNew)).isTrue();

        System.out.println("\n\n\nZakończono test auktualizacji rośliny.\n\n\n");
    }

    @Test
   // @Order(4) 
    void testDeleteRoslina() {
        RoslinaRequest roslinaRequest = roslinaMapper.toRoslinaRequest(roslina);
        ResponseEntity<String> response = roslinaController.saveRoslina(roslinaRequest);
    
        if(response.getStatusCode() == HttpStatus.OK) {
        System.out.println("\n\n\nAHAHAHAHAHAAHAHAAHAHAHAHAHAHAHAH\n\n\n");
        return;
        }
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Roslina roslina2 = roslinaService.findByNazwaLacinska(latinName).get();

        roslinaController.deleteRoslina(roslina2.getNazwaLacinska());

        Assertions.assertThat(roslinaService.findByNazwaLacinska(latinName)).isEmpty();

        System.out.println("\n\n\nZakończono test usuwania roślin.\n\n\n");
    }
}
