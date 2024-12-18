package com.example.yukka.model.social.controller;


import static org.springframework.http.HttpStatus.CREATED;
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
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.powiadomienie.PowiadomienieResponse;
import com.example.yukka.model.social.request.SpecjalnePowiadomienieRequest;
import com.example.yukka.model.social.request.ZgloszenieRequest;
import com.example.yukka.model.social.service.PowiadomienieService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


/**
 * Kontroler REST dla zarządzania powiadomieniami.
 * <ul>
 * <li><strong>getPowiadomienia</strong> - Pobiera stronę powiadomień użytkownika.</li>
 * <li><strong>getNieprzeczytaneCountOfUzytkownik</strong> - Pobiera liczbę nieprzeczytanych powiadomień użytkownika.</li>
 * <li><strong>sendSpecjalnePowiadomienieToPracownicy</strong> - Wysyła specjalne powiadomienie do pracowników.</li>
 * <li><strong>sendSpecjalnePowiadomienie</strong> - Wysyła specjalne powiadomienie.</li>
 * <li><strong>sendZgloszenie</strong> - Wysyła zgłoszenie.</li>
 * <li><strong>setPowiadomieniePrzeczytane</strong> - Ustawia powiadomienie jako przeczytane.</li>
 * <li><strong>setAllPowiadomieniaPrzeczytane</strong> - Ustawia wszystkie powiadomienia jako przeczytane.</li>
 * <li><strong>remove</strong> - Usuwa powiadomienie.</li>
 * </ul>
 */
@RestController
@RequestMapping("powiadomienia")
@RequiredArgsConstructor
@Tag(name = "Powiadomienie")
public class PowiadomienieController {
    private final PowiadomienieService powiadomienieService;

    

    /**
     * Pobiera powiadomienia użytkownika.
     *
     * @param page <ul><li><strong>page</strong> - numer strony do pobrania, domyślnie 0</li></ul>
     * @param size <ul><li><strong>size</strong> - liczba elementów na stronie, domyślnie 10</li></ul>
     * @param connectedUser <ul><li><strong>connectedUser</strong> - uwierzytelniony użytkownik</li></ul>
     * @return <ul><li><strong>ResponseEntity</strong> - odpowiedź zawierająca stronę z powiadomieniami użytkownika</li></ul>
     */
    @GetMapping(produces="application/json")
    public ResponseEntity<PageResponse<PowiadomienieResponse>> getPowiadomienia(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
        Authentication connectedUser) {
        return ResponseEntity.ok(powiadomienieService.findPowiadomieniaOfUzytkownik(page, size, connectedUser));
    }

    /**
     * Pobiera liczbę nieprzeczytanych powiadomień użytkownika.
     *
     * @param connectedUser <ul><li><strong>connectedUser</strong> - uwierzytelniony użytkownik</li></ul>
     * @return <ul><li><strong>ResponseEntity</strong> - odpowiedź zawierająca liczbę nieprzeczytanych powiadomień użytkownika</li></ul>
     */
    @GetMapping(value = "/nieprzeczytane/count", produces="application/json")
    public ResponseEntity<Integer> getNieprzeczytaneCountOfUzytkownik(Authentication connectedUser) {
        return ResponseEntity.ok(powiadomienieService.getNieprzeczytaneCountOfUzytkownik(connectedUser));
    }

    /**
     * Wysyła specjalne powiadomienie do pracowników.
     *
     * @param request <ul><li><strong>powiadomienieDTO</strong> - obiekt zawierający dane powiadomienia</li></ul>
     * @return <ul><li><strong>ResponseEntity</strong> - odpowiedź z kodem HTTP 201</li></ul>
     */
    @PostMapping(value = "/admin", produces="application/json")
    public ResponseEntity<?> sendSpecjalnePowiadomienieToPracownicy(@RequestBody SpecjalnePowiadomienieRequest request, Authentication connectedUser) {
        powiadomienieService.addSpecjalnePowiadomienieToPracownicy(request, connectedUser);
        return ResponseEntity.status(CREATED).build();
    }   

    /**
     * Wysyła specjalne powiadomienie.
     *
     * @param request <ul><li><strong>powiadomienieDTO</strong> - obiekt zawierający dane powiadomienia</li></ul>
     * @return <ul><li><strong>ResponseEntity</strong> - odpowiedź z kodem HTTP 201</li></ul>
     */
    @PostMapping(value = "/pracownik", produces="application/json")
    public ResponseEntity<?> sendSpecjalnePowiadomienie(@RequestBody SpecjalnePowiadomienieRequest request, Authentication connectedUser) {
        powiadomienieService.addSpecjalnePowiadomienie(request, connectedUser);
        return ResponseEntity.status(CREATED).build();
    }

    /**
     * Wysyła zgłoszenie.
     *
     * @param request <ul><li><strong>request</strong> - obiekt zawierający dane zgłoszenia</li></ul>
     * @param connectedUser <ul><li><strong>connectedUser</strong> - uwierzytelniony użytkownik</li></ul>
     * @return <ul><li><strong>ResponseEntity</strong> - odpowiedź z kodem HTTP 201</li></ul>
     */
    @PostMapping(value = "/zgloszenie", produces="application/json")
    public ResponseEntity<?> sendZgloszenie(@Valid @RequestBody ZgloszenieRequest request, Authentication connectedUser) {
        powiadomienieService.sendZgloszenie(request, connectedUser);
        return ResponseEntity.status(CREATED).build();
    }

    /**
     * Ustawia powiadomienie jako przeczytane.
     *
     * @param id <ul><li><strong>id</strong> - identyfikator powiadomienia</li></ul>
     * @param connectedUser <ul><li><strong>connectedUser</strong> - uwierzytelniony użytkownik</li></ul>
     * @return <ul><li><strong>ResponseEntity</strong> - odpowiedź z obiektem PowiadomienieResponse w formacie JSON</li></ul>
     */
    @PatchMapping(value = "/{id}/przeczytane", produces="application/json")
    public ResponseEntity<PowiadomienieResponse> setPowiadomieniePrzeczytane(@PathVariable("id") Long id, Authentication connectedUser) {
        return ResponseEntity.ok(powiadomienieService.setPrzeczytane(id, connectedUser));
    }

    /**
     * Ustawia wszystkie powiadomienia danego użytkownika jako przeczytane.
     *
     * @param connectedUser <ul><li><strong>connectedUser</strong> - uwierzytelniony użytkownik</li></ul>
     * @return <ul><li><strong>ResponseEntity</strong> - odpowiedź z kodem HTTP 202</li></ul>
     */
    @PatchMapping(value = "/przeczytane", produces="application/json")
    public ResponseEntity<?> setAllPowiadomieniaPrzeczytane(Authentication connectedUser) {
        powiadomienieService.setAllPrzeczytane(connectedUser);
        return ResponseEntity.accepted().build();
    }

    
    /**
     * Ukrywa powiadomienie.
     *
     * @param id <ul><li><strong>id</strong> - identyfikator powiadomienia</li></ul>
     * @param connectedUser <ul><li><strong>connectedUser</strong> - uwierzytelniony użytkownik</li></ul>
     * @return <ul><li><strong>ResponseEntity</strong> - odpowiedź z kodem HTTP 204</li></ul>
     */
    @PatchMapping(value = "/{id}/ukryte", produces="application/json")
    public ResponseEntity<?> ukryjPowiadomienie(@PathVariable("id") Long id, Authentication connectedUser) {
        powiadomienieService.ukryjPowiadomienie(id, connectedUser);
        return ResponseEntity.noContent().build();
    }


    /**
     * Usuwa powiadomienie.
     *
     * @param id <ul><li><strong>id</strong> - identyfikator powiadomienia</li></ul>
     * @param connectedUser <ul><li><strong>connectedUser</strong> - uwierzytelniony użytkownik</li></ul>
     * @return <ul><li><strong>ResponseEntity</strong> - odpowiedź z kodem HTTP 204</li></ul>
     */
    @DeleteMapping(value = "/{id}", produces="application/json")
    public ResponseEntity<?> removePowiadomienie(@PathVariable("id") Long id, Authentication connectedUser) {
        powiadomienieService.removePowiadomienie(id, connectedUser);
        return ResponseEntity.noContent().build();
    }

}
