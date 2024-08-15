package com.example.yukka.model.social.komentarz;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.social.Oceniany;
import com.example.yukka.model.social.post.Post;
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
public class Komentarz extends Oceniany {
    @Id @GeneratedValue
    private Long id;
    @Property(name = "komentarzId")
    private String komentarzId;

    @Property(name = "opis")
    private String opis;
    
    @Property(name = "obraz")
    private String obraz;
    
    @CreatedDate
    @Property(name = "dataUtworzenia")
    private LocalDateTime dataUtworzenia;

    // TODO: dodaj podobny obiekt w wiadomościach prywatnych
    @Relationship(type = "MA_KOMENTARZ", direction = Relationship.Direction.INCOMING)
    private Post posty;

    @Relationship(type = "ODPOWIEDZIAL", direction = Relationship.Direction.OUTGOING)
    private Komentarz odpowiadaKomentarzowi;

    @Relationship(type = "ODPOWIEDZIAL", direction = Relationship.Direction.INCOMING)
    private List<Komentarz> odpowiedzi;

    @Relationship(type = "SKOMENTOWAL", direction = Relationship.Direction.INCOMING)
    private Uzytkownik uzytkownik;

}
