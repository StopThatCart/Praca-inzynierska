package com.example.yukka.model.dzialka;

import java.util.Set;

import com.example.yukka.model.roslina.RoslinaResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Reprezentuje odpowiedź zawierającą informacje o zasadzonej roślinie.
 * 
 * <ul>
 *   <li><strong>roslina</strong>: Obiekt RoslinaResponse zawierający informacje o roślinie.</li>
 *   <li><strong>x</strong>: Współrzędna x położenia rośliny.</li>
 *   <li><strong>y</strong>: Współrzędna y położenia rośliny.</li>
 *   <li><strong>tabX</strong>: Tablica współrzędnych x dla dodatkowych pozycji.</li>
 *   <li><strong>tabY</strong>: Tablica współrzędnych y dla dodatkowych pozycji.</li>
 *   <li><strong>pozycje</strong>: Zbiór obiektów Pozycja reprezentujących dodatkowe pozycje rośliny.</li>
 *   <li><strong>kolor</strong>: Kolor rośliny.</li>
 *   <li><strong>tekstura</strong>: Tekstura rośliny w postaci tablicy bajtów.</li>
 *   <li><strong>wyswietlanie</strong>: Informacje o sposobie wyświetlania rośliny.</li>
 *   <li><strong>notatka</strong>: Dodatkowa notatka dotycząca rośliny.</li>
 *   <li><strong>obraz</strong>: Obraz rośliny w postaci tablicy bajtów.</li>
 * </ul>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZasadzonaRoslinaResponse {

    private RoslinaResponse roslina;
    private int x;
    private int y;

    private int[] tabX;
    private int[] tabY;
    private Set<Pozycja> pozycje; 

    private String kolor;
    private byte[] tekstura;
    private String wyswietlanie;

    private String notatka;
    
    private byte[] obraz;


 
}
