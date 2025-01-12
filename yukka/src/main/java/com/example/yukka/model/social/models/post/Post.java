package com.example.yukka.model.social.models.post;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.social.models.komentarz.Komentarz;
import com.example.yukka.model.social.models.ocenil.Oceniany;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Reprezentuje post.
 * 
 * <ul>
 *   <li><strong>id</strong>: Unikalny identyfikator posta.</li>
 *   <li><strong>uuid</strong>: Identyfikator posta jako uuid.</li>
 *   <li><strong>tytul</strong>: Tytuł posta.</li>
 *   <li><strong>opis</strong>: Opis posta.</li>
 *   <li><strong>obraz</strong>: URL obrazu powiązanego z postem.</li>
 *   <li><strong>dataUtworzenia</strong>: Data utworzenia posta.</li>
 *   <li><strong>autor</strong>: Użytkownik, który stworzył post.</li>
 *   <li><strong>komentarze</strong>: Lista komentarzy powiązanych z postem.</li>
 *   <li><strong>komentarzeWPoscie</strong>: Lista komentarzy zawartych w poście.</li>
 * </ul>
 */
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
    @Property(name = "uuid")
    private String uuid;

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
    private List<Komentarz> komentarze;

    @Relationship(type = "JEST_W_POSCIE", direction = Relationship.Direction.INCOMING)
      private List<Komentarz> komentarzeWPoscie;
}
