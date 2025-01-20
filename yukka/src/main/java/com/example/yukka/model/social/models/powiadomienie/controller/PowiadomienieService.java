package com.example.yukka.model.social.models.powiadomienie.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yukka.auth.requests.BanRequest;
import com.example.yukka.common.PageResponse;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.model.roslina.cecha.Cecha;
import com.example.yukka.model.roslina.controller.RoslinaRepository;
import com.example.yukka.model.social.mappers.PowiadomienieMapper;
import com.example.yukka.model.social.models.komentarz.Komentarz;
import com.example.yukka.model.social.models.post.Post;
import com.example.yukka.model.social.models.powiadomienie.Miesiac;
import com.example.yukka.model.social.models.powiadomienie.Powiadamia;
import com.example.yukka.model.social.models.powiadomienie.Powiadomienie;
import com.example.yukka.model.social.models.powiadomienie.PowiadomienieDTO;
import com.example.yukka.model.social.models.powiadomienie.PowiadomienieResponse;
import com.example.yukka.model.social.models.powiadomienie.TypPowiadomienia;
import com.example.yukka.model.social.models.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.social.requests.SpecjalnePowiadomienieRequest;
import com.example.yukka.model.social.requests.ZgloszenieRequest;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Transactional
@Slf4j
public class PowiadomienieService {
    private final PowiadomienieRepository powiadomienieRepository;
    private final PowiadomienieMapper powiadomienieMapper;

    private final UzytkownikRepository uzytkownikRepository;

    @SuppressWarnings("unused")
    private final RoslinaRepository roslinaRepository;
    

    @Value("${powiadomienia.obraz.default.name}")
    private String powiadomienieAvatar;

    /**
     * Metoda zwraca liczbę nieprzeczytanych powiadomień dla zalogowanego użytkownika.
     *
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return liczba nieprzeczytanych powiadomień dla użytkownika
     */
    @Transactional(readOnly = true)
    public Integer getNieprzeczytaneCountOfUzytkownik(Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Integer powiadomienia = powiadomienieRepository.getNieprzeczytaneCountOfUzytkownik(uzyt.getEmail());

        return powiadomienia;
    }

    /**
     * Metoda zwraca listę powiadomień użytkownika z paginacją.
     *
     * @param page numer strony
     * @param size rozmiar strony
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return lista powiadomień użytkownika z paginacją
     */
    @Transactional(readOnly = true)
    public PageResponse<PowiadomienieResponse> findPowiadomieniaOfUzytkownik(int page, int size, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Pageable pageable = PageRequest.of(page, size, Sort.by("powiadomienie.dataUtworzenia").descending());
        Page<Powiadomienie> powiadomienia = powiadomienieRepository.findPowiadomieniaOfUzytkownik(uzyt.getEmail(), pageable);

        return powiadomienieMapper.PowiadomieniePageToPagePowiadomienieResponse(powiadomienia);
    }


    /**
     * <p>Metoda sprawdzająca okresy powiadomień dla użytkowników.</p>
     * <p>Sprawdza okresy owocowania i kwitnienia roślin w działkach użytkowników i tworzy powiadomienia o tych okresach.</p>
    */
    @Scheduled(cron = "0 0 0 1 * ?")  // Na początku każdego miesiąca
    //@Scheduled(fixedDelay=2000L)  // Test co 2 sekundy
    public void checkOkresyPowiadomienia() {
        log.info("Sprawdzanie okresów powiadomień");
        if (LocalDate.now().getDayOfMonth() != 1) {
            log.info("Dzisiaj nie jest pierwszy dzień miesiąca, pomijanie sprawdzania okresów powiadomień");
            return;
        }
        List<Uzytkownik> uzytkownicy = uzytkownikRepository.getUzytkownicyWithRoslinyInDzialki();
        if(uzytkownicy.isEmpty()) return;
        

        Miesiac aktualnyMiesiac = Miesiac.values()[LocalDate.now().getMonthValue() - 1];
        log.info("Aktualny miesiąc: " + aktualnyMiesiac.name());
       // Miesiac aktualnyMiesiac = Miesiac.MAJ;  // Test

        for (Uzytkownik uzytkownik : uzytkownicy) {
            Set<String> nazwyRoslinOwocowanie = new HashSet<>();
            Set<String> nazwyRoslinKwitnienie = new HashSet<>();

            uzytkownik.getOgrod().getDzialki().forEach(dzialka -> {
                dzialka.getZasadzoneRosliny().forEach(zasadzona -> {
                    przetworzOkresy(zasadzona.getRoslina().getOkresyOwocowania(), aktualnyMiesiac, nazwyRoslinOwocowanie, zasadzona.getRoslina().getNazwa());
                    przetworzOkresy(zasadzona.getRoslina().getOkresyKwitnienia(), aktualnyMiesiac, nazwyRoslinKwitnienie, zasadzona.getRoslina().getNazwa());     
                });
            });
            if (uzytkownik.getUstawienia().isPowiadomieniaOgrodKwitnienie()) {
                utworzOkresPowiadomienie(nazwyRoslinKwitnienie, TypPowiadomienia.KWITNIENIE_ROSLIN_TERAZ, uzytkownik);
            }
            if (uzytkownik.getUstawienia().isPowiadomieniaOgrodOwocowanie()) {
                utworzOkresPowiadomienie(nazwyRoslinOwocowanie, TypPowiadomienia.OWOCOWANIE_ROSLIN_TERAZ, uzytkownik);
            }
        }
    }

    /**
     * <p>Metoda sprawdzająca okresy powiadomień dla użytkowników.</p>
     * <p>Sprawdza okresy owocowania i kwitnienia roślin w działkach użytkowników i tworzy powiadomienia o tych okresach.</p>
    */
   // @PostConstruct
    @EventListener(ApplicationReadyEvent.class)
    public void checkOkresyPowiadomieniaOnStartup() {
        checkOkresyPowiadomienia();
    }

    /**
     * Metoda sprawdzająca codziennie, którzy użytkownicy powinni zostać odbanowani.
     */
    @Scheduled(cron = "0 0 0 * * ?")  // Codziennie
    public void checkUnban() {
        log.info("Sprawdzanie zbanowanych użytkowników");
        List<Uzytkownik> uzytkownicy = uzytkownikRepository.getZbanowaniUzytkownicy();
        if(uzytkownicy.isEmpty()) return;
        
        for (Uzytkownik uzytkownik : uzytkownicy) {
            if(uzytkownik.getBanDo().isBefore(LocalDate.now())) {
                log.info("Odbanowywanie użytkownika: " + uzytkownik.getNazwa());
                uzytkownikRepository.banUzytkownik(uzytkownik.getNazwa(), false, null, null);
            }
        }
    }

    /**
     * Metoda sprawdzająca codziennie, którzy użytkownicy powinni zostać odbanowani.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void checkUnbanOnStartup() {
        checkUnban();
    }


    /**
     * Dodaje specjalne powiadomienie na podstawie przekazanego obiektu PowiadomienieDTO.
     *
     * @param request obiekt zawierający dane powiadomienia do dodania
     */
    public void addSpecjalnePowiadomienie(SpecjalnePowiadomienieRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        addSpecjalnePowiadomienie(request, uzyt);
    }

    /**
     * Dodaje specjalne powiadomienie na podstawie przekazanego obiektu PowiadomienieDTO.
     *
     * @param request obiekt zawierający dane powiadomienia do dodania
     */
    public void addSpecjalnePowiadomienie(SpecjalnePowiadomienieRequest request, Uzytkownik uzyt) {
        log.info("Użytkownik: " + uzyt.getNazwa() + " dodaje specjalne powiadomienie: " + request.getOpis());

        PowiadomienieDTO powiadomienieRequest = PowiadomienieDTO.builder()
            .typ(TypPowiadomienia.SPECJALNE.name())
            .tytul(request.getOpis())
            .build();
        Powiadomienie powiadomienie = createPowiadomienie(TypPowiadomienia.SPECJALNE, powiadomienieRequest, null);
        powiadomienieRepository.addGlobalCustomPowiadomienie(powiadomienie, uzyt.getNazwa());
    }

    /**
     * Dodaje specjalne powiadomienie dla pracowników na podstawie przekazanego obiektu PowiadomienieDTO.
     *
     * @param request obiekt zawierający dane powiadomienia do dodania
     */
    public void addSpecjalnePowiadomienieToPracownicy(SpecjalnePowiadomienieRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        log.info("Użytkownik: " + uzyt.getNazwa() + " dodaje specjalne powiadomienie dla pracowników: " + request.getOpis());

        PowiadomienieDTO powiadomienieRequest = PowiadomienieDTO.builder()
        .typ(TypPowiadomienia.SPECJALNE.name())
        .tytul(request.getOpis())
        .build();
        Powiadomienie powiadomienie = createPowiadomienie(TypPowiadomienia.SPECJALNE, powiadomienieRequest, null);
        powiadomienieRepository.addCustomPowiadomienieToPracownicy(powiadomienie, uzyt.getNazwa());
    }

    /**
     * Wysyła zgłoszenie do pracownika na podstawie przekazanego obiektu ZgloszenieRequest.
     *
     * @param request obiekt zawierający dane zgłoszenia
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     */
    public void sendZgloszenie(ZgloszenieRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        if (checkIfUzytkownikSentZgloszenieBeforeCooldown(connectedUser)) {
            throw new IllegalArgumentException("Możesz wysłać zgłoszenie raz na 15 minut");
        }
        

        sendZgloszenie(request, uzyt);
    }

    /**
     * Wysyła zgłoszenie do pracownika na podstawie przekazanego obiektu ZgloszenieRequest.
     *
     * @param request obiekt zawierający dane zgłoszenia
     * @param uzyt obiekt Uzytkownik reprezentujący aktualnie zalogowanego użytkownika
     */
    public void sendZgloszenie(ZgloszenieRequest request, Uzytkownik uzyt) {
        log.info("Zgłoszenie użytkownika: " + request.getZglaszany() + " przez użytkownika: " + uzyt.getNazwa());
        Uzytkownik zglaszany = uzytkownikRepository.findByNameOrEmail(request.getZglaszany())
            .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono użytkownika " + request.getZglaszany()));

        if(!zglaszany.isAktywowany()) {
            throw new IllegalArgumentException("Konto użytkownika nie jest aktywne");
        }

        if (zglaszany.getUuid().equals(uzyt.getUuid())) {
            throw new IllegalArgumentException("Nie można zgłosić samego siebie");
        }
        

        TypPowiadomienia typPowiadomienia = TypPowiadomienia.valueOf(request.getTypPowiadomienia());
        if (typPowiadomienia.equals(TypPowiadomienia.ZGLOSZENIE)) {
            request.setOdnosnik(zglaszany.getNazwa());
        }

        PowiadomienieDTO powiadomienieRequest = PowiadomienieDTO.builder()
            .typ(typPowiadomienia.name())
            .tytul(request.getOpis())
            .uzytkownikNazwa(uzyt.getNazwa())
            .avatar(uzyt.getAvatar())
            .zglaszany(zglaszany.getNazwa())
            .odnosnik(request.getOdnosnik())
            .build();

        Powiadomienie powiadomienie = createPowiadomienie(typPowiadomienia, powiadomienieRequest, null);
        powiadomienieRepository.sendZgloszenieToPracownik(powiadomienie, uzyt.getNazwa());

    }

    /**
     * Wysyła zgłoszenie do pracownika na podstawie przekazanego obiektu ZgloszenieRequest.
     *
     * @param request obiekt zawierający dane zgłoszenia
     * @param uzyt obiekt Uzytkownik reprezentujący aktualnie zalogowanego użytkownika
     */
    public Powiadomienie addPowiadomienie(PowiadomienieDTO request, Uzytkownik uzytkownik) {
        Powiadomienie powiadomienie = createPowiadomienie(TypPowiadomienia.valueOf(request.getTyp()), request, uzytkownik);

        Optional<Powiadomienie> powOpt = powiadomienieRepository
            .checkIfSamePowiadomienieExists(uzytkownik.getEmail(), powiadomienie.getTyp(), powiadomienie.getOpis());
            
        if(powOpt.isPresent()) {
            return powiadomienieRepository.updateData(uzytkownik.getEmail(), powOpt.get().getId(), 
            powiadomienie.getAvatar(),LocalDateTime.now()).get();
        } else{
            return powiadomienieRepository.addPowiadomienieToUzytkownik(uzytkownik.getEmail(), powiadomienie);
        }
    }

    /**
     * Wysyła zgłoszenie do pracownika na podstawie przekazanego obiektu ZgloszenieRequest.
     *
     * @param request obiekt zawierający dane zgłoszenia
     * @param uzyt obiekt Uzytkownik reprezentujący aktualnie zalogowanego użytkownika
     */
    public Powiadomienie createPowiadomienie(TypPowiadomienia typ, PowiadomienieDTO request, Uzytkownik uzytkownik) {
        String opis = generatePowiadomienieOpis(typ, request);

        Powiadamia powiadamia = Powiadamia.builder()
                .przeczytane(false)
                .oceniany(uzytkownik != null ? uzytkownik : null)
                .build();

        return Powiadomienie.builder()
                .typ(typ.name())
                .odnosnik(request.getOdnosnik())
                .tytul(request.getTytul())
                .opis(opis)
                .avatar(request.getAvatar())
                .nazwyRoslin(request.getNazwyRoslin())
                .powiadamia(powiadamia)
                .uzytkownikNazwa(request.getUzytkownikNazwa())
                .zglaszany(request.getZglaszany())
                .data(request.getData())
                .build();
    }

    
    /**
     * Wysyła powiadomienie o nowej rozmowie prywatnej.
     *
     * @param nadawca   Użytkownik, który wysyła powiadomienie.
     * @param odbiorca  Użytkownik, który otrzymuje powiadomienie.
     * @param rozmowa   Rozmowa prywatna, której dotyczy powiadomienie.
     */
    public void sendPowiadomienieOfRozmowa(Uzytkownik nadawca, Uzytkownik odbiorca, RozmowaPrywatna rozmowa) {
        PowiadomienieDTO powiadomienie = PowiadomienieDTO.builder()
            .typ(TypPowiadomienia.WIADOMOSC_PRYWATNA.name())
            .odnosnik(rozmowa.getNadawca())
            .uzytkownikNazwa(nadawca.getNazwa())
            .avatar(nadawca.getAvatar())
            .odnosnik(nadawca.getNazwa())
            .build();
        addPowiadomienie(powiadomienie, odbiorca);
    }

    /**
     * Wysyła powiadomienie o nałożeniu bana na użytkownika.
     *
     * @param odbiorca Użytkownik, który otrzymuje powiadomienie.
     * @param request Obiekt zawierający szczegóły bana, takie jak okres i powód.
     */
    public void sendPowiadomienieOfBan(Uzytkownik odbiorca, BanRequest request) {
        PowiadomienieDTO powiadomienie = PowiadomienieDTO.builder()
            .typ(TypPowiadomienia.BAN.name())
            .okres(request.getBanDo())
            .tytul(request.getPowod())
            .build();
        addPowiadomienie(powiadomienie, odbiorca);
    }

    /**
     * Wysyła powiadomienie o zdjęciu bana z użytkownika.
     *
     * @param odbiorca Użytkownik, który otrzymuje powiadomienie.
     */
    public void sendPowiadomienieOfUnban(Uzytkownik odbiorca) {
        PowiadomienieDTO powiadomienie = PowiadomienieDTO.builder()
            .typ(TypPowiadomienia.ODBANOWANIE.name())
            .build();
        addPowiadomienie(powiadomienie, odbiorca);
    }


    /**
     * Wysyła powiadomienie o nowym komentarzu.
     *
     * @param nadawca   Użytkownik, który wysyła powiadomienie.
     * @param komentarz Komentarz, który dotyczy powiadomienia.
     */
    public void sendPowiadomienieOfKomentarz(Uzytkownik nadawca, Komentarz komentarz) {
        if(komentarz.getUzytkownik().getUuid().equals(nadawca.getUuid())) {
            return;
        }

        Post post =  komentarz.getPost();
        if(post == null && komentarz.getWPoscie() != null) {
            post = komentarz.getWPoscie();
            if(post == null) {
                System.out.println("\n\n\nPost jest nullem\n\n\n");
                return;
            }
        }

        if (komentarz.getUzytkownik() == null) {
            throw new EntityNotFoundException("Nie znaleziono odbiorcy komentarza");
        }
        Uzytkownik odbiorca = uzytkownikRepository.findByUUID(komentarz.getUzytkownik().getUuid())
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono odbiorcy komentarza"));
        
        if (!odbiorca.getUstawienia().isPowiadomieniaKomentarzeOdpowiedz() || odbiorca.getUuid().equals(nadawca.getUuid())) {
            return;
        }

        PowiadomienieDTO powiadomienie = PowiadomienieDTO.builder()
            .typ(TypPowiadomienia.KOMENTARZ_POST.name())
            .tytul(post != null ? post.getTytul() : "")
            .odnosnik(post != null ? post.getUuid() : "")
            .uzytkownikNazwa(nadawca.getNazwa()).avatar(nadawca.getAvatar())
            .avatar(nadawca.getAvatar())
            .build();
            
        addPowiadomienie(powiadomienie, komentarz.getUzytkownik());
    }


    /**
     * Ustawia powiadomienie jako przeczytane dla zalogowanego użytkownika.
     *
     * @param id ID powiadomienia, które ma zostać oznaczone jako przeczytane.
     * @param connectedUser Obiekt Authentication reprezentujący zalogowanego użytkownika.
     * @return Obiekt PowiadomienieResponse zawierający zaktualizowane dane powiadomienia.
     */
    public PowiadomienieResponse setPrzeczytane(Long id, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        Powiadomienie pow = powiadomienieRepository.findPowiadomienieById(id)
        .orElse(null);
        if(pow == null) {
            return null;
        }

        Powiadomienie powiadomienie = powiadomienieRepository.setPrzeczytane(uzyt.getEmail(), id).orElse(null);

        return powiadomienieMapper.toPowiadomienieResponse(powiadomienie);
    }

    /**
     * Ustawia wszystkie powiadomienia użytkownika jako przeczytane.
     *
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     */
    public void setAllPrzeczytane(Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        log.info("Użytkownik: " + uzyt.getNazwa() + " oznacza wszystkie swoje powiadomienia jako przeczytane");

        powiadomienieRepository.setAllPrzeczytane(uzyt.getEmail());
    }


     /**
     * Ukrywa powiadomienie o podanym identyfikatorze.
     *
     * @param id            identyfikator powiadomienia do ukrycia
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     */
    public void ukryjPowiadomienie(Long id, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        log.info("Użytkownik: " + uzyt.getNazwa() + " ukrywa powiadomienie o id: " + id);

        Optional<Powiadomienie> pow = powiadomienieRepository.findPowiadomienieById(id);
        if(pow.isPresent() ) {
            powiadomienieRepository.ukryjPowiadomienie(uzyt.getEmail(), id);
        }

    }

    
    /**
     * Usuwa powiadomienie o podanym identyfikatorze.
     *
     * @param id            identyfikator powiadomienia do usunięcia
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     */
    public void removePowiadomienie(Long id, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        log.info("Użytkownik: " + uzyt.getNazwa() + " usuwa powiadomienie o id: " + id);
        
        Optional<Powiadomienie> pow = powiadomienieRepository.findPowiadomienieById(id);
        if(pow.isPresent() ) {
            if(pow.get().getTyp().equals(TypPowiadomienia.ZGLOSZENIE.name())) {
                throw new IllegalArgumentException("Nie można usunąć zgłoszenia");
            } else if (pow.get().getTyp().equals(TypPowiadomienia.SPECJALNE.name())) {
                throw new IllegalArgumentException("Nie można usunąć specjalnego powiadomienia");
            }
            powiadomienieRepository.removePowiadomienie(uzyt.getEmail(), id);
        }
    }

    /**
     * Sprawdza, czy użytkownik wysłał zgłoszenie w ciągu ostatnich 15 minut.
     *
     * @param connectedUser obiekt Authentication reprezentujący aktualnie zalogowanego użytkownika
     * @return true, jeśli użytkownik wysłał zgłoszenie w ciągu ostatnich 15 minut, w przeciwnym razie false
     */
    @Transactional(readOnly = true)
    private Boolean checkIfUzytkownikSentZgloszenieBeforeCooldown(Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Optional<Powiadomienie> pow = powiadomienieRepository.getNajnowszeZgloszenieUzytkownika(uzyt.getNazwa());
        if(pow.isEmpty()) {
            return false;
        }
        System.out.println("Data utworzenia zgłoszenia: " + pow.get().getDataUtworzenia());
        return !pow.get().getDataUtworzenia().plusMinutes(15).isBefore(LocalDateTime.now());
    }

    /**
     * Generuje opis powiadomienia na podstawie przekazanego obiektu PowiadomienieDTO.
     *
     * @param typ     typ powiadomienia
     * @param request obiekt zawierający dane powiadomienia
     * @return opis powiadomienia
     */
    private String generatePowiadomienieOpis(TypPowiadomienia typ, PowiadomienieDTO request) {
        String template = typ.getTemplate();
        template = template.replace("{tytul}", request.getTytul() != null ? request.getTytul() : "");
        template = template.replace("{odnosnik}", request.getOdnosnik() != null ? request.getOdnosnik() : "");
        template = template.replace("{uzytkownikNazwa}", request.getUzytkownikNazwa() != null ? request.getUzytkownikNazwa() : "");
        template = template.replace("{iloscPolubien}", request.getIloscPolubien() != 0 ? String.valueOf(request.getIloscPolubien()) : "");
        template = template.replace("{data}", request.getData() != null ? request.getData().toString() : "");
        template = template.replace("{okres}", request.getOkres() != null ? request.getOkres().toString() : "");
        template = template.replace("{zglaszany}", request.getZglaszany() != null ? request.getZglaszany() : "");

        StringJoiner joiner = new StringJoiner(", ");
        if (request.getNazwyRoslin() != null) {
            for (String roslina : request.getNazwyRoslin()) {
                joiner.add(roslina);
            }
        }
        template = template.replace("{nazwyRoslin}", joiner.toString());

        template = template.replace("{avatar}", request.getAvatar() != null ? request.getAvatar() : "");
        return template;
    }


    /**
     * Przetwarza zestaw okresów, filtrując je na podstawie nazwy aktualnego miesiąca,
     * a następnie dodaje nazwę rośliny do zestawu nazw roślin, jeśli okres pasuje do aktualnego miesiąca.
     *
     * @param okresy        Zestaw obiektów typu Cecha reprezentujących okresy.
     * @param aktualnyMiesiac Aktualny miesiąc, który jest używany do filtrowania okresów.
     * @param nazwyRoslin   Zestaw nazw roślin, do którego dodawana jest nazwa rośliny, jeśli okres pasuje do aktualnego miesiąca.
     * @param nazwaRosliny  Nazwa rośliny, która jest dodawana do zestawu nazw roślin, jeśli okres pasuje do aktualnego miesiąca.
     */
    private void przetworzOkresy(Set<Cecha> okresy, Miesiac aktualnyMiesiac, Set<String> nazwyRoslin, String nazwaRosliny) {
        okresy.stream()
              .filter(okres -> okres.getNazwa().equalsIgnoreCase(aktualnyMiesiac.name()))
              .forEach(okres -> nazwyRoslin.add(nazwaRosliny));
    }

    /**
     * Tworzy powiadomienie okresowe dla podanych nazw roślin, typu powiadomienia i użytkownika.
     *
     * @param nazwyRoslin Zbiór nazw roślin, dla których ma zostać utworzone powiadomienie.
     * @param typ Typ powiadomienia, który ma zostać utworzony.
     * @param uzytkownik Użytkownik, dla którego ma zostać utworzone powiadomienie.
     */
    private void utworzOkresPowiadomienie(Set<String> nazwyRoslin, TypPowiadomienia typ, Uzytkownik uzytkownik) {
        if (!nazwyRoslin.isEmpty()) {
            PowiadomienieDTO powiadomienieRequest = PowiadomienieDTO.builder()
                    .typ(typ.toString())
                    .odnosnik(uzytkownik.getUuid())
                    .nazwyRoslin(nazwyRoslin)
                    .build();
            addPowiadomienie(powiadomienieRequest, uzytkownik);
        }
    }
}
