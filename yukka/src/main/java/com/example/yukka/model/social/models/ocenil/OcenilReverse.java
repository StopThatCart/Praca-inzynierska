package com.example.yukka.model.social.models.ocenil;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Klasa reprezentująca relację ocenienia w odwrotnym kierunku.
 * 
 * <ul>
 *   <li><strong>id</strong>: Unikalny identyfikator relacji.</li>
 *   <li><strong>lubi</strong>: Wartość logiczna określająca, czy użytkownik lubi dany element.</li>
 *   <li><strong>uzytkownik</strong>: Obiekt reprezentujący użytkownika, który ocenił.</li>
 * </ul>
 */
@RelationshipProperties
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OcenilReverse {
    @Id @GeneratedValue
    private Long id;

    @Property("lubi")
    private boolean lubi;

    @TargetNode
    private Uzytkownik uzytkownik;
}
