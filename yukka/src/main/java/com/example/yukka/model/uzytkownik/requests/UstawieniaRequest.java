package com.example.yukka.model.uzytkownik.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Klasa reprezentująca żądanie ustawień użytkownika.
 * 
 * <ul>
 * <li><strong>statystykiProfilu</strong>: Wymagane. Określa, czy statystyki profilu są włączone.</li>
 * <li><strong>ogrodPokaz</strong>: Wymagane. Określa, czy pokaz ogrodu jest włączony.</li>
 * <li><strong>powiadomieniaKomentarzeOdpowiedz</strong>: Wymagane. Określa, czy powiadomienia o odpowiedziach na komentarze są włączone.</li>
 * <li><strong>powiadomieniaOgrodKwitnienie</strong>: Wymagane. Określa, czy powiadomienia o kwitnieniu w ogrodzie są włączone.</li>
 * <li><strong>powiadomieniaOgrodOwocowanie</strong>: Wymagane. Określa, czy powiadomienia o owocowaniu w ogrodzie są włączone.</li>
 * </ul>
 */
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
