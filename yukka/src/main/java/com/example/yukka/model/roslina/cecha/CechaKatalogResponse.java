package com.example.yukka.model.roslina.cecha;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class CechaKatalogResponse {
    private String etykieta;
    private List<NazwaLiczbaRoslin> nazwyLiczbaRoslin;
}
