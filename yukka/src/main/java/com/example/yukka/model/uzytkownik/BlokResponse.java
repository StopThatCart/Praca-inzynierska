package com.example.yukka.model.uzytkownik;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BlokResponse {
    private Boolean blok;
    private String blokujacy;
    private Boolean obojeBlokujacy;
}
