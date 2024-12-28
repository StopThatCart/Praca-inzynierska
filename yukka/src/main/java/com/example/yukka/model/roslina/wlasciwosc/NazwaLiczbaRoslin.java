package com.example.yukka.model.roslina.wlasciwosc;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class NazwaLiczbaRoslin {
    private String nazwa;
    private Integer liczbaRoslin;
}