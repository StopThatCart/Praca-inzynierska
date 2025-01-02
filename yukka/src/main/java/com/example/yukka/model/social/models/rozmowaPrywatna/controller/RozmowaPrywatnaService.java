package com.example.yukka.model.social.models.rozmowaPrywatna.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.yukka.common.PageResponse;
import com.example.yukka.handler.exceptions.EntityAlreadyExistsException;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.model.social.mappers.CommonMapperService;
import com.example.yukka.model.social.models.powiadomienie.PowiadomienieDTO;
import com.example.yukka.model.social.models.powiadomienie.TypPowiadomienia;
import com.example.yukka.model.social.models.powiadomienie.controller.PowiadomienieService;
import com.example.yukka.model.social.models.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.social.models.rozmowaPrywatna.RozmowaPrywatnaResponse;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serwis odpowiedzialny za zarządzanie rozmowami prywatnymi.
 * 
 * <ul>
 * <li><strong>RozmowaPrywatnaRepository</strong> - repozytorium do zarządzania rozmowami prywatnymi</li>
 * <li><strong>UzytkownikRepository</strong> - repozytorium do zarządzania użytkownikami</li>
 * <li><strong>CommonMapperService</strong> - serwis do mapowania obiektów</li>
 * <li><strong>PowiadomienieService</strong> - serwis do zarządzania powiadomieniami</li>
 * </ul>
 * 
 * <ul>
 * <li><strong>findRozmowyPrywatneOfUzytkownik</strong> - Znajduje rozmowy prywatne użytkownika</li>
 * <li><strong>findRozmowaPrywatna</strong> - Znajduje rozmowę prywatną na podstawie ID odbiorcy</li>
 * <li><strong>findRozmowaPrywatnaByNazwa</strong> - Znajduje rozmowę prywatną na podstawie nazwy odbiorcy</li>
 * <li><strong>findRozmowaPrywatnaById</strong> - Znajduje rozmowę prywatną na podstawie ID rozmowy</li>
 * <li><strong>inviteToRozmowaPrywatna</strong> - Zaprasza użytkownika do rozmowy prywatnej</li>
 * <li><strong>acceptRozmowaPrywatna</strong> - Akceptuje zaproszenie do rozmowy prywatnej</li>
 * <li><strong>rejectRozmowaPrywatna</strong> - Odrzuca zaproszenie do rozmowy prywatnej</li>
 * </ul>
 * 
 * <ul>
 * <li><strong>createPowiadomienie</strong> - Tworzy powiadomienie o określonym typie</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class RozmowaPrywatnaService {
    private final RozmowaPrywatnaRepository rozmowaPrywatnaRepository;
    private final UzytkownikRepository uzytkownikRepository;
    private final CommonMapperService commonMapperService;
    private final PowiadomienieService powiadomienieService;


    /**
     * Znajduje prywatne rozmowy użytkownika.
     *
     * @param page numer strony do pobrania, domyślnie 0
     * @param size rozmiar strony, domyślnie 10
     * @param currentUser aktualnie zalogowany użytkownik
     * @return odpowiedź zawierająca stronę z prywatnymi rozmowami użytkownika
     */
    public PageResponse<RozmowaPrywatnaResponse> findRozmowyPrywatneOfUzytkownik(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,    
            Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("priv.ostatnioAktualizowana").descending());
        Page<RozmowaPrywatna> rozmowy = rozmowaPrywatnaRepository.findRozmowyPrywatneOfUzytkownik(uzyt.getEmail(), pageable);

        return commonMapperService.rozmowaPrywatnaPagetoPageRozmowaPrywatnaResponse(rozmowy);
    }

    /**
     * Znajduje rozmowę prywatną pomiędzy zalogowanym użytkownikiem a odbiorcą.
     *
     * @param odbiorcaId Identyfikator użytkownika odbiorcy.
     * @param connectedUser Obiekt uwierzytelnienia zalogowanego użytkownika.
     * @return Obiekt RozmowaPrywatnaResponse zawierający szczegóły rozmowy prywatnej.
     * @throws EntityNotFoundException Jeśli użytkownik odbiorca lub rozmowa prywatna nie zostaną znalezione.
     * @throws IllegalArgumentException Jeśli zalogowany użytkownik próbuje rozmawiać sam ze sobą.
     */
    public RozmowaPrywatnaResponse findRozmowaPrywatna(String odbiorcaId, Authentication connectedUser) {
        Uzytkownik nadawca = (Uzytkownik) connectedUser.getPrincipal();

        Uzytkownik odbiorca = uzytkownikRepository.findByUzytId(odbiorcaId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika odbiorcy o nazwie: " + odbiorcaId));

        if (nadawca.getNazwa().equals(odbiorca.getNazwa())) {
            throw new IllegalArgumentException("Nie można rozmawiać sam ze sobą");
        }
        
        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaWithKomentarze(odbiorca.getUzytId(), nadawca.getUzytId())
        .orElseThrow(() -> new EntityNotFoundException("Rozmowa prywatna nie istnieje"));

        return commonMapperService.toRozmowaPrywatnaResponse(rozmowa);
    }

    /**
     * Znajduje rozmowę prywatną na podstawie nazwy użytkownika odbiorcy.
     *
     * @param nazwa Nazwa użytkownika odbiorcy.
     * @param connectedUser Aktualnie zalogowany użytkownik.
     * @return Odpowiedź zawierająca szczegóły rozmowy prywatnej.
     * @throws EntityNotFoundException Jeśli użytkownik odbiorca lub rozmowa prywatna nie zostaną znalezione.
     * @throws IllegalArgumentException Jeśli użytkownik próbuje znaleźć rozmowę ze sobą samym.
     */
    public RozmowaPrywatnaResponse findRozmowaPrywatnaByNazwa(String nazwa, Authentication connectedUser) {
        Uzytkownik nadawca = (Uzytkownik) connectedUser.getPrincipal();

        Uzytkownik odbiorca = uzytkownikRepository.findByNazwa(nazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika odbiorcy o nazwie: " + nazwa));

        if (nadawca.getNazwa().equals(odbiorca.getNazwa())) {
            throw new IllegalArgumentException("Rozmowę ze sobą polecamy poćwiczyć przed lustrem");
        }
        
        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaWithKomentarze(odbiorca.getUzytId(), nadawca.getUzytId())
        .orElseThrow(() -> new EntityNotFoundException("Rozmowa prywatna nie istnieje"));

        return commonMapperService.toRozmowaPrywatnaResponse(rozmowa);
    }

    /**
     * Znajduje rozmowę prywatną na podstawie podanego identyfikatora.
     *
     * @param id identyfikator rozmowy prywatnej
     * @param connectedUser aktualnie zalogowany użytkownik
     * @return odpowiedź zawierająca szczegóły rozmowy prywatnej
     * @throws EntityNotFoundException jeśli rozmowa prywatna o podanym identyfikatorze nie istnieje
     * @throws IllegalArgumentException jeśli zalogowany użytkownik nie ma dostępu do rozmowy
     */
    public RozmowaPrywatnaResponse findRozmowaPrywatnaById(Long id, Authentication connectedUser) {
        Uzytkownik nadawca = (Uzytkownik) connectedUser.getPrincipal();

        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaById(id)
            .orElseThrow(() -> new EntityNotFoundException("Rozmowa prywatna nie istnieje"));

        if(rozmowa.getUzytkownicy().stream().noneMatch(u -> u.getUzytId().equals(nadawca.getUzytId())) 
            && (!nadawca.isAdmin() && !nadawca.isPracownik())) {
            throw new IllegalArgumentException("Nie masz dostępu do tej rozmowy");
        }

        return commonMapperService.toRozmowaPrywatnaResponse(rozmowa);
    }

    /**
     * Zaprasza użytkownika do rozmowy prywatnej.
     *
     * @param odbiorcaNazwa Nazwa użytkownika odbiorcy.
     * @param currentUser Aktualnie zalogowany użytkownik.
     * @return Odpowiedź zawierająca szczegóły rozmowy prywatnej.
     */
    public RozmowaPrywatnaResponse inviteToRozmowaPrywatna(String odbiorcaNazwa, Authentication currentUser) {
        Uzytkownik nadawca = (Uzytkownik) currentUser.getPrincipal();

        return commonMapperService.toRozmowaPrywatnaResponse(inviteToRozmowaPrywatna(odbiorcaNazwa, nadawca));
    }

    /**
     * Zaprasza użytkownika do rozmowy prywatnej.
     *
     * @param odbiorcaNazwa Nazwa użytkownika odbiorcy.
     * @param currentUser Aktualnie zalogowany użytkownik.
     * @return Odpowiedź zawierająca szczegóły rozmowy prywatnej.
     */
    public RozmowaPrywatna inviteToRozmowaPrywatna(String odbiorcaNazwa, Uzytkownik currentUser) {
        Uzytkownik nadawca = currentUser;
        Uzytkownik odbiorca = uzytkownikRepository.findByNazwa(odbiorcaNazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika odbiorcy o nazwie: " + odbiorcaNazwa));

        if (nadawca.getEmail().equals(odbiorca.getEmail())) {
            throw new IllegalArgumentException("Nie można rozmawiać sam ze sobą");
        }

        if (!odbiorca.isAktywowany()) {
            throw new IllegalArgumentException("Konto użytkownika nie jest aktywne");
        }

        if (rozmowaPrywatnaRepository.findRozmowaPrywatnaByUzytId(nadawca.getUzytId(), odbiorca.getUzytId()).isPresent()) {
            throw new EntityAlreadyExistsException("Rozmowa prywatna już istnieje");
        }

        RozmowaPrywatna rozmowaPrywatna = rozmowaPrywatnaRepository.inviteToRozmowaPrywatna(nadawca.getUzytId(), odbiorca.getUzytId(), LocalDateTime.now());

        createPowiadomienie(TypPowiadomienia.ZAPROSZENIE, nadawca, odbiorca);

        return rozmowaPrywatna;
    }

    /**
     * Akceptuje zaproszenie do rozmowy prywatnej.
     *
     * @param uzytkownikNazwa Nazwa użytkownika, który zaprosił do rozmowy.
     * @param currentUser Aktualnie zalogowany użytkownik.
     * @return Odpowiedź zawierająca szczegóły rozmowy prywatnej.
     */
    public RozmowaPrywatna acceptRozmowaPrywatna(String nazwa, Authentication currentUser) {
        Uzytkownik odbiorca = (Uzytkownik) currentUser.getPrincipal();

        return acceptRozmowaPrywatna(nazwa, odbiorca);
    }

    /**
     * Akceptuje zaproszenie do rozmowy prywatnej.
     *
     * @param uzytkownikNazwa Nazwa użytkownika, który zaprosił do rozmowy.
     * @param currentUser Aktualnie zalogowany użytkownik.
     * @return Odpowiedź zawierająca szczegóły rozmowy prywatnej.
     */
    public RozmowaPrywatna acceptRozmowaPrywatna(String nazwa, Uzytkownik currentUser) {
        Uzytkownik odbiorca = currentUser;
        Uzytkownik nadawca = uzytkownikRepository.findByNazwa(nazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));

        if (nadawca.getEmail().equals(odbiorca.getEmail())) {
            throw new IllegalArgumentException("Nie można akceptować rozmowy z samym sobą");
        }

        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaByUzytId(odbiorca.getUzytId(), nadawca.getUzytId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rozmowy prywatnej"));

        if (rozmowa.isAktywna()) {
            throw new IllegalArgumentException("Rozmowa jest już aktywna");
        }

        if(rozmowa.getNadawca().equals(odbiorca.getEmail())) {
            throw new IllegalArgumentException("Nie możesz zaakceptować rozmowy, do której sam zaprosiłeś");
        }

        rozmowa.setAktywna(true);
        rozmowa = rozmowaPrywatnaRepository.acceptRozmowaPrywatna(nadawca.getUzytId(), odbiorca.getUzytId());

        createPowiadomienie(TypPowiadomienia.ZAPROSZENIE_ZAAKCEPTOWANE, nadawca, odbiorca);

        return rozmowa;
    }

    /**
     * Odrzuca zaproszenie do rozmowy prywatnej.
     *
     * @param uzytkownikNazwa Nazwa użytkownika, który zaprosił do rozmowy.
     * @param currentUser Aktualnie zalogowany użytkownik.
     */
    public void rejectRozmowaPrywatna(String uzytkownikNazwa, Authentication currentUser) {
        Uzytkownik odbiorca = (Uzytkownik) currentUser.getPrincipal();
        Uzytkownik nadawca = uzytkownikRepository.findByNazwa(uzytkownikNazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + uzytkownikNazwa));

        if (nadawca.getEmail().equals(odbiorca.getEmail())) {
            throw new IllegalArgumentException("Nie można odrzucać rozmowy z samym sobą");
        }

        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaByUzytId(odbiorca.getUzytId(), nadawca.getUzytId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rozmowy prywatnej"));

        if (rozmowa.isAktywna()) {
            throw new IllegalArgumentException("Rozmowa jest już aktywna");
        }

        rozmowa.setAktywna(false);
        rozmowaPrywatnaRepository.rejectRozmowaPrywatna(nadawca.getUzytId(), odbiorca.getUzytId(), rozmowa);

        createPowiadomienie(TypPowiadomienia.ZAPROSZENIE_ODRUCONE, nadawca, odbiorca);
    }

    /**
     * Tworzy powiadomienie o określonym typie.
     *
     * @param typ Typ powiadomienia.
     * @param nadawca Użytkownik, który wysłał powiadomienie.
     * @param odbiorca Użytkownik, który otrzymał powiadomienie.
     */
    private void createPowiadomienie(TypPowiadomienia typ, Uzytkownik nadawca, Uzytkownik odbiorca) {
        PowiadomienieDTO powiadomienie = PowiadomienieDTO.builder()
            .typ(typ.name())
            .odnosnik(nadawca.getNazwa())
            .uzytkownikNazwa(nadawca.getNazwa())
            .avatar(nadawca.getAvatar())
            .build();
        powiadomienieService.addPowiadomienie(powiadomienie, odbiorca);
    }
}