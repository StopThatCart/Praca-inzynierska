package com.example.yukka.model.uzytkownik;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Klasa reprezentująca ustawienia użytkownika.
 * 
 * <ul>
 * <li><strong>id</strong>: Unikalny identyfikator ustawień.</li>
 * <li><strong>statystykiProfilu</strong>: Flaga określająca, czy statystyki profilu są włączone.</li>
 * <li><strong>ogrodPokaz</strong>: Flaga określająca, czy ogród jest pokazany.</li>
 * <li><strong>powiadomieniaKomentarzeOdpowiedz</strong>: Flaga określająca, czy powiadomienia o odpowiedziach na komentarze są włączone.</li>
 * <li><strong>powiadomieniaOgrodKwitnienie</strong>: Flaga określająca, czy powiadomienia o kwitnieniu w ogrodzie są włączone.</li>
 * <li><strong>powiadomieniaOgrodOwocowanie</strong>: Flaga określająca, czy powiadomienia o owocowaniu w ogrodzie są włączone.</li>
 * </ul>
 */
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Node
public class Ustawienia {
    @Id @GeneratedValue
    private Long id;

    @Property("statystykiProfilu")
    @Builder.Default
    private boolean statystykiProfilu = true;

    @Property("ogrodPokaz")
    @Builder.Default
    private boolean ogrodPokaz = true;

    @Property("powiadomieniaKomentarzeOdpowiedz")
    @Builder.Default
    private boolean powiadomieniaKomentarzeOdpowiedz = true;

    @Property("powiadomieniaOgrodKwitnienie")
    @Builder.Default
    private boolean powiadomieniaOgrodKwitnienie = true;

    @Property("powiadomieniaOgrodOwocowanie")
    @Builder.Default
    private boolean powiadomieniaOgrodOwocowanie = true;

}
