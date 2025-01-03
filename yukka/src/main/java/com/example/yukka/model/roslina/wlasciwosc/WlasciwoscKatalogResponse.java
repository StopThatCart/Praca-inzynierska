package com.example.yukka.model.roslina.wlasciwosc;

import java.util.List;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class WlasciwoscKatalogResponse {
    private String etykieta;
    private List<NazwaLiczbaRoslin> nazwyLiczbaRoslin;
}
