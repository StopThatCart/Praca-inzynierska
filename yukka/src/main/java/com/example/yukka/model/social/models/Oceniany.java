package com.example.yukka.model.social.models;

import java.util.List;

import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



/**
 * Klasa <strong>Oceniany</strong> reprezentuje obiekt oceniany. Dziedziczą je obiekty Post i Komentarz.
 * 
 * <ul>
 * <li><strong>ocenil</strong> - lista obiektów typu <strong>OcenilReverse</strong> reprezentujących użytkowników którzy to ocenili.</li>
 * </ul>
 * 
 * Metody:
 * <ul>
 * <li><strong>getOcenyLubi</strong> - zwraca liczbę ocen pozytywnych.</li>
 * <li><strong>getOcenyNieLubi</strong> - zwraca liczbę ocen negatywnych.</li>
 * </ul>
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Oceniany {

    @Relationship(type = "OCENIL", direction = Relationship.Direction.INCOMING)
    private List<OcenilReverse> ocenil;

    
    /** 
     * @return int
     */
    public int getOcenyLubi() {
        return (int) ocenil.stream().filter(OcenilReverse::isLubi).count();
    }

    public int getOcenyNieLubi() {
        return (int) ocenil.stream().filter(ocenil -> !ocenil.isLubi()).count();
    }
}
