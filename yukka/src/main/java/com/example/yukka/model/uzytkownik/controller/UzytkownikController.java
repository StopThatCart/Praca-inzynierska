package com.example.yukka.model.uzytkownik.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.auth.requests.EmailRequest;
import com.example.yukka.auth.requests.UsunKontoRequest;
import com.example.yukka.common.FileResponse;
import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.models.post.PostResponse;
import com.example.yukka.model.social.models.powiadomienie.controller.PowiadomienieService;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.UzytkownikResponse;
import com.example.yukka.model.uzytkownik.requests.ProfilRequest;
import com.example.yukka.model.uzytkownik.requests.StatystykiDTO;
import com.example.yukka.model.uzytkownik.requests.UstawieniaRequest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


/**
 * Kontroler REST dla zasobów użytkowników.
 * 
 * <ul>
 * <li><strong>UzytkownikService</strong>: Serwis do obsługi operacji na użytkownikach.</li>
 * <li><strong>PowiadomienieService</strong>: Serwis do obsługi powiadomień.</li>
 * </ul>
 * 
 * Endpointy:
 * 
 * <ul>
 * <li><strong>GET /uzytkownicy</strong>: Pobiera listę wszystkich użytkowników.</li>
 * <li><strong>GET /uzytkownicy/nazwa/{nazwa}</strong>: Pobiera użytkownika na podstawie nazwy.</li>
 * <li><strong>GET /uzytkownicy/email/{email}</strong>: Pobiera użytkownika na podstawie emaila.</li>
 * <li><strong>GET /uzytkownicy/avatar</strong>: Pobiera avatar zalogowanego użytkownika.</li>
 * <li><strong>GET /uzytkownicy/ustawienia</strong>: Pobiera ustawienia zalogowanego użytkownika.</li>
 * <li><strong>GET /uzytkownicy/blokowani</strong>: Pobiera listę zablokowanych i blokujących użytkowników.</li>
 * <li><strong>PATCH /uzytkownicy/ustawienia</strong>: Aktualizuje ustawienia zalogowanego użytkownika.</li>
 * <li><strong>GET /uzytkownicy/profil/{nazwa}</strong>: Pobiera statystyki użytkownika na podstawie nazwy.</li>
 * <li><strong>PATCH /uzytkownicy/profil</strong>: Aktualizuje profil zalogowanego użytkownika.</li>
 * <li><strong>POST /uzytkownicy/send-zmiana-email</strong>: Wysyła żądanie zmiany emaila.</li>
 * <li><strong>PATCH /uzytkownicy/avatar</strong>: Aktualizuje avatar zalogowanego użytkownika.</li>
 * <li><strong>PATCH /uzytkownicy/blok/{nazwa}/{blok}</strong>: Ustawia blokadę na użytkownika.</li>
 * <li><strong>DELETE /uzytkownicy</strong>: Usuwa konto zalogowanego użytkownika.</li>
 * </ul>
 */
@RestController
@RequestMapping("uzytkownicy")
@RequiredArgsConstructor
@Tag(name = "Uzytkownik")
public class UzytkownikController {
    @Autowired
    UzytkownikService uzytkownikService;
    @Autowired
    PowiadomienieService powiadomienieService;


    /**
     * Metoda obsługująca żądanie GET na endpoint "/szukaj", zwracająca stronę z użytkownikami w formacie JSON.
     *
     * @param page numer strony do pobrania, domyślnie 0
     * @param size liczba elementów na stronie, domyślnie 10
     * @param szukaj opcjonalny parametr wyszukiwania
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierające stronę z użytkownikami w formacie JSON
     */
    @GetMapping(value = "/szukaj", produces="application/json")
    public ResponseEntity<PageResponse<UzytkownikResponse>> findAllUzytkownicy(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
           // @RequestParam(name="sortBy", defaultValue = "10", required = false) String sortBy,
            @RequestParam(name = "szukaj", required = false) String szukaj,
            Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.findAllUzytkownicy(page, size, szukaj, connectedUser));
    }
    
    
    /**
     * Metoda obsługująca żądanie GET na endpoint "/nazwa/{nazwa}".
     * Zwraca obiekt UzytkownikResponse w formacie JSON na podstawie podanej nazwy użytkownika.
     *
     * @param nazwa nazwa użytkownika, którego dane mają zostać zwrócone
     * @return ResponseEntity zawierające obiekt UzytkownikResponse z danymi użytkownika
     */
    @GetMapping(value = "/nazwa/{nazwa}", produces="application/json")
    public ResponseEntity<UzytkownikResponse> findByNazwa(@PathVariable("nazwa") String nazwa) {
        return ResponseEntity.ok(uzytkownikService.findByNazwa(nazwa));
    }


    /**
     * Metoda obsługująca żądanie GET na endpoint "/email/{email}".
     * Zwraca obiekt UzytkownikResponse na podstawie podanego adresu email.
     *
     * @param email Adres email użytkownika, którego dane mają zostać zwrócone.
     * @return ResponseEntity zawierający obiekt UzytkownikResponse z danymi użytkownika.
     */
    @GetMapping(value = "/email/{email}", produces="application/json")
    public ResponseEntity<UzytkownikResponse> findByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(uzytkownikService.findByEmail(email));
    }

    /**
     * Pobiera awatar zalogowanego użytkownika.
     *
     * @param connectedUser obiekt Authentication reprezentujący zalogowanego użytkownika
     * @return ResponseEntity zawierający obiekt FileResponse z awatarem użytkownika
     */
    @GetMapping(value = "/avatar", produces="application/json")
    public ResponseEntity<FileResponse> getAvatar(Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.getLoggedInAvatar(connectedUser));
    }

    /**
     * Metoda obsługująca żądanie GET na endpoint "/ustawienia".
     * Zwraca ustawienia użytkownika w formacie JSON.
     *
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierający obiekt UzytkownikResponse z ustawieniami użytkownika
     */
    @GetMapping(value = "/ustawienia", produces="application/json")
    public ResponseEntity<UzytkownikResponse> getUstawienia(Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.getUstawienia(connectedUser));
    }

    /**
     * Endpoint do pobierania listy zablokowanych użytkowników oraz użytkowników blokujących.
     *
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierające obiekt UzytkownikResponse z listą zablokowanych i blokujących użytkowników
     */
    @GetMapping(value = "/blokowani", produces="application/json")
    public ResponseEntity<UzytkownikResponse> getBlokowaniAndBlokujacy(Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.getBlokowaniAndBlokujacy(connectedUser));
    }

    /**
     * Aktualizuje ustawienia użytkownika.
     *
     * @param ustawienia obiekt zawierający nowe ustawienia użytkownika
     * @param connectedUser uwierzytelniony użytkownik, który dokonuje zmiany
     * @return odpowiedź zawierająca zaktualizowane dane użytkownika
     */
    @PatchMapping(value = "/ustawienia", consumes="multipart/form-data", produces="application/json")
    public ResponseEntity<UzytkownikResponse> updateUstawienia(@Valid @RequestPart("ustawienia") UstawieniaRequest ustawienia, Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.updateUstawienia(ustawienia, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie HTTP GET na endpoint "/profil/{nazwa}".
     * Zwraca statystyki użytkownika w formacie JSON.
     *
     * @param nazwa Nazwa użytkownika, którego statystyki mają zostać pobrane.
     * @param connectedUser Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     * @return ResponseEntity zawierające obiekt StatystykiDTO z danymi statystyk użytkownika.
     */
    @GetMapping(value = "/profil/{nazwa}",  produces="application/json")
    public ResponseEntity<StatystykiDTO> getStatystykiOfUzytkownik(@PathVariable("nazwa") String nazwa, Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.getStatystykiOfUzytkownik(nazwa, connectedUser));
    }

    /**
     * Aktualizuje profil użytkownika.
     *
     * @param request         Obiekt ProfilRequest zawierający dane do aktualizacji profilu.
     * @param connectedUser   Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     * @return                ResponseEntity zawierający zaktualizowany profil użytkownika w formacie JSON.
     */
    @PatchMapping(value = "/profil", consumes="multipart/form-data", produces="application/json")
    public ResponseEntity<UzytkownikResponse> updateProfil(@Valid @RequestPart("profil") ProfilRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.updateProfil(request, connectedUser));
    }

    /**
     * Obsługuje żądanie zmiany adresu e-mail użytkownika.
     *
     * @param request obiekt EmailRequest zawierający dane dotyczące nowego adresu e-mail
     * @param currentUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity z kodem statusu 202 (Accepted) w przypadku pomyślnego przetworzenia żądania
     * @throws MessagingException w przypadku problemów z wysyłaniem wiadomości e-mail
     */
    @PostMapping(value = "/send-zmiana-email", produces="application/json")
    public ResponseEntity<?> sendChangeEmail(@Valid @RequestBody EmailRequest request, Authentication currentUser) throws MessagingException {
        uzytkownikService.sendChangeEmail(request, currentUser);
        return ResponseEntity.accepted().build();
    }


    /**
     * Aktualizuje awatar użytkownika.
     *
     * @param file Plik zawierający nowy awatar użytkownika.
     * @param connectedUser Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     * @return ResponseEntity zawierający zaktualizowane dane użytkownika w formacie JSON.
     */
    @PatchMapping(value = "/avatar", consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<UzytkownikResponse> updateAvatar(@Parameter() @RequestPart("file") MultipartFile file, Authentication connectedUser) {
        return ResponseEntity.ok(uzytkownikService.updateUzytkownikAvatar(file, connectedUser));
    }

    /**
     * Blokuje/odblokowuje użytkownika dla aktualnie zalogowanego użytkownika.
     *
     * @param nazwa Nazwa użytkownika, którego blokada ma zostać zmieniona.
     * @param blok  Wartość blokady (true - zablokowany, false - odblokowany).
     * @param currentUser Obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     * @return ResponseEntity z wartością true, jeśli operacja się powiodła, w przeciwnym razie false.
     */
    @PatchMapping(value = "/blok/{nazwa}/{blok}", produces="application/json")
    public ResponseEntity<Boolean> setBlokUzytkownik(@PathVariable("nazwa") String nazwa, 
            @PathVariable("blok") boolean blok, Authentication currentUser) {
        return ResponseEntity.ok(uzytkownikService.setBlokUzytkownik(nazwa, currentUser, blok));
    }
    

    /**
     * Usuwa konto zalogoanego użytkownika.
     *
     * @param request obiekt zawierający dane żądania usunięcia konta
     * @param currentUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     */
    @DeleteMapping(consumes = "application/json", produces = "application/json")
    public void removeSelf(@RequestBody UsunKontoRequest request, Authentication currentUser) {
        uzytkownikService.removeSelf(request, currentUser);
    }


}
