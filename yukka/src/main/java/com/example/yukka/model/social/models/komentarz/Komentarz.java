package com.example.yukka.model.social.models.komentarz;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.social.models.ocenil.Oceniany;
import com.example.yukka.model.social.models.post.Post;
import com.example.yukka.model.social.models.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.uzytkownik.Uzytkownik;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Klasa reprezentująca komentarz.
 * <ul>
 *   <li><strong>id</strong>: Unikalny identyfikator komentarza.</li>
 *   <li><strong>uuid</strong>: Identyfikator komentarza jako String.</li>
 *   <li><strong>opis</strong>: Treść komentarza.</li>
 *   <li><strong>obraz</strong>: Ścieżka do obrazu związanego z komentarzem.</li>
 *   <li><strong>edytowany</strong>: Flaga oznaczająca, czy komentarz został edytowany.</li>
 *   <li><strong>dataUtworzenia</strong>: Data utworzenia komentarza.</li>
 *   <li><strong>rozmowaPrywatna</strong>: Relacja do prywatnej rozmowy, w której znajduje się komentarz.</li>
 *   <li><strong>wPoscie</strong>: Relacja do posta, w którym znajduje się komentarz.</li>
 *   <li><strong>post</strong>: Relacja do posta, do którego odnosi się komentarz.</li>
 *   <li><strong>odpowiadaKomentarzowi</strong>: Relacja do komentarza, na który odpowiada ten komentarz.</li>
 *   <li><strong>odpowiedzi</strong>: Lista odpowiedzi na ten komentarz.</li>
 *   <li><strong>uzytkownik</strong>: Użytkownik, który dodał komentarz.</li>
 * </ul>
 */
@Node
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Komentarz extends Oceniany {
    @Id @GeneratedValue
    private Long id;
    @Property(name = "uuid")
    private String uuid;

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
    @Relationship(type = "MA_WIADOMOSC", direction = Relationship.Direction.INCOMING)
    // @JsonBackReference
     @ToString.Exclude
     private RozmowaPrywatna rozmowaPrywatna;


    @Relationship(type = "JEST_W_POSCIE", direction = Relationship.Direction.OUTGOING)
    // @JsonBackReference
    @ToString.Exclude
    private Post wPoscie;

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
                ", uuid='" + uuid + '\'' +
                ", opis='" + opis + '\'' +
                ", polubienia='" + getOcenyLubi() + '\'' +
                ", niepolubienia='" + getOcenyNieLubi() + '\'' +
                ", edytowany=" + edytowany +
                ", dataUtworzenia=" + dataUtworzenia +
                ", postUUID=" + (post != null ? post.getUuid() : "null") +
                ", rozmowaPrywatna=" + (rozmowaPrywatna != null ? rozmowaPrywatna.getUzytkownicy().stream().map(Uzytkownik::getNazwa).collect(Collectors.joining(", ")) : "null") +
                ", odpowiedziCount=" + (odpowiedzi != null ? odpowiedzi.size() : 0) +
                '}';
    }
}
