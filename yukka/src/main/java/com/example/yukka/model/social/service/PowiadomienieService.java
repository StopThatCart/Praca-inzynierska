package com.example.yukka.model.social.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yukka.YukkaApplication;
import com.example.yukka.auth.requests.BanRequest;
import com.example.yukka.common.PageResponse;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.model.roslina.controller.RoslinaRepository;
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.powiadomienie.Powiadamia;
import com.example.yukka.model.social.powiadomienie.Powiadomienie;
import com.example.yukka.model.social.powiadomienie.PowiadomienieDTO;
import com.example.yukka.model.social.powiadomienie.PowiadomienieResponse;
import com.example.yukka.model.social.powiadomienie.TypPowiadomienia;
import com.example.yukka.model.social.repository.PowiadomienieRepository;
import com.example.yukka.model.social.request.ZgloszenieRequest;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;
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

    @Transactional(readOnly = true)
    public Integer getNieprzeczytaneCountOfUzytkownik(Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Integer powiadomienia = powiadomienieRepository.getNieprzeczytaneCountOfUzytkownik(uzyt.getEmail());

        return powiadomienia;
    }

    @Transactional(readOnly = true)
    public PageResponse<PowiadomienieResponse> findPowiadomieniaOfUzytkownik(int page, int size, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Pageable pageable = PageRequest.of(page, size, Sort.by("powiadomienie.dataUtworzenia").descending());
        Page<Powiadomienie> powiadomienia = powiadomienieRepository.findPowiadomieniaOfUzytkownik(uzyt.getEmail(), pageable);

        return powiadomienieMapper.PowiadomieniePageToPagePowiadomienieResponse(powiadomienia);
    }

    
    @Scheduled(cron = "0 0 0 1 * ?")  // Na początku każdego miesiąca
    //@Scheduled(fixedDelay=2000L)  // Test co 2 sekundy
    public void checkOkresyPowiadomienia() {
        if (!YukkaApplication.isApplicationReady()) {
            System.out.println("Aplikacja nie jest gotowa do sprawdzania okresów powiadomień");
            return; 
        }
        System.out.println("Sprawdzanie okresów powiadomień");
        List<Uzytkownik> uzytkownicy = uzytkownikRepository.getUzytkownicyWithRoslinyInDzialki();
        if(uzytkownicy.isEmpty()) return;
        

        Miesiac aktualnyMiesiac = Miesiac.values()[LocalDate.now().getMonthValue() - 1];
        System.out.println("Aktualny miesiąc: " + aktualnyMiesiac.name());
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
            utworzOkresPowiadomienie(nazwyRoslinOwocowanie, TypPowiadomienia.OWOCOWANIE_ROSLIN_TERAZ, uzytkownik);
            utworzOkresPowiadomienie(nazwyRoslinKwitnienie, TypPowiadomienia.KWITNIENIE_ROSLIN_TERAZ, uzytkownik);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")  // Codziennie
    public void checkUnban() {
        if (!YukkaApplication.isApplicationReady()) {
            System.out.println("Aplikacja nie jest gotowa do sprawdzania okresów powiadomień");
            return; 
        }
        System.out.println("Sprawdzanie zbanowanych użytkowników");
        List<Uzytkownik> uzytkownicy = uzytkownikRepository.getZbanowaniUzytkownicy();
        if(uzytkownicy.isEmpty()) return;
        
        for (Uzytkownik uzytkownik : uzytkownicy) {
            if(uzytkownik.getBanDo().isBefore(LocalDate.now())) {
                uzytkownikRepository.banUzytkownik(uzytkownik.getNazwa(), false, null);
                sendPowiadomienieOfBan(uzytkownik, null);
            }
        }
    }

    public void addSpecjalnePowiadomienie(PowiadomienieDTO powiadomienieRequest) {
        Powiadomienie powiadomienie = createPowiadomienie(TypPowiadomienia.SPECJALNE, powiadomienieRequest, null);
        powiadomienieRepository.addGlobalCustomPowiadomienie(powiadomienie);

    }

    public void addSpecjalnePowiadomienieToPracownicy(PowiadomienieDTO powiadomienieRequest) {
        Powiadomienie powiadomienie = createPowiadomienie(TypPowiadomienia.SPECJALNE, powiadomienieRequest, null);
        powiadomienieRepository.addCustomPowiadomienieToPracownicy(powiadomienie);
    }

    public void sendZgloszenie(ZgloszenieRequest request, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        if (checkIfUzytkownikSentZgloszenieBeforeCooldown(connectedUser)) {
            throw new IllegalArgumentException("Możesz wysłać zgłoszenie raz na 15 minut");
        }

        sendZgloszenie(request, uzyt);
    }

    public void sendZgloszenie(ZgloszenieRequest request, Uzytkownik uzyt) {
        log.info("Zgłoszenie użytkownika: " + request.getZglaszany() + " przez użytkownika: " + uzyt.getNazwa());
        Uzytkownik zglaszany = uzytkownikRepository.findByNameOrEmail(request.getZglaszany())
            .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono użytkownika " + request.getZglaszany()));

        if (zglaszany.getUzytId().equals(uzyt.getUzytId())) {
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

    // TODO: Jakoś to połączyć jak mnie już głowa przestanie boleć
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

    public void sendPowiadomienieOfBan(Uzytkownik odbiorca, BanRequest request) {
        PowiadomienieDTO powiadomienie = PowiadomienieDTO.builder()
            .typ(TypPowiadomienia.BAN.name())
            .okres(request.getBanDo())
            .tytul(request.getPowod())
            .build();
        addPowiadomienie(powiadomienie, odbiorca);
    }

    public void sendPowiadomienieOfUnban(Uzytkownik odbiorca) {
        PowiadomienieDTO powiadomienie = PowiadomienieDTO.builder()
            .typ(TypPowiadomienia.ODBANOWANIE.name())
            .build();
        addPowiadomienie(powiadomienie, odbiorca);
    }


    public void sendPowiadomienieOfKomentarz(Uzytkownik nadawca, Uzytkownik odbiorca, Komentarz komentarz) {
        if(komentarz.getUzytkownik().getUzytId().equals(nadawca.getUzytId())) {
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

        PowiadomienieDTO powiadomienie = PowiadomienieDTO.builder()
            .typ(TypPowiadomienia.KOMENTARZ_POST.name())
            .tytul(post != null ? post.getTytul() : "")
            .odnosnik(post != null ? post.getPostId() : "")
            .uzytkownikNazwa(nadawca.getNazwa()).avatar(nadawca.getAvatar())
            .avatar(nadawca.getAvatar())
            .build();
            
        addPowiadomienie(powiadomienie, komentarz.getUzytkownik());
    }

    // TODO: Dodać przycisk ustawiający wszystkie powiadomienia jako przeczytane oraz przycisk zgłaszania użytkownika + modal do tego
    
    public PowiadomienieResponse setPrzeczytane(Long id, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        Powiadomienie powiadomienie = powiadomienieRepository.setPrzeczytane(uzyt.getEmail(), id).orElse(null);;

        return powiadomienieMapper.toPowiadomienieResponse(powiadomienie);
    }

    public void setAllPrzeczytane(Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        powiadomienieRepository.setAllPrzeczytane(uzyt.getEmail());
    }

    
    public void remove(Long id, Authentication connectedUser) {
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();
        Optional<Powiadomienie> pow = powiadomienieRepository.findById(id);
        if(pow.isPresent() && pow.get().getTyp().equals(TypPowiadomienia.ZGLOSZENIE.name())) {
            throw new IllegalArgumentException("Nie można usunąć zgłoszenia");
        }
        powiadomienieRepository.remove(uzyt.getEmail(), id);
    }

    @Transactional(readOnly = true)
    private Boolean checkIfUzytkownikSentZgloszenieBeforeCooldown(Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());

        Optional<Powiadomienie> pow = powiadomienieRepository.getNajnowszeZgloszenieUzytkownika(uzyt.getNazwa());
        if(pow.isEmpty()) {
            return false;
        }
        System.out.println("Data utworzenia zgłoszenia: " + pow.get().getDataUtworzenia());
        if (pow.get().getDataUtworzenia().plusMinutes(15).isBefore(LocalDateTime.now())) {
            return false;
        }


        return true;
    }

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


    private void przetworzOkresy(Set<Wlasciwosc> okresy, Miesiac aktualnyMiesiac, Set<String> nazwyRoslin, String nazwaRosliny) {
        okresy.stream()
              .filter(okres -> okres.getNazwa().equalsIgnoreCase(aktualnyMiesiac.name()))
              .forEach(okres -> nazwyRoslin.add(nazwaRosliny));
    }

    private void utworzOkresPowiadomienie(Set<String> nazwyRoslin, TypPowiadomienia typ, Uzytkownik uzytkownik) {
        if (!nazwyRoslin.isEmpty()) {
            PowiadomienieDTO powiadomienieRequest = PowiadomienieDTO.builder()
                    .typ(typ.toString())
                    .odnosnik(uzytkownik.getUzytId())
                    .nazwyRoslin(nazwyRoslin)
                    .build();
            addPowiadomienie(powiadomienieRequest, uzytkownik);
        }
    }
}
