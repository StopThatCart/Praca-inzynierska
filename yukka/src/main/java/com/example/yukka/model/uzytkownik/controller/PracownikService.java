package com.example.yukka.model.uzytkownik.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yukka.auth.requests.BanRequest;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.model.social.powiadomienie.Powiadomienie;
import com.example.yukka.model.social.service.PowiadomienieService;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PracownikService {
    private final UzytkownikRepository uzytkownikRepository;
    private final PowiadomienieService powiadomienieService;

    public Boolean setBanUzytkownik(BanRequest request, Authentication currentUser){
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();

        Uzytkownik targetUzyt = uzytkownikRepository.findByNazwa(request.getNazwa())
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + request.getNazwa()));

        if(targetUzyt.getEmail().equals(uzyt.getEmail())) {
            throw new IllegalArgumentException("Nie można banować samego siebie");
        }

        if(targetUzyt.isBan() == request.getBan()) {
            throw new IllegalArgumentException("Użytkownik jest już zbanowany/odbanowany");
        }
        
        if(targetUzyt.isPracownik() || !uzyt.isAdmin()) { 
            throw new IllegalArgumentException("Nie masz uprawnień do banowania/odbanowywania tego użytkownika");
        }

        if(targetUzyt.isAdmin()) {
            throw new IllegalArgumentException("Admini nie mogą być banowani ani odbanowywani");
        }

        if(request.getBan() == false) {
            log.info("Użytkownik {} odbanowany przez {}", targetUzyt.getNazwa(), uzyt.getNazwa());
            request.setBanDo(null);
        }

        Boolean banus = uzytkownikRepository.banUzytkownik(request.getNazwa(), request.getBan(), request.getBanDo());
        powiadomienieService.sendPowiadomienieOfBan(targetUzyt, request);
        return banus;
    }
}
