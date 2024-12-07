package com.example.yukka.model.social.powiadomienie;

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
import lombok.Setter;


/**
 * Reprezentuje relację powiadomienia.
 * 
 * <ul>
 *   <li><strong>id</strong>: Unikalny identyfikator powiadomienia.</li>
 *   <li><strong>przeczytane</strong>: Flaga oznaczająca, czy powiadomienie zostało przeczytane.</li>
 *   <li><strong>oceniany</strong>: Użytkownik, który jest oceniany w powiadomieniu.</li>
 * </ul>
 */
@RelationshipProperties
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Powiadamia {
    @Id @GeneratedValue
    private Long id;

    @Property("przeczytane")
    private Boolean przeczytane;

    @TargetNode
    private Uzytkownik oceniany;
}
