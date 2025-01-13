package com.example.yukka.model.roslina.controller;

import java.util.List;
import java.util.Optional;
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
import com.example.yukka.handler.exceptions.EntityAlreadyExistsException;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.handler.exceptions.ForbiddenException;
import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.RoslinaRequest;
import com.example.yukka.model.roslina.RoslinaResponse;
import com.example.yukka.model.roslina.cecha.CechaKatalogResponse;
import com.example.yukka.model.roslina.cecha.CechaResponse;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Serwis do zarządzania roślinami.
 * 
 * <ul>
 * <li><strong>findAllRosliny</strong>: Znajduje wszystkie rośliny z paginacją.</li>
 * <li><strong>findAllRoslinyWithParameters</strong>: Znajduje wszystkie rośliny z paginacją i filtrami.</li>
 * <li><strong>findByUUID</strong>: Znajduje roślinę po jej identyfikatorze.</li>
 * <li><strong>findByNazwaLacinska</strong>: Znajduje roślinę po jej nazwie łacińskiej.</li>
 * <li><strong>getCechyWithRelations</strong>: Pobiera cechy roślin z relacjami.</li>
 * <li><strong>save</strong>: Zapisuje nową roślinę.</li>
 * <li><strong>saveSeededRoslina</strong>: Zapisuje nową roślinę z plikiem obrazu.</li>
 * <li><strong>update</strong>: Aktualizuje istniejącą roślinę.</li>
 * <li><strong>uploadRoslinaObraz</strong>: Aktualizuje obraz rośliny.</li>
 * <li><strong>deleteByNazwaLacinska</strong>: Usuwa roślinę po jej nazwie łacińskiej.</li>
 * <li><strong>deleteByUUID</strong>: Usuwa roślinę po jej identyfikatorze.</li>
 * </ul>
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RoslinaService {
    private final RoslinaRepository roslinaRepository;
    private final CechaRepository cechaRepository;
    private final FileStoreService fileStoreService;
    private final FileUtils fileUtils;
    private final RoslinaMapper roslinaMapper;

    @Value("${roslina.obraz.default.name}")
    private String defaultRoslinaObrazName;

    

    /**
     * Znajduje wszystkie rośliny z paginacją i sortowaniem.
     *
     * @param page numer strony do pobrania (0-indexed)
     * @param size liczba elementów na stronie
     * @return PageResponse<RoslinaResponse> zawierający listę roślin oraz informacje o paginacji
     */
    @Transactional(readOnly = true)
    public PageResponse<RoslinaResponse> findAllRosliny(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("roslina.nazwa").descending());
        Page<Roslina> rosliny = roslinaRepository.findAllRosliny(pageable);
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

    
    /**
     * Znajduje wszystkie rośliny z parametrami.
     *
     * @param page numer strony do pobrania.
     * @param size rozmiar strony.
     * @param request obiekt zawierający parametry wyszukiwania roślin.
     * @return PageResponse<RoslinaResponse> zawierający listę roślin spełniających kryteria wyszukiwania oraz informacje o paginacji.
     */
    @Transactional(readOnly = true)
    public PageResponse<RoslinaResponse> findAllRoslinyWithParameters(int page, int size, RoslinaRequest request) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("roslina.nazwa").ascending());
        if(request == null) {
            System.out.println("Request is null");
            return findAllRosliny(page, size);
        }
        Roslina ros = roslinaMapper.toRoslina(request);

        Page<Roslina> rosliny = roslinaRepository.findAllRoslinyWithParameters(
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

    /**
     * Znajduje roślinę po jej identyfikatorze.
     *
     * @param uuid identyfikator rośliny.
     * @return RoslinaResponse zawierający informacje o roślinie.
     */
    @Transactional(readOnly = true)
    public RoslinaResponse findByUUID(String uuid) {
        Roslina ros = roslinaRepository.findByUUID(uuid)
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rośliny o Id: " + uuid));
        return roslinaMapper.roslinaToRoslinaResponseWithCechy(ros);
    }

    /**
     * Znajduje roślinę po jej identyfikatorze.
     *
     * @param uuid identyfikator rośliny.
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     * @return RoslinaResponse zawierający informacje o roślinie.
     */
    @Transactional(readOnly = true)
    public RoslinaResponse findByUUID(String uuid, Authentication connectedUser) {
        Roslina ros = roslinaRepository.findByUUID(uuid)
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rośliny o Id: " + uuid));
        
        if (ros.isRoslinaWlasna()) {
            Uzytkownik uzyt = null;
            if (connectedUser == null) {
                throw new ForbiddenException("Nie masz uprawnień do wyświetlenia tej rośliny.");
            }
            
            uzyt = (Uzytkownik) connectedUser.getPrincipal();
            if (!ros.getUzytkownik().getUstawienia().isOgrodPokaz() && !uzyt.hasAuthenticationRights(ros.getUzytkownik())) {
                throw new ForbiddenException("Nie masz uprawnień do wyświetlenia tej rośliny.");
            }
        }

        return roslinaMapper.roslinaToRoslinaResponseWithCechy(ros);
    }

    /**
     * Znajduje roślinę po jej nazwie łacińskiej.
     *
     * @param nazwaLacinska nazwa łacińska rośliny.
     * @return RoslinaResponse zawierający informacje o roślinie.
     */
    @Transactional(readOnly = true)
    public RoslinaResponse findByNazwaLacinska(String nazwaLacinska) {
        Roslina ros = roslinaRepository.findByNazwaLacinskaWithCechy(nazwaLacinska)
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rośliny o nazwie łacińskiej: " + nazwaLacinska));
        RoslinaResponse res = roslinaMapper.roslinaToRoslinaResponseWithCechy(ros);

        return res;
    }

    /**
     * Pobiera cechy roślin wraz z ich relacjami.
     *
     * @return zbiór obiektów CechaResponse.
     */
    @Transactional(readOnly = true)
    public Set<CechaResponse> getCechyWithRelations() {
        Set<CechaResponse> responses = cechaRepository.getCechyWithRelacje();
        return responses;
    }

    @Transactional(readOnly = true)
    public Set<CechaKatalogResponse> getCechyCountFromQuery(RoslinaRequest request) {
        Roslina ros = roslinaMapper.toRoslina(request);

        Set<CechaKatalogResponse> responses = cechaRepository.getCechyCountFromQuery(
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
     * Zapisuje nową roślinę.
     *
     * @param request obiekt zawierający dane nowej rośliny.
     * @param file obiekt MultipartFile zawierający obraz rośliny, opcjonalny.
     * @return Roslina zawierający informacje o zapisanej roślinie.
     */
    public Roslina save(RoslinaRequest request, MultipartFile file) {
        request.setNazwaLacinska(request.getNazwaLacinska().toLowerCase());

        Optional<Roslina> roslina = roslinaRepository.findByNazwaLacinska(request.getNazwaLacinska());
        if (roslina.isPresent()) {
            throw new EntityAlreadyExistsException("Roslina o nazwie łacińskiej \"" + request.getNazwaLacinska() + "\" już istnieje w katalogu.");
        }
        
        if (file != null) {
            request.setObraz(fileStoreService.saveRoslina(file, request.getUuid()));
        } else {
            request.setObraz(defaultRoslinaObrazName);
        }

        if(request.areCechyEmpty()) {
            Roslina pl = roslinaMapper.toRoslina(request);
            Roslina ros = roslinaRepository.addRoslina(pl);
            return ros;
        }

        Roslina ros = roslinaRepository.addRoslina(
            request.getNazwa(), request.getNazwaLacinska(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getCechyAsMap());

        return ros;
    }

    /**
     * Zapisuje nową roślinę z plikiem obrazu.
     *
     * @param request obiekt zawierający dane nowej rośliny.
     * @param file obiekt MultipartFile zawierający obraz rośliny.
     * @return Roslina zawierający informacje o zapisanej roślinie.
     */
    public Roslina saveSeededRoslina(RoslinaRequest request, MultipartFile file) {
        request.setNazwaLacinska(request.getNazwaLacinska().toLowerCase());
        Roslina pl = roslinaMapper.toRoslina(request);
        
        String leObraz = fileStoreService.saveSeededRoslina(file, request.getObraz());
        request.setObraz(leObraz);
        if(request.areCechyEmpty()) {
            Roslina ros = roslinaRepository.addRoslina(pl);
            return ros;
        }
        Roslina ros = roslinaRepository.addRoslina(
            request.getNazwa(), request.getNazwaLacinska(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getCechyAsMap());

        return ros;
    }

    /**
     * Aktualizuje istniejącą roślinę.
     *
     * @param uuid identyfikator rośliny
     * @param request obiekt zawierający nowe dane rośliny.
     * @return Roslina zawierający informacje o zaktualizowanej roślinie.
     */
    public RoslinaResponse update(String uuid, RoslinaRequest request) {
        request.setNazwaLacinska(request.getNazwaLacinska().toLowerCase());

        Roslina targetRoslina = roslinaRepository.findByUUID(uuid)
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rośliny o id " + uuid));

        if (!targetRoslina.getNazwaLacinska().equals(request.getNazwaLacinska())) {
            Optional<Roslina> anotherRoslina = roslinaRepository.findByNazwaLacinska(request.getNazwaLacinska());
            if(anotherRoslina.isPresent()) {
                throw new EntityAlreadyExistsException("Roslina o nazwie łacińskiej \"" + request.getNazwaLacinska() + "\" już istnieje w katalogu.");
            }    
        }

        if(request.areCechyEmpty()) {
            Roslina ros = roslinaRepository.updateRoslina(
                uuid,
                request.getNazwa(),  
                request.getNazwaLacinska(),
                request.getOpis(),  
                request.getWysokoscMin(), request.getWysokoscMax()
                );
            return roslinaMapper.toRoslinaResponse(ros);
        }

        Roslina ros = roslinaRepository.updateRoslina(
            uuid, 
            request.getNazwa(),
            request.getNazwaLacinska(), 
            request.getOpis(),
            request.getWysokoscMin(), request.getWysokoscMax(),
            request.getCechyAsMap());
               
        return roslinaMapper.roslinaToRoslinaResponseWithCechy(ros);
    }

    /**
     * Aktualizuje obraz rośliny.
     *
     * @param uuid identyfikator rośliny.
     * @param file obiekt MultipartFile zawierający obraz rośliny. W razie braku pliku, ustawia domyślny obraz.
     */
    // Uwaga: to jest do głównej rośliny, nie customowej
    public void uploadRoslinaObraz(String uuid, MultipartFile file) {
        log.info("Aktualizacja obrazu rośliny: " + uuid);
        Roslina roslina = roslinaRepository.findByUUID(uuid)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rośliny o id: " + uuid));

        if (file == null) {
            log.info("Brak pliku obrazu, ustawienie domyślnego obrazu");
            log.info("Obraz: " + roslina.getObraz());
            if(!roslina.getObraz().equals(defaultRoslinaObrazName)) {
                fileUtils.deleteObraz(roslina.getObraz());
                roslinaRepository.updateRoslinaObraz(roslina.getUuid(), defaultRoslinaObrazName);
            }
            return;
        }
        
        String pfp = fileStoreService.saveRoslina(file, roslina.getUuid());
        if(pfp == null){
            throw new EntityNotFoundException("Nie udało się załadować pliku obrazu");
        }

        if(roslina.getObraz() != null) fileUtils.deleteObraz(roslina.getObraz());
        roslina.setObraz(pfp);

        roslinaRepository.updateRoslinaObraz(roslina.getUuid(), pfp);
   }


    // Usuwanie po ID zajmuje ogromną ilość czasu i wywołuje HeapOverflow, więc lepiej jest użyć UNIQUE atrybutu jak nazwaLacinska

    /**
     * Usuwa roślinę po jej nazwie łacińskiej.
     *
     * @param nazwaLacinska nazwa łacińska rośliny.
     */
    public void deleteByNazwaLacinska(String nazwaLacinska) {
        roslinaRepository.findByNazwaLacinska(nazwaLacinska).ifPresent(
            roslina -> {
                fileUtils.deleteObraz(roslina.getObraz());
                roslinaRepository.deleteByNazwaLacinska(nazwaLacinska);
            }
        );
    }

    /**
     * Usuwa roślinę po jej identyfikatorze.
     *
     * @param uuid identyfikator rośliny.
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     */
    public void deleteByUUID(String uuid, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Roslina roslina = roslinaRepository.findByUUID(uuid)
            .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono rośliny o id " + uuid));
        

        if(roslina.isRoslinaWlasna() ) {
            Uzytkownik targetUzyt = roslina.getUzytkownik();
            if (!uzyt.hasAuthenticationRights(targetUzyt)) {
                throw new ForbiddenException("Nie masz uprawnień do usunięcia rośliny użytkownika: " + targetUzyt.getNazwa());
            }

            fileUtils.deleteObraz(roslina.getObraz());
            roslinaRepository.deleteByUUID(uuid);
            return;
            
        } else if (uzyt.isNormalUzytkownik()) {
            throw new ForbiddenException("Nie masz uprawnień do usunięcia tej rośliny.");
        }
        
        fileUtils.deleteObraz(roslina.getObraz());
        roslinaRepository.deleteByUUID(uuid);
    }

    /**
     * Usuwa roślinę po jej identyfikatorze.
     *
     * @param uuid identyfikator rośliny.
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     */
    public void deleteByUUID(String uuid, Uzytkownik uzyt) {
        Roslina roslina = roslinaRepository.findByUUID(uuid)
            .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono rośliny o id " + uuid));

        fileUtils.deleteObraz(roslina.getObraz());
        roslinaRepository.deleteByUUID(uuid);
    }
}
