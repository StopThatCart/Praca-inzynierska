
package com.example.yukka.model.ogrod.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.ogrod.OgrodResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Kontroler REST dla zasobów ogrodów.
 * <ul>
 * <li><strong>findAllOgrody</strong> - Pobiera wszystkie ogrody z opcjonalnym filtrowaniem i paginacją.</li>
 * <li><strong>getDzialki</strong> - Pobiera szczegóły ogrodu dla danego użytkownika.</li>
 * <li><strong>setOgrodNazwa</strong> - Ustawia nazwę ogrodu dla danego użytkownika.</li>
 * </ul>
 */
@RestController
@RequestMapping("ogrody")
@Tag(name = "Ogród")
@RequiredArgsConstructor
public class OgrodController {
    private final OgrodService ogrodService;

    
    /**
     * Metoda obsługująca żądanie GET do pobrania wszystkich ogrodów.
     *
     * @param page numer strony do pobrania, domyślnie 0
     * @param size liczba elementów na stronie, domyślnie 10
     * @param szukaj opcjonalny parametr wyszukiwania
     * @return ResponseEntity zawierające stronę z odpowiedziami OgrodResponse
     */
    @GetMapping(produces="application/json")
    public ResponseEntity<PageResponse<OgrodResponse>> findAllOgrody(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "szukaj", required = false) String szukaj) {
        return ResponseEntity.ok(ogrodService.findAllOgrody(page, size, szukaj));
    }

    /**
     * Metoda obsługująca żądanie GET do pobrania działek użytkownika.
     *
     * @param uzytkownikNazwa nazwa użytkownika, którego działki mają zostać pobrane
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierające odpowiedź z działkami użytkownika w formacie JSON
     */
    @GetMapping(value = "/{uzytkownik-nazwa}", produces="application/json")
    public ResponseEntity<OgrodResponse> getDzialki(@PathVariable("uzytkownik-nazwa") String uzytkownikNazwa, 
    Authentication connectedUser) {
        return ResponseEntity.ok(ogrodService.getOgrodOfUzytkownik(uzytkownikNazwa, connectedUser));
    }

    /**
     * Aktualizuje nazwę ogrodu na podstawie podanej nazwy ogrodu.
     *
     * @param ogrodNazwa nowa nazwa ogrodu przekazana w ścieżce URL
     * @param connectedUser uwierzytelniony użytkownik wykonujący żądanie
     * @return odpowiedź HTTP z nową nazwą ogrodu w formacie JSON
     */
    @PatchMapping(value = "/{nazwa}", produces="application/json")
    public ResponseEntity<String> setOgrodNazwa(@PathVariable("nazwa") String ogrodNazwa, 
    Authentication connectedUser) {
        ogrodService.setOgrodNazwa(ogrodNazwa, connectedUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
