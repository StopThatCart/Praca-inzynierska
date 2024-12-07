package com.example.yukka.model.dzialka;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.PostLoad;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Klasa reprezentująca relację "ZasadzonaNa" w modelu danych.
 * 
 * <ul>
 * <li><strong>id</strong>: Unikalny identyfikator relacji.</li>
 * <li><strong>x</strong>: Współrzędna x.</li>
 * <li><strong>y</strong>: Współrzędna y.</li>
 * <li><strong>kolor</strong>: Kolor obiektu.</li>
 * <li><strong>tekstura</strong>: Tekstura obiektu.</li>
 * <li><strong>wyswietlanie</strong>: Sposób wyświetlania obiektu.</li>
 * <li><strong>notatka</strong>: Dodatkowa notatka dotycząca obiektu.</li>
 * <li><strong>obraz</strong>: Ścieżka do obrazu obiektu.</li>
 * <li><strong>tabX</strong>: Tablica współrzędnych x.</li>
 * <li><strong>tabY</strong>: Tablica współrzędnych y.</li>
 * <li><strong>dzialka</strong>: Obiekt klasy Dzialka, do którego odnosi się relacja.</li>
 * <li><strong>pozycje</strong>: Zbiór obiektów klasy Pozycja, reprezentujących pozycje na działce.</li>
 * </ul>
 * 
 * Metoda <strong>initPozycje</strong> jest wywoływana po załadowaniu obiektu i inicjalizuje zbiór pozycje na podstawie tablic tabX i tabY.
 */
@RelationshipProperties
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZasadzonaNa {
    @RelationshipId 
    @GeneratedValue
    private Long id;

    @Property("x")
    private int x;

    @Property("y")
    private int y;

    @Property("kolor")
    private String kolor;

    @Property("tekstura")
    private String tekstura;

    @Property("wyswietlanie")
    private String wyswietlanie;
    
    @Property("notatka")
    private String notatka;
    
    @Property("obraz")
    private String obraz;

    @Property("tabX")
    private int[] tabX;

    @Property("tabY")
    private int[] tabY;
    
    @JsonIgnore
    @TargetNode
    private Dzialka dzialka;

    @JsonIgnore
    private Set<Pozycja> pozycje;

    @PostLoad
    private void initPozycje() {
        pozycje = new HashSet<>();
        for (int i = 0; i < tabX.length; i++) {
            pozycje.add(new Pozycja(tabX[i], tabY[i]));
        }
    }

}
