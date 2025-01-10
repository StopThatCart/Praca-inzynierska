package com.example.yukka.model.roslina.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.handler.exceptions.ForbiddenException;
import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.RoslinaResponse;
import com.example.yukka.model.roslina.RoslinaWlasnaRequest;
import com.example.yukka.model.roslina.cecha.CechaKatalogResponse;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Serwis zarządzający roślinami użytkowników.
 * 
 * <ul>
 * <li><strong>UzytkownikRepository</strong> - repozytorium użytkowników</li>
 * <li><strong>RoslinaWlasnaRepository</strong> - repozytorium roślin użytkowników</li>
 * <li><strong>RoslinaService</strong> - serwis roślin</li>
 * <li><strong>FileUtils</strong> - narzędzia do obsługi plików</li>
 * <li><strong>FileStoreService</strong> - serwis przechowywania plików</li>
 * <li><strong>RoslinaMapper</strong> - mapper roślin</li>
 * <li><strong>CommonMapperService</strong> - wspólny serwis mapperów</li>
 * </ul>
 * 
 * Metody:
 * 
 * <ul>
 * <li><strong>findByRoslinaId</strong> - Znajduje roślinę po jej identyfikatorze</li>
 * <li><strong>findRoslinyOfUzytkownik</strong> - Znajduje rośliny użytkownika</li>
 * <li><strong>save</strong> - Zapisuje roślinę użytkownika</li>
 * <li><strong>update</strong> - Aktualizuje roślinę użytkownika</li>
 * <li><strong>uploadRoslinaWlasnaObraz</strong> - Przesyła obraz rośliny użytkownika</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoslinaWlasnaService {
    private final UzytkownikRepository uzytkownikRepository;
    private final RoslinaWlasnaRepository roslinaWlasnaRepository;
    private final RoslinaRepository roslinaRepository;
    private final CechaRepository cechaRepository;
    private final RoslinaService roslinaService;

    private final FileUtils fileUtils;
    private final FileStoreService fileStoreService;

    private final RoslinaMapper roslinaMapper;

    @Value("${roslina.obraz.default.name}")
    private String defaultRoslinaObrazName;

    /**
     * Znajduje rośliny użytkownika.
     * 
     * @param page Numer strony wyników
     * @param size Rozmiar strony wyników
     * @param request Żądanie zawierające kryteria wyszukiwania
     * @param nazwa Nazwa użytkownika
     * @param connectedUser Aktualnie zalogowany użytkownik
     * @return Zwraca stronę z roślinami użytkownika
     */
    public PageResponse<RoslinaResponse> findRoslinyOfUzytkownik(int page, int size, RoslinaWlasnaRequest request, String nazwa, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        log.info(nazwa + " próbuje pobrać rośliny użytkownika: " + nazwa);
        Uzytkownik targetUzyt = uzytkownikRepository.findByNazwa(nazwa)
            .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie " + nazwa));

        if(!targetUzyt.getUstawienia().isOgrodPokaz() && !uzyt.hasAuthenticationRights(targetUzyt)) {
            throw new ForbiddenException("Brak uprawnień do przeglądania roślin użytkownika " + targetUzyt.getNazwa());
        }

        return findRoslinyOfUzytkownik(page, size, request, nazwa, uzyt);
    }

    /**
     * Znajduje rośliny użytkownika.
     * 
     * @param page Numer strony wyników
     * @param size Rozmiar strony wyników
     * @param request Żądanie zawierające kryteria wyszukiwania
     * @param nazwa Nazwa użytkownika
     * @param uzyt Użytkownik
     * @return Zwraca stronę z roślinami użytkownika
     */
    public PageResponse<RoslinaResponse> findRoslinyOfUzytkownik(int page, int size, RoslinaWlasnaRequest request, String nazwa, Uzytkownik uzyt) {
        if (request == null) {
            System.out.println("Request is null");
            request = RoslinaWlasnaRequest.builder().build();
        }
        log.info(nazwa + " próbuje pobrać rośliny użytkownika: " + nazwa);
        Pageable pageable = PageRequest.of(page, size, Sort.by("roslina.nazwa").descending());

        Roslina ros = roslinaMapper.toRoslina(request);
        Page<Roslina> rosliny = roslinaWlasnaRepository.findAllRoslinyOfUzytkownikWithParameters(
            nazwa,
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


    @Transactional(readOnly = true)
    public Set<CechaKatalogResponse> getUzytkownikCechyCountFromQuery(RoslinaWlasnaRequest request, String nazwa, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        log.info(nazwa + " próbuje pobrać rośliny użytkownika: " + nazwa);
        Uzytkownik targetUzyt = uzytkownikRepository.findByNazwa(nazwa)
            .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie " + nazwa));


        if(!targetUzyt.getUstawienia().isOgrodPokaz() && !uzyt.hasAuthenticationRights(targetUzyt)) {
            throw new ForbiddenException("Brak uprawnień do przeglądania roślin użytkownika " + targetUzyt.getNazwa());
        }
        
        Roslina ros = roslinaMapper.toRoslina(request);

        Set<CechaKatalogResponse> responses = cechaRepository.getUzytkownikCechyCountFromQuery(
            nazwa,
            ros, 
            ros.getFormy(), ros.getGleby(), ros.getGrupy(), ros.getKoloryLisci(),
            ros.getKoloryKwiatow(), ros.getKwiaty(), ros.getOdczyny(),
            ros.getOkresyKwitnienia(), ros.getOkresyOwocowania(), ros.getOwoce(), ros.getPodgrupa(),
            ros.getPokroje(), ros.getSilyWzrostu(), ros.getStanowiska(), ros.getWalory(),
            ros.getWilgotnosci(), ros.getZastosowania(), ros.getZimozielonosci()
        );
        
        return responses;
    }

    /**
     * Zapisuje roślinę użytkownika.
     * 
     * @param request Żądanie zawierające dane rośliny
     * @param file Plik obrazu rośliny
     * @param connectedUser Aktualnie zalogowany użytkownik
     * @return Zwraca zapisaną roślinę
     */
    public RoslinaResponse save(RoslinaWlasnaRequest request, MultipartFile file, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        return  roslinaMapper.toRoslinaResponse(save(request, file, uzyt));
    }

    /**
     * Aktualizuje roślinę użytkownika.
     * 
     * @param request Żądanie zawierające nowe dane rośliny
     * @param connectedUser Aktualnie zalogowany użytkownik
     * @return Zwraca zaktualizowaną roślinę
     */
    public Roslina save(RoslinaWlasnaRequest request, MultipartFile file, Uzytkownik connectedUser) {
        log.info("Zapisywanie rośliny użytkownika: " + connectedUser.getNazwa());
        Uzytkownik uzyt = connectedUser;
        request.setRoslinaId(roslinaService.createRoslinaId());
        
        if (file != null) {
            request.setObraz(fileStoreService.saveRoslinaWlasnaObraz(file, request.getRoslinaId(), uzyt.getUzytId()));
        } else {
            request.setObraz(defaultRoslinaObrazName);
        }

        if(request.areCechyEmpty()) {
            log.info("Zapisywanie rośliny bez właściwości");
            log.info("Request id: " + request.getRoslinaId());
            Roslina pl = roslinaMapper.toRoslina(request);
            log.info("Roslina id: " + pl.getRoslinaId());

            Roslina ros = roslinaWlasnaRepository.addRoslina(uzyt.getUzytId(), pl);
            return ros;
        }
        log.info("Zapisywanie rośliny z właściwościami");
        Roslina ros = roslinaWlasnaRepository.addRoslina(
            uzyt.getUzytId(),
            request.getNazwa(), request.getRoslinaId(),
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getCechyAsMap());

        return ros;
    }

    /**
     * Aktualizuje roślinę użytkownika.
     * 
     * @param request Żądanie zawierające nowe dane rośliny
     * @param connectedUser Aktualnie zalogowany użytkownik
     * @return Zwraca zaktualizowaną roślinę
     */
    public RoslinaResponse update(RoslinaWlasnaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        log.info("Aktualizacja rośliny " + request.getRoslinaId() + " przez użytkownika: " + uzyt.getNazwa());
        log.info("Właściwości: " + request.getCechyAsMap());


        Roslina roslina = roslinaWlasnaRepository.findRoslinaOfUzytkownik(uzyt.getNazwa(), request.getRoslinaId())
            .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono rośliny o id " + request.getRoslinaId() + " dla użytkownika " + uzyt.getNazwa()));

        Uzytkownik targetUzyt = roslina.getUzytkownik();
        if (!uzyt.hasAuthenticationRights(targetUzyt)) {
            throw new ForbiddenException("Brak uprawnień do zmiany rośliny " + request.getRoslinaId());
        }
        
        if(request.areCechyEmpty()) {
            log.info("Aktualizacja rośliny bez właściwości");
            Roslina ros = roslinaWlasnaRepository.updateRoslina(
                request.getRoslinaId(), request.getNazwa(), 
                request.getOpis(), request.getObraz(), 
                request.getWysokoscMin(), request.getWysokoscMax());
            return roslinaMapper.toRoslinaResponse(ros);
        }
        log.info("Aktualizacja rośliny z właściwościami");
        Roslina ros = roslinaWlasnaRepository.updateRoslina(
            request.getRoslinaId(), request.getNazwa(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getCechyAsMap());
       // roslinaWlasnaRepository.removeLeftoverUzytkownikCechy();

        return  roslinaMapper.toRoslinaResponse(ros);
    }

    /**
     * Przesyła obraz rośliny użytkownika.
     * 
     * @param file Nowy plik obrazu rośliny
     * @param connectedUser Aktualnie zalogowany użytkownik
     * @param roslinaId Identyfikator rośliny
     */
    public void uploadRoslinaWlasnaObraz(MultipartFile file, String roslinaId, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        log.info("Zmiana obrazu rośliny " + roslinaId + " przez użytkownika: " + uzyt.getNazwa());

        Roslina roslina = roslinaWlasnaRepository.findRoslinaOfUzytkownik(uzyt.getNazwa(), roslinaId)
            .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono rośliny o id " + roslinaId + " dla użytkownika " + uzyt.getNazwa()));

        Uzytkownik targetUzyt = roslina.getUzytkownik();
        System.out.println("Uzytkownik: " + targetUzyt.getNazwa());

        if (!uzyt.hasAuthenticationRights(targetUzyt)) {
            throw new ForbiddenException("Brak uprawnień do zmiany obrazu rośliny " + roslinaId);
        }

        if (file == null) {
            log.info("Brak pliku obrazu, ustawienie domyślnego obrazu");
            log.info("Obraz: " + roslina.getObraz());
            if(!roslina.getObraz().equals(defaultRoslinaObrazName)) {
                fileUtils.deleteObraz(roslina.getObraz());
                roslinaRepository.updateRoslinaObraz(roslina.getRoslinaId(), defaultRoslinaObrazName);
            }
            return;
        }
        
        if (roslina.getObraz() != null) fileUtils.deleteObraz(roslina.getObraz());
        String pfp = fileStoreService.saveRoslinaWlasnaObraz(file, roslinaId, uzyt.getUzytId());
        if(pfp == null) {
            return;
        }
       
        roslina.setObraz(pfp);
        roslinaRepository.updateRoslinaObraz(roslinaId, pfp);
    }
}
