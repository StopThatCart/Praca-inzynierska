package com.example.yukka.model.dzialka.service;

import java.util.List;
import java.util.Set;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.FileResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.handler.EntityNotFoundException;
import com.example.yukka.model.dzialka.Dzialka;
import com.example.yukka.model.dzialka.DzialkaResponse;
import com.example.yukka.model.dzialka.Pozycja;
import com.example.yukka.model.dzialka.ZasadzonaNaReverse;
import com.example.yukka.model.dzialka.ZasadzonaRoslinaResponse;
import com.example.yukka.model.dzialka.repository.DzialkaRepository;
import com.example.yukka.model.dzialka.requests.BaseDzialkaRequest;
import com.example.yukka.model.dzialka.requests.DzialkaRoslinaRequest;
import com.example.yukka.model.dzialka.requests.MoveRoslinaRequest;
import com.example.yukka.model.enums.Wyswietlanie;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.controller.RoslinaRepository;
import com.example.yukka.model.roslina.controller.UzytkownikRoslinaRepository;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import jakarta.validation.Valid;
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

    @Transactional(readOnly = true)
    public List<DzialkaResponse> getPozycjeInDzialki(Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        Uzytkownik wlasciciel = uzytkownikRepository.findByNazwa(uzyt.getNazwa())
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika " + uzyt.getNazwa()));

        if(!wlasciciel.getUstawienia().isOgrodPokaz() && !uzyt.getEmail().equals(wlasciciel.getEmail())) {
            throw new AccessDeniedException("Ogród użytkownika " + uzyt.getNazwa() + " jest ukryty");
        }

        List<Dzialka> dzialki = dzialkaRepository.getPozycjeInDzialki(uzyt.getNazwa());
        List<DzialkaResponse> dzialkiResponse = dzialki.stream()
                .map(roslinaMapper::toDzialkaResponse)
                .toList();
        
        return dzialkiResponse;
    }

    // Pobiera działki jakiegoś użytkownika, o ile on na to pozwala
    @Transactional(readOnly = true)
    public DzialkaResponse getDzialkaOfUzytkownikByNumer(int numer, String nazwa, Authentication connectedUser) {
        Uzytkownik uzyt = null;
        if(connectedUser != null) {
            uzyt = (Uzytkownik) connectedUser.getPrincipal();
        }

        Uzytkownik wlasciciel = uzytkownikRepository.findByNazwa(nazwa).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika " + nazwa));

        if (!wlasciciel.getUstawienia().isOgrodPokaz() && (uzyt == null || !uzyt.getEmail().equals(wlasciciel.getEmail()))) {
            throw new AccessDeniedException("Ogród użytkownika " + nazwa + " jest ukryty");
        }

        Dzialka dzialka = getDzialkaByNumer(numer, wlasciciel);

        for (ZasadzonaNaReverse zasadzona : dzialka.getZasadzoneRosliny()) {
            System.out.println("\nZasadzona: " + zasadzona.getRoslina().getNazwa());
            System.out.println("Obraz: " + zasadzona.getObraz());
            System.out.println("Obraz 2: " + zasadzona.getRoslina().getObraz());
            System.out.println("Tekstura: " + zasadzona.getTekstura());
        }

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

    public DzialkaResponse saveRoslinaToDzialka(DzialkaRoslinaRequest request, 
    MultipartFile file, MultipartFile tekstura, 
    Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        return saveRoslinaToDzialka(request, file, tekstura, uzyt);
    }

    public DzialkaResponse saveRoslinaToDzialka(DzialkaRoslinaRequest request, 
    MultipartFile obraz, MultipartFile tekstura, 
    Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);

        if(!request.isValidDzialkaRoslinaRequest()) {
            throw new IllegalArgumentException("Pozycja rośliny musi być w przydzielonych kafelkach");
        }
            
        if(dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY()) != null) {
            System.out.println("Koordynaty zajęte.");
            throw new IllegalArgumentException("Pozycja (" + request.getX() + ", " + request.getY() + ") jest zajęta");
        }

        Dzialka dzialkaZRoslina = saveToDzialka(request, obraz, tekstura, uzyt);
        return roslinaMapper.toDzialkaResponse(dzialkaZRoslina);
    }

    private Dzialka saveToDzialka(@Valid DzialkaRoslinaRequest request, MultipartFile obraz, MultipartFile tekstura, Uzytkownik uzyt) {
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

        Dzialka dzialka = dzialkaRepository.getDzialkaByNumer(uzyt.getEmail(), request.getNumerDzialki())
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono działki " + request.getNumerDzialki() + " dla użytkownika " + uzyt.getEmail()));

        if(dzialka.isRoslinaInDzialka(request)) {
            throw new IllegalArgumentException("Podana roślina już znajduje się na działce " + request.getNumerDzialki());
        }

        
        if(tekstura != null) {
            request.setTekstura(fileStoreService.saveRoslinaObrazInDzialka(tekstura, uzyt.getUzytId()));    
        } else if(request.getWyswietlanie().equals(Wyswietlanie.TEKSTURA.toString())) {
            throw new IllegalArgumentException("Nie można wybrać wyświetlania samej tekstury bez podania tekstury");
        }

        if(obraz != null) {
            request.setObraz(fileStoreService.saveRoslinaObrazInDzialka(obraz, uzyt.getUzytId()));    
        }
        

        return dzialkaRepository.saveRoslinaToDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
        request.getX(), request.getY(), 
        request.getPozycjeX(), request.getPozycjeY(),
        request.getKolor(), request.getTekstura(), 
        request.getWyswietlanie().toString(),
        request.getObraz(), nazwaLacinskaOrId);
    }


    public DzialkaResponse updateRoslinaPozycjaInDzialka(MoveRoslinaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        return updateRoslinaPozycjaInDzialka(request, uzyt);
    }


    public DzialkaResponse updateRoslinaPozycjaInDzialka(MoveRoslinaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);

        if(!request.isValidMoveRoslinaRequest()) {
            throw new IllegalArgumentException("Pozycja rośliny musi być w przydzielonych kafelkach");
        }

        ZasadzonaNaReverse zasadzonaRoslina = dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY());
        if (zasadzonaRoslina == null) {
            throw new IllegalArgumentException("Nie znaleziono rośliny na pozycji (" + request.getX() + ", " + request.getY() + ")");
        }

        if (request.getNumerDzialkiNowy() != null && request.getNumerDzialki() != request.getNumerDzialkiNowy()) {
            return moveRoslinaToDifferentDzialka(request, uzyt);
        }
    
        return moveRoslinaWithinSameDzialka(request, uzyt, dzialka);
    }

    private DzialkaResponse moveRoslinaToDifferentDzialka(MoveRoslinaRequest request, Uzytkownik uzyt) {
        Dzialka dzialka2 = getDzialkaByNumer(request.getNumerDzialkiNowy(), uzyt);
    
        if (dzialka2.getZasadzonaNaByCoordinates(request.getXNowy(), request.getYNowy()) != null) {
            throw new IllegalArgumentException("Pozycja (" + request.getXNowy() + ", " + request.getYNowy() + ") jest zajęta");
        }
    
        Dzialka res = dzialkaRepository.changeRoslinaPozycjaInDzialka(uzyt.getEmail(),
                request.getNumerDzialki(), request.getNumerDzialkiNowy(),
                request.getX(), request.getY(),
                request.getXNowy(), request.getYNowy(),
                request.getPozycjeX(), request.getPozycjeY());
        return roslinaMapper.toDzialkaResponse(res);
    }
    
    private DzialkaResponse moveRoslinaWithinSameDzialka(MoveRoslinaRequest request, Uzytkownik uzyt, Dzialka dzialka) {
        ZasadzonaNaReverse zasadzonaRoslina = dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY());
        ZasadzonaNaReverse zasadzonaRoslinaNowa = dzialka.getZasadzonaNaByCoordinates(request.getXNowy(), request.getYNowy());
        if(zasadzonaRoslina == null || zasadzonaRoslina.getRoslina() == null) {
            throw new IllegalArgumentException("Nie znaleziono rośliny na pozycji (" + request.getX() + ", " + request.getY() + ")");
        }

        if (zasadzonaRoslinaNowa != null) {
            if(!zasadzonaRoslina.equalsRoslina(zasadzonaRoslinaNowa)) {
                throw new IllegalArgumentException("Pozycja (" + request.getXNowy() + ", " + request.getYNowy() + ") jest zajęta");
            }
        }

        // List<ZasadzonaNaReverse> zasadzoneRosliny = dzialka.getZasadzoneRosliny();
        // for (ZasadzonaNaReverse zasadzona : zasadzoneRosliny) {
        //     if(zasadzona.equalsRoslina(zasadzonaRoslina.getRoslina().getRoslinaId()) 
        //     || zasadzona.equalsRoslina(zasadzonaRoslina.getRoslina().getNazwaLacinska())) {
        //         continue;
        //     }

        //     Set<Pozycja> pozycje = zasadzona.getPozycje();
        //     Set<Pozycja> nowePozycje = request.getPozycje();

        //     for (Pozycja pozycja : pozycje) {
        //         if (nowePozycje.contains(pozycja)) {
        //             throw new IllegalArgumentException("Pozycja (" + pozycja.getX() + ", " + pozycja.getY() 
        //             + ") jest zajęta przez inną roślinę: " + zasadzona.getRoslina().getNazwa());
        //         }
        //     }
        // }
        List<Pozycja> zajetePozycje = dzialka.isPozycjeOccupied(request);
        if (!zajetePozycje.isEmpty()) {
            throw new IllegalArgumentException("Pozycje są zajęte przez inne rośliny: " + zajetePozycje);
        }

        System.out.println("Pozycje: " + request.getPozycje().toString());
    
        Dzialka res = dzialkaRepository.changeRoslinaPozycjaInDzialka(uzyt.getEmail(),
                request.getNumerDzialki(),
                request.getX(), request.getY(),
                request.getXNowy(), request.getYNowy(),
                request.getPozycjeX(), request.getPozycjeY());
        return roslinaMapper.toDzialkaResponse(res);
    }


    public DzialkaResponse updateRoslinaKolorInDzialka(DzialkaRoslinaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        return updateRoslinaKolorInDzialka(request, uzyt);
    }

    public DzialkaResponse updateRoslinaKolorInDzialka(DzialkaRoslinaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);

        if (request.getKolor() == null) throw new IllegalArgumentException("Nie podano koloru");

        System.out.println("Le aktualizacja koloru rośliny na działce");
        if (dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY()) != null) {

            Dzialka dzialkaZRoslina = dzialkaRepository.updateRoslinaKolorInDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
            request.getX(), request.getY(), request.getKolor());

            return roslinaMapper.toDzialkaResponse(dzialkaZRoslina);
        } else return null;
    }

    public DzialkaResponse updateRoslinaWyswietlanieInDzialka(DzialkaRoslinaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        return updateRoslinaWyswietlanieInDzialka(request, uzyt);
    }

    public DzialkaResponse updateRoslinaWyswietlanieInDzialka(DzialkaRoslinaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);

        if (request.getWyswietlanie() == null) throw new IllegalArgumentException("Nie podano sposobu wyświetlania");


        System.out.println("Le aktualizacja wyświetlania rośliny na działce");
        if (dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY()) != null) {

            Dzialka dzialkaZRoslina = dzialkaRepository.updateRoslinaWyswietlanieInDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
            request.getX(), request.getY(), request.getWyswietlanie().toString());
            // TODO: O nie.
            return roslinaMapper.toDzialkaResponse(dzialkaZRoslina);
        } else return null;
    }


    public FileResponse updateRoslinaObrazInDzialka(DzialkaRoslinaRequest request, MultipartFile obraz, MultipartFile tekstura, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        return updateRoslinaObrazInDzialka(request, obraz, tekstura, uzyt);
    }

    public FileResponse updateRoslinaObrazInDzialka(DzialkaRoslinaRequest request, MultipartFile obraz, MultipartFile tekstura, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);

        System.out.println("Le aktualizacja obrazu rośliny na działce");
        ZasadzonaNaReverse pozycja = dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY());

        if(pozycja != null) {


            Dzialka dzialkaZRoslina = null;
            String pfp = null;
            if(obraz != null) {
                if (pozycja.getObraz() != null) fileUtils.deleteObraz(pozycja.getObraz());
                
                System.out.println("Zapisuję obraz rośliny na działce");
                pfp = fileStoreService.saveRoslinaObrazInDzialka(obraz, uzyt.getUzytId());
                if(pfp == null) return null;
                dzialkaZRoslina = dzialkaRepository.updateRoslinaObrazInDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
                 request.getX(), request.getY(), pfp);
            } else if (tekstura != null) {
                if (pozycja.getTekstura() != null) fileUtils.deleteObraz(pozycja.getTekstura());
                
                System.out.println("Zapisuję teksturę rośliny na działce");
                pfp = fileStoreService.saveRoslinaObrazInDzialka(tekstura, uzyt.getUzytId());
                if(pfp == null) return null;
                dzialkaZRoslina = dzialkaRepository.updateRoslinaTeksturaInDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
                 request.getX(), request.getY(), pfp);
            }
            if (dzialkaZRoslina != null) return FileResponse.builder().content(fileUtils.readRoslinaObrazFile(pfp)).build();
        }

        return null;
    }

    public void deleteRoslinaObrazInDzialka(BaseDzialkaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        deleteRoslinaObrazInDzialka(request, uzyt);
    }

    public void deleteRoslinaObrazInDzialka(BaseDzialkaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        // Zawsze zwróci działkę zalogowanego użytkownika
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);

        ZasadzonaNaReverse target = dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY());
        if (target != null && target.getObraz() != null) {
            fileUtils.deleteObraz(target.getObraz());
            dzialkaRepository.deleteRoslinaObrazInDzialka(
                uzyt.getEmail(), request.getNumerDzialki(), 
                request.getX(), request.getY()); 
        }
    }


    public void deleteRoslinaTeksturaInDzialka(BaseDzialkaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        deleteRoslinaTeksturaInDzialka(request, uzyt);
    }

    public void deleteRoslinaTeksturaInDzialka(BaseDzialkaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);

        ZasadzonaNaReverse target = dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY());
        if (target != null && target.getTekstura() != null) {
            fileUtils.deleteObraz(target.getTekstura());
            dzialkaRepository.deleteRoslinaTeksturaInDzialka(
                uzyt.getEmail(), request.getNumerDzialki(), 
                request.getX(), request.getY());
                
            dzialkaRepository.updateRoslinaWyswietlanieInDzialka(
                uzyt.getEmail(), request.getNumerDzialki(), 
                request.getX(), request.getY(), Wyswietlanie.TEKSTURA_KOLOR.toString());
        }
    }

    public void deleteRoslinaFromDzialka(BaseDzialkaRequest request, Authentication connectedUser) {
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
        if(pozycja.getObraz() != null) {
            fileUtils.deleteObraz(pozycja.getObraz());
        }

        if(pozycja.getTekstura() != null) {
            fileUtils.deleteObraz(pozycja.getTekstura());
        }

        dzialkaRepository.removeRoslinaFromDzialka(uzyt.getEmail(), request.getNumerDzialki(), request.getX(), request.getY());
    }
    
   
}
