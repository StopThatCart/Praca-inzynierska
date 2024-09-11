package com.example.yukka.model.social.service;

import java.util.HashSet;
import java.util.List;
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
import com.example.yukka.common.PageResponse;
import com.example.yukka.model.roslina.controller.RoslinaRepository;
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;
import com.example.yukka.model.social.powiadomienie.Powiadomienie;
import com.example.yukka.model.social.powiadomienie.PowiadomienieDTO;
import com.example.yukka.model.social.powiadomienie.TypPowiadomienia;
import com.example.yukka.model.social.repository.PowiadomienieRepository;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Transactional
public class PowiadomienieService {
    private final PowiadomienieRepository powiadomienieRepository;
    private final PowiadomienieMapper powiadomienieMapper;

    private final UzytkownikRepository uzytkownikRepository;

    @SuppressWarnings("unused")
    private final RoslinaRepository roslinaRepository;
    

    @Value("${powiadomienia.obraz.default.name}")
    private String powiadomienieAvatar;

    @Transactional(readOnly = true)
    public PageResponse<PowiadomienieDTO> findPowiadomieniaOfUzytkownik(int page, int size, Authentication connectedUser) {
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
        

        //Miesiac aktualnyMiesiac = Miesiac.values()[LocalDate.now().getMonthValue() - 1];
       // System.out.println("Aktualny miesiąc: " + aktualnyMiesiac.name());
        Miesiac aktualnyMiesiac = Miesiac.MAJ;  // Test

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

    public Powiadomienie addSpecjalnePowiadomienie(PowiadomienieDTO powiadomienieRequest) {
        Powiadomienie powiadomienie = createPowiadomienie(TypPowiadomienia.SPECJALNE, powiadomienieRequest);
        return powiadomienieRepository.addGlobalCustomPowiadomienie(powiadomienie);
    }

    public Powiadomienie addSpecjalnePowiadomienieToPracownicy(PowiadomienieDTO powiadomienieRequest) {
        Powiadomienie powiadomienie = createPowiadomienie(TypPowiadomienia.SPECJALNE, powiadomienieRequest);
        return powiadomienieRepository.addCustomPowiadomienieToPracownicy(powiadomienie);
    }

    public Powiadomienie addPowiadomienie(PowiadomienieDTO request, Uzytkownik uzytkownik) {
        Powiadomienie powiadomienie = createPowiadomienie(TypPowiadomienia.valueOf(request.getTyp()), request, uzytkownik);
      //  System.out.println("Powiadomienie: " + powiadomienie);
        return powiadomienieRepository.addPowiadomienieToUzytkownik(uzytkownik.getEmail(), powiadomienie);
    }

    public String generatePowiadomienieOpis(TypPowiadomienia typ, PowiadomienieDTO request) {
        String template = typ.getTemplate();
        template = template.replace("{tytul}", request.getTytul() != null ? request.getTytul() : "");
        template = template.replace("{odnosnik}", request.getOdnosnik() != null ? request.getOdnosnik() : "");
        template = template.replace("{uzytkownikNazwa}", request.getUzytkownikNazwa() != null ? request.getUzytkownikNazwa() : "");
        template = template.replace("{iloscPolubien}", request.getIloscPolubien() != 0 ? String.valueOf(request.getIloscPolubien()) : "");
        template = template.replace("{data}", request.getData() != null ? request.getData().toString() : "");

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

    public Powiadomienie createPowiadomienie(TypPowiadomienia typ, PowiadomienieDTO request, Uzytkownik uzytkownik) {
        String opis = generatePowiadomienieOpis(typ, request);
        return Powiadomienie.builder()
                .typ(typ.name())
                .odnosnik(request.getOdnosnik())
                .tytul(request.getTytul())
                .opis(opis)
                .avatar(request.getAvatar())
                .nazwyRoslin(request.getNazwyRoslin())
                .uzytkownik(uzytkownik)
                .uzytkownikNazwa(request.getUzytkownikNazwa())
                .data(request.getData())
                .build();
    }

    public Powiadomienie createPowiadomienie(TypPowiadomienia typ, PowiadomienieDTO request) {
        String opis = generatePowiadomienieOpis(typ, request);
        return Powiadomienie.builder()
                .typ(typ.name())
                .odnosnik(request.getOdnosnik())
                .tytul(request.getTytul())
                .opis(opis)
                .avatar(request.getAvatar())
                .nazwyRoslin(request.getNazwyRoslin())
                .uzytkownikNazwa(request.getUzytkownikNazwa())
                .data(request.getData())
                .build();
    }

    /* 
    public static void main(String[] args) {
        PowiadomienieService service = new PowiadomienieService();

        // Example usage
        PowiadomienieResponse request = PowiadomienieResponse.builder()
                .typ(TypPowiadomienia.KOMENTARZ_POST.toString())
                .tytul("Jan Kowalski")
                .odnosnik("jakisPostId")
                .nazwyRoslin(Arrays.asList("Pomidory", "Ogórki"))
                .avatar("powiadomienie_avatar.png")
                .uzytkownikNazwa("JanK")
                .iloscPolubien(100)
                .data(LocalDate.now())
                .build();
        
        request.setOdnosnik("jakiesPostId");

        String description = service.generateNotificationDescription(TypPowiadomienia.OWOCOWANIE_ROSLIN, request);
        request.setOpis(description);

        System.out.println(description);  // Output: Jan Kowalski odpowiedzial na twój komentarz pod postem Jak uprawiać pomidory
        System.out.println(request.getTyp());
    }

    */
}
