package com.example.yukka.model.roslina;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.yukka.model.roslina.enums.RoslinaRelacje;
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;

import jakarta.validation.Valid;

@Service
public class RoslinaMapper {
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

    private List<Map<String, String>> mapWlasciwosciToMap(Roslina roslina) {
        return Arrays.stream(RoslinaRelacje.values())
            .map(relacja -> mapRelationToMap(getWlasciwosciByRelacja(roslina, relacja), relacja))
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

    private Set<Wlasciwosc> getWlasciwosciByRelacja(Roslina roslina, RoslinaRelacje relacja) {
        return switch (relacja) {
            case MA_FORME -> roslina.getFormy();
            case MA_GLEBE -> roslina.getGleby();
            case MA_GRUPE -> roslina.getGrupy();
            case MA_KOLOR_LISCI -> roslina.getKoloryLisci();
            case MA_KOLOR_KWIATOW -> roslina.getKoloryKwiatow();
            case MA_KWIAT -> roslina.getKwiaty();
            case MA_NAGRODE -> roslina.getNagrody();
            case MA_ODCZYNY -> roslina.getOdczyny();
            case MA_OKRES_KWITNIENIA -> roslina.getOkresyKwitnienia();
            case MA_OKRES_OWOCOWANIA -> roslina.getOkresyOwocowania();
            case MA_OWOC -> roslina.getOwoce();
            case MA_PODGRUPE -> roslina.getPodgrupa();
            case MA_POKROJ -> roslina.getPokroje();
            case MA_SILE_WZROSTU -> roslina.getSilyWzrostu();
            case MA_STANOWISKO -> roslina.getStanowiska();
            case MA_WALOR -> roslina.getWalory();
            case MA_WILGOTNOSC -> roslina.getWilgotnosci();
            case MA_ZASTOSOWANIE -> roslina.getZastosowania();
            case MA_ZIMOZIELONOSC_LISCI -> roslina.getZimozielonosci();
            default -> new HashSet<>();
        };
    }

    private void mapMapToRoslina(Roslina roslina, List<Map<String, String>> wlasciwosci) {
        for (Map<String, String> w : wlasciwosci) {
            Wlasciwosc wlasciwosc = new Wlasciwosc();
            wlasciwosc.setNazwa(w.get("nazwa"));

            RoslinaRelacje relacja = RoslinaRelacje.valueOf(w.get("relacja"));
            addWlasciwoscToSet(roslina, wlasciwosc, relacja);
        }
    }

    private void addWlasciwoscToSet(Roslina roslina, Wlasciwosc wlasciwosc, RoslinaRelacje relacja) {
        switch (relacja) {
            case MA_FORME -> roslina.setFormy(addToSet(roslina.getFormy(), wlasciwosc));
            case MA_GLEBE -> roslina.setGleby(addToSet(roslina.getGleby(), wlasciwosc));
            case MA_GRUPE -> roslina.setGrupy(addToSet(roslina.getGrupy(), wlasciwosc));
            case MA_KOLOR_LISCI -> roslina.setKoloryLisci(addToSet(roslina.getKoloryLisci(), wlasciwosc));
            case MA_KOLOR_KWIATOW -> roslina.setKoloryKwiatow(addToSet(roslina.getKoloryKwiatow(), wlasciwosc));
            case MA_KWIAT -> roslina.setKwiaty(addToSet(roslina.getKwiaty(), wlasciwosc));
            case MA_NAGRODE -> roslina.setNagrody(addToSet(roslina.getNagrody(), wlasciwosc));
            case MA_ODCZYNY -> roslina.setOdczyny(addToSet(roslina.getOdczyny(), wlasciwosc));
            case MA_OKRES_KWITNIENIA -> roslina.setOkresyKwitnienia(addToSet(roslina.getOkresyKwitnienia(), wlasciwosc));
            case MA_OKRES_OWOCOWANIA -> roslina.setOkresyOwocowania(addToSet(roslina.getOkresyOwocowania(), wlasciwosc));
            case MA_OWOC -> roslina.setOwoce(addToSet(roslina.getOwoce(), wlasciwosc));
            case MA_PODGRUPE -> roslina.setPodgrupa(addToSet(roslina.getPodgrupa(), wlasciwosc));
            case MA_POKROJ -> roslina.setPokroje(addToSet(roslina.getPokroje(), wlasciwosc));
            case MA_SILE_WZROSTU -> roslina.setSilyWzrostu(addToSet(roslina.getSilyWzrostu(), wlasciwosc));
            case MA_STANOWISKO -> roslina.setStanowiska(addToSet(roslina.getStanowiska(), wlasciwosc));
            case MA_WALOR -> roslina.setWalory(addToSet(roslina.getWalory(), wlasciwosc));
            case MA_WILGOTNOSC -> roslina.setWilgotnosci(addToSet(roslina.getWilgotnosci(), wlasciwosc));
            case MA_ZASTOSOWANIE -> roslina.setZastosowania(addToSet(roslina.getZastosowania(), wlasciwosc));
            case MA_ZIMOZIELONOSC_LISCI -> roslina.setZimozielonosci(addToSet(roslina.getZimozielonosci(), wlasciwosc));
            default -> throw new IllegalArgumentException("Relacja nie występuje w " + RoslinaRelacje.class);
        }
    }

    private Set<Wlasciwosc> addToSet(Set<Wlasciwosc> set, Wlasciwosc wlasciwosc) {
        if (set == null) {
            set = new HashSet<>();
        }
        set.add(wlasciwosc);
        return set;
    }

}
