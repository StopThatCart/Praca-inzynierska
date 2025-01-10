package com.example.yukka.seeder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;

import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.RoslinaResponse;
import com.example.yukka.model.roslina.RoslinaWlasnaRequest;
import com.example.yukka.model.roslina.cecha.Cecha;
import com.example.yukka.model.roslina.controller.RoslinaWlasnaService;
import com.example.yukka.model.roslina.enums.RoslinaEtykietyFrontend;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.RequiredArgsConstructor;

/**
 * Klasa <strong>RoslinaWlasnaSeeder</strong> jest odpowiedzialna za inicjalizację danych roślin użytkownika.
 * 
 * <ul>
 * <li><strong>roslinaWlasnaService</strong>: Serwis do obsługi operacji na roślinach użytkownika.</li>
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
public class RoslinaWlasnaSeeder {
    private final RoslinaWlasnaService roslinaWlasnaService;
    //private final RoslinaWlasnaRepository uzytkownikRoslinaRepository;
    private final RoslinaMapper roslinaMapper;
    //private final DzialkaService dzialkaService;
    

    Roslina roslina1;

	
    /** 
     * @param uzyt
     * @return PageResponse<RoslinaResponse>
     */
    public PageResponse<RoslinaResponse> seedUzytkownikRosliny(Uzytkownik uzyt) {
        String roslinaNazwa = "Pierwsza roślina użytkownika";
        String roslinaOpis = "Jest to pierwsza roślina.";
       // String roslinaObraz = "tilia_henryana.jpg";
        Double wysokoscMin = 0.5;
        Double wysokoscMax = 4.0;

		
		Cecha formaDrzewo = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.FORMA.getBackendValue()), "TAKA TESTOWA");
        Cecha glebaPrzecietna = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.GLEBA.getBackendValue()),"TEŻ TESTOWA");
        Cecha glebaProchniczna = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.GLEBA.getBackendValue()),"próchniczna");
        Cecha glebaGliniasta = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.GRUPA.getBackendValue()),"gliniasta");
        Cecha grupaLisciaste = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.GRUPA.getBackendValue()), "liściaste");
        Cecha kolorLisciCiemnozielone = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.KOLOR_LISCI.getBackendValue()),"ciemnozielone");
        Cecha kolorKwiatowKremowy = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.KOLOR_KWIATOW.getBackendValue()),"kremowe");
        Cecha kwiatPojedynczy = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.KWIAT.getBackendValue()),"pojedyncze");
        Cecha kwiatPachnace = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.KWIAT.getBackendValue()),"pachnące");
        Cecha okresKwitnieniaWrzesien = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.OKRES_KWITNIENIA.getBackendValue()),"wrzesień");
        Cecha okresOwocowaniaPazdziernik = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.OKRES_OWOCOWANIA.getBackendValue()),"październik");
        Cecha okresOwocowaniaListopad = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.OKRES_OWOCOWANIA.getBackendValue()),"listopad");
        Cecha owocBrazowy = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.OWOC.getBackendValue()),"brązowe");
        Cecha podgrupaLiscisteDrzewa = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.PODGRUPA.getBackendValue()),"liściaste drzewa");
        Cecha pokrojDrzewiastyRozlozysty = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.POKROJ.getBackendValue()),"drzewiasty rozłożysty");
        Cecha pokrojSzerokostoszkowy = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.POKROJ.getBackendValue()),"szerokostożkowy");
        Cecha silaWzrostuTypowa = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.SILA_WZROSTU.getBackendValue()),"wzrost typowy dla gatunku");
        Cecha stanowiskoSloneczne = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.STANOWISKO.getBackendValue()),"stanowisko słoneczne");
        Cecha walorPachnaceKwiaty = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.WALOR.getBackendValue()),"pachnące kwiaty");
        Cecha walorRoslinaMiododajna = new Cecha(Collections.singletonList(RoslinaEtykietyFrontend.WALOR.getBackendValue()),"roślina miododajna");
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

        RoslinaWlasnaRequest roslinaRequest = roslinaMapper.toRoslinaWlasnaRequest(roslina1);

        roslinaWlasnaService.save(roslinaRequest, null, uzyt);

      //  System.out.println("ROSLINA1: " + roslina1);
        return roslinaWlasnaService.findRoslinyOfUzytkownik(0, 12, null, uzyt.getNazwa(), uzyt);
	}

}
