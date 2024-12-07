package com.example.yukka.model.uzytkownik.controller;

import static java.io.File.separator;

import java.nio.file.Path;
import java.nio.file.Paths;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yukka.auth.requests.BanRequest;
import com.example.yukka.file.FileUtils;
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
    private final FileUtils fileUtils;

    @Value("${application.file.uploads.photos-output-path}")
    String fileUploadPath;

    
    /** 
     * @param nazwa
     * @param currentUser
     * @return Boolean
     */
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


    public void remove(String nazwa, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info("Usuwanie użytkownika o nazwie: " + nazwa);
        Uzytkownik targetUzyt = uzytkownikRepository.findByNazwa(nazwa)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));
        
        if(targetUzyt.getEmail().equals(uzyt.getEmail())) {
            throw new IllegalArgumentException("Nie można usuwać samego siebie w tym requescie.");
        }

        if(targetUzyt.isAdmin()) {
            throw new IllegalArgumentException("Nie można usuwać admina.");
        }else if(targetUzyt.isPracownik() && !uzyt.isAdmin()) {
            throw new IllegalArgumentException("Nikt oprócz admina nie może usuwać pracownika.");
        } else if (targetUzyt.isNormalUzytkownik() && !uzyt.isPracownik()) {
            throw new IllegalArgumentException("Nie można usuwać innych użytkowników.");
        }


        //uzytkownikRepository.removeUzytkownik(uzytOpt.getEmail());
        removeUzytkownikQueries(targetUzyt.getEmail());

        Path path = Paths.get(fileUploadPath + separator + "uzytkownicy" + separator + targetUzyt.getUzytId());
        System.out.println("Usuwanie folderu: " + path);
        fileUtils.deleteDirectory(path);
    }

    private void removeUzytkownikQueries(String email) {
        uzytkownikRepository.removePostyOfUzytkownik(email);
        uzytkownikRepository.removeKomentarzeOfUzytkownik(email);
        uzytkownikRepository.removeRoslinyOfUzytkownik(email);
        uzytkownikRepository.removeUzytkownik(email);
    }

}
