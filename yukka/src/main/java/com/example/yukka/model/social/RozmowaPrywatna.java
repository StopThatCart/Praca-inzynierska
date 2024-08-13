package com.example.yukka.model.social;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Node
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RozmowaPrywatna {
    @Id @GeneratedValue
    private Long id;

    @Property(name = "emaile")
    private List<String> emaile;

    @CreatedDate
    @Property(name = "data_utworzenia")
    private LocalDateTime dataUtworzenia;

    @Relationship(type = "JEST_W_ROZMOWIE", direction = Relationship.Direction.INCOMING)
    private List<Uzytkownik> uzytkownicy;

    @Relationship(type = "MA_KOMENTARZ", direction = Relationship.Direction.OUTGOING)
    private List<Komentarz> komentarze;

}
