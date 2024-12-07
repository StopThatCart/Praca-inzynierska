package com.example.yukka.model.uzytkownik;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import lombok.Builder;
import lombok.Getter;

/**
 * Klasa reprezentująca relację między użytkownikiem a jego ustawieniami.
 * 
 * <ul>
 *   <li><strong>id</strong>: Unikalny identyfikator relacji.</li>
 *   <li><strong>ustawienia</strong>: Obiekt ustawień powiązany z użytkownikiem.</li>
 * </ul>
 */
@RelationshipProperties
@Getter
@Builder
public class MaUstawienia {
    @Id @GeneratedValue
    private Long id;

    @TargetNode
    private Ustawienia ustawienia;

}
