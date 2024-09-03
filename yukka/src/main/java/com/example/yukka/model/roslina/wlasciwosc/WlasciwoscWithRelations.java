package com.example.yukka.model.roslina.wlasciwosc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@Getter
@Setter
public class WlasciwoscWithRelations {
    private String etykieta;
    private String nazwa;
    private String relacja;
}
