package com.example.yukka.model.dzialka;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Klasa reprezentująca pozycję na płaszczyźnie z współrzędnymi x i y.
 * 
 * <ul>
 *   <li><strong>x</strong> - współrzędna x</li>
 *   <li><strong>y</strong> - współrzędna y</li>
 * </ul>
 * 
 * Klasa zawiera metody do porównywania obiektów oraz generowania hash kodu.
 * 
 * <ul>
 *   <li><strong>equals</strong> - porównuje obiekty na podstawie współrzędnych x i y</li>
 *   <li><strong>hashCode</strong> - generuje hash kod na podstawie współrzędnych x i y</li>
 * </ul>
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pozycja {
    int x;
    int y;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pozycja pozycja = (Pozycja) o;
        return x == pozycja.x && y == pozycja.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    
}
