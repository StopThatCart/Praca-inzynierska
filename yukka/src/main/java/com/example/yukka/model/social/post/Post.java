package com.example.yukka.model.social.post;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.social.Oceniany;
import com.example.yukka.model.social.Ocenil;
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
public class Post implements Oceniany {
    @Id @GeneratedValue
    private Long id;
    @Property(name = "post_id")
    private String postId;

    @Property(name = "tytul")
    private String tytul;

    @Property(name = "opis")
    private String opis;

    @Property(name = "oceny_lubi")
    private Integer ocenyLubi;

    @Property(name = "oceny_nie_lubi")
    private Integer ocenyNieLubi;
    
    @Property(name = "obraz")
    private String obraz;

    @Property(name = "liczba_komentarzy")
    private Integer liczbaKomentarzy;
    
    @CreatedDate
    @Property(name = "data_utworzenia")
    private LocalDateTime dataUtworzenia;

    @Relationship(type = "MA_POST", direction = Relationship.Direction.INCOMING)
    private Uzytkownik autor;

    @Relationship(type = "OCENIL", direction = Relationship.Direction.INCOMING)
    private List<Ocenil> ocenil;

    @Relationship(type = "MA_KOMENTARZ", direction = Relationship.Direction.OUTGOING)
    private List<Komentarz> komentarze;

}
