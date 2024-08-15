package com.example.yukka.model.roslina;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoslinaResponse {
    private Long id;
    private String nazwa;
    private String nazwaLacinska;
    private String opis;
    private double wysokoscMin;
    private double wysokoscMax;
    private byte[] obraz;
}
