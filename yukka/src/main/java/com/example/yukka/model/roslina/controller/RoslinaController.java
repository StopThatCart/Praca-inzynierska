package com.example.yukka.model.roslina.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.RoslinaRequest;
import com.example.yukka.model.roslina.RoslinaResponse;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscKatalogResponse;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscResponse;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


/**
 * Kontroler dla zasobów związanych z roślinami.
 * 
 * <ul>
 * <li><strong>findAllRoslinyWithParameters</strong> - Wyszukuje rośliny na podstawie podanych parametrów.</li>
 * <li><strong>getWlasciwosciWithRelations</strong> - Pobiera właściwości roślin wraz z relacjami.</li>
 * <li><strong>findByNazwaLacinska</strong> - Wyszukuje roślinę na podstawie nazwy łacińskiej.</li>
 * <li><strong>findByRoslinaId</strong> - Wyszukuje roślinę na podstawie identyfikatora rośliny.</li>
 * <li><strong>saveRoslina</strong> - Zapisuje nową roślinę.</li>
 * <li><strong>updateRoslina</strong> - Aktualizuje dane rośliny na podstawie nazwy łacińskiej.</li>
 * <li><strong>updateRoslinaObraz</strong> - Aktualizuje obraz rośliny na podstawie nazwy łacińskiej.</li>
 * <li><strong>deleteRoslina</strong> - Usuwa roślinę na podstawie identyfikatora rośliny.</li>
 * </ul>
 */
@RestController
@RequestMapping("rosliny")
@Tag(name = "Roslina")
@RequiredArgsConstructor
public class RoslinaController {

    private final RoslinaService roslinaService;
    private final RoslinaMapper roslinaMapper;

    // Pomijam valid bo tutaj się szuka rośliny i wiele parametrów może być pustych
    // Post bo miałem problem z dawaniem requesta w get
    /**
     * Metoda obsługująca żądanie HTTP POST do wyszukiwania roślin z parametrami.
     *
     * @param page numer strony wyników, domyślnie 0
     * @param size rozmiar strony wyników, domyślnie 12
     * @param request obiekt zawierający kryteria wyszukiwania roślin, opcjonalny
     * @return ResponseEntity zawierający stronę wyników z roślinami zgodnymi z podanymi parametrami
     */
    @PostMapping(value="/szukaj", produces="application/json")
    public ResponseEntity<PageResponse<RoslinaResponse>> findAllRoslinyWithParameters(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @RequestBody(required = false) RoslinaRequest request) {
        return ResponseEntity.ok(roslinaService.findAllRoslinyWithParameters(page, size, request));
    }

    /**
     * Endpoint do pobierania właściwości roślin wraz z ich relacjami.
     *
     * @return ResponseEntity zawierające zbiór obiektów WlasciwoscResponse w formacie JSON.
     */
    @GetMapping(value = "/wlasciwosci", produces="application/json")
    public ResponseEntity<Set<WlasciwoscResponse>> getWlasciwosciWithRelations() {
        return ResponseEntity.ok(roslinaService.getWlasciwosciWithRelations());
    }

    @PostMapping(value = "/wlasciwosciQuery", produces="application/json")
    public ResponseEntity<Set<WlasciwoscKatalogResponse>> getWlasciwosciCountFromQuery(
        @RequestBody(required = false) RoslinaRequest request
    ) {

        return ResponseEntity.ok(roslinaService.getWlasciwosciCountFromQuery(request));
    }

    /**
     * Metoda obsługująca żądanie HTTP GET do wyszukiwania rośliny na podstawie nazwy łacińskiej.
     *
     * @param nazwaLacinska nazwa łacińska rośliny
     * @return ResponseEntity zawierające odpowiedź z rośliną w formacie JSON
     */
    @GetMapping(value = "/nazwa-lacinska/{nazwa-lacinska}", produces="application/json")
    public ResponseEntity<RoslinaResponse> findByNazwaLacinska(@PathVariable("nazwa-lacinska") String nazwaLacinska) {
        return ResponseEntity.ok(roslinaService.findByNazwaLacinska(nazwaLacinska.toLowerCase()));
    }

    // @GetMapping(value = "/id/{id}", produces="application/json")
    // public ResponseEntity<RoslinaResponse> findById(@PathVariable("id") Long id) {
    //     return ResponseEntity.ok(roslinaService.findById(id));
    // }

    /**
     * Metoda obsługująca żądanie HTTP GET do wyszukiwania rośliny na podstawie identyfikatora rośliny.
     *
     * @param id identyfikator rośliny
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika. Używany jeśli roślina jest użytkownika.
     * @return ResponseEntity zawierające odpowiedź z rośliną w formacie JSON
     */
    @GetMapping(value = "/roslina-id/{roslina-id}", produces="application/json")
    public ResponseEntity<RoslinaResponse> findByRoslinaId(@PathVariable("roslina-id") String id, Authentication connectedUser) {
        return ResponseEntity.ok(roslinaService.findByRoslinaId(id, connectedUser));
    }

    /**
     * Metoda obsługująca żądanie HTTP POST do zapisywania nowej rośliny.
     *
     * @param request obiekt zawierający dane nowej rośliny
     * @param file obiekt MultipartFile zawierający obraz rośliny, opcjonalny
     * @param currentUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity zawierające odpowiedź z nową rośliną w formacie JSON
     */
    @PostMapping(consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<RoslinaResponse> saveRoslina(@Valid @RequestPart("request") RoslinaRequest request, 
    @Parameter() @RequestPart(value = "file", required = false) MultipartFile file,
    Authentication currentUser) {
        Roslina roslina = roslinaService.save(request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(roslinaMapper.toRoslinaResponse(roslina));
    }

    /**
     * Metoda obsługująca żądanie HTTP PUT do aktualizacji danych rośliny na podstawie nazwy łacińskiej.
     *
     * @param nazwaLacinska nazwa łacińska rośliny
     * @param request obiekt zawierający nowe dane rośliny
     * @return ResponseEntity zawierające odpowiedź z zaktualizowaną rośliną w formacie JSON
     */
    @PutMapping(value = "/{nazwa-lacinska}", consumes="application/json", produces="application/json")
    public ResponseEntity<RoslinaResponse> updateRoslina(@PathVariable("nazwa-lacinska") String nazwaLacinska,
            @Valid @RequestBody RoslinaRequest request) {
        RoslinaResponse roslina = roslinaService.update(nazwaLacinska, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(roslina);
    }

    /**
     * Metoda obsługująca żądanie HTTP PUT do aktualizacji obrazu rośliny na podstawie nazwy łacińskiej.
     *
     * @param nazwaLacinska nazwa łacińska rośliny
     * @param file obiekt MultipartFile zawierający nowy obraz rośliny
     * @return ResponseEntity zawierające odpowiedź z zaktualizowaną rośliną w formacie JSON
     */
    @PutMapping(value = "/{nazwa-lacinska}/obraz", consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<RoslinaResponse> updateRoslinaObraz(
            @PathVariable("nazwa-lacinska") String nazwaLacinska,
            @Parameter() @RequestPart("file") MultipartFile file) {
        RoslinaResponse roslina = roslinaService.uploadRoslinaObraz(nazwaLacinska, file);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(roslina);
    }
    
    /**
     * Metoda obsługująca żądanie HTTP DELETE do usuwania rośliny na podstawie identyfikatora rośliny.
     *
     * @param roslinaId identyfikator rośliny
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity z odpowiedzią HTTP 204 NO CONTENT
     */
    @DeleteMapping("/{roslina-id}")
    public ResponseEntity<String> deleteRoslina(@PathVariable("roslina-id") String roslinaId, Authentication connectedUser) {
        roslinaService.deleteByRoslinaId(roslinaId, connectedUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
