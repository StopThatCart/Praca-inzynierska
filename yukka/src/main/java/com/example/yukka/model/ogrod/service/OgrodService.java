package com.example.yukka.model.ogrod.service;

import java.util.Comparator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yukka.common.PageResponse;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.model.dzialka.Dzialka;
import com.example.yukka.model.ogrod.Ogrod;
import com.example.yukka.model.ogrod.OgrodResponse;
import com.example.yukka.model.ogrod.repository.OgrodRepository;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OgrodService {
    private final OgrodRepository ogrodRepository;
    private final UzytkownikRepository uzytRepository;
   
    private final RoslinaMapper roslinaMapper;


    @Transactional(readOnly = true)
    public PageResponse<OgrodResponse> findAllOgrody(int page, int size, String szukaj) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("ogrod.nazwa").descending());
        Page<Ogrod> ogrody = ogrodRepository.findAllOgrody(szukaj, pageable);

        return roslinaMapper.ogrodResponsetoPageResponse(ogrody);
    }

    @Transactional(readOnly = true)
    public OgrodResponse getOgrodOfUzytkownik(String uzytkownikNazwa, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        Uzytkownik targetUzyt = uzytRepository.findByNazwa(uzytkownikNazwa)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie " + uzytkownikNazwa));

        if(!uzyt.hasAuthenticationRights(uzyt, connectedUser) && !targetUzyt.getUstawienia().isOgrodPokaz()) {
            throw new AccessDeniedException("Nie masz uprawnień do przeglądania tego ogrodu");
        }
            
        Ogrod ogrod = ogrodRepository.getOgrodOfUzytkownikByNazwa(uzytkownikNazwa)
        .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono ogrodu użytkownika " + uzytkownikNazwa));

        ogrod.getDzialki().sort(Comparator.comparingInt(Dzialka::getNumer));
        
        return roslinaMapper.toOgrodResponse(ogrod);
    }



    public String setOgrodNazwa(String ogrodNazwa, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        if(ogrodNazwa.isEmpty() || ogrodNazwa.isBlank()) {
            throw new IllegalArgumentException("Nazwa ogrodu nie może być pusta");
        }

        Ogrod ogrod = ogrodRepository.setOgrodNazwa(uzyt.getNazwa(), ogrodNazwa)
        .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono ogrodu użytkownika " + ogrodNazwa));

        return ogrod.getNazwa();
    }


}
