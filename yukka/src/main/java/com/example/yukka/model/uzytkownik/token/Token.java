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
