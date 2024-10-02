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

    @NotNull(message = "Gdzie galeria pokaz?")
    @JsonProperty("galeriaPokaz")
    private boolean galeriaPokaz;

    @NotNull(message = "Gdzie galeria ocena komentarze?")
    @JsonProperty("galeriaOcenaKomentarze")
    private boolean galeriaOcenaKomentarze;

    @NotNull(message = "Gdzie ogrod pokaz?")
    @JsonProperty("ogrodPokaz")
    private boolean ogrodPokaz;

    @NotNull(message = "Gdzie ogrod ocena komentarze?")
    @JsonProperty("ogrodOcenaKomentarze")
    private boolean ogrodOcenaKomentarze;

    @NotNull(message = "Gdzie powiadomienia komentarze odpowiedz?")
    @JsonProperty("powiadomieniaKomentarzeOdpowiedz")
    private boolean powiadomieniaKomentarzeOdpowiedz;

    @NotNull(message = "Gdzie powiadomienia komentarze ogrod?")
    @JsonProperty("powiadomieniaKomentarzeOgrod")
    private boolean powiadomieniaKomentarzeOgrod;

    @NotNull(message = "Gdzie powiadomienia komentarze galeria?")
    @JsonProperty("powiadomieniaKomentarzeGaleria")
    private boolean powiadomieniaKomentarzeGaleria;

    @NotNull(message = "Gdzie powiadomienia ogrod podlewanie?")
    @JsonProperty("powiadomieniaOgrodPodlewanie")
    private boolean powiadomieniaOgrodPodlewanie;

    @NotNull(message = "Gdzie powiadomienia ogrod kwitnienie?")
    @JsonProperty("powiadomieniaOgrodKwitnienie")
    private boolean powiadomieniaOgrodKwitnienie;

    @NotNull(message = "Gdzie powiadomienia ogrod owocowanie?")
    @JsonProperty("powiadomieniaOgrodOwocowanie")
    private boolean powiadomieniaOgrodOwocowanie;
}
