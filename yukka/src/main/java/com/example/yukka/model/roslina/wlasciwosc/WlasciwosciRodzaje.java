package com.example.yukka.model.roslina.wlasciwosc;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class WlasciwosciRodzaje {
    private String etykieta;
    private Set<String> nazwy;
}