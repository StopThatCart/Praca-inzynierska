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

        Uzytkownik odbiorca = uzytkownikRepository.findByUUID(odbiorcaId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika odbiorcy o nazwie: " + odbiorcaId));

        if (nadawca.getNazwa().equals(odbiorca.getNazwa())) {
            throw new IllegalArgumentException("Nie można rozmawiać sam ze sobą");
        }
        
        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaWithKomentarze(odbiorca.getUuid(), nadawca.getUuid())
        .orElseThrow(() -> new EntityNotFoundException("Rozmowa prywatna nie istnieje"));

        return commonMapperService.toRozmowaPrywatnaResponse(rozmowa);
    }

    /**
     * Znajduje rozmowę prywatną na podstawie nazwy użytkownika odbiorcy.
     *
     * @param odbiorcaNazwa Nazwa użytkownika odbiorcy.
     * @param connectedUser Aktualnie zalogowany użytkownik.
     * @return Odpowiedź zawierająca szczegóły rozmowy prywatnej.
     * @throws EntityNotFoundException Jeśli użytkownik odbiorca lub rozmowa prywatna nie zostaną znalezione.
     * @throws IllegalArgumentException Jeśli użytkownik próbuje znaleźć rozmowę ze sobą samym.
     */
    public RozmowaPrywatnaResponse findRozmowaPrywatnaByNazwa(String odbiorcaNazwa, Authentication connectedUser) {
        Uzytkownik nadawca = (Uzytkownik) connectedUser.getPrincipal();

        Uzytkownik odbiorca = uzytkownikRepository.findByNazwa(odbiorcaNazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika odbiorcy o nazwie: " + odbiorcaNazwa));

        if (nadawca.getNazwa().equals(odbiorca.getNazwa())) {
            throw new IllegalArgumentException("Rozmowę ze sobą polecamy poćwiczyć przed lustrem");
        }
        
        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaWithKomentarze(odbiorca.getUuid(), nadawca.getUuid())
        .orElseThrow(() -> new EntityNotFoundException("Rozmowa prywatna nie istnieje"));

        return commonMapperService.toRozmowaPrywatnaResponse(rozmowa);
    }

    /**
     * Zaprasza użytkownika do rozmowy prywatnej.
     *
     * @param odbiorcaNazwa Nazwa użytkownika odbiorcy.
     * @param currentUser Aktualnie zalogowany użytkownik.
     */
    public void inviteToRozmowaPrywatna(String odbiorcaNazwa, Authentication currentUser) {
        Uzytkownik nadawca = (Uzytkownik) currentUser.getPrincipal();

        inviteToRozmowaPrywatna(odbiorcaNazwa, nadawca);
    }

    /**
     * Zaprasza użytkownika do rozmowy prywatnej.
     *
     * @param odbiorcaNazwa Nazwa użytkownika odbiorcy.
     * @param currentUser Aktualnie zalogowany użytkownik.
     */
    public void inviteToRozmowaPrywatna(String odbiorcaNazwa, Uzytkownik currentUser) {
        Uzytkownik nadawca = currentUser;
        Uzytkownik odbiorca = uzytkownikRepository.findByNazwa(odbiorcaNazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika odbiorcy o nazwie: " + odbiorcaNazwa));

        if (nadawca.getEmail().equals(odbiorca.getEmail())) {
            throw new IllegalArgumentException("Nie można rozmawiać sam ze sobą");
        }

        if (!odbiorca.isAktywowany()) {
            throw new IllegalArgumentException("Konto użytkownika nie jest aktywne");
        }

        if (rozmowaPrywatnaRepository.findRozmowaPrywatnaByUUID(nadawca.getUuid(), odbiorca.getUuid()).isPresent()) {
            throw new EntityAlreadyExistsException("Rozmowa prywatna już istnieje");
        }

        rozmowaPrywatnaRepository.inviteToRozmowaPrywatna(nadawca.getUuid(), odbiorca.getUuid(), LocalDateTime.now());

        createPowiadomienie(TypPowiadomienia.ZAPROSZENIE, nadawca, odbiorca);
    }

    /**
     * Akceptuje zaproszenie do rozmowy prywatnej.
     *
     * @param nadawcaNazwa Nazwa użytkownika, który zaprosił do rozmowy.
     * @param currentUser Aktualnie zalogowany użytkownik.
     */
    public void acceptRozmowaPrywatna(String nadawcaNazwa, Authentication currentUser) {
        Uzytkownik odbiorca = (Uzytkownik) currentUser.getPrincipal();

        acceptRozmowaPrywatna(nadawcaNazwa, odbiorca);
    }

    /**
     * Akceptuje zaproszenie do rozmowy prywatnej.
     *
     * @param nadawcaNazwa Nazwa użytkownika, który zaprosił do rozmowy.
     * @param currentUser Aktualnie zalogowany użytkownik.
     */
    public void acceptRozmowaPrywatna(String nadawcaNazwa, Uzytkownik currentUser) {
        Uzytkownik odbiorca = currentUser;
        Uzytkownik nadawca = uzytkownikRepository.findByNazwa(nadawcaNazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + nadawcaNazwa));

        if (nadawca.getEmail().equals(odbiorca.getEmail())) {
            throw new IllegalArgumentException("Nie można akceptować rozmowy z samym sobą");
        }

        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaByUUID(odbiorca.getUuid(), nadawca.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rozmowy prywatnej"));

        if (rozmowa.isAktywna()) {
            throw new IllegalArgumentException("Rozmowa jest już aktywna");
        }

        if(rozmowa.getNadawca().equals(odbiorca.getEmail())) {
            throw new IllegalArgumentException("Nie możesz zaakceptować rozmowy, do której sam zaprosiłeś");
        }

        rozmowaPrywatnaRepository.acceptRozmowaPrywatna(nadawca.getUuid(), odbiorca.getUuid());
        createPowiadomienie(TypPowiadomienia.ZAPROSZENIE_ZAAKCEPTOWANE, nadawca, odbiorca);
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

        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaByUUID(odbiorca.getUuid(), nadawca.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rozmowy prywatnej"));

        if (rozmowa.isAktywna()) {
            throw new IllegalArgumentException("Rozmowa jest już aktywna");
        }

        rozmowa.setAktywna(false);
        rozmowaPrywatnaRepository.rejectRozmowaPrywatna(nadawca.getUuid(), odbiorca.getUuid(), rozmowa);

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