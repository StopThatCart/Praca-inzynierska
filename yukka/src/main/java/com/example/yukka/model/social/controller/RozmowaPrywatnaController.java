package com.example.yukka.model.social.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatnaResponse;
import com.example.yukka.model.social.service.RozmowaPrywatnaService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Kontroler REST dla zarządzania rozmowami prywatnymi.
 * 
 * <ul>
 * <li><strong>findRozmowyPrywatneOfUzytkownik</strong> - Pobiera listę rozmów prywatnych użytkownika.</li>
 * <li><strong>getRozmowaPrywatnaById</strong> - Pobiera rozmowę prywatną na podstawie identyfikatora.</li>
 * <li><strong>getRozmowaPrywatna</strong> - Pobiera rozmowę prywatną na podstawie nazwy użytkownika.</li>
 * <li><strong>inviteToRozmowaPrywatna</strong> - Zaprasza użytkownika do rozmowy prywatnej.</li>
 * <li><strong>acceptRozmowaPrywatna</strong> - Akceptuje zaproszenie do rozmowy prywatnej.</li>
 * <li><strong>rejectRozmowaPrywatna</strong> - Odrzuca zaproszenie do rozmowy prywatnej.</li>
 * </ul>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("rozmowy")
@Tag(name = "RozmowaPrywatna")
public class RozmowaPrywatnaController {
    private final RozmowaPrywatnaService rozmowaPrywatnaService;
    

    

    /**
     * Metoda obsługująca żądanie GET do pobrania prywatnych rozmów użytkownika.
     *
     * @param page numer strony wyników (opcjonalny, domyślnie 0)
     * @param size rozmiar strony wyników (opcjonalny, domyślnie 10)
     * @param connectedUser uwierzytelniony użytkownik
     * @return ResponseEntity zawierające PageResponse z rozmowami prywatnymi użytkownika
     *
     * <ul>
     *   <li><strong>page</strong>: numer strony wyników (opcjonalny, domyślnie 0)</li>
     *   <li><strong>size</strong>: rozmiar strony wyników (opcjonalny, domyślnie 10)</li>
     *   <li><strong>connectedUser</strong>: uwierzytelniony użytkownik</li>
     * </ul>
     */
    @GetMapping(produces="application/json")
    public ResponseEntity<PageResponse<RozmowaPrywatnaResponse>> findRozmowyPrywatneOfUzytkownik(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
        Authentication connectedUser) {
        PageResponse<RozmowaPrywatnaResponse> rozmowy = rozmowaPrywatnaService.findRozmowyPrywatneOfUzytkownik(page, size, connectedUser);
        return ResponseEntity.ok(rozmowy);
    }


    /**
     * Pobiera rozmowę prywatną na podstawie podanego identyfikatora.
     *
     * @param id <ul><li><strong>Long</strong></li></ul> identyfikator rozmowy prywatnej
     * @param connectedUser <ul><li><strong>Authentication</strong></li></ul> obiekt uwierzytelnienia bieżącego użytkownika
     * @return <ul><li><strong>ResponseEntity<RozmowaPrywatnaResponse></strong></li></ul> odpowiedź zawierająca rozmowę prywatną
     */
    @GetMapping(value = "/id/{id}", produces="application/json")
    public ResponseEntity<RozmowaPrywatnaResponse> getRozmowaPrywatnaById(@PathVariable("id") Long id, Authentication connectedUser) {
        RozmowaPrywatnaResponse rozmowa = rozmowaPrywatnaService.findRozmowaPrywatnaById(id, connectedUser);
        return ResponseEntity.ok(rozmowa);
    }
    
    /**
     * Metoda obsługująca żądanie GET dla uzyskania prywatnej rozmowy użytkownika.
     *
     * @param nazwa <ul><li><strong>uzytkownik-nazwa</strong></li></ul> Nazwa użytkownika, dla którego ma zostać pobrana rozmowa prywatna.
     * @param connectedUser <ul><li><strong>connectedUser</strong></li></ul> Obiekt uwierzytelnionego użytkownika.
     * @return <ul><li><strong>ResponseEntity<RozmowaPrywatnaResponse></strong></li></ul> Odpowiedź zawierająca prywatną rozmowę użytkownika w formacie JSON.
     */
    @GetMapping(value = "/{uzytkownik-nazwa}", produces="application/json")
    public ResponseEntity<RozmowaPrywatnaResponse> getRozmowaPrywatna(@PathVariable("uzytkownik-nazwa") String nazwa, Authentication connectedUser) {
        RozmowaPrywatnaResponse rozmowa = rozmowaPrywatnaService.findRozmowaPrywatnaByNazwa(nazwa, connectedUser);
        return ResponseEntity.ok(rozmowa);
    }

    /**
     * Zaprasza użytkownika do prywatnej rozmowy.
     *
     * @param nazwa <ul><li><strong>nazwa</strong> - Nazwa użytkownika, który ma zostać zaproszony do rozmowy.</li></ul>
     * @param connectedUser <ul><li><strong>connectedUser</strong> - Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.</li></ul>
     * @return <ul><li><strong>ResponseEntity<RozmowaPrywatnaResponse></strong> - Odpowiedź HTTP zawierająca status oraz obiekt RozmowaPrywatnaResponse.</li></ul>
     */
    @PostMapping(value = "/{uzytkownik-nazwa}", produces="application/json")
    public ResponseEntity<RozmowaPrywatnaResponse> inviteToRozmowaPrywatna(@PathVariable("uzytkownik-nazwa") String nazwa, Authentication connectedUser) {
        RozmowaPrywatnaResponse rozmowa = rozmowaPrywatnaService.inviteToRozmowaPrywatna(nazwa, connectedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(rozmowa);
    }
    
    /**
     * Akceptuje zaproszenie do prywatnej rozmowy.
     *
     * @param nazwa <ul><li><strong>nazwa</strong> - Nazwa użytkownika, który ma zostać zaproszony do rozmowy.</li></ul>
     * @param connectedUser <ul><li><strong>connectedUser</strong> - Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.</li></ul>
     * @return <ul><li><strong>ResponseEntity<RozmowaPrywatna></strong> - Odpowiedź HTTP zawierająca status oraz obiekt RozmowaPrywatna.</li></ul>
     */
    @PutMapping(value = "/{uzytkownik-nazwa}/accept", produces="application/json")
    public ResponseEntity<RozmowaPrywatna> acceptRozmowaPrywatna(@PathVariable("uzytkownik-nazwa") String nazwa, Authentication connectedUser) {
        RozmowaPrywatna rozmowa = rozmowaPrywatnaService.acceptRozmowaPrywatna(nazwa, connectedUser);
        return ResponseEntity.ok(rozmowa);
    }

    /**
     * Odrzuca zaproszenie do prywatnej rozmowy.
     *
     * @param uzytkownikNazwa <ul><li><strong>uzytkownikNazwa</strong> - Nazwa użytkownika, który ma zostać zaproszony do rozmowy.</li></ul>
     * @param connectedUser <ul><li><strong>connectedUser</strong> - Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.</li></ul>
     * @return <ul><li><strong>ResponseEntity<RozmowaPrywatna></strong> - Odpowiedź HTTP zawierająca status oraz obiekt RozmowaPrywatna.</li></ul>
     */
    @PutMapping(value = "/{uzytkownik-nazwa}/reject")
    public ResponseEntity<RozmowaPrywatna> rejectRozmowaPrywatna(@PathVariable("uzytkownik-nazwa") String uzytkownikNazwa, Authentication connectedUser) {
        rozmowaPrywatnaService.rejectRozmowaPrywatna(uzytkownikNazwa, connectedUser);
        return ResponseEntity.ok().build();
    }
}