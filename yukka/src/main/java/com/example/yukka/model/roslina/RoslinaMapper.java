package com.example.yukka.model.roslina;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.yukka.file.FileUtils;
import com.example.yukka.model.dzialka.Dzialka;
import com.example.yukka.model.dzialka.DzialkaResponse;
import com.example.yukka.model.dzialka.ZasadzonaNaReverse;
import com.example.yukka.model.dzialka.ZasadzonaRoslinaResponse;
import com.example.yukka.model.roslina.enums.RoslinaRelacje;
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoslinaMapper {
    private final FileUtils fileUtils;

    public DzialkaResponse toDzialkaResponse(Dzialka dzialka) {
        return DzialkaResponse.builder()
            .id(dzialka.getId())
            .numer(dzialka.getNumer())
            .wlascicielNazwa(dzialka.getOgrod().getUzytkownik().getNazwa())
            .zasadzoneRosliny(dzialka.getZasadzoneRosliny().stream()
                .map(this::toZasadzonaRoslinaResponse)
                .collect(Collectors.toList()))
            .liczbaRoslin(dzialka.getZasadzoneRosliny().size())
            .build();
    }

    private ZasadzonaRoslinaResponse toZasadzonaRoslinaResponse(ZasadzonaNaReverse zasadzonaNaReverse) {
        if (zasadzonaNaReverse == null || zasadzonaNaReverse.getRoslina() == null) {
            return null;
        }
    
        byte[] obraz = null;
        if (zasadzonaNaReverse.getObraz() != null) {
            obraz = fileUtils.readRoslinaObrazFile(zasadzonaNaReverse.getObraz());
        } else if (zasadzonaNaReverse.getRoslina().getObraz() != null) {
            obraz = fileUtils.readRoslinaObrazFile(zasadzonaNaReverse.getRoslina().getObraz());
        }
    
        return ZasadzonaRoslinaResponse.builder()
            .roslina(roslinaToRoslinaResponseWithWlasciwosci(zasadzonaNaReverse.getRoslina()))
            .x(zasadzonaNaReverse.getX())
            .y(zasadzonaNaReverse.getY())
            .obraz(obraz)
            .build();
    }

    public UzytkownikRoslinaRequest toUzytkownikRoslinaRequest(UzytkownikRoslina roslina) {
        return UzytkownikRoslinaRequest.builder()
            .roslinaId(roslina.getRoslinaId())
            .nazwa(roslina.getNazwa())
            .opis(roslina.getOpis())
            .obraz(roslina.getObraz())
            .wysokoscMin(roslina.getWysokoscMin())
            .wysokoscMax(roslina.getWysokoscMax())
            .wlasciwosci(mapWlasciwosciToMap(roslina))
            .build();
    }

    public UzytkownikRoslina toUzytkownikRoslina(@Valid UzytkownikRoslinaRequest request) {
        UzytkownikRoslina roslina = UzytkownikRoslina.builder()
            .roslinaId(request.getRoslinaId())
            .nazwa(request.getNazwa())
            .opis(request.getOpis())
            .obraz(request.getObraz())
            .wysokoscMin(request.getWysokoscMin())
            .wysokoscMax(request.getWysokoscMax())
            .build();
        
        mapMapToRoslina(roslina, request.getWlasciwosci());
        
        return roslina;
    }

    public UzytkownikRoslinaResponse toUzytkownikRoslinaResponse(UzytkownikRoslina roslina) {
        return UzytkownikRoslinaResponse.builder()
                .id(roslina.getId())
                .roslinaId(roslina.getRoslinaId())
                .nazwa(roslina.getNazwa())
                .opis(roslina.getOpis())
                .wysokoscMin(roslina.getWysokoscMin())
                .wysokoscMax(roslina.getWysokoscMax())
                .obraz(fileUtils.readRoslinaObrazFile(roslina.getObraz()))
                .build();
    }


    public RoslinaRequest toRoslinaRequest(Roslina roslina) {
        return RoslinaRequest.builder()
            .nazwa(roslina.getNazwa())
            .nazwaLacinska(roslina.getNazwaLacinska())
            .opis(roslina.getOpis())
            .obraz(roslina.getObraz())
            .wysokoscMin(roslina.getWysokoscMin())
            .wysokoscMax(roslina.getWysokoscMax())
            .wlasciwosci(mapWlasciwosciToMap(roslina))
            .build();
    }

    public Roslina toRoslina(@Valid RoslinaRequest request) {
        Roslina roslina = Roslina.builder()
            .nazwa(request.getNazwa())
            .nazwaLacinska(request.getNazwaLacinska())
            .opis(request.getOpis())
            .obraz(request.getObraz())
            .wysokoscMin(request.getWysokoscMin())
            .wysokoscMax(request.getWysokoscMax())
            .build();
        
        mapMapToRoslina(roslina, request.getWlasciwosci());
        
        return roslina;
    }

    public RoslinaResponse toRoslinaResponse(Roslina roslina) {
        return RoslinaResponse.builder()
                .id(roslina.getId())
                .nazwa(roslina.getNazwa())
                .nazwaLacinska(roslina.getNazwaLacinska())
                .opis(roslina.getOpis())
                .wysokoscMin(roslina.getWysokoscMin())
                .wysokoscMax(roslina.getWysokoscMax())
                .obraz(fileUtils.readRoslinaObrazFile(roslina.getObraz()))
                .build();
    }

    public RoslinaResponse roslinaToRoslinaResponseWithWlasciwosci(Roslina roslina) {
        return RoslinaResponse.builder()
                .id(roslina.getId())
                .nazwa(roslina.getNazwa())
                .nazwaLacinska(roslina.getNazwaLacinska())
                .opis(roslina.getOpis())
                .wysokoscMin(roslina.getWysokoscMin())
                .wysokoscMax(roslina.getWysokoscMax())
                .obraz(fileUtils.readRoslinaObrazFile(roslina.getObraz()))
                .grupy(extractNazwy(roslina.getGrupy()))
                .formy(extractNazwy(roslina.getFormy()))
                .gleby(extractNazwy(roslina.getGleby()))
                .koloryLisci(extractNazwy(roslina.getKoloryLisci()))
                .koloryKwiatow(extractNazwy(roslina.getKoloryKwiatow()))
                .kwiaty(extractNazwy(roslina.getKwiaty()))
                .nagrody(extractNazwy(roslina.getNagrody()))
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

    private Set<String> extractNazwy(Set<Wlasciwosc> wlasciwosci) {
        return wlasciwosci.stream()
                          .map(Wlasciwosc::getNazwa)
                          .collect(Collectors.toSet());
    }

    private List<Map<String, String>> mapWlasciwosciToMap(Roslina roslina) {
        return Arrays.stream(RoslinaRelacje.values())
            .map(relacja -> mapRelationToMap(relacja.getWlasciwosci(roslina), relacja))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
    
    private List<Map<String, String>> mapRelationToMap(Set<Wlasciwosc> wlasciwosci, RoslinaRelacje relacja) {
        return wlasciwosci.stream()
            .map(w -> {
                Map<String, String> map = new HashMap<>();
                map.put("labels", w.getLabels());
                map.put("nazwa", w.getNazwa());
                map.put("relacja", relacja.name());
                return map;
            })
            .collect(Collectors.toList());
    }

    private void mapMapToRoslina(Roslina roslina, List<Map<String, String>> wlasciwosci) {
        for (Map<String, String> w : wlasciwosci) {
            Wlasciwosc wlasciwosc = new Wlasciwosc();
            wlasciwosc.setNazwa(w.get("nazwa"));

            RoslinaRelacje relacja = RoslinaRelacje.valueOf(w.get("relacja"));
            Set<Wlasciwosc> existingSet = relacja.getWlasciwosci(roslina);
            if (existingSet == null) {
                existingSet = new HashSet<>();
            }
            existingSet.add(wlasciwosc);
            relacja.setWlasciwosci(roslina, existingSet);
        }
    }

}
