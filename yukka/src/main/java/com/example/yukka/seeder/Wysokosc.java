package com.example.yukka.seeder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Klasa reprezentująca zakres wysokości.
 * 
 * <ul>
 *   <li><strong>min</strong> - minimalna wysokość</li>
 *   <li><strong>max</strong> - maksymalna wysokość</li>
 * </ul>
 */
@AllArgsConstructor
@Getter
@Setter
public class Wysokosc {
    private double min;
    private double max;
}
