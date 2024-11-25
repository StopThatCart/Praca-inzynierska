package com.example.yukka.model.roslina.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.AccessDeniedException;
import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.RoslinaResponse;
import com.example.yukka.model.roslina.UzytkownikRoslinaRequest;
import com.example.yukka.model.social.CommonMapperService;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UzytkownikRoslinaService {
    private final UzytkownikRepository uzytkownikRepository;
    private final UzytkownikRoslinaRepository uzytkownikRoslinaRepository;
    private final RoslinaRepository roslinaRepository;
    private final RoslinaService roslinaService;

    @SuppressWarnings("unused")
    private final FileUtils fileUtils;
    private final FileStoreService fileStoreService;

    private final RoslinaMapper roslinaMapper;
    @SuppressWarnings("unused")
    private final CommonMapperService commonMapperService;

    public Optional<Roslina> findByRoslinaId(String roslinaId) {
        return uzytkownikRoslinaRepository.findByRoslinaIdWithRelations(roslinaId);
    }


    public PageResponse<RoslinaResponse> findRoslinyOfUzytkownik(int page, int size, UzytkownikRoslinaRequest request, String nazwa, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        log.info(nazwa + " próbuje pobrać rośliny użytkownika: " + nazwa);
        Uzytkownik targetUzyt = uzytkownikRepository.findByNazwa(nazwa)
            .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie " + nazwa));


        if(!uzyt.hasAuthenticationRights(targetUzyt, uzyt)) {
            throw new AccessDeniedException("Brak uprawnień do przeglądania roślin użytkownika " + targetUzyt.getNazwa());
        }

        return findRoslinyOfUzytkownik(page, size, request, nazwa, uzyt);
    }

    public PageResponse<RoslinaResponse> findRoslinyOfUzytkownik(int page, int size, UzytkownikRoslinaRequest request, String nazwa, Uzytkownik uzyt) {
        if (request == null) {
            System.out.println("Request is null");
            request = UzytkownikRoslinaRequest.builder().build();
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("roslina.nazwa").descending());
        //Page<Roslina> rosliny = uzytkownikRoslinaRepository.findRoslinyOfUzytkownik(nazwa, pageable);
        Roslina ros = roslinaMapper.toRoslina(request);
        Page<Roslina> rosliny = uzytkownikRoslinaRepository.findAllRoslinyOfUzytkownikWithParameters(
            ros, 
            ros.getFormy(), ros.getGleby(), ros.getGrupy(), ros.getKoloryLisci(),
            ros.getKoloryKwiatow(), ros.getKwiaty(), ros.getOdczyny(),
            ros.getOkresyKwitnienia(), ros.getOkresyOwocowania(), ros.getOwoce(), ros.getPodgrupa(),
            ros.getPokroje(), ros.getSilyWzrostu(), ros.getStanowiska(), ros.getWalory(),
            ros.getWilgotnosci(), ros.getZastosowania(), ros.getZimozielonosci(),
            pageable);

        List<RoslinaResponse> roslinyResponse = rosliny.stream()
                .map(roslinaMapper::toRoslinaResponse)
                .toList();
        return new PageResponse<>(
                roslinyResponse,
                rosliny.getNumber(),
                rosliny.getSize(),
                rosliny.getTotalElements(),
                rosliny.getTotalPages(),
                rosliny.isFirst(),
                rosliny.isLast()
        );
    }

    public Roslina save(UzytkownikRoslinaRequest request, MultipartFile file, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        return save(request, file, uzyt);
    }

    public Roslina save(UzytkownikRoslinaRequest request, MultipartFile file, Uzytkownik connectedUser) {
        log.info("Zapisywanie rośliny użytkownika: " + connectedUser.getNazwa());
        Uzytkownik uzyt = connectedUser;
        request.setRoslinaId(roslinaService.createRoslinaId());
        
        if (file != null) {
            request.setObraz(fileStoreService.saveUzytkownikRoslinaObraz(file, request.getRoslinaId(), uzyt.getUzytId()));
        }

        if(request.areWlasciwosciEmpty()) {
            log.info("Zapisywanie rośliny bez właściwości");
            log.info("Request id: " + request.getRoslinaId());
            Roslina pl = roslinaMapper.toRoslina(request);
            log.info("Roslina id: " + pl.getRoslinaId());

            Roslina ros = uzytkownikRoslinaRepository.addRoslina(uzyt.getUzytId(), pl);
            return ros;
        }
        log.info("Zapisywanie rośliny z właściwościami");
        Roslina ros = uzytkownikRoslinaRepository.addRoslina(
            uzyt.getUzytId(),
            request.getNazwa(), request.getRoslinaId(),
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosciAsMap());

        return ros;
    }


    public Roslina update(UzytkownikRoslinaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        log.info("Aktualizacja rośliny " + request.getRoslinaId() + " przez użytkownika: " + uzyt.getNazwa());

        Roslina roslina = uzytkownikRoslinaRepository.findRoslinaOfUzytkownik(uzyt.getNazwa(), request.getRoslinaId())
            .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono rośliny o id " + request.getRoslinaId()));

        System.out.println("Właściwości: " + request.getWlasciwosciAsMap());
        if(request.areWlasciwosciEmpty()) {
            return uzytkownikRoslinaRepository.updateRoslina(
            request.getRoslinaId(), request.getNazwa(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax());
        }
        
        Roslina ros = uzytkownikRoslinaRepository.updateRoslina(
            request.getRoslinaId(), request.getNazwa(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosciAsMap());
        uzytkownikRoslinaRepository.removeLeftoverUzytkownikWlasciwosci();

        return ros;
    }

    public void uploadUzytkownikRoslinaObraz(MultipartFile file, Authentication connectedUser, String roslinaId) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        log.info("Zmiana obrazu rośliny " + roslinaId + " przez użytkownika: " + uzyt.getNazwa());
        // Roslina roslina = roslinaRepository.findByRoslinaId(roslinaId).orElse(null);
        // if (roslina == null) {
        //     return;
        // }
        // if(!roslina.getUzytkownik().getUzytId().equals(uzyt.getUzytId())){
        //     return;
        // }

        Roslina roslina = uzytkownikRoslinaRepository.findRoslinaOfUzytkownik(uzyt.getNazwa(), roslinaId)
            .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono rośliny o id " + roslinaId));
        
        fileUtils.deleteObraz(roslina.getObraz());
        String pfp = fileStoreService.saveUzytkownikRoslinaObraz(file, roslinaId, uzyt.getUsername());
        if(pfp == null) {
            return;
        }
       
        roslina.setObraz(pfp);
        uzytkownikRoslinaRepository.updateRoslinaObraz(roslinaId, pfp);
    }

    
}
