package com.example.yukka.model.social.models.rozmowaPrywatna.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.models.komentarz.KomentarzResponse;
import com.example.yukka.model.social.models.komentarz.controller.KomentarzService;
import com.example.yukka.model.social.models.rozmowaPrywatna.RozmowaPrywatnaResponse;
import com.example.yukka.model.social.requests.KomentarzRequest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Kontroler REST dla zarządzania rozmowami prywatnymi.
 * 
 * <ul>
 * <li><strong>findRozmowyPrywatneOfUzytkownik</strong> - Pobiera listę rozmów prywatnych użytkownika.</li>
 * <li><strong>getRozmowaPrywatna</strong> - Pobiera rozmowę prywatną na podstawie nazwy użytkownika.</li>
 * <li><strong>inviteToRozmowaPrywatna</strong> - Zaprasza użytkownika do rozmowy prywatnej.</li>
 * <li><strong>acceptRozmowaPrywatna</strong> - Akceptuje zaproszenie do rozmowy prywatnej.</li>
 * <li><strong>rejectRozmowaPrywatna</strong> - Odrzuca zaproszenie do rozmowy prywatnej.</li>
 * <li><strong>addKomentarzToWiadomoscPrywatna</strong> - Dodaje wiadomość do rozmowy prywatnej.</li>
 * </ul>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("rozmowy")
@Tag(name = "RozmowaPrywatna")
public class RozmowaPrywatnaController {
    private final RozmowaPrywatnaService rozmowaPrywatnaService;
    private final KomentarzService komentarzService;
    
    /**
     * Metoda obsługująca żądanie GET do pobrania prywatnych rozmów użytkownika.
     *
     * @param page numer strony wyników (opcjonalny, domyślnie 0)
     * @param size rozmiar strony wyników (opcjonalny, domyślnie 10)
     * @param connectedUser uwierzytelniony użytkownik
     * @return ResponseEntity zawierające PageResponse z rozmowami prywatnymi użytkownika
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
     * Metoda obsługująca żądanie GET dla uzyskania prywatnej rozmowy użytkownika.
     *
     * @param odbiorcaNazwa Nazwa użytkownika, z którym się rozmawia.
     * @param connectedUser Obiekt uwierzytelnionego użytkownika.
     * @return <strong>ResponseEntity<RozmowaPrywatnaResponse></strong> Odpowiedź zawierająca prywatną rozmowę użytkownika w formacie JSON.
     */
    @GetMapping(value = "/{uzytkownik-nazwa}", produces="application/json")
    public ResponseEntity<RozmowaPrywatnaResponse> findRozmowaPrywatnaByNazwa(@PathVariable("uzytkownik-nazwa") String odbiorcaNazwa, Authentication connectedUser) {
        RozmowaPrywatnaResponse rozmowa = rozmowaPrywatnaService.findRozmowaPrywatnaByNazwa(odbiorcaNazwa, connectedUser);
        return ResponseEntity.ok(rozmowa);
    }

    /**
     * Zaprasza użytkownika do prywatnej rozmowy.
     *
     * @param nazwa Nazwa użytkownika, który ma zostać zaproszony do rozmowy.
     * @param connectedUser Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     * @return <strong>ResponseEntity<RozmowaPrywatnaResponse></strong> - Odpowiedź HTTP zawierająca status oraz obiekt RozmowaPrywatnaResponse.
     */
    @PostMapping(value = "/{uzytkownik-nazwa}", produces="application/json")
    public ResponseEntity<?> inviteToRozmowaPrywatna(@PathVariable("uzytkownik-nazwa") String nazwa, Authentication connectedUser) {
        rozmowaPrywatnaService.inviteToRozmowaPrywatna(nazwa, connectedUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    /**
     * Akceptuje zaproszenie do prywatnej rozmowy.
     *
     * @param nadawcaNazwa Nazwa użytkownika, który zaprosił do rozmowy.</li></ul>
     * @param connectedUser Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.</li></ul>
     * @return <strong>ResponseEntity<RozmowaPrywatna></strong> - Odpowiedź HTTP zawierająca status oraz obiekt RozmowaPrywatna.
     */
    @PutMapping(value = "/{uzytkownik-nazwa}/accept", produces="application/json")
    public ResponseEntity<?> acceptRozmowaPrywatna(@PathVariable("uzytkownik-nazwa") String nadawcaNazwa, Authentication connectedUser) {
        rozmowaPrywatnaService.acceptRozmowaPrywatna(nadawcaNazwa, connectedUser);
        return ResponseEntity.accepted().build();
    }

    /**
     * Odrzuca zaproszenie do prywatnej rozmowy.
     *
     * @param uzytkownikNazwa Nazwa użytkownika, który ma zostać zaproszony do rozmowy.
     * @param connectedUser Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     */
    @PutMapping(value = "/{uzytkownik-nazwa}/reject")
    public ResponseEntity<?> rejectRozmowaPrywatna(@PathVariable("uzytkownik-nazwa") String uzytkownikNazwa, Authentication connectedUser) {
        rozmowaPrywatnaService.rejectRozmowaPrywatna(uzytkownikNazwa, connectedUser);
        return ResponseEntity.accepted().build();
    }


    /**
     * Metoda obsługująca żądanie POST do dodania wiadomości do rozmowy prywatnej.
     *
     * @param request obiekt żądania zawierający dane wiadomości
     * @param file plik obrazu (opcjonalnie)
     * @param connectedUser aktualnie zalogowany użytkownik
     * @return ResponseEntity zawierające obiekt KomentarzResponse
     */
    @PostMapping(value =  "/wiadomosc", consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<KomentarzResponse> addKomentarzToWiadomoscPrywatna(
                    @Valid @RequestPart("request") KomentarzRequest request, 
                    @Parameter() @RequestPart(value = "file", required = false) MultipartFile file,
                    Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(komentarzService.addKomentarzToWiadomoscPrywatna(request, file, connectedUser));
    }

}