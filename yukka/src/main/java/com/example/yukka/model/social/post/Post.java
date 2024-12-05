package com.example.yukka.model.social.post;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.models.Oceniany;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Node
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends Oceniany {
    @Id @GeneratedValue
    private Long id;
    @Property(name = "postId")
    private String postId;

    @Property(name = "tytul")
    private String tytul;

    @Property(name = "opis")
    private String opis;

    @Property(name = "obraz")
    private String obraz;
    
    @CreatedDate
    @Property(name = "dataUtworzenia")
    private LocalDateTime dataUtworzenia;

    @Relationship(type = "MA_POST", direction = Relationship.Direction.INCOMING)
    private Uzytkownik autor;
    
    @Relationship(type = "MA_KOMENTARZ", direction = Relationship.Direction.OUTGOING)
  //  @JsonBackReference
    private List<Komentarz> komentarze;

    @Relationship(type = "JEST_W_POSCIE", direction = Relationship.Direction.INCOMING)
    //  @JsonBackReference
      private List<Komentarz> komentarzeWPoscie;

}
