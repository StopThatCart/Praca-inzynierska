package com.example.yukka.model.social.mappers;

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
import com.example.yukka.file.DefaultImage;
import com.example.yukka.file.FileUtils;
import com.example.yukka.model.social.models.powiadomienie.Powiadomienie;
import com.example.yukka.model.social.models.powiadomienie.PowiadomienieDTO;
import com.example.yukka.model.social.models.powiadomienie.PowiadomienieResponse;

import lombok.RequiredArgsConstructor;

/**
 * Klasa <strong>PowiadomienieMapper</strong> jest odpowiedzialna za mapowanie obiektów typu <strong>Powiadomienie</strong> na różne formaty odpowiedzi.
 * 
 * Metody:
 * <ul>
 * <li><strong>toPowiadomienieResponse</strong>: Mapuje obiekt <strong>Powiadomienie</strong> na <strong>PowiadomienieResponse</strong>.</li>
 * <li><strong>toPowiadomienieDTO</strong>: Mapuje obiekt <strong>Powiadomienie</strong> na <strong>PowiadomienieDTO</strong>.</li>
 * <li><strong>PowiadomieniePageToPagePowiadomienieResponse</strong>: Mapuje stronę obiektów <strong>Powiadomienie</strong> na stronę odpowiedzi <strong>PowiadomienieResponse</strong>.</li>
 * <li><strong>timeAgo</strong>: Formatuje datę na tekst w stylu "czas temu".</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class PowiadomienieMapper {
    private final FileUtils fileUtils;

    private final CommonMapperService commonMapperService;


    

    /**
     * Konwertuje obiekt typu <code>Powiadomienie</code> na obiekt typu <code>PowiadomienieResponse</code>.
     *
     * @param powiadomienie obiekt typu <code>Powiadomienie</code> do konwersji
     * @return obiekt typu <code>PowiadomienieResponse</code> lub <code>null</code>, jeśli <code>powiadomienie</code> jest <code>null</code>
     */
    public PowiadomienieResponse toPowiadomienieResponse(Powiadomienie powiadomienie) {
        if (powiadomienie == null) {
            return null;
        }
        Boolean przeczytane = null;
        Boolean ukryte = null;
        if (powiadomienie.getPowiadamia() != null) {
            przeczytane = powiadomienie.getPowiadamia().getPrzeczytane();
            ukryte = powiadomienie.getPowiadamia().getUkryte();
        }

        return PowiadomienieResponse.builder()
            .id(powiadomienie.getId())
            .typ(powiadomienie.getTyp())
            .isZgloszenie(powiadomienie.isZgloszenie())
            .zglaszajacy(commonMapperService.toUzytkownikResponse(powiadomienie.getZglaszajacy()))
            .tytul(powiadomienie.getTytul())
            .przeczytane(przeczytane)
            .ukryte(ukryte)
            .odnosnik(powiadomienie.getOdnosnik())
            .opis(powiadomienie.getOpis())
            .nazwyRoslin(powiadomienie.getNazwyRoslin())
            .avatar(fileUtils.readFile(powiadomienie.getAvatar(), DefaultImage.POWIADOMIENIA))
            .uzytkownikNazwa(powiadomienie.getUzytkownikNazwa())
            .iloscPolubien(powiadomienie.getIloscPolubien())
            .data(powiadomienie.getData())
            .dataUtworzenia(timeAgo(powiadomienie.getDataUtworzenia()))
            .build();
    }


    /**
     * Konwertuje obiekt typu Powiadomienie na obiekt typu PowiadomienieDTO.
     *
     * @param powiadomienie obiekt typu Powiadomienie, który ma zostać przekonwertowany
     * @return obiekt typu PowiadomienieDTO lub null, jeśli powiadomienie jest null
     */
    public PowiadomienieDTO toPowiadomienieDTO(Powiadomienie powiadomienie) {
        if (powiadomienie == null) {
            return null;
        }
        return PowiadomienieDTO.builder()
            .id(powiadomienie.getId())
            .isZgloszenie(powiadomienie.isZgloszenie())
            .zglaszajacy(powiadomienie.getZglaszajacy())
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

    /**
     * Konwertuje stronę obiektów typu Powiadomienie na stronę odpowiedzi typu PowiadomienieResponse.
     *
     * @param powiadomienia strona obiektów typu Powiadomienie do konwersji
     * @return strona odpowiedzi typu PowiadomienieResponse
     */
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

    /**
     * Formatuje datę na tekst w stylu "czas temu".
     *
     * @param dateTime data do sformatowania
     * @return sformatowana data
     */
    public static String timeAgo(LocalDateTime dateTime) {
        PrettyTime p = new PrettyTime(Locale.forLanguageTag("pl"));
        return p.format(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }
}
