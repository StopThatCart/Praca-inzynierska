package com.example.yukka.model.roslina.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscResponse;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwosciRodzaje;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Serwis do zarządzania roślinami.
 * 
 * <ul>
 * <li><strong>findAllRosliny</strong>: Znajduje wszystkie rośliny z paginacją.</li>
 * <li><strong>findAllRoslinyWithParameters</strong>: Znajduje wszystkie rośliny z paginacją i filtrami.</li>
 * <li><strong>findByRoslinaId</strong>: Znajduje roślinę po jej identyfikatorze.</li>
 * <li><strong>findByNazwaLacinska</strong>: Znajduje roślinę po jej nazwie łacińskiej.</li>
 * <li><strong>getWlasciwosciWithRelations</strong>: Pobiera właściwości roślin z relacjami.</li>
 * <li><strong>save</strong>: Zapisuje nową roślinę.</li>
 * <li><strong>saveSeededRoslina</strong>: Zapisuje nową roślinę z plikiem obrazu.</li>
 * <li><strong>update</strong>: Aktualizuje istniejącą roślinę.</li>
 * <li><strong>uploadRoslinaObraz</strong>: Aktualizuje obraz rośliny.</li>
 * <li><strong>deleteByNazwaLacinska</strong>: Usuwa roślinę po jej nazwie łacińskiej.</li>
 * <li><strong>deleteByRoslinaId</strong>: Usuwa roślinę po jej identyfikatorze.</li>
 * <li><strong>createRoslinaId</strong>: Tworzy unikalny identyfikator dla rośliny.</li>
 * </ul>
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RoslinaService {
    private final RoslinaRepository roslinaRepository;
    private final WlasciwoscRepository wlasciwoscRepository;
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

    // @Transactional(readOnly = true)
    // public RoslinaResponse findById(Long id) {
    //     Roslina ros = roslinaRepository.findById(id)
    //         .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rośliny o id: " + id));
    //     return roslinaMapper.toRoslinaResponse(ros);
    // }

    /**
     * Znajduje roślinę po jej identyfikatorze.
     *
     * @param id identyfikator rośliny.
     * @return RoslinaResponse zawierający informacje o roślinie.
     */
    @Transactional(readOnly = true)
    public RoslinaResponse findByRoslinaId(String id) {
        Roslina ros = roslinaRepository.findByRoslinaId(id)
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rośliny o roslinaId: " + id));
        return roslinaMapper.roslinaToRoslinaResponseWithWlasciwosci(ros);
    }

    /**
     * Znajduje roślinę po jej nazwie łacińskiej.
     *
     * @param nazwaLacinska nazwa łacińska rośliny.
     * @return RoslinaResponse zawierający informacje o roślinie.
     */
    @Transactional(readOnly = true)
    public RoslinaResponse findByNazwaLacinska(String nazwaLacinska) {
        Roslina ros = roslinaRepository.findByNazwaLacinskaWithWlasciwosci(nazwaLacinska)
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rośliny o nazwie łacińskiej: " + nazwaLacinska));
        RoslinaResponse res = roslinaMapper.roslinaToRoslinaResponseWithWlasciwosci(ros);

        return res;
    }

    /**
     * Pobiera właściwości roślin wraz z ich relacjami.
     *
     * @return zbiór obiektów WlasciwoscResponse.
     */
    @Transactional(readOnly = true)
    public Set<WlasciwoscResponse> getWlasciwosciWithRelations() {
        Set<WlasciwosciRodzaje> responses = wlasciwoscRepository.getWlasciwosciWithRelacje();
        return responses.stream()
            .map(response -> WlasciwoscResponse.builder()
                .etykieta(response.getEtykieta())
                .nazwy(response.getNazwy())
                .build())
            .collect(Collectors.toSet());
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
        request.setRoslinaId(createRoslinaId());

        Optional<Roslina> roslina = roslinaRepository.findByNazwaLacinska(request.getNazwaLacinska());
        if (roslina.isPresent()) {
            throw new EntityAlreadyExistsException("Roslina o nazwie łacińskiej \"" + request.getNazwaLacinska() + "\" już istnieje.");
        }
        
        if (file != null) {
            request.setObraz(fileStoreService.saveRoslina(file, request.getNazwaLacinska()));
        } else {
            request.setObraz(defaultRoslinaObrazName);
        }

        if(request.areWlasciwosciEmpty()) {
            Roslina pl = roslinaMapper.toRoslina(request);
            Roslina ros = roslinaRepository.addRoslina(pl);
            return ros;
        }

        Roslina ros = roslinaRepository.addRoslina(
            request.getRoslinaId(),
            request.getNazwa(), request.getNazwaLacinska(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosciAsMap());

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
        request.setRoslinaId(createRoslinaId());
        Roslina pl = roslinaMapper.toRoslina(request);
        
        String leObraz = fileStoreService.saveSeededRoslina(file, request.getObraz());
        request.setObraz(leObraz);
        if(request.areWlasciwosciEmpty()) {
            Roslina ros = roslinaRepository.addRoslina(pl);
            return ros;
        }
        Roslina ros = roslinaRepository.addRoslina(
            request.getRoslinaId(),
            request.getNazwa(), request.getNazwaLacinska(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosciAsMap());

        return ros;
    }

    /**
     * Aktualizuje istniejącą roślinę.
     *
     * @param staraNazwaLacinska stara nazwa łacińska rośliny.
     * @param request obiekt zawierający nowe dane rośliny.
     * @return Roslina zawierający informacje o zaktualizowanej roślinie.
     */
    public RoslinaResponse update(String staraNazwaLacinska, RoslinaRequest request) {
        request.setNazwaLacinska(request.getNazwaLacinska().toLowerCase());
        roslinaRepository.findByNazwaLacinska(staraNazwaLacinska)
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rośliny o nazwie łacińskiej: " + request.getNazwaLacinska()));

        if(!staraNazwaLacinska.equals(request.getNazwaLacinska())) {
            Optional<Roslina> anotherRoslina = roslinaRepository.findByNazwaLacinska(request.getNazwaLacinska());
            if(anotherRoslina.isPresent()) {
                throw new EntityAlreadyExistsException("Roslina o nazwie łacińskiej \"" + request.getNazwaLacinska() + "\" już istnieje.");
            }           
        }

        System.out.println("\n\n\n Nazwa: " + request.getNazwa() + "\n\n\n");


        if(request.areWlasciwosciEmpty()) {
            Roslina ros = roslinaRepository.updateRoslina(
                request.getNazwa(), staraNazwaLacinska, 
                request.getOpis(),  
                request.getWysokoscMin(), request.getWysokoscMax(),
                request.getNazwaLacinska());
            return roslinaMapper.toRoslinaResponse(ros);
        }
        
        Roslina ros = roslinaRepository.updateRoslina(
            request.getNazwa(), staraNazwaLacinska, 
            request.getOpis(),
            request.getWysokoscMin(), request.getWysokoscMax(),
            request.getNazwaLacinska(), 
            request.getWlasciwosciAsMap());
        roslinaRepository.removeLeftoverWlasciwosci();
        
        return roslinaMapper.roslinaToRoslinaResponseWithWlasciwosci(ros);
    }

    /**
     * Aktualizuje obraz rośliny.
     *
     * @param nazwaLacinska nazwa łacińska rośliny.
     * @param file obiekt MultipartFile zawierający obraz rośliny.
     * @return Roslina zawierający informacje o zaktualizowanej roślinie.
     */
    // Uwaga: to jest do głównej rośliny, nie customowej
    public RoslinaResponse uploadRoslinaObraz(String nazwaLacinska, MultipartFile file) {
        log.info("Aktualizacja obrazu rośliny: " + nazwaLacinska);
        Roslina roslina = roslinaRepository.findByNazwaLacinska(nazwaLacinska)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rośliny o nazwie łacińskiej: " + nazwaLacinska));
        
        String pfp = fileStoreService.saveRoslina(file, nazwaLacinska);
        if(pfp == null){
            throw new EntityNotFoundException("Nie udało się załadować pliku obrazu");
        }
        fileUtils.deleteObraz(roslina.getObraz());

        roslina.setObraz(pfp);
        Roslina ros = roslinaRepository.updateRoslinaObraz(roslina.getNazwaLacinska(), pfp);
        return roslinaMapper.toRoslinaResponse(ros);
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
     * @param roslinaId identyfikator rośliny.
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     */
    public void deleteByRoslinaId(String roslinaId, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Roslina roslina = roslinaRepository.findByRoslinaId(roslinaId)
            .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono rośliny o id " + roslinaId));
        

        if(roslina.isUzytkownikRoslina() ) {
            Uzytkownik targetUzyt = roslina.getUzytkownik();
            if (!uzyt.hasAuthenticationRights(targetUzyt, uzyt)) {
                throw new ForbiddenException("Nie masz uprawnień do usunięcia rośliny użytkownika: " + targetUzyt.getNazwa());
            }

            fileUtils.deleteObraz(roslina.getObraz());
            roslinaRepository.deleteByRoslinaId(roslinaId);
            return;
            
        } else if (uzyt.isNormalUzytkownik()) {
            throw new ForbiddenException("Nie masz uprawnień do usunięcia tej rośliny.");
        }
        
        fileUtils.deleteObraz(roslina.getObraz());
        roslinaRepository.deleteByRoslinaId(roslinaId);
    }

    /**
     * Usuwa roślinę po jej identyfikatorze.
     *
     * @param roslinaId identyfikator rośliny.
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika.
     */
    public void deleteByRoslinaId(String roslinaId, Uzytkownik uzyt) {
        Roslina roslina = roslinaRepository.findByRoslinaId(roslinaId)
            .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono rośliny o id " + roslinaId));

        fileUtils.deleteObraz(roslina.getObraz());
        roslinaRepository.deleteByRoslinaId(roslinaId);
    }


    /**
     * Tworzy unikalny identyfikator dla rośliny.
     *
     * @return unikalny identyfikator rośliny.
     */
    public String createRoslinaId() {
        String resultId = UUID.randomUUID().toString();
        do { 
            Optional<Roslina> kom = roslinaRepository.findByRoslinaId(resultId);
            if(kom.isEmpty()){
                break;
            }
            resultId = UUID.randomUUID().toString();
        } while (true);
        return resultId;
    }
}
