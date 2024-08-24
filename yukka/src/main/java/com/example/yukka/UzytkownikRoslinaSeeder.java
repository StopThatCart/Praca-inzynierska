package com.example.yukka;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.RoslinaRequest;
import com.example.yukka.model.roslina.UzytkownikRoslina;
import com.example.yukka.model.roslina.UzytkownikRoslinaRequest;
import com.example.yukka.model.roslina.controller.UzytkownikRoslinaRepository;
import com.example.yukka.model.roslina.controller.UzytkownikRoslinaService;
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UzytkownikRoslinaSeeder {
    private final UzytkownikRoslinaService uzytkownikRoslinaService;
    private final UzytkownikRoslinaRepository uzytkownikRoslineRepository;
    private final RoslinaMapper roslinaMapper;
    

    UzytkownikRoslina roslina1;

	void seedUzytkownikRosliny(Uzytkownik uzyt) {
        String roslinaNazwa = "pierwsza roślina użytkownika";
        String roslinaOpis = "To jest dramat.";
        String roslinaObraz = "tilia_henryana.jpg";
        Double wysokoscMin = 0.5;
        Double wysokoscMax = 4.0;

		
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

        UzytkownikRoslina lipaHenryego = UzytkownikRoslina.builder()
            //.id(12345678L)
            .roslinaId("12345678")
            .nazwa(roslinaNazwa)
            .opis(roslinaOpis)
            .wysokoscMin(wysokoscMin)
            .wysokoscMax(wysokoscMax)
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

        roslina1 = lipaHenryego;

        UzytkownikRoslinaRequest roslinaRequest = roslinaMapper.toUzytkownikRoslinaRequest(roslina1);

        UzytkownikRoslina res = uzytkownikRoslinaService.save(roslinaRequest, uzyt);

        System.out.println("ROSLINA1: " + roslina1);
	}

}
