package com.example.yukka.model.post;

import java.time.LocalDateTime;
import java.util.List;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Komentarz implements Oceniany {
    @Id @GeneratedValue
    private Long id;
    @Property(name = "komentarz_id")
    private String komentarzId;

    @Property(name = "opis")
    private String opis;

    @Property(name = "oceny_lubi")
    private Integer ocenyLubi;

    @Property(name = "oceny_nie_lubi")
    private Integer ocenyNieLubi;
    
    @Property(name = "obraz")
    private String obraz;
    
    @CreatedDate
    @Property(name = "data_utworzenia")
    private LocalDateTime dataUtworzenia;

    @Relationship(type = "OCENIL", direction = Relationship.Direction.INCOMING)
    private List<Ocenil> ocenil;

    @Relationship(type = "MA_KOMENTARZ", direction = Relationship.Direction.INCOMING)
    private List<Post> posty;

    @Relationship(type = "ODPOWIEDZIAL", direction = Relationship.Direction.OUTGOING)
    private List<Komentarz> komentarze;

    @Relationship(type = "ODPOWIEDZIAL", direction = Relationship.Direction.INCOMING)
    private List<Komentarz> odpowiedzi;

    @Relationship(type = "SKOMENTOWAL", direction = Relationship.Direction.INCOMING)
    private Uzytkownik uzytkownik;

}
