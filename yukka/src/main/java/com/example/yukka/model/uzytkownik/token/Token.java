package com.example.yukka.model.uzytkownik.token;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Reprezentuje token używany do walidacji użytkownika.
 * 
 * <ul>
 *   <li><strong>id</strong>: Unikalny identyfikator tokena.</li>
 *   <li><strong>token</strong>: Wartość tokena.</li>
 *   <li><strong>typ</strong>: Typ tokena.</li>
 *   <li><strong>nowyEmail</strong>: Nowy adres email powiązany z tokenem.</li>
 *   <li><strong>dataUtworzenia</strong>: Data utworzenia tokena.</li>
 *   <li><strong>dataWygasniecia</strong>: Data wygaśnięcia tokena.</li>
 *   <li><strong>dataWalidacji</strong>: Data walidacji tokena.</li>
 *   <li><strong>uzytkownik</strong>: Użytkownik powiązany z tokenem.</li>
 * </ul>
 */
@Node
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id @GeneratedValue
    private Long id;

    @Property("token")
    private String token;

    @Property("typ")
    private String typ;

    @Property("nowyEmail")
    private String nowyEmail;

    @CreatedDate
    @Property("dataUtworzenia")
    @Builder.Default
    private LocalDateTime dataUtworzenia = LocalDateTime.now();

    @Property("dataWygasniecia")
    @Builder.Default
    private LocalDateTime dataWygasniecia = LocalDateTime.now().plusMinutes(15);

    @Property("dataWalidacji")
    private LocalDateTime dataWalidacji;

    @Relationship(type = "WALIDUJE", direction = Relationship.Direction.OUTGOING)
    private Uzytkownik uzytkownik;
}
