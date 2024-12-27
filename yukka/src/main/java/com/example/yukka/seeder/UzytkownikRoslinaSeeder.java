package com.example.yukka.seeder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;

import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.RoslinaResponse;
import com.example.yukka.model.roslina.UzytkownikRoslinaRequest;
import com.example.yukka.model.roslina.controller.UzytkownikRoslinaService;
import com.example.yukka.model.roslina.enums.RoslinaEtykietyFrontend;
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.RequiredArgsConstructor;

/**
 * Klasa <strong>UzytkownikRoslinaSeeder</strong> jest odpowiedzialna za inicjalizację danych roślin użytkownika.
 * 
 * <ul>
 * <li><strong>uzytkownikRoslinaService</strong>: Serwis do obsługi operacji na roślinach użytkownika.</li>
 * <li><strong>roslinaMapper</strong>: Mapper do konwersji obiektów roślin.</li>
 * <li><strong>roslina1</strong>: Przykładowa roślina użytkownika.</li>
 * </ul>
 * 
 * Metoda <strong>seedUzytkownikRosliny</strong> inicjalizuje przykładową roślinę użytkownika i zapisuje ją w bazie danych.
 * 
 * @param uzyt Obiekt użytkownika, dla którego inicjalizowane są rośliny.
 * @return PageResponse<RoslinaResponse> Zwraca stronę z odpowiedzią zawierającą rośliny użytkownika.
 */
@Service
@RequiredArgsConstructor
public class UzytkownikRoslinaSeeder {
    private final UzytkownikRoslinaService uzytkownikRoslinaService;
    //private final UzytkownikRoslinaRepository uzytkownikRoslinaRepository;
    private final RoslinaMapper roslinaMapper;
    //private final DzialkaService dzialkaService;
    

    Roslina roslina1;

	
    /** 
     * @param uzyt
     * @return PageResponse<RoslinaResponse>
     */
    public PageResponse<RoslinaResponse> seedUzytkownikRosliny(Uzytkownik uzyt) {
        String roslinaNazwa = "pierwsza roślina użytkownika";
        String roslinaOpis = "To jest dramat.";
       // String roslinaObraz = "tilia_henryana.jpg";
        Double wysokoscMin = 0.5;
        Double wysokoscMax = 4.0;

		
		Wlasciwosc formaDrzewo = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.FORMA.getBackendValue()), "TAKA TESTOWA");
        Wlasciwosc glebaPrzecietna = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.GLEBA.getBackendValue()),"TEŻ TESTOWA");
        Wlasciwosc glebaProchniczna = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.GLEBA.getBackendValue()),"próchniczna");
        Wlasciwosc glebaGliniasta = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.GRUPA.getBackendValue()),"gliniasta");
        Wlasciwosc grupaLisciaste = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.GRUPA.getBackendValue()), "liściaste");
        Wlasciwosc kolorLisciCiemnozielone = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.KOLOR_LISCI.getBackendValue()),"ciemnozielone");
        Wlasciwosc kolorKwiatowKremowy = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.KOLOR_KWIATOW.getBackendValue()),"kremowe");
        Wlasciwosc kwiatPojedynczy = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.KWIAT.getBackendValue()),"pojedyncze");
        Wlasciwosc kwiatPachnace = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.KWIAT.getBackendValue()),"pachnące");
        Wlasciwosc okresKwitnieniaWrzesien = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.OKRES_KWITNIENIA.getBackendValue()),"wrzesień");
        Wlasciwosc okresOwocowaniaPazdziernik = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.OKRES_OWOCOWANIA.getBackendValue()),"październik");
        Wlasciwosc okresOwocowaniaListopad = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.OKRES_OWOCOWANIA.getBackendValue()),"listopad");
        Wlasciwosc owocBrazowy = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.OWOC.getBackendValue()),"brązowe");
        Wlasciwosc podgrupaLiscisteDrzewa = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.PODGRUPA.getBackendValue()),"liściaste drzewa");
        Wlasciwosc pokrojDrzewiastyRozlozysty = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.POKROJ.getBackendValue()),"drzewiasty rozłożysty");
        Wlasciwosc pokrojSzerokostoszkowy = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.POKROJ.getBackendValue()),"szerokostożkowy");
        Wlasciwosc silaWzrostuTypowa = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.SILA_WZROSTU.getBackendValue()),"wzrost typowy dla gatunku");
        Wlasciwosc stanowiskoSloneczne = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.STANOWISKO.getBackendValue()),"stanowisko słoneczne");
        Wlasciwosc walorPachnaceKwiaty = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.WALOR.getBackendValue()),"pachnące kwiaty");
        Wlasciwosc walorRoslinaMiododajna = new Wlasciwosc(Collections.singletonList(RoslinaEtykietyFrontend.WALOR.getBackendValue()),"roślina miododajna");
        // Resztę właściwości zostawia się pustą.

        Roslina lipaHenryego = Roslina.builder()
            //.id(12345678L)
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

        uzytkownikRoslinaService.save(roslinaRequest, null, uzyt);

      //  System.out.println("ROSLINA1: " + roslina1);
        return uzytkownikRoslinaService.findRoslinyOfUzytkownik(0, 12, null, uzyt.getNazwa(), uzyt);
	}

}
