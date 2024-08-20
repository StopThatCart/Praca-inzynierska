package com.example.yukka.model.social.service;

import java.util.StringJoiner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.powiadomienie.Powiadomienie;
import com.example.yukka.model.social.powiadomienie.PowiadomienieResponse;
import com.example.yukka.model.social.powiadomienie.TypPowiadomienia;
import com.example.yukka.model.social.repository.PowiadomienieRepository;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PowiadomienieService {
    private final PowiadomienieRepository powiadomienieRepository;
    private final PowiadomienieMapper powiadomienieMapper;

    public PageResponse<PowiadomienieResponse> findPowiadomieniaOfUzytkownik(int page, int size, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
       // Optional<Uzytkownik> targetUzyt = uzytkownikRepository.findByEmail(email);
       // if (targetUzyt.isEmpty() || !uzyt.hasAuthenticationRights(targetUzyt.get(), connectedUser)) {
      //      return new PageResponse<>();
      //  }
       // System.out.println("\n\n\n EMAIL: " + email + "\n\n\n");
        Pageable pageable = PageRequest.of(page, size, Sort.by("powiadomienie.dataUtworzenia").descending());
      //  System.out.println("KOMENTARZE:  AAA ");

        Page<Powiadomienie> powiadomienia = powiadomienieRepository.findPowiadomieniaOfUzytkownik(uzyt.getEmail(), pageable);

        return powiadomienieMapper.PowiadomieniePageToPagePowiadomienieResponse(powiadomienia);
    }

    public Powiadomienie addPowiadomienie(PowiadomienieResponse request, Uzytkownik uzytkownik) {
        Powiadomienie powiadomienie = createPowiadomienie(TypPowiadomienia.valueOf(request.getTyp()), request, uzytkownik);
      //  System.out.println("Powiadomienie: " + powiadomienie);
        return powiadomienieRepository.addPowiadomienieToUzytkownik(uzytkownik.getEmail(), powiadomienie);
    }

    public String generatePowiadomienieOpis(TypPowiadomienia typ, PowiadomienieResponse request) {
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

    public Powiadomienie createPowiadomienie(TypPowiadomienia typ, PowiadomienieResponse request, Uzytkownik uzytkownik) {
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
