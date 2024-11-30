package com.example.yukka.model.uzytkownik.controller;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yukka.auth.requests.BanRequest;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.handler.exceptions.ForbiddenException;
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

    public Boolean unbanUzytkownik(String nazwa, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();

        Uzytkownik targetUzyt = uzytkownikRepository.findByNazwa(nazwa)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));

        if(!targetUzyt.isBan()) {
            throw new IllegalArgumentException("Użytkownik nie jest zbanowany");
        }

        if(targetUzyt.isPracownik() && !uzyt.isAdmin()) {
            throw new ForbiddenException("Nie masz uprawnień do odbanowywania tego użytkownika");
        }

        log.info("Użytkownik {} odbanowany przez {}", targetUzyt.getNazwa(), uzyt.getNazwa());
        Boolean unbanus = uzytkownikRepository.banUzytkownik(nazwa, false, null);
        powiadomienieService.sendPowiadomienieOfUnban(targetUzyt);
        return unbanus;
    }

    public Boolean setBanUzytkownik(BanRequest request, Authentication currentUser){
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info( "Użytkownik {} próbuje zbanować użytkownika {}", uzyt.getNazwa(), request.getNazwa());

        Uzytkownik targetUzyt = uzytkownikRepository.findByNazwa(request.getNazwa())
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + request.getNazwa()));

        if(targetUzyt.getEmail().equals(uzyt.getEmail())) {
            throw new IllegalArgumentException("Nie można banować samego siebie");
        }

        if(targetUzyt.isBan() == request.getBan()) {
            throw new IllegalArgumentException("Użytkownik jest już zbanowany/odbanowany");
        }
        
        if(targetUzyt.isPracownik() && !uzyt.isAdmin()) { 
            throw new ForbiddenException("Nie masz uprawnień do banowania/odbanowywania tego użytkownika");
        }

        if(targetUzyt.isAdmin()) {
            throw new IllegalArgumentException("Admini nie mogą być banowani ani odbanowywani");
        }

        if(request.getBan() == false) {
            throw new IllegalArgumentException("Użytkownik nie może być odbanowany podczas banowania");
        }

        Boolean banus = uzytkownikRepository.banUzytkownik(request.getNazwa(), request.getBan(), request.getBanDo());
        powiadomienieService.sendPowiadomienieOfBan(targetUzyt, request);
        return banus;
    }
}
