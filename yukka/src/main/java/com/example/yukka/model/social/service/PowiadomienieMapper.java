package com.example.yukka.model.social.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileUtils;
import com.example.yukka.model.social.powiadomienie.Powiadomienie;
import com.example.yukka.model.social.powiadomienie.PowiadomienieDTO;
import com.example.yukka.model.social.powiadomienie.PowiadomienieResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PowiadomienieMapper {
    private final FileUtils fileUtils;


    public PowiadomienieResponse toPowiadomienieResponse(Powiadomienie powiadomienie) {
        if (powiadomienie == null) {
            return null;
        }
        Boolean przeczytane = null;
        if (powiadomienie.getPowiadamia() != null) {
            przeczytane = powiadomienie.getPowiadamia().getPrzeczytane();
        }
        
        return PowiadomienieResponse.builder()
            .id(powiadomienie.getId())
            .typ(powiadomienie.getTyp())
            .isZgloszenie(powiadomienie.isZgloszenie())
            .tytul(powiadomienie.getTytul())
            .przeczytane(przeczytane)
            .odnosnik(powiadomienie.getOdnosnik())
            .opis(powiadomienie.getOpis())
            .nazwyRoslin(powiadomienie.getNazwyRoslin())
            .avatar(fileUtils.readFile(powiadomienie.getAvatar()))
            .uzytkownikNazwa(powiadomienie.getUzytkownikNazwa())
            .iloscPolubien(powiadomienie.getIloscPolubien())
            .data(powiadomienie.getData())
            .dataUtworzenia(timeAgo(powiadomienie.getDataUtworzenia()))
            .build();
    }


    public PowiadomienieDTO toPowiadomienieDTO(Powiadomienie powiadomienie) {
        if (powiadomienie == null) {
            return null;
        }
        return PowiadomienieDTO.builder()
            .id(powiadomienie.getId())
            .isZgloszenie(powiadomienie.isZgloszenie())
            .typ(powiadomienie.getTyp())
            .tytul(powiadomienie.getTytul())
            .przeczytane(powiadomienie.getPowiadamia() != null ? powiadomienie.getPowiadamia().getPrzeczytane() : null)
            .odnosnik(powiadomienie.getOdnosnik())
            .opis(powiadomienie.getOpis())
            .nazwyRoslin(powiadomienie.getNazwyRoslin())
            .avatar(powiadomienie.getAvatar())
            .uzytkownikNazwa(powiadomienie.getUzytkownikNazwa())
            .iloscPolubien(powiadomienie.getIloscPolubien())
            .data(powiadomienie.getData())
            .dataUtworzenia(powiadomienie.getDataUtworzenia())
            .okres(powiadomienie.getOkres())
            .build();
    }

    public PageResponse<PowiadomienieResponse> PowiadomieniePageToPagePowiadomienieResponse(Page<Powiadomienie> powiadomienia) {
            List<PowiadomienieResponse> powiadomieniaResponse = powiadomienia.getContent().stream()
            .map(this::toPowiadomienieResponse)
            .collect(Collectors.toList());
    
            return new PageResponse<>(
                powiadomieniaResponse,
                powiadomienia.getNumber(),
                powiadomienia.getSize(),
                powiadomienia.getTotalElements(),
                powiadomienia.getTotalPages(),
                powiadomienia.isFirst(),
                powiadomienia.isLast()
        );
    }


    public static String timeAgo(LocalDateTime dateTime) {
        PrettyTime p = new PrettyTime(Locale.forLanguageTag("pl"));
        return p.format(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }
}
