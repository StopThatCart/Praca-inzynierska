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
import com.example.yukka.model.roslina.cecha.CechaKatalogResponse;
import com.example.yukka.model.roslina.cecha.CechaResponse;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


/**
 * Kontroler dla zasobów związanych z roślinami.
 * 
 * <ul>
 * <li><strong>findAllRoslinyWithParameters</strong> - Wyszukuje rośliny na podstawie podanych parametrów.</li>
 * <li><strong>getCechyWithRelations</strong> - Pobiera cechy roślin wraz z relacjami.</li>
 * <li><strong>findByUUID</strong> - Wyszukuje roślinę na podstawie identyfikatora rośliny.</li>
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
    @PostMapping(value="/search", produces="application/json")
    public ResponseEntity<PageResponse<RoslinaResponse>> findAllRoslinyWithParameters(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "12", required = false) int size,
            @RequestBody(required = false) RoslinaRequest request) {
        return ResponseEntity.ok(roslinaService.findAllRoslinyWithParameters(page, size, request));
    }

    /**
     * Endpoint do pobierania cech roślin wraz z ich relacjami.
     *
     * @return ResponseEntity zawierające zbiór obiektów CechaResponse w formacie JSON.
     */
    @GetMapping(value = "/cechy", produces="application/json")
    public ResponseEntity<Set<CechaResponse>> getCechyWithRelations() {
        return ResponseEntity.ok(roslinaService.getCechyWithRelations());
    }

    @PostMapping(value = "/cechyQuery", produces="application/json")
    public ResponseEntity<Set<CechaKatalogResponse>> getCechyCountFromQuery(@RequestBody(required = false) RoslinaRequest request) {

        return ResponseEntity.ok(roslinaService.getCechyCountFromQuery(request));
    }

    /**
     * Metoda obsługująca żądanie HTTP GET do wyszukiwania rośliny na podstawie identyfikatora rośliny.
     *
     * @param uuid identyfikator rośliny
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika. Używany jeśli roślina jest użytkownika.
     * @return ResponseEntity zawierające odpowiedź z rośliną w formacie JSON
     */
    @GetMapping(value = "/{uuid}", produces="application/json")
    public ResponseEntity<RoslinaResponse> findByUUID(@PathVariable("uuid") String uuid, Authentication connectedUser) {
        return ResponseEntity.ok(roslinaService.findByUUID(uuid, connectedUser));
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
     * @param uuid identyfikator rośliny
     * @param request obiekt zawierający nowe dane rośliny
     * @return ResponseEntity zawierające odpowiedź z zaktualizowaną rośliną w formacie JSON
     */
    @PutMapping(value = "/{uuid}", consumes="application/json", produces="application/json")
    public ResponseEntity<RoslinaResponse> updateRoslina(@PathVariable() String uuid,
            @Valid @RequestBody RoslinaRequest request) {
        RoslinaResponse roslina = roslinaService.update(uuid, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(roslina);
    }

    /**
     * Metoda obsługująca żądanie HTTP PUT do aktualizacji obrazu rośliny na podstawie nazwy łacińskiej.
     *
     * @param uuid identyfikator rośliny
     * @param file obiekt MultipartFile zawierający nowy obraz rośliny
     * @return ResponseEntity zawierające odpowiedź z zaktualizowaną rośliną w formacie JSON
     */
    @PutMapping(value = "/{uuid}/obraz", consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<String> updateRoslinaObraz(
            @PathVariable() String uuid,
            @Parameter() @RequestPart(value = "file", required = false) MultipartFile file) {
        roslinaService.uploadRoslinaObraz(uuid, file);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    
    /**
     * Metoda obsługująca żądanie HTTP DELETE do usuwania rośliny na podstawie identyfikatora rośliny.
     *
     * @param uuid identyfikator rośliny
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return ResponseEntity z odpowiedzią HTTP 204 NO CONTENT
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteRoslina(@PathVariable("uuid") String uuid, Authentication connectedUser) {
        roslinaService.deleteByUUID(uuid, connectedUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
