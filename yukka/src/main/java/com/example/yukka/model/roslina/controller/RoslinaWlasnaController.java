package com.example.yukka.model.roslina.controller;


import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import com.example.yukka.model.roslina.RoslinaResponse;
import com.example.yukka.model.roslina.RoslinaWlasnaRequest;
import com.example.yukka.model.roslina.cecha.CechaKatalogResponse;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


/**
 * Kontroler dla operacji związanych z użytkownikami i ich roślinami.
 * <ul>
 * <li><strong>RoslinaWlasnaService</strong>: Serwis obsługujący logikę biznesową dla roślin użytkowników.</li>
 * </ul>
 * 
 * <ul>
 * <li><strong>findAllRoslinyOfUzytkownik</strong>: Wyszukuje wszystkie rośliny użytkownika.
 * <ul>
 * <li><strong>page</strong>: Numer strony wyników.</li>
 * <li><strong>size</strong>: Rozmiar strony wyników.</li>
 * <li><strong>request</strong>: Żądanie zawierające kryteria wyszukiwania.</li>
 * <li><strong>uzytkownikNazwa</strong>: Nazwa użytkownika (opcjonalnie).</li>
 * <li><strong>connectedUser</strong>: Aktualnie zalogowany użytkownik.</li>
 * </ul>
 * </li>
 * 
 * <li><strong>saveRoslina</strong>: Zapisuje nową roślinę.
 * <ul>
 * <li><strong>request</strong>: Żądanie zawierające dane rośliny.</li>
 * <li><strong>file</strong>: Plik obrazu rośliny (opcjonalnie).</li>
 * <li><strong>currentUser</strong>: Aktualnie zalogowany użytkownik.</li>
 * </ul>
 * </li>
 * 
 * <li><strong>updateRoslina</strong>: Aktualizuje dane rośliny.
 * <ul>
 * <li><strong>request</strong>: Żądanie zawierające nowe dane rośliny.</li>
 * <li><strong>connectedUser</strong>: Aktualnie zalogowany użytkownik.</li>
 * </ul>
 * </li>
 * 
 * <li><strong>updateRoslinaObraz</strong>: Aktualizuje obraz rośliny.
 * <ul>
 * <li><strong>roslinaId</strong>: Identyfikator rośliny.</li>
 * <li><strong>file</strong>: Nowy plik obrazu rośliny.</li>
 * <li><strong>connectedUser</strong>: Aktualnie zalogowany użytkownik.</li>
 * </ul>
 * </li>
 * </ul>
 */
@RestController
@RequestMapping("uzytkownikRosliny")
@Tag(name = "RoslinaWlasna")
@RequiredArgsConstructor
public class RoslinaWlasnaController {
    private final RoslinaWlasnaService roslinaWlasnaService;
  

    /**
     * Metoda obsługująca żądanie HTTP POST do wyszukiwania roślin użytkownika.
     *
     * @param page numer strony wyników (domyślnie 0)
     * @param size liczba wyników na stronę (domyślnie 12)
     * @param request obiekt żądania zawierający kryteria wyszukiwania
     * @param uzytkownikNazwa opcjonalna nazwa użytkownika do filtrowania wyników
     * @param connectedUser obiekt uwierzytelnionego użytkownika
     * @return ResponseEntity zawierające stronę wyników z roślinami użytkownika
     */
    @PostMapping(value = "/szukaj", produces="application/json")
    public ResponseEntity<PageResponse<RoslinaResponse>> findAllRoslinyOfUzytkownik(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @RequestBody RoslinaWlasnaRequest request,
            @RequestParam(name = "uzytkownik-nazwa", required = false) String uzytkownikNazwa,
            Authentication connectedUser) {
        return ResponseEntity.ok(roslinaWlasnaService.findRoslinyOfUzytkownik(page, size, request, uzytkownikNazwa, connectedUser));
    }

    @PostMapping(value = "/cechyQuery", produces="application/json")
    public ResponseEntity<Set<CechaKatalogResponse>> getUzytkownikCechyCountFromQuery(
        @RequestBody(required = false) RoslinaWlasnaRequest request,
        @RequestParam(name = "uzytkownik-nazwa", required = false) String uzytkownikNazwa,
        Authentication connectedUser
    ) {

        return ResponseEntity.ok(roslinaWlasnaService.getUzytkownikCechyCountFromQuery(request, uzytkownikNazwa, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie HTTP POST do zapisywania nowej rośliny.
     *
     * @param request obiekt żądania zawierający dane rośliny
     * @param file plik obrazu rośliny (opcjonalnie)
     * @param connectedUser obiekt uwierzytelnionego użytkownika
     * @return ResponseEntity zawierające odpowiedź z informacją o zapisaniu rośliny
     */
    @PostMapping(consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<RoslinaResponse> save(@Valid @RequestPart("request") RoslinaWlasnaRequest request, 
    @Parameter() @RequestPart(value = "file", required = false) MultipartFile file,
    Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roslinaWlasnaService.save(request, file, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie HTTP PATCH do aktualizacji danych rośliny.
     *
     * @param request obiekt żądania zawierający nowe dane rośliny
     * @param connectedUser obiekt uwierzytelnionego użytkownika
     * @return ResponseEntity zawierające odpowiedź z informacją o zaktualizowaniu rośliny
     */
    @PatchMapping(consumes = "application/json", produces="application/json")
    public ResponseEntity<RoslinaResponse> update(@Valid @RequestBody RoslinaWlasnaRequest request, Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(roslinaWlasnaService.update(request, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie HTTP POST do aktualizacji obrazu rośliny.
     *
     * @param roslinaId identyfikator rośliny
     * @param file plik obrazu rośliny
     * @param connectedUser obiekt uwierzytelnionego użytkownika
     * @return ResponseEntity zawierające odpowiedź z informacją o zaktualizowaniu obrazu rośliny
     */
    @PutMapping(value = "/{roslinaId}", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<?> updateObraz(
            @PathVariable("roslinaId") String roslinaId, 
            @Parameter() @RequestPart(value = "file", required = false) MultipartFile file, 
            Authentication connectedUser) {
        roslinaWlasnaService.uploadRoslinaWlasnaObraz(file, roslinaId, connectedUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    
}
