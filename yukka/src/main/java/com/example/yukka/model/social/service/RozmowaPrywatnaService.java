package com.example.yukka.model.social.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.yukka.common.PageResponse;
import com.example.yukka.handler.EntityAlreadyExistsException;
import com.example.yukka.handler.EntityNotFoundException;
import com.example.yukka.model.social.CommonMapperService;
import com.example.yukka.model.social.repository.RozmowaPrywatnaRepository;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatnaResponse;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RozmowaPrywatnaService {

    @Autowired
    private final RozmowaPrywatnaRepository rozmowaPrywatnaRepository;

    @Autowired
    private final UzytkownikRepository uzytkownikRepository;

    @Autowired
    private final CommonMapperService commonMapperService;

    public RozmowaPrywatnaResponse findRozmowaPrywatna(String otherUzytNazwa, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        Uzytkownik uzyt2 = uzytkownikRepository.findByNazwa(otherUzytNazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika odbiorcy o nazwie: " + otherUzytNazwa));

        if (uzyt.getNazwa().equals(uzyt2.getNazwa())) {
            throw new IllegalArgumentException("Nie można rozmawiać sam ze sobą");
        }
        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaWithKomentarze(uzyt2.getNazwa(), uzyt.getNazwa())
        .orElseThrow(() -> new EntityNotFoundException("Rozmowa prywatna nie istnieje"));


        return commonMapperService.toRozmowaPrywatnaResponse(rozmowa);
    }

    public RozmowaPrywatna addRozmowaPrywatna(String odbiorca, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        Uzytkownik uzyt2 = uzytkownikRepository.findByNazwa(odbiorca)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika odbiorcy o nazwie: " + odbiorca));

        if (uzyt.getEmail().equals(uzyt2.getEmail())) {
            throw new IllegalArgumentException("Nie można rozmawiać sam ze sobą");
        }

        if (rozmowaPrywatnaRepository.findRozmowaPrywatna(uzyt.getNazwa(), uzyt2.getNazwa()).isPresent()) {
            throw new EntityAlreadyExistsException("Rozmowa prywatna już istnieje");
        }

        return rozmowaPrywatnaRepository.saveRozmowaPrywatna(uzyt.getNazwa(), uzyt2.getNazwa());
    }

    public RozmowaPrywatna addRozmowaPrywatnaNoPunjabi(String odbiorca, Uzytkownik currentUser) {
        Uzytkownik uzyt = currentUser;
        Uzytkownik uzyt2 = uzytkownikRepository.findByNazwa(odbiorca)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika odbiorcy o nazwie: " + odbiorca));

        if (uzyt.getEmail().equals(uzyt2.getEmail())) {
            throw new IllegalArgumentException("Nie można rozmawiać sam ze sobą");
        }

        if (rozmowaPrywatnaRepository.findRozmowaPrywatna(uzyt.getNazwa(), uzyt2.getNazwa()).isPresent()) {
            throw new EntityAlreadyExistsException("Rozmowa prywatna już istnieje");
        }

        return rozmowaPrywatnaRepository.saveRozmowaPrywatna(uzyt.getNazwa(), uzyt2.getNazwa());
    }

    // TODO: Dodać implementację, że tylko odbiorca może zaakceptować rozmowę
    public RozmowaPrywatna acceptRozmowaPrywatna(String odbiorca, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        Uzytkownik uzytOpt = uzytkownikRepository.findByNazwa(odbiorca)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + odbiorca));

        if (uzytOpt.getEmail().equals(uzyt.getEmail())) {
            throw new IllegalArgumentException("Nie można akceptować rozmowy z samym sobą");
        }

        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatna(uzyt.getNazwa(), uzytOpt.getNazwa())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rozmowy prywatnej"));

        if (rozmowa.isAktywna()) {
            throw new IllegalArgumentException("Rozmowa jest już aktywna");
        }

        rozmowa.setAktywna(true);
        return rozmowaPrywatnaRepository.updateRozmowaPrywatna(uzyt.getNazwa(), uzytOpt.getNazwa(), rozmowa);
    }


    public RozmowaPrywatna acceptRozmowaPrywatnaNoPunjabi(String odbiorca, Uzytkownik currentUser) {
        Uzytkownik uzyt = currentUser;
        Uzytkownik uzytOpt = uzytkownikRepository.findByNazwa(odbiorca)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + odbiorca));

        if (uzytOpt.getEmail().equals(uzyt.getEmail())) {
            throw new IllegalArgumentException("Nie można akceptować rozmowy z samym sobą");
        }

        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatna(uzyt.getNazwa(), uzytOpt.getNazwa())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rozmowy prywatnej"));

        if (rozmowa.isAktywna()) {
            throw new IllegalArgumentException("Rozmowa jest już aktywna");
        }

        rozmowa.setAktywna(true);
        return rozmowaPrywatnaRepository.updateRozmowaPrywatna(uzyt.getNazwa(), uzytOpt.getNazwa(), rozmowa);
    }

    public PageResponse<RozmowaPrywatnaResponse> findRozmowyPrywatneOfUzytkownik(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,    
            Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("komentarz.ostatnioAktualizowane").descending());
        Page<RozmowaPrywatna> rozmowy = rozmowaPrywatnaRepository.findRozmowyPrywatneOfUzytkownik(uzyt.getEmail(), pageable);

        return commonMapperService.rozmowaPrywatnaResponsetoPageRozmowaPrywatnaResponse(rozmowy);
    }
}