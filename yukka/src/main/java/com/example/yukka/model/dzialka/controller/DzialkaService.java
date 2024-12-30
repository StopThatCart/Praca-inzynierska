package com.example.yukka.model.dzialka.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.FileResponse;
import com.example.yukka.file.DefaultImage;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.handler.exceptions.ForbiddenException;
import com.example.yukka.model.dzialka.Dzialka;
import com.example.yukka.model.dzialka.Pozycja;
import com.example.yukka.model.dzialka.ZasadzonaNaReverse;
import com.example.yukka.model.dzialka.enums.Wyswietlanie;
import com.example.yukka.model.dzialka.requests.BaseDzialkaRequest;
import com.example.yukka.model.dzialka.requests.DzialkaRoslinaRequest;
import com.example.yukka.model.dzialka.requests.MoveRoslinaRequest;
import com.example.yukka.model.dzialka.response.DzialkaResponse;
import com.example.yukka.model.dzialka.response.ZasadzonaRoslinaResponse;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.controller.RoslinaRepository;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Serwis zarządzający działkami użytkowników.
 * 
 * <ul>
 * <li><strong>getDzialki</strong> - Pobiera listę działek zalogowanego użytkownika.</li>
 * <li><strong>getDzialkiOfUzytkownik</strong> - Pobiera listę działek innego użytkownika, jeśli ten na to pozwala.</li>
 * <li><strong>getPozycjeInDzialki</strong> - Pobiera pozycje w działkach zalogowanego użytkownika.</li>
 * <li><strong>getDzialkaOfUzytkownikByNumer</strong> - Pobiera działkę innego użytkownika po numerze, jeśli ten na to pozwala.</li>
 * <li><strong>getDzialkaByNumer</strong> - Pobiera własną działkę po numerze.</li>
 * <li><strong>saveRoslinaToDzialka</strong> - Zapisuje roślinę na działce użytkownika.</li>
 * <li><strong>updateRoslinaPozycjaInDzialka</strong> - Aktualizuje pozycję rośliny na działce użytkownika.</li>
 * <li><strong>updateRoslinaKolorInDzialka</strong> - Aktualizuje kolor rośliny na działce użytkownika.</li>
 * <li><strong>updateRoslinaWyswietlanieInDzialka</strong> - Aktualizuje sposób wyświetlania rośliny na działce użytkownika.</li>
 * <li><strong>updateRoslinaNotatkaInDzialka</strong> - Aktualizuje notatkę rośliny na działce użytkownika.</li>
 * <li><strong>updateRoslinaObrazInDzialka</strong> - Aktualizuje obraz rośliny na działce użytkownika.</li>
 * <li><strong>deleteRoslinaObrazInDzialka</strong> - Usuwa obraz rośliny z działki użytkownika.</li>
 * <li><strong>deleteRoslinaTeksturaInDzialka</strong> - Usuwa teksturę rośliny z działki użytkownika.</li>
 * <li><strong>deleteRoslinaFromDzialka</strong> - Usuwa roślinę z działki użytkownika.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DzialkaService {
    private final DzialkaRepository dzialkaRepository;
    private final RoslinaRepository roslinaRepository;
    private final UzytkownikRepository uzytkownikRepository;
    private final FileStoreService fileStoreService;
    private final FileUtils fileUtils;

    private final RoslinaMapper roslinaMapper;

    


    /**
     * Metoda zwraca listę działek powiązanych z użytkownikiem o podanej nazwie.
     *
     * @param nazwa nazwa użytkownika, którego działki mają być zwrócone
     * @param connectedUser obiekt Authentication reprezentujący zalogowanego użytkownika
     * @return lista obiektów DzialkaResponse reprezentujących działki użytkownika
     */
    @Transactional(readOnly = true)
    public List<DzialkaResponse> getDzialkiOfUzytkownik(String nazwa, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        Uzytkownik wlasciciel = uzytkownikRepository.findByNazwa(nazwa)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika " + nazwa));

        if(!wlasciciel.getUstawienia().isOgrodPokaz() && !uzyt.getEmail().equals(wlasciciel.getEmail())) {
            throw new ForbiddenException("Ogród użytkownika " + nazwa + " jest ukryty");
        }

        List<Dzialka> dzialki = dzialkaRepository.getDzialkiOfUzytkownikByNazwa(nazwa);
        List<DzialkaResponse> dzialkiResponse = dzialki.stream()
                .map(roslinaMapper::toDzialkaResponse)
                .toList();
        
        return dzialkiResponse;
    }

    /**
     * Metoda zwraca listę pozycji w działkach powiązanych z zalogowanym użytkownikiem.
     *
     * @param connectedUser obiekt Authentication reprezentujący zalogowanego użytkownika
     * @return lista obiektów DzialkaResponse reprezentujących pozycje w działkach użytkownika
     */
    @Transactional(readOnly = true)
    public List<DzialkaResponse> getPozycjeInDzialki(Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        Uzytkownik wlasciciel = uzytkownikRepository.findByNazwa(uzyt.getNazwa())
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika " + uzyt.getNazwa()));

        if(!wlasciciel.getUstawienia().isOgrodPokaz() && !uzyt.getEmail().equals(wlasciciel.getEmail())) {
            throw new ForbiddenException("Ogród użytkownika " + uzyt.getNazwa() + " jest ukryty");
        }

        List<Dzialka> dzialki = dzialkaRepository.getPozycjeInDzialki(uzyt.getNazwa());
        List<DzialkaResponse> dzialkiResponse = dzialki.stream()
                .map(roslinaMapper::toDzialkaResponse)
                .toList();
        
        return dzialkiResponse;
    }

    /**
     * Metoda zwraca działkę powiązaną z użytkownikiem o podanej nazwie i numerze.
     *
     * @param numer numer działki
     * @param nazwa nazwa użytkownika, którego działka ma być zwrócona
     * @param connectedUser obiekt Authentication reprezentujący zalogowanego użytkownika
     * @return obiekt DzialkaResponse reprezentujący działkę użytkownika
     */
    @Transactional(readOnly = true)
    public DzialkaResponse getDzialkaOfUzytkownikByNumer(int numer, String nazwa, Authentication connectedUser) {
        Uzytkownik uzyt = null;
        if(connectedUser != null) {
            uzyt = (Uzytkownik) connectedUser.getPrincipal();
        }

        Uzytkownik wlasciciel = uzytkownikRepository.findByNazwa(nazwa).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika " + nazwa));
        if (!wlasciciel.getUstawienia().isOgrodPokaz() && (uzyt == null || !uzyt.getEmail().equals(wlasciciel.getEmail()))) {
            throw new ForbiddenException("Ogród użytkownika " + nazwa + " jest ukryty");
        }

        Dzialka dzialka = getDzialkaByNumer(numer, wlasciciel);

        return roslinaMapper.toDzialkaResponse(dzialka);
    }

    /**
     * Metoda zwraca działkę powiązaną z zalogowanym użytkownikiem po numerze.
     *
     * @param numer numer działki
     * @param connectedUser obiekt Authentication reprezentujący zalogowanego użytkownika
     * @return obiekt DzialkaResponse reprezentujący działkę użytkownika
     */
    @Transactional(readOnly = true)
    public DzialkaResponse getDzialkaByNumer(int numer, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        return roslinaMapper.toDzialkaResponse(getDzialkaByNumer(numer, uzyt));
    }

    @Transactional(readOnly = true)
    public Dzialka getDzialkaByNumer(int numer, Uzytkownik uzyt) {
        return dzialkaRepository.getDzialkaByNumer(uzyt.getEmail(), numer)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono działki " + numer + " dla użytkownika " + uzyt.getNazwa()));
    }


    @Transactional(readOnly = true)
    public ZasadzonaRoslinaResponse getRoslinaInDzialka(int numer, int x, int y, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        Dzialka dzialka = dzialkaRepository.getRoslinaInDzialka(uzyt.getEmail(), numer, x, y)
        .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono rośliny na pozycji (" + x + ", " + y + ") na działce " + numer));

        for (ZasadzonaNaReverse zasadzonaNa : dzialka.getZasadzoneRosliny()) {
            System.out.println("Zasadzona na: " + zasadzonaNa.getX() + ", " + zasadzonaNa.getY());
        }

        if (dzialka.getZasadzoneRosliny().isEmpty()) {
            throw new EntityNotFoundException("Nie znaleziono rośliny na pozycji (" + x + ", " + y + ") na działce " + numer);
        }
        
        return roslinaMapper.toZasadzonaRoslinaResponse(dzialka.getZasadzoneRosliny().get(0));
    }

    @Transactional(readOnly = true)
    public ZasadzonaRoslinaResponse getRoslinaInDzialka(int numer, String roslinaId, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        Dzialka dzialka = dzialkaRepository.getRoslinaInDzialka(uzyt.getEmail(), numer, roslinaId)
        .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono rośliny o id " + roslinaId + " na działce " + numer));

        if (dzialka.getZasadzoneRosliny().isEmpty()) {
            throw new EntityNotFoundException("Nie znaleziono rośliny o id " + roslinaId + " na działce " + numer);
        }
        
        return roslinaMapper.toZasadzonaRoslinaResponse(dzialka.getZasadzoneRosliny().get(0));
    }


    /**
     * Zmienia nazwę działki o podanym numerze.
     *
     * @param numer numer działki, której nazwa ma zostać zmieniona
     * @param nazwa nowa nazwa działki
     * @param connectedUser aktualnie zalogowany użytkownik
     * @return nowa nazwa działki
     * @throws IllegalArgumentException jeśli nazwa działki jest pusta lub dłuższa niż 150 znaków
     */
    public String renameDzialka(int numer, String nazwa, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        if(nazwa == null || nazwa.isBlank()) {
            throw new IllegalArgumentException("Nazwa działki nie może być pusta");
        } else if (nazwa.length() > 150) {
            throw new IllegalArgumentException("Nazwa działki nie może być dłuższa niż 150 znaków");
        }

        Dzialka dzialka = getDzialkaByNumer(numer, uzyt);
        dzialka = dzialkaRepository.renameDzialka(uzyt.getEmail(), numer, nazwa);

        return dzialka.getNazwa();
    }

    /**
     * Metoda zapisuje roślinę na działce użytkownika.
     *
     * @param request obiekt DzialkaRoslinaRequest reprezentujący żądanie zapisania rośliny na działce
     * @param file obiekt MultipartFile reprezentujący obraz rośliny
     * @param tekstura obiekt MultipartFile reprezentujący teksturę rośliny
     * @param connectedUser obiekt Authentication reprezentujący zalogowanego użytkownika
     * @return obiekt DzialkaResponse reprezentujący działkę użytkownika
     */
    public DzialkaResponse saveRoslinaToDzialka(DzialkaRoslinaRequest request, 
    MultipartFile file, MultipartFile tekstura, 
    Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        return saveRoslinaToDzialka(request, file, tekstura, uzyt);
    }

    /**
     * Metoda zapisuje roślinę na działce użytkownika.
     *
     * @param request obiekt DzialkaRoslinaRequest reprezentujący żądanie zapisania rośliny na działce
     * @param obraz obiekt MultipartFile reprezentujący obraz rośliny
     * @param tekstura obiekt MultipartFile reprezentujący teksturę rośliny
     * @param connectedUser obiekt Uzytkownik reprezentujący zalogowanego użytkownika
     * @return obiekt DzialkaResponse reprezentujący działkę użytkownika
     */
    public DzialkaResponse saveRoslinaToDzialka(DzialkaRoslinaRequest request, 
    MultipartFile obraz, MultipartFile tekstura, 
    Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        log.info("Zapisywanie rośliny na działce [" + request.getNumerDzialki() + "] użytkownika " + uzyt.getNazwa());
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);

        if(!request.isValidDzialkaRoslinaRequest()) {
            throw new IllegalArgumentException("Pozycja rośliny musi być w przydzielonych kafelkach");
        }
            
        if(dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY()) != null) {
            throw new IllegalArgumentException("Pozycja (" + request.getX() + ", " + request.getY() + ") jest zajęta");
        }

        Dzialka dzialkaZRoslina = saveToDzialka(request, obraz, tekstura, uzyt);
        return roslinaMapper.toDzialkaResponse(dzialkaZRoslina);
    }

    /**
     * Metoda aktualizuje pozycję rośliny na działce użytkownika.
     *
     * @param request obiekt MoveRoslinaRequest reprezentujący żądanie aktualizacji pozycji rośliny na działce
     * @param connectedUser obiekt Authentication reprezentujący zalogowanego użytkownika
     * @return obiekt DzialkaResponse reprezentujący działkę użytkownika
     */
    private Dzialka saveToDzialka(@Valid DzialkaRoslinaRequest request, MultipartFile obraz, MultipartFile tekstura, Uzytkownik uzyt) {
        String nazwaLacinskaOrId;

        if(request.getRoslinaId() != null) {
            roslinaRepository.findByRoslinaId(request.getRoslinaId())
            .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono rośliny użytkownika o roslinaId: " + request.getRoslinaId()));
            nazwaLacinskaOrId = request.getRoslinaId();
        } else {
            throw new IllegalArgumentException("Nie podano id rośliny");
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
        } else {
            request.setTekstura(null);
        }

        if(obraz != null) {
            request.setObraz(fileStoreService.saveRoslinaObrazInDzialka(obraz, uzyt.getUzytId()));    
        } else {
            request.setObraz(null);
        }
        

        return dzialkaRepository.saveRoslinaToDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
        request.getX(), request.getY(), 
        request.getPozycjeX(), request.getPozycjeY(),
        request.getKolor(), request.getTekstura(), 
        request.getWyswietlanie(),
        request.getObraz(), nazwaLacinskaOrId);
    }

    /**
     * Metoda aktualizuje pozycję rośliny na działce użytkownika.
     *
     * @param request obiekt MoveRoslinaRequest reprezentujący żądanie aktualizacji pozycji rośliny na działce
     * @param connectedUser obiekt Authentication reprezentujący zalogowanego użytkownika
     * @return obiekt DzialkaResponse reprezentujący działkę użytkownika
     */
    public DzialkaResponse updateRoslinaPozycjaInDzialka(MoveRoslinaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        return updateRoslinaPozycjaInDzialka(request, uzyt);
    }


    /**
     * Metoda aktualizuje pozycję rośliny na działce użytkownika.
     *
     * @param request obiekt MoveRoslinaRequest reprezentujący żądanie aktualizacji pozycji rośliny na działce
     * @param connectedUser obiekt Uzytkownik reprezentujący zalogowanego użytkownika
     * @return obiekt DzialkaResponse reprezentujący działkę użytkownika
     */
    public DzialkaResponse updateRoslinaPozycjaInDzialka(MoveRoslinaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);
        log.info("Zmiana pozycji rośliny z [" + request.getX() + ", " + request.getY()  
        + "] na [" + request.getXNowy() + ", " + request.getYNowy()
        + "] na działce [" + request.getNumerDzialki() 
        + "] użytkownika " + uzyt.getNazwa());

        ZasadzonaNaReverse zasadzonaRoslina = dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY());
        if (zasadzonaRoslina == null) {
            throw new IllegalArgumentException("Nie znaleziono rośliny na pozycji (" + request.getX() + ", " + request.getY() + ")");
        }

        if (request.getNumerDzialkiNowy() != null && !request.getNumerDzialki().equals(request.getNumerDzialkiNowy())) {
            return moveRoslinaToDifferentDzialka(request, zasadzonaRoslina, uzyt);
        }
    
        return moveRoslinaWithinSameDzialka(request, uzyt, dzialka);
    }

    /**
     * Metoda przenosi roślinę na inną działkę użytkownika.
     * @param request obiekt MoveRoslinaRequest reprezentujący żądanie przeniesienia rośliny na inną działkę
     * @param connectedUser obiekt Uzytkownik reprezentujący zalogowanego użytkownika
     * @return obiekt DzialkaResponse reprezentujący działkę użytkownika
     */
    private DzialkaResponse moveRoslinaToDifferentDzialka(MoveRoslinaRequest request, ZasadzonaNaReverse zasadzonaRoslina, Uzytkownik uzyt) {
        log.info("Przenoszenie rośliny do innej działki" );
        Dzialka dzialka2 = getDzialkaByNumer(request.getNumerDzialkiNowy(), uzyt);

        if(dzialka2.isRoslinaInDzialka(zasadzonaRoslina)) {
            throw new IllegalArgumentException("Podana roślina już znajduje się na działce " + request.getNumerDzialki());
        }
    
        if (dzialka2.getZasadzonaNaByCoordinates(request.getXNowy(), request.getYNowy()) != null) {
            throw new IllegalArgumentException("Pozycja (" + request.getXNowy() + ", " + request.getYNowy() + ") jest zajęta");
        }

        List<Pozycja> zajetePozycje = dzialka2.arePozycjeOccupied(request);
        if (!zajetePozycje.isEmpty()) {
            throw new IllegalArgumentException("Pozycje są zajęte przez inne rośliny: " + zajetePozycje);
        }
    
        Dzialka res = dzialkaRepository.changeRoslinaPozycjaInDzialka(uzyt.getEmail(),
                request.getNumerDzialki(), request.getNumerDzialkiNowy(),
                request.getX(), request.getY(),
                request.getXNowy(), request.getYNowy(),
                request.getPozycjeX(), request.getPozycjeY());
        return roslinaMapper.toDzialkaResponse(res);
    }
    
    /**
     * Metoda przenosi roślinę na tę samą działkę użytkownika.
     * @param request obiekt MoveRoslinaRequest reprezentujący żądanie przeniesienia rośliny na tę samą działkę
     * @param connectedUser obiekt Uzytkownik reprezentujący zalogowanego użytkownika
     * @return obiekt DzialkaResponse reprezentujący działkę użytkownika
     */
    private DzialkaResponse moveRoslinaWithinSameDzialka(MoveRoslinaRequest request, Uzytkownik uzyt, Dzialka dzialka) {
        log.info("Przenoszenie rośliny na tę samą działkę" );
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

        List<Pozycja> zajetePozycje = dzialka.arePozycjeOccupied(request);
        if (!zajetePozycje.isEmpty()) {
            throw new IllegalArgumentException("Pozycje są zajęte przez inne rośliny: " + zajetePozycje);
        }

        //System.out.println("Pozycje: " + request.getPozycje().toString());
    
        Dzialka res = dzialkaRepository.changeRoslinaPozycjaInDzialka(uzyt.getEmail(),
                request.getNumerDzialki(),
                request.getX(), request.getY(),
                request.getXNowy(), request.getYNowy(),
                request.getPozycjeX(), request.getPozycjeY());
        return roslinaMapper.toDzialkaResponse(res);
    }

    /**
     * Metoda aktualizuje kolor rośliny na działce użytkownika.
     *
     * @param request obiekt DzialkaRoslinaRequest reprezentujący żądanie aktualizacji koloru rośliny na działce
     * @param connectedUser obiekt Authentication reprezentujący zalogowanego użytkownika
     * @return obiekt DzialkaResponse reprezentujący działkę użytkownika
     */
    public DzialkaResponse updateRoslinaKolorInDzialka(DzialkaRoslinaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        return updateRoslinaKolorInDzialka(request, uzyt);
    }

    /**
     * Metoda aktualizuje kolor rośliny na działce użytkownika.
     *
     * @param request obiekt DzialkaRoslinaRequest reprezentujący żądanie aktualizacji koloru rośliny na działce
     * @param connectedUser obiekt Uzytkownik reprezentujący zalogowanego użytkownika
     * @return obiekt DzialkaResponse reprezentujący działkę użytkownika
     */
    public DzialkaResponse updateRoslinaKolorInDzialka(DzialkaRoslinaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);

        if (request.getKolor() == null) throw new IllegalArgumentException("Nie podano koloru");

        log.info("Aktualizacja koloru rośliny na pozycji: [" 
        + request.getX() + ", " + request.getY()  
        + "] na działce [" + request.getNumerDzialki() 
        + "] użytkownika " + uzyt.getNazwa());
        if (dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY()) != null) {

            Dzialka dzialkaZRoslina = dzialkaRepository.updateRoslinaKolorInDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
            request.getX(), request.getY(), request.getKolor());

            return roslinaMapper.toDzialkaResponse(dzialkaZRoslina);
        } else return null;
    }
    /**
     * Metoda aktualizuje sposób wyświetlania rośliny na działce użytkownika.
     *
     * @param request obiekt DzialkaRoslinaRequest reprezentujący żądanie aktualizacji sposobu wyświetlania rośliny na działce
     * @param connectedUser obiekt Authentication reprezentujący zalogowanego użytkownika
     * @return obiekt DzialkaResponse reprezentujący działkę użytkownika
     */
    public DzialkaResponse updateRoslinaWyswietlanieInDzialka(DzialkaRoslinaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        return updateRoslinaWyswietlanieInDzialka(request, uzyt);
    }

    /**
     * Metoda aktualizuje sposób wyświetlania rośliny na działce użytkownika.
     *
     * @param request obiekt DzialkaRoslinaRequest reprezentujący żądanie aktualizacji sposobu wyświetlania rośliny na działce
     * @param connectedUser obiekt Uzytkownik reprezentujący zalogowanego użytkownika
     * @return obiekt DzialkaResponse reprezentujący działkę użytkownika
     */
    public DzialkaResponse updateRoslinaWyswietlanieInDzialka(DzialkaRoslinaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        log.info("Aktualizacja wyświetlania rośliny na pozycji: [" 
        + request.getX() + ", " + request.getY()  
        + "] na działce [" + request.getNumerDzialki() 
        + "] użytkownika " + uzyt.getNazwa());

        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);

        if (request.getWyswietlanie() == null) throw new IllegalArgumentException("Nie podano sposobu wyświetlania");

        ZasadzonaNaReverse pozycja = dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY());

        if (pozycja != null) {
            if(pozycja.getTekstura() == null && request.getWyswietlanie().equals(Wyswietlanie.TEKSTURA.toString())) {
                throw new IllegalArgumentException("Nie można wybrać wyświetlania samej tekstury bez podania obrazu");
            }
            
            Dzialka dzialkaZRoslina = dzialkaRepository.updateRoslinaWyswietlanieInDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
            request.getX(), request.getY(), request.getWyswietlanie());
            return roslinaMapper.toDzialkaResponse(dzialkaZRoslina);
        } else {
            throw new IllegalArgumentException("Nie znaleziono rośliny na pozycji (" + request.getX() + ", " + request.getY() + ")");
        }
    }

    /**
     * Metoda aktualizuje notatkę rośliny na działce użytkownika.
     *
     * @param request obiekt DzialkaRoslinaRequest reprezentujący żądanie aktualizacji notatki rośliny na działce
     * @param connectedUser obiekt Authentication reprezentujący zalogowanego użytkownika
     * @return obiekt DzialkaResponse reprezentujący działkę użytkownika
     */
    public DzialkaResponse updateRoslinaNotatkaInDzialka(DzialkaRoslinaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        return updateRoslinaNotatkaInDzialka(request, uzyt);
    }

    /**
     * Metoda aktualizuje notatkę rośliny na działce użytkownika.
     *
     * @param request obiekt DzialkaRoslinaRequest reprezentujący żądanie aktualizacji notatki rośliny na działce
     * @param connectedUser obiekt Uzytkownik reprezentujący zalogowanego użytkownika
     * @return obiekt DzialkaResponse reprezentujący działkę użytkownika
     */
    public DzialkaResponse updateRoslinaNotatkaInDzialka(DzialkaRoslinaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);

        ZasadzonaNaReverse pozycja = dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY());
        System.out.println("Le aktualizacja notatki rośliny na działce");
        if (pozycja != null) {
            Dzialka dzialkaZRoslina = dzialkaRepository.updateRoslinaNotatkaInDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
            request.getX(), request.getY(), request.getNotatka());

            return roslinaMapper.toDzialkaResponse(dzialkaZRoslina);
        } else {
            throw new IllegalArgumentException("Nie znaleziono rośliny na pozycji (" + request.getX() + ", " + request.getY() + ")");
        }
    }

    /**
     * Metoda aktualizuje obraz rośliny na działce użytkownika.
     *
     * @param request obiekt DzialkaRoslinaRequest reprezentujący żądanie aktualizacji obrazu rośliny na działce
     * @param obraz obiekt MultipartFile reprezentujący obraz rośliny
     * @param tekstura obiekt MultipartFile reprezentujący teksturę rośliny
     * @param connectedUser obiekt Authentication reprezentujący zalogowanego użytkownika
     * @return obiekt FileResponse reprezentujący obraz rośliny
     */
    public FileResponse updateRoslinaObrazInDzialka(DzialkaRoslinaRequest request, MultipartFile obraz, MultipartFile tekstura, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        return updateRoslinaObrazInDzialka(request, obraz, tekstura, uzyt);
    }

    /**
     * Metoda aktualizuje obraz rośliny na działce użytkownika.
     *
     * @param request obiekt DzialkaRoslinaRequest reprezentujący żądanie aktualizacji obrazu rośliny na działce
     * @param obraz obiekt MultipartFile reprezentujący obraz rośliny
     * @param tekstura obiekt MultipartFile reprezentujący teksturę rośliny
     * @param connectedUser obiekt Uzytkownik reprezentujący zalogowanego użytkownika
     * @return obiekt FileResponse reprezentujący obraz rośliny
     */
    public FileResponse updateRoslinaObrazInDzialka(DzialkaRoslinaRequest request, MultipartFile obraz, MultipartFile tekstura, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);

        log.info("Aktualizacja obrazu/tesktury rośliny na pozycji: [" 
        + request.getX() + ", " + request.getY()  
        + "] na działce: [" + request.getNumerDzialki()
        + "] użytkownika " + uzyt.getNazwa());
        ZasadzonaNaReverse pozycja = dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY());

        if(pozycja != null) {
            Dzialka dzialkaZRoslina = null;
            String pfp = null;
            if(obraz != null) {
                if (pozycja.getObraz() != null) fileUtils.deleteObraz(pozycja.getObraz());
                
                log.info("Zapisywanie obrazu rośliny na działce[" + request.getNumerDzialki() + "]");
                pfp = fileStoreService.saveRoslinaObrazInDzialka(obraz, uzyt.getUzytId());
                if(pfp == null) return null;
                dzialkaZRoslina = dzialkaRepository.updateRoslinaObrazInDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
                 request.getX(), request.getY(), pfp);
            }
            
            if (tekstura != null) {
                if (pozycja.getTekstura() != null) fileUtils.deleteObraz(pozycja.getTekstura());

                log.info("Zapisywanie tekstury rośliny na działce[" + request.getNumerDzialki() + "]");
                pfp = fileStoreService.saveRoslinaObrazInDzialka(tekstura, uzyt.getUzytId());
                if(pfp == null) return null;
                dzialkaZRoslina = dzialkaRepository.updateRoslinaTeksturaInDzialka(uzyt.getEmail(), request.getNumerDzialki(), 
                 request.getX(), request.getY(), pfp);
            }

            if (dzialkaZRoslina != null) 
            return FileResponse.builder().content(fileUtils.readFile(pfp, DefaultImage.ROSLINA)).build();
        }

        return null;
    }

    /**
     * Metoda usuwa obraz rośliny na działce użytkownika.
     *
     * @param request obiekt BaseDzialkaRequest reprezentujący żądanie usunięcia obrazu rośliny na działce
     * @param connectedUser obiekt Authentication reprezentujący zalogowanego użytkownika
     */
    public void deleteRoslinaObrazInDzialka(BaseDzialkaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        deleteRoslinaObrazInDzialka(request, uzyt);
    }

    /**
     * Metoda usuwa obraz rośliny na działce użytkownika.
     *
     * @param request obiekt BaseDzialkaRequest reprezentujący żądanie usunięcia obrazu rośliny na działce
     * @param connectedUser obiekt Uzytkownik reprezentujący zalogowanego użytkownika
     */
    public void deleteRoslinaObrazInDzialka(BaseDzialkaRequest request, Uzytkownik connectedUser) {
        Uzytkownik uzyt = connectedUser;
        // Zawsze zwróci działkę zalogowanego użytkownika
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);

        log.info("Usuwanie obrazu rośliny na pozycji: [" + 
                + request.getX() + ", " + request.getY()   
                + "] na działce [" + request.getNumerDzialki() + "]"
        + " użytkownika " + uzyt.getNazwa());

        ZasadzonaNaReverse target = dzialka.getZasadzonaNaByCoordinates(request.getX(), request.getY());
        if (target != null && target.getObraz() != null) {
            fileUtils.deleteObraz(target.getObraz());
            dzialkaRepository.deleteRoslinaObrazInDzialka(
                uzyt.getEmail(), request.getNumerDzialki(), 
                request.getX(), request.getY()); 
        }
    }

    /**
     * Metoda usuwa teksturę rośliny na działce użytkownika.
     *
     * @param request obiekt BaseDzialkaRequest reprezentujący żądanie usunięcia tekstury rośliny na działce
     * @param connectedUser obiekt Authentication reprezentujący zalogowanego użytkownika
     */
    public void deleteRoslinaTeksturaInDzialka(BaseDzialkaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        deleteRoslinaTeksturaInDzialka(request, uzyt);
    }

    /**
     * Metoda usuwa teksturę rośliny na działce użytkownika.
     *
     * @param request obiekt BaseDzialkaRequest reprezentujący żądanie usunięcia tekstury rośliny na działce
     * @param connectedUser obiekt Uzytkownik reprezentujący zalogowanego użytkownika
     */
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

    /**
     * Metoda usuwa roślinę z działki użytkownika.
     *
     * @param request obiekt BaseDzialkaRequest reprezentujący żądanie usunięcia rośliny z działki
     * @param connectedUser obiekt Authentication reprezentujący zalogowanego użytkownika
     */
    public void deleteRoslinaFromDzialka(BaseDzialkaRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        Dzialka dzialka = getDzialkaByNumer(request.getNumerDzialki(), uzyt);
        
        Uzytkownik wlasciciel = dzialka.getOgrod().getUzytkownik();
        
        if(!uzyt.hasAuthenticationRights(wlasciciel, uzyt)) {
            throw new ForbiddenException("Nie masz uprawnień do usunięcia rośliny z działki użytkownika " + dzialka.getOgrod().getUzytkownik().getNazwa());
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
