package com.example.yukka.model.roslina;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileUtils;
import com.example.yukka.model.dzialka.Dzialka;
import com.example.yukka.model.dzialka.DzialkaResponse;
import com.example.yukka.model.dzialka.ZasadzonaNaReverse;
import com.example.yukka.model.dzialka.ZasadzonaRoslinaResponse;
import com.example.yukka.model.ogrod.Ogrod;
import com.example.yukka.model.ogrod.OgrodResponse;
import com.example.yukka.model.roslina.enums.RoslinaRelacje;
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscWithRelations;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.post.PostResponse;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoslinaMapper {
    private final FileUtils fileUtils;


    // TODO: PRzerobić na simple response jak trzeba.
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

    private ZasadzonaRoslinaResponse toZasadzonaRoslinaResponse(ZasadzonaNaReverse zasadzonaNaReverse) {
        if (zasadzonaNaReverse == null || zasadzonaNaReverse.getRoslina() == null) {
            return null;
        }
    
        byte[] obraz = null;
        if (zasadzonaNaReverse.getObraz() != null) {
            obraz = fileUtils.readRoslinaObrazFile(zasadzonaNaReverse.getObraz());
        } 
        // else if (zasadzonaNaReverse.getRoslina().getObraz() != null) {
        //     obraz = fileUtils.readRoslinaObrazFile(zasadzonaNaReverse.getRoslina().getObraz());
        // }

        // TODO: zrobić to lepiej
        byte[] tekstura = null;
        if (zasadzonaNaReverse.getTekstura() != null) {
            tekstura = fileUtils.readRoslinaObrazFile(zasadzonaNaReverse.getTekstura());
        } else  {
          
        }
    
        return ZasadzonaRoslinaResponse.builder()
            .roslina(roslinaToRoslinaResponseWithWlasciwosci(zasadzonaNaReverse.getRoslina()))
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

    public UzytkownikRoslinaRequest toUzytkownikRoslinaRequest(Roslina roslina) {
        if (roslina == null) {
            return null;
        }
        return UzytkownikRoslinaRequest.builder()
            .roslinaId(roslina.getRoslinaId())
            .nazwa(roslina.getNazwa())
            .opis(roslina.getOpis())
            .obraz(roslina.getObraz())
            .wysokoscMin(roslina.getWysokoscMin())
            .wysokoscMax(roslina.getWysokoscMax())
            .wlasciwosci(mapWlasciwosciWithRelationsToMap(roslina))
            .build();
    }

    
    public RoslinaResponse toRoslinaResponseWithoutWlasciwosci(Roslina roslina) {
        if (roslina == null) {
            return null;
        }

        return RoslinaResponse.builder()
                .id(roslina.getId())
                .roslinaId(roslina.getRoslinaId())
                .nazwa(roslina.getNazwa())
                .opis(roslina.getOpis())
                .wysokoscMin(roslina.getWysokoscMin())
                .wysokoscMax(roslina.getWysokoscMax())
                .roslinaUzytkownika(roslina.isUzytkownikRoslina())
                .obraz(fileUtils.readRoslinaObrazFile(roslina.getObraz()))
                .autor(roslina.getUzytkownik() != null ? roslina.getUzytkownik().getNazwa() : null)
                .build();
    }

    

    public RoslinaRequest toRoslinaRequest(Roslina roslina) {
        if (roslina == null) {
            return null;
        }
        return RoslinaRequest.builder()
            .roslinaId(roslina.getRoslinaId())
            .nazwa(roslina.getNazwa())
            .nazwaLacinska(roslina.getNazwaLacinska())
            .opis(roslina.getOpis())
            .obraz(roslina.getObraz())
            .wysokoscMin(roslina.getWysokoscMin())
            .wysokoscMax(roslina.getWysokoscMax())
            .wlasciwosci(mapWlasciwosciWithRelationsToMap(roslina))
            .build();
    }

    public Roslina toRoslina(@Valid UzytkownikRoslinaRequest request) {
        if (request == null) {
            return null;
        }
        Roslina roslina = Roslina.builder()
            .labels(List.of("UzytkownikRoslina"))
            .roslinaId(request.getRoslinaId())
            .nazwa(request.getNazwa())
            .opis(request.getOpis())
            .obraz(request.getObraz())
            .wysokoscMin(request.getWysokoscMin())
            .wysokoscMax(request.getWysokoscMax())
            .build();
        mapWlasciwosciToRoslina(roslina, request.getWlasciwosci());
        
        return roslina;
    }


    public Roslina toRoslina(@Valid RoslinaRequest request) {
        if (request == null) {
            return null;
        }
        Roslina roslina = Roslina.builder()
            .roslinaId(request.getRoslinaId())
            .nazwa(request.getNazwa())
            .nazwaLacinska(request.getNazwaLacinska())
            .opis(request.getOpis())
            .obraz(request.getObraz())
            .wysokoscMin(request.getWysokoscMin())
            .wysokoscMax(request.getWysokoscMax())
            .build();
        
        if(request.getWlasciwosci() == null || request.getWlasciwosci().isEmpty()) {
                return roslina;
        }
        mapWlasciwosciToRoslina(roslina, request.getWlasciwosci());
        
        return roslina;
    }

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
                .roslinaId(roslina.getRoslinaId())
                .nazwa(roslina.getNazwa())
                .nazwaLacinska(roslina.getNazwaLacinska())
                .opis(roslina.getOpis())
                .wysokoscMin(roslina.getWysokoscMin())
                .wysokoscMax(roslina.getWysokoscMax())
                .obraz(fileUtils.readRoslinaObrazFile(roslina.getObraz()))
                .autor(roslina.getUzytkownik() != null ? roslina.getUzytkownik().getNazwa() : null)
                .roslinaUzytkownika(isRoslinaUzytkownika)
                .build();
    }

    public RoslinaResponse roslinaToRoslinaResponseWithWlasciwosci(Roslina roslina) {
        if (roslina == null) {
            return null;
        }

        return RoslinaResponse.builder()
                .id(roslina.getId())
                .roslinaId(roslina.getRoslinaId())

                .labels(roslina.getLabels())

                .nazwa(roslina.getNazwa())
                .nazwaLacinska(roslina.getNazwaLacinska())
                .opis(roslina.getOpis())
                .wysokoscMin(roslina.getWysokoscMin())
                .wysokoscMax(roslina.getWysokoscMax())
                .obraz(fileUtils.readRoslinaObrazFile(roslina.getObraz()))

                .autor(roslina.getUzytkownik() != null ? roslina.getUzytkownik().getNazwa() : null)
                .roslinaUzytkownika(roslina.isUzytkownikRoslina())

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

    private Set<String> extractNazwy(Set<Wlasciwosc> wlasciwosci) {
        return wlasciwosci.stream()
                          .map(Wlasciwosc::getNazwa)
                          .collect(Collectors.toSet());
    }

    private List<WlasciwoscWithRelations> mapWlasciwosciWithRelationsToMap(Roslina roslina) {
        return Arrays.stream(RoslinaRelacje.values())
            .map(relacja -> mapWlasciwosciRelationToMap(relacja.getWlasciwosci(roslina), relacja))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
    
    private List<WlasciwoscWithRelations> mapWlasciwosciRelationToMap(Set<Wlasciwosc> wlasciwosci, RoslinaRelacje relacja) {
        return wlasciwosci.stream()
            .map(w -> {
                WlasciwoscWithRelations wlasciwosc = new WlasciwoscWithRelations(w.getLabels(), w.getNazwa(), relacja.name());
                return wlasciwosc;
            })
            .collect(Collectors.toList());
    }


    private void mapWlasciwosciToRoslina(Roslina roslina, List<WlasciwoscWithRelations> wlasciwosci) {
        for (WlasciwoscWithRelations w : wlasciwosci) {
            Wlasciwosc wlasciwosc = new Wlasciwosc();
            wlasciwosc.setNazwa(w.getNazwa());

            RoslinaRelacje relacja = RoslinaRelacje.valueOf(w.getRelacja());
            Set<Wlasciwosc> existingSet = relacja.getWlasciwosci(roslina);
            if (existingSet == null) {
                existingSet = new HashSet<>();
            }
            existingSet.add(wlasciwosc);
            relacja.setWlasciwosci(roslina, existingSet);
        }
    }

}
