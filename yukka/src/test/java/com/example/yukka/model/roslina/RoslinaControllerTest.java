package com.example.yukka.model.roslina;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.AutoConfigureDataNeo4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscWithRelations;

@SpringBootTest
@AutoConfigureDataNeo4j
@ContextConfiguration()
public class RoslinaControllerTest {

    @Autowired
    private RoslinaService roslinaService;
    @Autowired
    private RoslinaMapper roslinaMapper;

    Roslina roslina;

    @BeforeEach
    void setUp() {
        String name = "Róża";
        String latinName = "Rosa";
        String description = "Piękna roślina ozdobna";
        String imageFilename = "roza.jpg";
        Double minHeight = 0.5;
        Double maxHeight = 4.0;
        List<WlasciwoscWithRelations> properties = List.of(
            new WlasciwoscWithRelations("Grupa", "próchniczna", "MA_GRUPE"),
            new WlasciwoscWithRelations("Podgrupa", "Krzewy", "MA_PODGRUPE")
        );

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

        // Tworzymy obiekt klasy Roslina
        Roslina lipaHenryego = Roslina.builder()
            //.id(294L)
            .nazwa("Na pewno takiej nazwy nie ma")
            .nazwaLacinska("Nomen Latinum certe nullum est")
            .opis("To jest dramat.")
            .wysokoscMin(minHeight)
            .wysokoscMax(maxHeight)
            .obraz("tilia_henryana.jpg")
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
       

        /* 
        RoslinaRequest roslinaRequest = RoslinaRequest.builder()
        .nazwa(roslina.getNazwa())
        .nazwaLacinska(roslina.getNazwaLacinska())
        .opis(roslina.getOpis())
        .obraz(roslina.getObraz())
        .wysokoscMin(roslina.getWysokoscMin())
        .wysokoscMax(roslina.getWysokoscMax())
        .wlasciwosci(properties)
        .build();
        */

        RoslinaRequest roslinaRequest = roslinaMapper.toRoslinaRequest(roslina);
        
        roslinaService.save(roslinaRequest);

        // roslinaService.save2(roslina);

    }
}
