package com.example.yukka.model.social.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.StringJoiner;

import org.springframework.stereotype.Service;

import com.example.yukka.model.social.powiadomienie.Powiadomienie;
import com.example.yukka.model.social.powiadomienie.PowiadomienieResponse;
import com.example.yukka.model.social.powiadomienie.TypPowiadomienia;
import com.example.yukka.model.uzytkownik.Uzytkownik;

@Service
public class PowiadomienieService {
    public String generateNotificationDescription(TypPowiadomienia typ, PowiadomienieResponse request) {
        String template = typ.getTemplate();
        template = template.replace("{tytul}", request.getTytul());
        template = template.replace("{odnosnik}", request.getOdnosnik());
        template = template.replace("{uzytkownikNazwa}", request.getUzytkownikNazwa());
        template = template.replace("{iloscPolubien}", String.valueOf(request.getIloscPolubien()));
        template = template.replace("{data}", request.getData().toString());

        StringJoiner joiner = new StringJoiner(", ");
        for (String roslina : request.getNazwyRoslin()) {
            joiner.add(roslina);
        }
        template = template.replace("{nazwyRoslin}", joiner.toString());
        
        template = template.replace("{avatar}", request.getAvatar());
        return template;
    }

    public Powiadomienie createPowiadomienie(TypPowiadomienia typ, PowiadomienieResponse request, Uzytkownik uzytkownik) {
        String opis = generateNotificationDescription(typ, request);
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
}
