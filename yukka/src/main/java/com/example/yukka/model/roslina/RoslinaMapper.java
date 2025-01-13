package com.example.yukka.model.roslina;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.DefaultImage;
import com.example.yukka.file.FileUtils;
import com.example.yukka.model.dzialka.Dzialka;
import com.example.yukka.model.dzialka.ZasadzonaNaReverse;
import com.example.yukka.model.dzialka.response.DzialkaResponse;
import com.example.yukka.model.dzialka.response.ZasadzonaRoslinaResponse;
import com.example.yukka.model.ogrod.Ogrod;
import com.example.yukka.model.ogrod.OgrodResponse;
import com.example.yukka.model.roslina.cecha.Cecha;
import com.example.yukka.model.roslina.cecha.CechaWithRelations;
import com.example.yukka.model.roslina.enums.RoslinaRelacje;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoslinaMapper {
    private final FileUtils fileUtils;


        /**
     * Konwertuje obiekt @see OgrodResponse na PageResponse<OgrodResponse>.
     *
     * @param ogrody obiekt Page zawierający listę obiektów Ogrod
     * @return obiekt PageResponse zawierający listę obiektów OgrodResponse
     * 
     * <ul>
     *   <li><strong>postyResponse</strong> - lista obiektów OgrodResponse</li>
     *   <li><strong>ogrody.getNumber()</strong> - numer bieżącej strony</li>
     *   <li><strong>ogrody.getSize()</strong> - rozmiar strony</li>
     *   <li><strong>ogrody.getTotalElements()</strong> - całkowita liczba elementów</li>
     *   <li><strong>ogrody.getTotalPages()</strong> - całkowita liczba stron</li>
     *   <li><strong>ogrody.isFirst()</strong> - czy jest to pierwsza strona</li>
     *   <li><strong>ogrody.isLast()</strong> - czy jest to ostatnia strona</li>
     * </ul>
     */
    public PageResponse<OgrodResponse> ogrodResponsetoPageResponse(Page<Ogrod> ogrody) {
        List<OgrodResponse> postyResponse = ogrody.stream()
                .map(this::toOgrodResponse)
                .toList();
        return new PageResponse<>(
            postyResponse,
            ogrody.getNumber(),
            ogrody.getSize(),
            ogrody.getTotalElements(),
            ogrody.getTotalPages(),
            ogrody.isFirst(),
            ogrody.isLast()
        );
    }

    /**
     * Konwertuje obiekt typu <strong>Ogrod</strong> na obiekt typu <strong>OgrodResponse</strong>.
     *
     * @param ogrod obiekt typu <strong>Ogrod</strong>, który ma być skonwertowany
     * @return obiekt typu <strong>OgrodResponse</strong> lub <strong>null</strong>, jeśli wejściowy obiekt <strong>Ogrod</strong> jest <strong>null</strong>
     * 
     */
    public OgrodResponse toOgrodResponse(Ogrod ogrod) {
        if (ogrod == null) {
            return null;
        }
        return OgrodResponse.builder()
            .id(ogrod.getId())
            .nazwa(ogrod.getNazwa())
            .wlascicielNazwa(ogrod.getUzytkownik() != null ? ogrod.getUzytkownik().getNazwa() : null)
            .dzialki(ogrod.getDzialki().stream()
                .map(this::toDzialkaResponse)
                .collect(Collectors.toList()))
            .build();
    }


    /**
     * Konwertuje obiekt typu <strong>Dzialka</strong> na obiekt typu <strong>DzialkaResponse</strong>.
     *
     * @param dzialka obiekt typu <strong>Dzialka</strong>, który ma być skonwertowany
     * @return obiekt typu <strong>DzialkaResponse</strong> lub <strong>null</strong>, jeśli wejściowy obiekt <strong>Dzialka</strong> jest <strong>null</strong>
     *
     */
    public DzialkaResponse toDzialkaResponse(Dzialka dzialka) {
        if (dzialka == null) {
            return null;
        }
        Uzytkownik uzyt = null;
        String nazwa = null;
        if(dzialka.getOgrod() != null && dzialka.getOgrod().getUzytkownik() != null) {
            uzyt = dzialka.getOgrod().getUzytkownik();
            nazwa = uzyt.getNazwa();
        }

        return DzialkaResponse.builder()
            .id(dzialka.getId())
            .nazwa(dzialka.getNazwa())
            .numer(dzialka.getNumer())
            .wlascicielNazwa(dzialka.getOgrod() != null ? nazwa : null)
            .zasadzoneRosliny(dzialka.getZasadzoneRosliny().stream()
                .map(this::toZasadzonaRoslinaResponse)
                .collect(Collectors.toList()))
            .liczbaRoslin(dzialka.getZasadzoneRosliny().size())
            .build();
    }

    /**
     * Konwertuje obiekt typu <strong>ZasadzonaNa</strong> na obiekt typu <strong>ZasadzonaRoslinaResponse</strong>.
     *
     * @param zasadzonaNa obiekt typu <strong>ZasadzonaNa</strong>, który ma być skonwertowany
     * @return obiekt typu <strong>ZasadzonaRoslinaResponse</strong> lub <strong>null</strong>, jeśli wejściowy obiekt <strong>ZasadzonaNa</strong> jest <strong>null</strong>
     *
     */
    public ZasadzonaRoslinaResponse toZasadzonaRoslinaResponse(ZasadzonaNaReverse zasadzonaNaReverse) {
        if (zasadzonaNaReverse == null || zasadzonaNaReverse.getRoslina() == null) {
            return null;
        }
    
        byte[] obraz = null;
        if (zasadzonaNaReverse.getObraz() != null && !zasadzonaNaReverse.getObraz().isEmpty()) {
            obraz = fileUtils.readFile(zasadzonaNaReverse.getObraz(), DefaultImage.ROSLINA);
        }

        byte[] tekstura = null;
        if (zasadzonaNaReverse.getTekstura() != null && !zasadzonaNaReverse.getTekstura().isEmpty()) {
            tekstura = fileUtils.readFile(zasadzonaNaReverse.getTekstura(), null);
        }
    
        return ZasadzonaRoslinaResponse.builder()
            .roslina(roslinaToRoslinaResponseWithCechy(zasadzonaNaReverse.getRoslina()))
            .x(zasadzonaNaReverse.getX())
            .y(zasadzonaNaReverse.getY())
            .tabX(zasadzonaNaReverse.getTabX())
            .tabY(zasadzonaNaReverse.getTabY())
            .pozycje(zasadzonaNaReverse.getPozycje())
            .kolor(zasadzonaNaReverse.getKolor())
            .tekstura(tekstura)
            .wyswietlanie(zasadzonaNaReverse.getWyswietlanie())
            .notatka(zasadzonaNaReverse.getNotatka())
            .obraz(obraz)
            .build();
    }
    
    /**
     * Konwertuje obiekt typu <strong>Roslina</strong> na obiekt typu <strong>RoslinaRequest</strong>.
     *
     * @param roslina obiekt typu <strong>Roslina</strong>, który ma zostać przekonwertowany
     * @return obiekt typu <strong>RoslinaRequest</strong> lub <strong>null</strong>, jeśli przekazany obiekt <strong>Roslina</strong> jest <strong>null</strong>
     */
    public RoslinaResponse toRoslinaResponseWithoutCechy(Roslina roslina) {
        if (roslina == null) {
            return null;
        }

        return RoslinaResponse.builder()
                .id(roslina.getId())
                .uuid(roslina.getUuid())
                .nazwa(roslina.getNazwa())
                .opis(roslina.getOpis())
                .wysokoscMin(roslina.getWysokoscMin())
                .wysokoscMax(roslina.getWysokoscMax())
                .roslinaUzytkownika(roslina.isRoslinaWlasna())
                .obraz(fileUtils.readFile(roslina.getObraz(), DefaultImage.ROSLINA))
                .autor(roslina.getUzytkownik() != null ? roslina.getUzytkownik().getNazwa() : null)
                .build();
    }

    
    /**
     * Konwertuje obiekt typu <strong>Roslina</strong> na obiekt typu <strong>RoslinaRequest</strong>.
     *
     * @param roslina obiekt typu <strong>Roslina</strong>, który ma zostać przekonwertowany
     * @return obiekt typu <strong>RoslinaRequest</strong> lub <strong>null</strong>, jeśli przekazany obiekt <strong>Roslina</strong> jest <strong>null</strong>
     */
    public RoslinaRequest toRoslinaRequest(Roslina roslina) {
        if (roslina == null) {
            return null;
        }
        return RoslinaRequest.builder()
            .uuid(roslina.getUuid())
            .nazwa(roslina.getNazwa())
            .nazwaLacinska(roslina.getNazwaLacinska())
            .opis(roslina.getOpis())
            .obraz(roslina.getObraz())
            .wysokoscMin(roslina.getWysokoscMin())
            .wysokoscMax(roslina.getWysokoscMax())
            .cechy(mapCechaWithRelationsToMap(roslina))
            .build();
    }

    /**
     * Konwertuje obiekt typu <strong>RoslinaRequest</strong> na obiekt typu <strong>Roslina</strong>.
     *
     * @param request obiekt typu <strong>RoslinaRequest</strong>, który ma zostać przekonwertowany
     * @return obiekt typu <strong>Roslina</strong> lub <strong>null</strong>, jeśli przekazany obiekt <strong>RoslinaRequest</strong> jest <strong>null</strong>
     */
    public Roslina toRoslina(@Valid RoslinaRequest request) {
        if (request == null) {
            return null;
        }

        Roslina roslina = Roslina.builder()
            .uuid(request.getUuid())
            .nazwa(request.getNazwa())
            .nazwaLacinska(request.getNazwaLacinska())
            .opis(request.getOpis())
            .obraz(request.getObraz())
            .wysokoscMin(request.getWysokoscMin())
            .wysokoscMax(request.getWysokoscMax())
            .build();
        
        if(request.getCechy() == null || request.getCechy().isEmpty()) {
                return roslina;
        }
        mapCechyToRoslina(roslina, request.getCechy());
        
        return roslina;
    }

    /**
     * Konwertuje obiekt typu <strong>Roslina</strong> na obiekt typu <strong>RoslinaResponse</strong>.
     *
     * @param roslina obiekt typu <strong>Roslina</strong>, który ma zostać przekonwertowany
     * @return obiekt typu <strong>RoslinaResponse</strong> lub <strong>null</strong>, jeśli przekazany obiekt <strong>Roslina</strong> jest <strong>null</strong>
     */
    public RoslinaResponse toRoslinaResponse(Roslina roslina) {
        if (roslina == null) {
            return null;
        }

        boolean isRoslinaUzytkownika = false;
        if(roslina.getUzytkownik() != null || roslina.getNazwaLacinska() == null) {
            isRoslinaUzytkownika = true;
        }

        return RoslinaResponse.builder()
                .id(roslina.getId())
                .uuid(roslina.getUuid())
                .nazwa(roslina.getNazwa())
                .nazwaLacinska(roslina.getNazwaLacinska())
                .opis(roslina.getOpis())
                .wysokoscMin(roslina.getWysokoscMin())
                .wysokoscMax(roslina.getWysokoscMax())
                .obraz(fileUtils.readFile(roslina.getObraz(), DefaultImage.ROSLINA))
                .autor(roslina.getUzytkownik() != null ? roslina.getUzytkownik().getNazwa() : null)
                .roslinaUzytkownika(isRoslinaUzytkownika)
                .build();
    }

    /**
     * Konwertuje obiekt typu <strong>Roslina</strong> na obiekt typu <strong>RoslinaResponse</strong>.
     *
     * @param roslina obiekt typu <strong>Roslina</strong>, który ma zostać przekonwertowany
     * @return obiekt typu <strong>RoslinaResponse</strong> lub <strong>null</strong>, jeśli przekazany obiekt <strong>Roslina</strong> jest <strong>null</strong>
     */
    public RoslinaResponse roslinaToRoslinaResponseWithCechy(Roslina roslina) {
        if (roslina == null) {
            return null;
        }

        return RoslinaResponse.builder()
                .id(roslina.getId())
                .uuid(roslina.getUuid())

                .labels(roslina.getLabels())

                .nazwa(roslina.getNazwa())
                .nazwaLacinska(roslina.getNazwaLacinska())
                .opis(roslina.getOpis())
                .wysokoscMin(roslina.getWysokoscMin())
                .wysokoscMax(roslina.getWysokoscMax())
                .obraz(fileUtils.readFile(roslina.getObraz(), DefaultImage.ROSLINA))

                .autor(roslina.getUzytkownik() != null ? roslina.getUzytkownik().getNazwa() : null)
                .roslinaUzytkownika(roslina.isRoslinaWlasna())

                .grupy(extractNazwy(roslina.getGrupy()))
                .formy(extractNazwy(roslina.getFormy()))
                .gleby(extractNazwy(roslina.getGleby()))
                .koloryLisci(extractNazwy(roslina.getKoloryLisci()))
                .koloryKwiatow(extractNazwy(roslina.getKoloryKwiatow()))
                .kwiaty(extractNazwy(roslina.getKwiaty()))
                .odczyny(extractNazwy(roslina.getOdczyny()))
                .okresyKwitnienia(extractNazwy(roslina.getOkresyKwitnienia()))
                .okresyOwocowania(extractNazwy(roslina.getOkresyOwocowania()))
                .owoce(extractNazwy(roslina.getOwoce()))
                .podgrupa(extractNazwy(roslina.getPodgrupa()))
                .pokroje(extractNazwy(roslina.getPokroje()))
                .silyWzrostu(extractNazwy(roslina.getSilyWzrostu()))
                .stanowiska(extractNazwy(roslina.getStanowiska()))
                .walory(extractNazwy(roslina.getWalory()))
                .wilgotnosci(extractNazwy(roslina.getWilgotnosci()))
                .zastosowania(extractNazwy(roslina.getZastosowania()))
                .zimozielonosci(extractNazwy(roslina.getZimozielonosci()))
                .build();
    }

    private Set<String> extractNazwy(Set<Cecha> cechy) {
        return cechy.stream()
                          .map(Cecha::getNazwa)
                          .collect(Collectors.toSet());
    }

    /**
     * Konwertuje obiekt typu <strong>Roslina</strong> na obiekt typu <strong>List<CechaWithRelations></strong>.
     *
     * @param roslina obiekt typu <strong>Roslina</strong>, który ma zostać przekonwertowany
     * @return obiekt typu <strong>List<CechaWithRelations></strong>
     */
    private List<CechaWithRelations> mapCechaWithRelationsToMap(Roslina roslina) {
        return Arrays.stream(RoslinaRelacje.values())
            .map(relacja -> mapCechaRelationToMap(relacja.getCechy(roslina), relacja))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
    
    /**
     * Konwertuje obiekt typu <strong>Set<Cecha></strong> na obiekt typu <strong>List<CechaWithRelations></strong>.
     *
     * @param cechy obiekt typu <strong>Set<Cecha></strong>, który ma zostać przekonwertowany
     * @param relacja obiekt typu <strong>RoslinaRelacje</strong>, który określa relację cechy
     * @return obiekt typu <strong>List<CechaWithRelations></strong>
     */
    private List<CechaWithRelations> mapCechaRelationToMap(Set<Cecha> cechy, RoslinaRelacje relacja) {
        return cechy.stream()
            .map(w -> {
                CechaWithRelations cecha = new CechaWithRelations(w.getLabels(), w.getNazwa(), relacja.name());
                return cecha;
            })
            .collect(Collectors.toList());
    }


    /**
     * Mapuje cechy z obiektu <strong>RoslinaWlasnaRequest</strong> na cechy obiektu <strong>Roslina</strong>.
     *
     * @param roslina obiekt typu <strong>Roslina</strong>, do którego mają zostać przypisane cechy
     * @param cechy lista obiektów <strong>CechaWithRelations</strong>, które mają zostać przypisane do obiektu <strong>Roslina</strong>
     */
    private void mapCechyToRoslina(Roslina roslina, List<CechaWithRelations> cechy) {
        for (CechaWithRelations w : cechy) {
            Cecha cecha = new Cecha();
            cecha.setNazwa(w.getNazwa());

            RoslinaRelacje relacja = RoslinaRelacje.valueOf(w.getRelacja());
            Set<Cecha> existingSet = relacja.getCechy(roslina);
            if (existingSet == null) {
                existingSet = new HashSet<>();
            }
            existingSet.add(cecha);
            relacja.setCechy(roslina, existingSet);
        }
    }

}
