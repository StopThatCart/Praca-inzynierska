package com.example.yukka.model.dzialka.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum określający sposób wyświetlania rośliny na działce
 * 
 * <p>TEKSTURA - wyświetlanie tekstury</p> 
 * <p>KOLOR - wyświetlanie koloru</p>
 * <p>TEKSTURA_KOLOR - wyświetlanie tekstury i koloru</p>
 * 
 *
 */
@Getter
@RequiredArgsConstructor
public enum Wyswietlanie {
    TEKSTURA("TEKSTURA"),
    KOLOR("KOLOR"),
    TEKSTURA_KOLOR("TEKSTURA_KOLOR");

    private final String property;
}
