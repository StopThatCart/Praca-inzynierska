package com.example.yukka.model.dzialka.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.handler.EntityNotFoundException;
import com.example.yukka.model.dzialka.Dzialka;
import com.example.yukka.model.dzialka.DzialkaResponse;
import com.example.yukka.model.dzialka.DzialkaRoslinaRequest;
import com.example.yukka.model.dzialka.MoveRoslinaRequest;
import com.example.yukka.model.dzialka.Pozycja;
import com.example.yukka.model.dzialka.ZasadzonaNaReverse;
import com.example.yukka.model.dzialka.repository.DzialkaRepository;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.controller.RoslinaRepository;
import com.example.yukka.model.roslina.controller.UzytkownikRoslinaRepository;
import com.example.yukka.model.social.CommonMapperService;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DzialkaService {
    private final DzialkaRepository dzialkaRepository;
    private final RoslinaRepository roslinaRepository;
    private final UzytkownikRoslinaRepository uzytkownikRoslinaRepository;
    @SuppressWarnings("unused")
    private final ZasadzonaNaService zasadzonaNaService;
    private final UzytkownikRepository uzytkownikRepository;
    private final FileStoreService fileStoreService;
    private final FileUtils fileUtils;

    private final RoslinaMapper roslinaMapper;
    private final CommonMapperService commonMapperService;



    @Transactional(readOnly = true)
    public List<DzialkaResponse> getDzialki(Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        List<Dzialka> dzialki = dzialkaRepository.getDzialkiOfUzytkownikByNazwa(uzyt.getNazwa());
        List<DzialkaResponse> dzialkiResponse = dzialki.stream()
                .map(roslinaMapper::toDzialkaResponse)
                .toList();
        return dzialkiResponse;
    }

    @Transactional(readOnly = true)
    public List<DzialkaResponse> getDzialkiOfUzytkownik(String nazwa, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        Uzytkownik wlasciciel = uzytkownikRepository.findByNazwa(nazwa)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika " + nazwa));

        if(!wlasciciel.getUstawienia().isOgrodPokaz() && !uzyt.getEmail().equals(wlasciciel.getEmail())) {
            throw new AccessDeniedException("Ogród użytkownika " + nazwa + " jest ukryty");
        }

        List<Dzialka> dzialki = dzialkaRepository.getDzialkiOfUzytkownikByNazwa(nazwa);
        List<DzialkaResponse> dzialkiResponse = dzialki.stream()
                .map(roslinaMapper::toDzialkaResponse)
                .toList();
        
        return dzialkiResponse;
    }


    // Pobiera działki jakiegoś użytkownika, o ile on na to pozwala
    @Transactional(readOnly = true)
    public DzialkaResponse getDzialkaOfUzytkownikByNumer(int numer, String nazwa) {
        Uzytkownik wlasciciel = uzytkownikRepository.findByNazwa(nazwa).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika " + nazwa));

        if(!wlasciciel.getUstawienia().isOgrodPokaz()) {
            throw new AccessDeniedException("Ogród użytkownika " + nazwa + " jest ukryty");
        }
        Dzialka dzialka = getDzialkaByNumer(numer, wlasciciel);

        return roslinaMapper.toDzialkaResponse(dzialka);
    }

    // Pobiera WŁASNĄ działkę
    @Transactional(readOnly = true)
    public DzialkaResponse getDzialkaByNumer(int numer, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        return roslinaMapper.toDzialkaResponse(getDzialkaByNumer(numer, uzyt));
    }

    @Transactional(readOnly = true)
    public Dzialka getDzialkaByNumer(int numer, Uzytkownik uzyt) {
        return dzialkaRepository.getDzialkaByNumer(uzyt.getEmail(), numer)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono działki " + numer + " dla użytkownika " + uzyt.getEmail()));
    }
/* 
    public Optional<Dzialka> getDzialkaById(Long id) {
        return dzialkaRepository.findById(id);
    }
*/
    // Do zapisywania z obrazem lepiej na razie poczekać
    public DzialkaResponse saveRoslinaToDzialka(DzialkaRoslinaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        return saveRoslinaToDzialka(request, uzyt);
    }

    public DzialkaResponse saveRoslinaToDzialka(DzialkaRoslinaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);
            
        if(dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY()) != null) {
            System.out.println("Koordynaty zajęte.");
            throw new IllegalArgumentException("Pozycja (" + request.getX() + ", " + request.getY() + ") jest zajęta");
        }

        Dzialka dzialkaZRoslina = saveToDzialka(request, uzyt);
        return roslinaMapper.toDzialkaResponse(dzialkaZRoslina);
    }


    private Dzialka saveToDzialka(DzialkaRoslinaRequest request, Uzytkownik uzyt) {
        String nazwaLacinskaOrId;

        if(request.getRoslinaId() != null) {
            uzytkownikRoslinaRepository.findByRoslinaId(request.getRoslinaId())
            .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono rośliny użytkownika o roslinaId: " + request.getNazwaLacinska()));
            nazwaLacinskaOrId = request.getRoslinaId();
        } else if (request.getNazwaLacinska() != null) {
            roslinaRepository.findByNazwaLacinska(request.getNazwaLacinska())
            .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono rośliny o nazwie łacińskiej " + request.getNazwaLacinska()));
            nazwaLacinskaOrId = request.getNazwaLacinska();
        } else {
            throw new IllegalArgumentException("Nie podano nazwy łacińskiej ani id rośliny");
        }

        return dzialkaRepository.saveRoslinaToDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
        request.getX(), request.getY(), 
        request.getPozycjeX(), request.getPozycjeY(),
        request.getObraz(), nazwaLacinskaOrId);
    }


    public DzialkaResponse updateRoslinaPositionInDzialka(MoveRoslinaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        return updateRoslinaPositionInDzialka(request, uzyt);
    }

    // TODO: Sprawdzanie pozycji tabX tabY albo position
    public DzialkaResponse updateRoslinaPositionInDzialka(MoveRoslinaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialkiStary(), uzyt);

        ZasadzonaNaReverse zasadzonaRoslina = dzialka.getZasadzonaNaByCoordinates(request.getXStary(), request.getYStary());
        if (zasadzonaRoslina == null) {
            throw new IllegalArgumentException("Nie znaleziono rośliny na pozycji (" + request.getXStary() + ", " + request.getYStary() + ")");
        }

        if (request.getNumerDzialkiNowy() != 0 && request.getNumerDzialkiStary() != request.getNumerDzialkiNowy()) {
            return moveRoslinaToDifferentDzialka(request, uzyt);
        }
    
        return moveRoslinaWithinSameDzialka(request, uzyt, dzialka);
    }

    private DzialkaResponse moveRoslinaToDifferentDzialka(MoveRoslinaRequest request, Uzytkownik uzyt) {
        Dzialka dzialka2 = getDzialkaByNumer(request.getNumerDzialkiNowy(), uzyt);
    
        if (dzialka2.getZasadzonaNaByCoordinates(request.getXNowy(), request.getYNowy()) != null) {
            System.out.println("BOUIIIIIIIIIIIIIIIIIII");
            throw new IllegalArgumentException("Pozycja (" + request.getXNowy() + ", " + request.getYNowy() + ") jest zajęta");
        }
    
        Dzialka res = dzialkaRepository.changeRoslinaPozycjaInDzialka(uzyt.getEmail(),
                request.getNumerDzialkiStary(), request.getNumerDzialkiNowy(),
                request.getXStary(), request.getYStary(),
                request.getXNowy(), request.getYNowy(),
                request.getPozycjeX(), request.getPozycjeY());
        return roslinaMapper.toDzialkaResponse(res);
    }
    
    private DzialkaResponse moveRoslinaWithinSameDzialka(MoveRoslinaRequest request, Uzytkownik uzyt, Dzialka dzialka) {
        ZasadzonaNaReverse zasadzonaRoslina = dzialka.getZasadzonaNaByCoordinates(request.getXStary(), request.getYStary());
        ZasadzonaNaReverse zasadzonaRoslinaNowa = dzialka.getZasadzonaNaByCoordinates(request.getXNowy(), request.getYNowy());
        if (zasadzonaRoslinaNowa != null) {
            if(!zasadzonaRoslina.equalsRoslina(zasadzonaRoslinaNowa)) {
                throw new IllegalArgumentException("Pozycja (" + request.getXNowy() + ", " + request.getYNowy() + ") jest zajęta");
            }
        }

        List<ZasadzonaNaReverse> zasadzoneRosliny = dzialka.getZasadzoneRosliny();
        for (ZasadzonaNaReverse zasadzona : zasadzoneRosliny) {
            if(zasadzona.equalsRoslina(request.getRoslinaId()) || zasadzona.equalsRoslina(request.getNazwaLacinska())) {
                continue;
            }

            List<Pozycja> pozycje = zasadzona.getPozycje();
            List<Pozycja> nowePozycje = request.getPozycje();

            for (Pozycja pozycja : pozycje) {
                if (nowePozycje.contains(pozycja)) {
                    throw new IllegalArgumentException("Pozycja (" + pozycja.getX() + ", " + pozycja.getY() 
                    + ") jest zajęta przez inną roślinę: " + zasadzona.getRoslina().getNazwa());
                }
            }
        }

        System.out.println("Pozycje: " + request.getPozycje().toString());
    
        Dzialka res = dzialkaRepository.changeRoslinaPozycjaInDzialka(uzyt.getEmail(),
                request.getNumerDzialkiStary(),
                request.getXStary(), request.getYStary(),
                request.getXNowy(), request.getYNowy(),
                request.getPozycjeX(), request.getPozycjeY());
        return roslinaMapper.toDzialkaResponse(res);
    }



    public DzialkaResponse updateRoslinaObrazInDzialka(DzialkaRoslinaRequest request, MultipartFile file, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        return updateRoslinaObrazInDzialka(request, file, uzyt);
    }

    public DzialkaResponse updateRoslinaObrazInDzialka(DzialkaRoslinaRequest request, MultipartFile file, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        // Zawsze zwróci działkę zalogowanego użytkownika
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);

        System.out.println("Le aktualizacja obrazu rośliny na działce");
        if(dzialkaRepository.checkIfCoordinatesAreOccupied(uzyt.getEmail(), request.getNumerDzialki(), request.getX(), request.getY())) {
            if (dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY()).getObraz() != null) {
                fileUtils.deleteObraz(dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY()).getObraz());
            }
            String pfp = fileStoreService.saveRoslinaObrazInDzialka(file, uzyt.getUzytId());
            if(pfp == null) return null;
            
            Dzialka dzialkaZRoslina = dzialkaRepository.updateRoslinaObrazInDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
            request.getX(), request.getY(), pfp);

            return roslinaMapper.toDzialkaResponse(dzialkaZRoslina);
        } else {
            return null;
        }
    }

    public void deleteRoslinaObrazInDzialka(DzialkaRoslinaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        deleteRoslinaObrazInDzialka(request, uzyt);
    }

    public void deleteRoslinaObrazInDzialka(DzialkaRoslinaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        // Zawsze zwróci działkę zalogowanego użytkownika
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);

        System.out.println("Le aktualizacja obrazu rośliny na działce");
        if(dzialkaRepository.checkIfCoordinatesAreOccupied(uzyt.getEmail(), request.getNumerDzialki(), request.getX(), request.getY())) {
            if (dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY()).getObraz() != null) {
                fileUtils.deleteObraz(dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY()).getObraz());
                Dzialka dzialkaZRoslina = dzialkaRepository.deleteRoslinaObrazInDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
                request.getX(), request.getY()); 
            }
        }
    }

    public void deleteRoslinaFromDzialka(DzialkaRoslinaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);
        
        Uzytkownik wlasciciel = dzialka.getOgrod().getUzytkownik();
        
        if(!uzyt.hasAuthenticationRights(wlasciciel, connectedUser)) {
            throw new AccessDeniedException("Nie masz uprawnień do usunięcia rośliny z działki użytkownika " + dzialka.getOgrod().getUzytkownik().getEmail());
        }
        ZasadzonaNaReverse pozycja = dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY());

        if(pozycja == null) {
            throw new IllegalArgumentException("Nie znaleziono rośliny na pozycji (" + request.getX() + ", " + request.getY() + ")");
        }

        fileUtils.deleteObraz(pozycja.getObraz());

        dzialkaRepository.removeRoslinaFromDzialka(uzyt.getEmail(), request.getNumerDzialki(), request.getX(), request.getY());
    }
    
   
}
