package com.example.yukka.model.uzytkownik.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.example.yukka.auth.requests.BanRequest;
import com.example.yukka.auth.requests.RegistrationRequest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Kontroler REST dla operacji na pracownikach.
 * 
 * <ul>
 * <li><strong>pracownikService</strong>: Serwis do obsługi operacji na pracownikach.</li>
 * </ul>
 * 
 * <ul>
 * <li><strong>setBanUzytkownik</strong>: Ustawia ban na użytkownika.</li>
 * <li><strong>unbanUzytkownik</strong>: Usuwa ban z użytkownika.</li>
 * <li><strong>remove</strong>: Usuwa użytkownika.</li>
 * </ul>
 */
@RestController
@RequestMapping("pracownicy")
@RequiredArgsConstructor
@Tag(name = "Pracownik")
public class PracownikController {
    //private final UzytkownikService uzytkownikService;
    private final PracownikService pracownikService;

    @PostMapping(consumes="multipart/form-data", produces="application/json")
    public ResponseEntity<?> addPracownik(@RequestPart("request") @Valid RegistrationRequest request) throws MessagingException {
        pracownikService.addPracownik(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Ustawia ban dla użytkownika.
     *
     * @param request <ul><li><strong>BanRequest</strong> - obiekt zawierający szczegóły dotyczące bana</li></ul>
     * @param currentUser <ul><li><strong>Authentication</strong> - obiekt reprezentujący aktualnie zalogowanego użytkownika</li></ul>
     * @return <ul><li><strong>ResponseEntity<Boolean></strong> - odpowiedź zawierająca informację, czy operacja się powiodła</li></ul>
     */
    @PatchMapping(value = "/ban", consumes="multipart/form-data", produces="application/json")
    public ResponseEntity<Boolean> setBanUzytkownik(@Valid @RequestPart("request") BanRequest request, 
    Authentication currentUser) {
        return ResponseEntity.ok(pracownikService.setBanUzytkownik(request, currentUser));
    }


    /**
     * Odblokowuje użytkownika o podanej nazwie.
     *
     * @param nazwa <ul><li><strong>nazwa</strong> - Nazwa użytkownika, który ma zostać odblokowany.</li></ul>
     * @param currentUser <ul><li><strong>currentUser</strong> - Obecnie zalogowany użytkownik wykonujący operację.</li></ul>
     * @return <ul><li><strong>ResponseEntity<Boolean></strong> - Zwraca odpowiedź HTTP z wartością true, jeśli operacja się powiodła.</li></ul>
     */
    @PatchMapping(value = "/unban/{uzytkownik-nazwa}", produces="application/json") 
    public ResponseEntity<Boolean> unbanUzytkownik(@PathVariable("uzytkownik-nazwa") String nazwa, 
    Authentication currentUser) {
        return ResponseEntity.ok(pracownikService.unbanUzytkownik(nazwa, currentUser));
    }

    /**
     * Usuwa użytkownika o podanej nazwie.
     *
     * @param nazwa <ul><li><strong>nazwa</strong> - Nazwa użytkownika, który ma zostać usunięty.</li></ul>
     * @param currentUser <ul><li><strong>currentUser</strong> - Obecnie zalogowany użytkownik wykonujący operację.</li></ul>
     */
    @DeleteMapping("/{uzytkownik-nazwa}")
    public ResponseEntity<String> remove(@PathVariable("uzytkownik-nazwa") String nazwa, Authentication currentUser) {
        pracownikService.remove(nazwa, currentUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
