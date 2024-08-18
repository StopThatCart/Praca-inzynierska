package com.example.yukka.model.social.komentarz;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.social.Oceniany;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;
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

    @Property(name = "edytowany")
    private boolean edytowany;
    
    @CreatedDate
    @Property(name = "dataUtworzenia")
    @Builder.Default
    private LocalDateTime dataUtworzenia = LocalDateTime.now();
    @Relationship(type = "MA_KOMENTARZ", direction = Relationship.Direction.INCOMING)
    // @JsonBackReference
     @ToString.Exclude
     private RozmowaPrywatna rozmowaPrywatna;

    @Relationship(type = "MA_KOMENTARZ", direction = Relationship.Direction.INCOMING)
   // @JsonBackReference
    @ToString.Exclude
    private Post post;

    @Relationship(type = "ODPOWIEDZIAL", direction = Relationship.Direction.OUTGOING)
  //  @JsonManagedReference
    @ToString.Exclude
    private Komentarz odpowiadaKomentarzowi;

    @Relationship(type = "ODPOWIEDZIAL", direction = Relationship.Direction.INCOMING)
 //   @JsonManagedReference
    @ToString.Exclude
    private List<Komentarz> odpowiedzi;

    @Relationship(type = "SKOMENTOWAL", direction = Relationship.Direction.INCOMING)
    private Uzytkownik uzytkownik;

    @Override
    public String toString() {
        return "Komentarz{" +
                "id=" + id +
                ", komentarzId='" + komentarzId + '\'' +
                ", opis='" + opis + '\'' +
                ", edytowany=" + edytowany +
                ", dataUtworzenia=" + dataUtworzenia +
                ", postId=" + (post != null ? post.getPostId() : "null") +
                ", rozmowaPrywatna=" + (rozmowaPrywatna != null ? rozmowaPrywatna.getUzytkownicy().stream().map(Uzytkownik::getNazwa).collect(Collectors.joining(", ")) : "null") +
                ", odpowiedziCount=" + (odpowiedzi != null ? odpowiedzi.size() : 0) +
                '}';
    }

}
