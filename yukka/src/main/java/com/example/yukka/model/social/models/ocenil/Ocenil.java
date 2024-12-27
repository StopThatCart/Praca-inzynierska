package com.example.yukka.model.social.models.ocenil;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Reprezentuje relację oceny.
 * 
 * <ul>
 *   <li><strong>id</strong>: Unikalny identyfikator relacji.</li>
 *   <li><strong>lubi</strong>: Określa, czy użytkownik lubi oceniany obiekt.</li>
 *   <li><strong>oceniany</strong>: Obiekt, który jest oceniany.</li>
 * </ul>
 */
@RelationshipProperties
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ocenil {
    @Id @GeneratedValue
    private Long id;

    @Property("lubi")
    private boolean lubi;

    @TargetNode
    private Oceniany oceniany;

}
