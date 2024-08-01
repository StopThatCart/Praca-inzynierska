package com.example.yukka.model.roslina;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.yukka.model.roslina.enums.RoslinaRelacje;
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;

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

    public Roslina toRoslina(RoslinaRequest request) {
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
        System.out.println("ROŚLINA:  "+ roslina.toString());
        return Arrays.stream(RoslinaRelacje.values())
            .map(relacja -> mapRelationToMap(getWlasciwosciByRelacja(roslina, relacja), relacja))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
    
    private List<Map<String, String>> mapRelationToMap(List<Wlasciwosc> wlasciwosci, RoslinaRelacje relacja) {
        return wlasciwosci.stream()
        .map(w ->  {
            Map<String, String> map = new HashMap<>();
            map.put("labels", w.getLabels());
            map.put("nazwa", w.getNazwa());
            map.put("relacja", relacja.name());
            return map;
        })
        .collect(Collectors.toList());
    }

    private List<Wlasciwosc> getWlasciwosciByRelacja(Roslina roslina, RoslinaRelacje relacja) {
        switch (relacja) {
            case MA_FORME: return roslina.getFormy();
            case MA_GLEBE: return roslina.getGleby();
            case MA_GRUPE: return roslina.getGrupy();
            case MA_KOLOR_LISCI: return roslina.getKoloryLisci();
            case MA_KOLOR_KWIATOW: return roslina.getKoloryKwiatow();
            case MA_KWIAT: return roslina.getKwiaty();
            case MA_NAGRODE: return roslina.getNagrody();
            case MA_ODCZYNY: return roslina.getOdczyny();
            case MA_OKRES_KWITNIENIA: return roslina.getOkresyKwitnienia();
            case MA_OKRES_OWOCOWANIA: return roslina.getOkresyOwocowania();
            case MA_OWOC: return roslina.getOwoce();
            case MA_PODGRUPE: return roslina.getPodgrupa();
            case MA_POKROJ: return roslina.getPokroje();
            case MA_SILE_WZROSTU: return roslina.getSilyWzrostu();
            case MA_STANOWISKO: return roslina.getStanowiska();
            case MA_WALOR: return roslina.getWalory();
            case MA_WILGOTNOSC: return roslina.getWilgotnosci();
            case MA_ZASTOSOWANIE: return roslina.getZastosowania();
            case MA_ZIMOZIELONOSC_LISCI: return roslina.getZimozielonosci();
            default: return new ArrayList<>();
        }
    }

    private void mapMapToRoslina(Roslina roslina, List<Map<String, String>> wlasciwosci) {
        for (Map<String, String> w : wlasciwosci) {
            Wlasciwosc wlasciwosc = new Wlasciwosc();
            wlasciwosc.setNazwa(w.get("nazwa"));

            RoslinaRelacje relacja = RoslinaRelacje.valueOf(w.get("relacja"));
            addWlasciwoscToList(roslina, wlasciwosc, relacja);
        }
    }

    private void addWlasciwoscToList(Roslina roslina, Wlasciwosc wlasciwosc, RoslinaRelacje relacja) {
        switch (relacja) {
            case MA_FORME: roslina.setFormy(addToList(roslina.getFormy(), wlasciwosc)); break;
            case MA_GLEBE: roslina.setGleby(addToList(roslina.getGleby(), wlasciwosc)); break;
            case MA_GRUPE: roslina.setGrupy(addToList(roslina.getGrupy(), wlasciwosc)); break;
            case MA_KOLOR_LISCI: roslina.setKoloryLisci(addToList(roslina.getKoloryLisci(), wlasciwosc)); break;
            case MA_KOLOR_KWIATOW: roslina.setKoloryKwiatow(addToList(roslina.getKoloryKwiatow(), wlasciwosc)); break;
            case MA_KWIAT: roslina.setKwiaty(addToList(roslina.getKwiaty(), wlasciwosc)); break;
            case MA_NAGRODE: roslina.setNagrody(addToList(roslina.getNagrody(), wlasciwosc)); break;
            case MA_ODCZYNY: roslina.setOdczyny(addToList(roslina.getOdczyny(), wlasciwosc)); break;
            case MA_OKRES_KWITNIENIA: roslina.setOkresyKwitnienia(addToList(roslina.getOkresyKwitnienia(), wlasciwosc)); break;
            case MA_OKRES_OWOCOWANIA: roslina.setOkresyOwocowania(addToList(roslina.getOkresyOwocowania(), wlasciwosc)); break;
            case MA_OWOC: roslina.setOwoce(addToList(roslina.getOwoce(), wlasciwosc)); break;
            case MA_PODGRUPE: roslina.setPodgrupa(addToList(roslina.getPodgrupa(), wlasciwosc)); break;
            case MA_POKROJ: roslina.setPokroje(addToList(roslina.getPokroje(), wlasciwosc)); break;
            case MA_SILE_WZROSTU: roslina.setSilyWzrostu(addToList(roslina.getSilyWzrostu(), wlasciwosc)); break;
            case MA_STANOWISKO: roslina.setStanowiska(addToList(roslina.getStanowiska(), wlasciwosc)); break;
            case MA_WALOR: roslina.setWalory(addToList(roslina.getWalory(), wlasciwosc)); break;
            case MA_WILGOTNOSC: roslina.setWilgotnosci(addToList(roslina.getWilgotnosci(), wlasciwosc)); break;
            case MA_ZASTOSOWANIE: roslina.setZastosowania(addToList(roslina.getZastosowania(), wlasciwosc)); break;
            case MA_ZIMOZIELONOSC_LISCI: roslina.setZimozielonosci(addToList(roslina.getZimozielonosci(), wlasciwosc)); break;
            default:
                throw new IllegalArgumentException("Relacja nie występuje w " + RoslinaRelacje.class);
        }
    }

    private List<Wlasciwosc> addToList(List<Wlasciwosc> list, Wlasciwosc wlasciwosc) {
        if (list == null) {
            return List.of(wlasciwosc);
        } else {
            List<Wlasciwosc> newList = new ArrayList<>(list);
            newList.add(wlasciwosc);
            return newList;
        }
    }

}
