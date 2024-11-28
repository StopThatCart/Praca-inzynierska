package com.example.yukka.model.social.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UstawieniaRequest {
    @NotNull(message = "Gdzie statystyki profilu?")
    @JsonProperty("statystykiProfilu")
    private boolean statystykiProfilu;

    @NotNull(message = "Gdzie ogrod pokaz?")
    @JsonProperty("ogrodPokaz")
    private boolean ogrodPokaz;

    @NotNull(message = "Gdzie powiadomienia komentarze odpowiedz?")
    @JsonProperty("powiadomieniaKomentarzeOdpowiedz")
    private boolean powiadomieniaKomentarzeOdpowiedz;

    @NotNull(message = "Gdzie powiadomienia ogrod kwitnienie?")
    @JsonProperty("powiadomieniaOgrodKwitnienie")
    private boolean powiadomieniaOgrodKwitnienie;

    @NotNull(message = "Gdzie powiadomienia ogrod owocowanie?")
    @JsonProperty("powiadomieniaOgrodOwocowanie")
    private boolean powiadomieniaOgrodOwocowanie;
}
