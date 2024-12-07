package com.example.yukka.model.roslina;

import java.util.Set;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Reprezentuje odpowiedź zawierającą informacje o roślinie.
 * 
 * <ul>
 * <li><strong>id</strong>: Unikalny identyfikator rośliny.</li>
 * <li><strong>roslinaId</strong>: Identyfikator rośliny.</li>
 * <li><strong>labels</strong>: Lista etykiet przypisanych do rośliny.</li>
 * <li><strong>nazwa</strong>: Nazwa rośliny.</li>
 * <li><strong>nazwaLacinska</strong>: Łacińska nazwa rośliny.</li>
 * <li><strong>opis</strong>: Opis rośliny.</li>
 * <li><strong>wysokoscMin</strong>: Minimalna wysokość rośliny.</li>
 * <li><strong>wysokoscMax</strong>: Maksymalna wysokość rośliny.</li>
 * <li><strong>obraz</strong>: Obraz rośliny w formie tablicy bajtów.</li>
 * <li><strong>autor</strong>: Autor opisu rośliny.</li>
 * <li><strong>roslinaUzytkownika</strong>: Informacja, czy roślina należy do użytkownika.</li>
 * <li><strong>grupy</strong>: Zestaw grup, do których należy roślina.</li>
 * <li><strong>formy</strong>: Zestaw form rośliny.</li>
 * <li><strong>gleby</strong>: Zestaw typów gleb, na których roślina może rosnąć.</li>
 * <li><strong>koloryLisci</strong>: Zestaw kolorów liści rośliny.</li>
 * <li><strong>koloryKwiatow</strong>: Zestaw kolorów kwiatów rośliny.</li>
 * <li><strong>kwiaty</strong>: Zestaw cech kwiatów rośliny.</li>
 * <li><strong>odczyny</strong>: Zestaw odczynów gleby odpowiednich dla rośliny.</li>
 * <li><strong>okresyKwitnienia</strong>: Zestaw okresów kwitnienia rośliny.</li>
 * <li><strong>okresyOwocowania</strong>: Zestaw okresów owocowania rośliny.</li>
 * <li><strong>owoce</strong>: Zestaw cech owoców rośliny.</li>
 * <li><strong>podgrupa</strong>: Zestaw podgrup, do których należy roślina.</li>
 * <li><strong>pokroje</strong>: Zestaw pokrojów rośliny.</li>
 * <li><strong>silyWzrostu</strong>: Zestaw sił wzrostu rośliny.</li>
 * <li><strong>stanowiska</strong>: Zestaw stanowisk odpowiednich dla rośliny.</li>
 * <li><strong>walory</strong>: Zestaw walorów rośliny.</li>
 * <li><strong>wilgotnosci</strong>: Zestaw poziomów wilgotności odpowiednich dla rośliny.</li>
 * <li><strong>zastosowania</strong>: Zestaw zastosowań rośliny.</li>
 * <li><strong>zimozielonosci</strong>: Zestaw cech zimozieloności rośliny.</li>
 * </ul>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoslinaResponse {
    private Long id;
    private String roslinaId;

    private List<String> labels;

    private String nazwa;
    private String nazwaLacinska;

    

    private String opis;
    private double wysokoscMin;
    private double wysokoscMax;
    private byte[] obraz;

    private String autor;
    private boolean roslinaUzytkownika;
    
    private Set<String> grupy;
    private Set<String> formy;
    private Set<String> gleby;
    private Set<String> koloryLisci;
    private Set<String> koloryKwiatow;
    private Set<String> kwiaty;
    private Set<String> odczyny;
    private Set<String> okresyKwitnienia;
    private Set<String> okresyOwocowania;
    private Set<String> owoce;
    private Set<String> podgrupa;
    private Set<String> pokroje;
    private Set<String> silyWzrostu;
    private Set<String> stanowiska;
    private Set<String> walory;
    private Set<String> wilgotnosci;
    private Set<String> zastosowania;
    private Set<String> zimozielonosci;
}
