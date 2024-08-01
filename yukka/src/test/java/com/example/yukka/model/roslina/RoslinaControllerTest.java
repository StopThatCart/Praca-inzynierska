package com.example.yukka.model.roslina;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.AutoConfigureDataNeo4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;

@SpringBootTest
@AutoConfigureDataNeo4j
@ContextConfiguration()
public class RoslinaControllerTest {

    @Autowired
    private RoslinaService roslinaService;
    @Autowired
    private RoslinaMapper roslinaMapper;

    Roslina roslina;

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
        String name = "Na pewno takiej nazwy nie ma";
        String latinName = "Nomen Latinum certe nullum est";
        String description = "To jest dramat.";
        String imageFilename = "tilia_henryana.jpg";
        Double minHeight = 0.5;
        Double maxHeight = 4.0;

        // Tworzenie kilku obiektów Wlasciwosc
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

        // Tworzymy obiekt klasy Roslina
        Roslina lipaHenryego = Roslina.builder()
            //.id(294L)
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
    }


    @Test
    void testSaveRoslina() {
        RoslinaRequest roslinaRequest = roslinaMapper.toRoslinaRequest(roslina);
        Roslina roslina2 = roslinaService.save(roslinaRequest);

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
    }
}
