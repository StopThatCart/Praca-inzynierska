package com.example.yukka.model.ogrod.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yukka.handler.EntityNotFoundException;
import com.example.yukka.model.ogrod.Ogrod;
import com.example.yukka.model.ogrod.OgrodResponse;
import com.example.yukka.model.ogrod.repository.OgrodRepository;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.social.CommonMapperService;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OgrodService {
    private final OgrodRepository ogrodRepository;
    private final UzytkownikRepository uzytRepository;
    private final CommonMapperService commonMapperService;
    private final RoslinaMapper roslinaMapper;

    public OgrodResponse getOgrodOfUzytkownik(String uzytkownikNazwa, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        Uzytkownik targetUzyt = uzytRepository.findByNazwa(uzytkownikNazwa)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie " + uzytkownikNazwa));

        if(!uzyt.hasAuthenticationRights(uzyt, connectedUser) && !targetUzyt.getUstawienia().isOgrodPokaz()) {
            throw new AccessDeniedException("Nie masz uprawnień do przeglądania tego ogrodu");
        }
            
        Ogrod ogrod = ogrodRepository.getOgrodOfUzytkownikByNazwa(uzytkownikNazwa)
        .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono ogrodu użytkownika " + uzytkownikNazwa));
        
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
