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
import com.example.yukka.model.roslina.enums.RoslinaRelacje;
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoslinaMapper {
    private final FileUtils fileUtils;
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
