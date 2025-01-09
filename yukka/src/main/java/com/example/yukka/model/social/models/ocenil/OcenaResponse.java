package com.example.yukka.model.social.models.ocenil;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Klasa reprezentująca odpowiedź z ocenami.
 * 
 * <p>OcenaResponse zawiera informacje o liczbie pozytywnych i negatywnych ocen.</p>
 * 
 * <ul>
 *   <li>{@code ocenyLubi} - liczba pozytywnych ocen</li>
 *   <li>{@code ocenyNieLubi} - liczba negatywnych ocen</li>
 * </ul>
 */
@Builder
@Getter
@Setter
@ToString
public class OcenaResponse {
    private Integer ocenyLubi;
    private Integer ocenyNieLubi;
}
