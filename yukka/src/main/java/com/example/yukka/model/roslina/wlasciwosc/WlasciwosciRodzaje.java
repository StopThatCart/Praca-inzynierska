package com.example.yukka.model.roslina.wlasciwosc;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Klasa reprezentująca rodzaje właściwości roślin.
 * 
 * <ul>
 *   <li><strong>etykieta</strong> - Etykieta właściwości.</li>
 *   <li><strong>nazwy</strong> - Zbiór nazw właściwości.</li>
 * </ul>
 */
@Getter
@Setter
@Builder
@ToString
public class WlasciwosciRodzaje {
    private String etykieta;
    private Set<String> nazwy;
}