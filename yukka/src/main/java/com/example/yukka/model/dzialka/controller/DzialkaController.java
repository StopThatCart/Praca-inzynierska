package com.example.yukka.model.dzialka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.FileResponse;
import com.example.yukka.model.dzialka.requests.BaseDzialkaRequest;
import com.example.yukka.model.dzialka.requests.DzialkaRoslinaRequest;
import com.example.yukka.model.dzialka.requests.MoveRoslinaRequest;
import com.example.yukka.model.dzialka.response.DzialkaResponse;
import com.example.yukka.model.dzialka.response.ZasadzonaRoslinaResponse;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Kontroler REST dla zasobów Działka.
 * 
 * <ul>
 * <li><strong>getPozycjeInDzialki</strong> - Pobiera listę pozycji w działkach dla zalogowanego użytkownika.</li>
 * <li><strong>getDzialkaOfUzytkownikByNumer</strong> - Pobiera działkę określonego użytkownika na podstawie numeru.</li>
 * <li><strong>saveRoslinaToDzialka</strong> - Zapisuje roślinę do działki.</li>
 * <li><strong>updateRoslinaPozycjaInDzialka</strong> - Aktualizuje pozycję rośliny w działce.</li>
 * <li><strong>updateRoslinaKolorInDzialka</strong> - Aktualizuje kolor rośliny w działce.</li>
 * <li><strong>updateRoslinaObrazInDzialka</strong> - Aktualizuje obraz rośliny w działce.</li>
 * <li><strong>updateRoslinaWyswietlanieInDzialka</strong> - Aktualizuje wyświetlanie rośliny w działce.</li>
 * <li><strong>updateRoslinaNotatkaInDzialka</strong> - Aktualizuje notatkę rośliny w działce.</li>
 * <li><strong>deleteRoslinaFromDzialka</strong> - Usuwa roślinę z działki.</li>
 * <li><strong>deleteRoslinaObrazFromDzialka</strong> - Usuwa obraz rośliny z działki.</li>
 * <li><strong>deleteRoslinaTeksturaFromDzialka</strong> - Usuwa teksturę rośliny z działki.</li>
 * </ul>
 */
@RestController
@RequestMapping("dzialki")
@Tag(name = "Dzialka")
public class DzialkaController {
@Autowired
    private DzialkaService dzialkaService;

    /**
     * Metoda obsługująca żądanie GET do pobrania listy pozycji w działkach. Używana podczas dodawania rośliny do działki i przenoszenia na inną.
     *
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierające listę obiektów DzialkaResponse w formacie JSON
     */
    @GetMapping(value = "/pozycje", produces="application/json")
    public ResponseEntity<List<DzialkaResponse>> getPozycjeInDzialki(Authentication connectedUser) {
        return ResponseEntity.ok(dzialkaService.getPozycjeInDzialki(connectedUser));
    }

    /**
     * Metoda obsługująca żądanie GET do pobrania zasadzonej rośliny w określonej działce.
     *
     * @param numer numer działki
     * @param x współrzędna x rośliny w działce
     * @param y współrzędna y rośliny w działce
     * @param connectedUser uwierzytelniony użytkownik
     * @return ResponseEntity zawierające odpowiedź z zasadzoną rośliną
     */
    @GetMapping(value = "/{numer}/{x}/{y}", produces="application/json")
    public ResponseEntity<ZasadzonaRoslinaResponse> getRoslinaInDzialka(@PathVariable int numer, @PathVariable int x, 
    @PathVariable int y, Authentication connectedUser) {
        return ResponseEntity.ok(dzialkaService.getRoslinaInDzialka(numer, x, y, connectedUser));
    }


    /**
     * Metoda obsługująca żądanie GET do pobrania zasadzonej roślinę w działce na podstawie numeru działki i id rośliny.
     *
     * @param numer numer działki
     * @param uuid id rośliny
     * @param connectedUser uwierzytelniony użytkownik
     * @return ResponseEntity zawierające odpowiedź z zasadzoną rośliną
     */
    @GetMapping(value = "/{numer}/{uuid}", produces="application/json")
    public ResponseEntity<ZasadzonaRoslinaResponse> getRoslinaInDzialkaByUUID(@PathVariable int numer, @PathVariable("uuid") String uuid, 
    Authentication connectedUser) {
        return ResponseEntity.ok(dzialkaService.getRoslinaInDzialka(numer, uuid, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie GET do pobrania działki określonego użytkownika na podstawie numeru.
     *
     * @param numer numer działki
     * @param uzytkownikNazwa nazwa użytkownika
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierające obiekt DzialkaResponse w formacie JSON
     */
    @GetMapping(value = "/{numer}/uzytkownicy/{uzytkownik-nazwa}", produces="application/json")
    public ResponseEntity<DzialkaResponse> getDzialkaOfUzytkownikByNumer(@PathVariable("numer") int numer, 
    @PathVariable("uzytkownik-nazwa") String uzytkownikNazwa, Authentication connectedUser) {
        return ResponseEntity.ok(dzialkaService.getDzialkaOfUzytkownikByNumer(numer, uzytkownikNazwa, connectedUser));
    }



    /**
     * Zmienia nazwę działki o podanym numerze.
     *
     * @param numer numer działki, której nazwa ma zostać zmieniona
     * @param nowaNazwa nowa nazwa dla działki
     * @param connectedUser uwierzytelniony użytkownik wykonujący operację
     * @return ResponseEntity z kodem statusu HTTP 202 (Accepted) w przypadku powodzenia
     */
    @PatchMapping(value = "/{numer}/{nazwa}", produces="application/json")
    public ResponseEntity<String> renameDzialka(@PathVariable int numer, @PathVariable("nazwa") String nowaNazwa, Authentication connectedUser) {
        dzialkaService.renameDzialka(numer, nowaNazwa, connectedUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * Metoda obsługująca żądanie POST do zapisania rośliny do działki.
     *
     * @param request obiekt DzialkaRoslinaRequest reprezentujący dane rośliny
     * @param obraz obiekt MultipartFile reprezentujący obraz rośliny
     * @param tekstura obiekt MultipartFile reprezentujący teksturę rośliny
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierające obiekt DzialkaResponse w formacie JSON
     */
    @PostMapping(value = "/rosliny", consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<DzialkaResponse> saveRoslinaToDzialka(@Valid @RequestPart("request") DzialkaRoslinaRequest request, 
    @Parameter() @RequestPart(value = "obraz", required = false) MultipartFile obraz,
    @Parameter() @RequestPart(value = "tekstura", required = false)  MultipartFile tekstura,
     Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dzialkaService.saveRoslinaToDzialka(request, obraz, tekstura, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie PATCH do aktualizacji pozycji rośliny w działce.
     *
     * @param request obiekt MoveRoslinaRequest reprezentujący dane rośliny
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierające obiekt DzialkaResponse w formacie JSON
     */
    @PatchMapping(value = "/rosliny/pozycja", consumes="application/json", produces="application/json")
    public ResponseEntity<DzialkaResponse> updateRoslinaPozycjaInDzialka(@Valid @RequestBody MoveRoslinaRequest request, 
    Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(dzialkaService.updateRoslinaPozycjaInDzialka(request, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie PATCH do aktualizacji koloru rośliny w działce.
     *
     * @param request obiekt DzialkaRoslinaRequest reprezentujący dane rośliny
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierające obiekt DzialkaResponse w formacie JSON
     */
    @PatchMapping(value = "/rosliny/kolor", consumes="application/json", produces="application/json")
    public ResponseEntity<DzialkaResponse> updateRoslinaKolorInDzialka(@Valid @RequestBody DzialkaRoslinaRequest request,
        Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(dzialkaService.updateRoslinaKolorInDzialka(request, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie PATCH do aktualizacji obrazu albo tekstury rośliny w działce.
     *
     * @param request obiekt DzialkaRoslinaRequest reprezentujący dane rośliny
     * @param obraz obiekt MultipartFile reprezentujący obraz rośliny
     * @param tekstura obiekt MultipartFile reprezentujący teksturę rośliny
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierające obiekt FileResponse w formacie JSON
     */
    @PatchMapping(value = "/rosliny/obraz", consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<FileResponse> updateRoslinaObrazInDzialka(@Valid @RequestPart("request") DzialkaRoslinaRequest request,
        @Parameter() @RequestPart(value = "obraz", required = false) MultipartFile obraz,
        @Parameter() @RequestPart(value = "tekstura", required = false)  MultipartFile tekstura,
        Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(dzialkaService.updateRoslinaObrazInDzialka(request, obraz, tekstura, connectedUser));
    }


    /**
     * Metoda obsługująca żądanie PATCH do aktualizacji wyświetlania rośliny w działce.
     *
     * @param request obiekt DzialkaRoslinaRequest reprezentujący dane rośliny
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierające obiekt DzialkaResponse w formacie JSON
     */
    @PatchMapping(value = "/rosliny/wyswietlanie", consumes = "application/json", produces="application/json")
    public ResponseEntity<DzialkaResponse> updateRoslinaWyswietlanieInDzialka(@Valid @RequestBody DzialkaRoslinaRequest request,
        Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(dzialkaService.updateRoslinaWyswietlanieInDzialka(request, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie PATCH do aktualizacji notatki rośliny w działce.
     *
     * @param request obiekt DzialkaRoslinaRequest reprezentujący dane rośliny
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierające obiekt DzialkaResponse w formacie JSON
     */
    @PatchMapping(value = "/rosliny/notatka", consumes = "application/json", produces="application/json")
    public ResponseEntity<DzialkaResponse> updateRoslinaNotatkaInDzialka(@Valid @RequestBody DzialkaRoslinaRequest request,
        Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(dzialkaService.updateRoslinaNotatkaInDzialka(request, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie DELETE do usunięcia rośliny z działki.
     *
     * @param request obiekt BaseDzialkaRequest reprezentujący dane działki
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierające status NO_CONTENT
     */
    @DeleteMapping(value = "/rosliny", consumes="application/json", produces="application/json")
    public ResponseEntity<String> deleteRoslinaFromDzialka(@Valid @RequestBody BaseDzialkaRequest request, Authentication connectedUser) {
        dzialkaService.deleteRoslinaFromDzialka(request, connectedUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Metoda obsługująca żądanie DELETE do usunięcia obrazu rośliny z działki.
     *
     * @param request obiekt BaseDzialkaRequest reprezentujący dane działki
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierające status NO_CONTENT
     */
    @DeleteMapping(value = "/rosliny/obraz", consumes="application/json", produces="application/json")
    public ResponseEntity<String> deleteRoslinaObrazFromDzialka(@Valid @RequestBody BaseDzialkaRequest request, Authentication connectedUser) {
        dzialkaService.deleteRoslinaObrazInDzialka(request, connectedUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Metoda obsługująca żądanie DELETE do usunięcia tekstury rośliny z działki.
     *
     * @param request obiekt BaseDzialkaRequest reprezentujący dane działki
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierające status NO_CONTENT
     */
    @DeleteMapping(value = "/rosliny/tekstura", consumes="application/json", produces="application/json")
    public ResponseEntity<String> deleteRoslinaTeksturaFromDzialka(@Valid @RequestBody BaseDzialkaRequest request, Authentication connectedUser) {
        dzialkaService.deleteRoslinaTeksturaInDzialka(request, connectedUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
