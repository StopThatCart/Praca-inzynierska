package com.example.yukka.model.social.models.komentarz.controller;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.models.komentarz.KomentarzResponse;
import com.example.yukka.model.social.models.ocenil.OcenaResponse;
import com.example.yukka.model.social.requests.KomentarzRequest;
import com.example.yukka.model.social.requests.OcenaRequest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Kontroler Komentarzy.
 * <ul>
 * <li><strong>findKomentarzById:</strong> Znajduje komentarz po jego identyfikatorze.</li>
 * <li><strong>findKomentarzeOfUzytkownik:</strong> Znajduje komentarze użytkownika na podstawie jego nazwy.</li>
 * <li><strong>addOdpowiedzToKomentarz:</strong> Dodaje odpowiedź do komentarza.</li>
 * <li><strong>addKomentarzToPost:</strong> Dodaje komentarz do posta.</li>
 * <li><strong>addOcenaToKomentarz:</strong> Dodaje ocenę do komentarza (działa również jako aktualizacja).</li>
 * <li><strong>removeOcenaFromKomentarz:</strong> Usuwa ocenę z komentarza.</li>
 * <li><strong>updateKomentarz:</strong> Aktualizuje komentarz.</li>
 * <li><strong>removeKomentarz:</strong> Usuwa komentarz.</li>
 * </ul>
 */
@RestController
@RequestMapping("komentarze")
@RequiredArgsConstructor
@Tag(name = "Komentarz")
public class KomentarzController {
    private final KomentarzService komentarzService;
    
    
    /**
     * Metoda obsługująca żądanie GET do wyszukania komentarza po jego identyfikatorze.
     *
     * @param uuid identyfikator komentarza
     * @return ResponseEntity zawierające obiekt KomentarzResponse
     */
    @GetMapping(value = "/{uuid}", produces="application/json")
    public ResponseEntity<KomentarzResponse> findKomentarzByUUID(@PathVariable() String uuid) {
        
        return ResponseEntity.ok(komentarzService.findByUUIDWithOdpowiedzi(uuid));
    }

    /**
     * Metoda obsługująca żądanie GET do wyszukania komentarzy użytkownika na podstawie jego nazwy.
     *
     * @param page numer strony wyników, domyślnie 0
     * @param size rozmiar strony wyników, domyślnie 10
     * @param nazwa nazwa użytkownika
     * @param connectedUser aktualnie zalogowany użytkownik
     * @return ResponseEntity zawierające stronę wyników z komentarzami użytkownika
     */
    @GetMapping(value = "/uzytkownicy/{nazwa}", produces="application/json")
    public ResponseEntity<PageResponse<KomentarzResponse>> findKomentarzeOfUzytkownik(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
        @PathVariable("nazwa") String nazwa, Authentication connectedUser) {
        return ResponseEntity.ok(komentarzService.findKomentarzeOfUzytkownik(page, size, nazwa, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie POST do dodania odpowiedzi do komentarza.
     *
     * @param request obiekt żądania zawierający dane komentarza
     * @param file plik obrazu (opcjonalnie)
     * @param connectedUser aktualnie zalogowany użytkownik
     * @return ResponseEntity zawierające obiekt KomentarzResponse
     */
    @PostMapping(value = "/odpowiedzi", consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<KomentarzResponse> addOdpowiedzToKomentarz(
                    @Valid @RequestPart("request") KomentarzRequest request, 
                    @Parameter() @RequestPart(value = "file", required = false) MultipartFile file, 
                    Authentication connectedUser) {
        return ResponseEntity.status(CREATED).body(komentarzService.addOdpowiedzToKomentarz(request, file, connectedUser));
    }

    // TODO: Jak masz problem z plikami to w endpointach z plikami zamień @RequestBody na @RequestPart("request")

    /**
     * Metoda obsługująca żądanie POST do dodania komentarza do posta.
     *
     * @param request obiekt żądania zawierający dane komentarza
     * @param file plik obrazu (opcjonalnie)
     * @param connectedUser aktualnie zalogowany użytkownik
     * @return ResponseEntity zawierające obiekt KomentarzResponse
     */
    @PostMapping(value =  "/posty", consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<KomentarzResponse> addKomentarzToPost(
                     @Valid @RequestPart("request") KomentarzRequest request, 
                     @RequestPart(value = "file", required = false) MultipartFile file,
                    Authentication connectedUser) {
        return ResponseEntity.status(CREATED).body(komentarzService.addKomentarzToPost(request, file, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie PUT do dodania oceny do komentarza.
     *
     * @param request obiekt żądania zawierający dane oceny
     * @param connectedUser aktualnie zalogowany użytkownik
     * @return ResponseEntity zawierające obiekt OcenaResponse
     */
    @PutMapping(value = "/oceny", consumes="application/json", produces="application/json")
    public ResponseEntity<OcenaResponse> addOcenaToKomentarz(@Valid @RequestBody OcenaRequest request, Authentication connectedUser) {
        return ResponseEntity.status(CREATED).body(komentarzService.addOcenaToKomentarz(request, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie PATCH do aktualizacji komentarza.
     *
     * @param request obiekt żądania zawierający dane komentarza
     * @param connectedUser aktualnie zalogowany użytkownik
     * @return ResponseEntity zawierające obiekt KomentarzResponse
     */
    @PatchMapping(consumes="application/json", produces="application/json")
    public ResponseEntity<KomentarzResponse> updateKomentarz(
                    @Valid @RequestBody KomentarzRequest request, Authentication connectedUser) {
        return ResponseEntity.status(ACCEPTED).body(komentarzService.updateKomentarz(request, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie DELETE do usunięcia komentarza.
     *
     * @param uuid identyfikator komentarza
     * @param currentUser aktualnie zalogowany użytkownik
     * @return ResponseEntity zawierające informację o usunięciu komentarza
     */
    @DeleteMapping(value = "/{uuid}", produces="application/json")
    public ResponseEntity<String> removeKomentarz(
                    @PathVariable() String uuid,
                    Authentication currentUser) {

        komentarzService.deleteKomentarz(uuid, currentUser);
        return ResponseEntity.noContent().build();
    }
}
