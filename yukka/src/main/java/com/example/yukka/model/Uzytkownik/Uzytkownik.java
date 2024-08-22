package com.example.yukka.model.uzytkownik;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.DynamicLabels;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.yukka.authorities.ROLE;
import com.example.yukka.model.ogrod.Ogrod;
import com.example.yukka.model.social.Ocenil;
import com.example.yukka.model.social.komentarz.Komentarz;
import com.example.yukka.model.social.post.Post;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Node
public class Uzytkownik implements UserDetails, Principal{
    @Id @GeneratedValue
    private Long id;

    @Property(name = "uzytId")
    private String uzytId;

    @DynamicLabels
    @Builder.Default
    private List<String> labels = new ArrayList<>();

    @Property("nazwa")
    private String nazwa;

    @Property("email")
    private String email;

    @Property("haslo")
    private String haslo;

    @Property("avatar")
    @Builder.Default
    private String avatar = "default_avatar.png";

    @CreatedDate
    @Property("dataUtworzenia")
    @Builder.Default
    private LocalDateTime dataUtworzenia = LocalDateTime.now();

    @Property("ban")
    @Builder.Default
    private boolean ban = false;

    @Relationship(type = "MA_POST", direction = Relationship.Direction.OUTGOING)
    private List<Post> posty;

    // TODO: Profil

    @Relationship(type = "SKOMENTOWAL", direction = Relationship.Direction.OUTGOING)
    private List<Komentarz> komentarze;
    
    @Relationship(type = "OCENIL", direction = Relationship.Direction.OUTGOING)
    private List<Ocenil> oceny;

    @Relationship(type = "MA_USTAWIENIA", direction = Relationship.Direction.OUTGOING)
    // Daj List jak nie działa
    private MaUstawienia ustawienia;
    
    @Relationship(type = "MA_OGROD", direction = Relationship.Direction.OUTGOING)
    private Ogrod ogrod;

    @Relationship(type = "JEST_W_ROZMOWIE", direction = Relationship.Direction.OUTGOING)
    private Set<RozmowaPrywatna> rozmowyPrywatne;

    @ToString.Exclude
    @Relationship(type = "BLOKUJE", direction = Relationship.Direction.OUTGOING)
    private Set<Uzytkownik> blokowaniUzytkownicy;

    @ToString.Exclude
    @Relationship(type = "JEST_BLOKOWANY_PRZEZ", direction = Relationship.Direction.INCOMING)
    private Set<Uzytkownik> blokujacyUzytkownicy;


    public Uzytkownik(String name, String email, String password) {
        this.nazwa = name;
        this.email = email;
        this.haslo = password;
    }

    public Uzytkownik(String name, String email, String password, String label) {
        this.nazwa = name;
        this.email = email;
        this.haslo = password;
        this.labels = List.of(label);
    }

    @Override
    public String getName() {
        return nazwa;
    }

    @Override
    public String getPassword() {
        return haslo;
    }

    @Override
    public String getUsername() {
        return nazwa;
    }

    public boolean isBan() {
        return ban;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return labels.stream()
                     .map(SimpleGrantedAuthority::new) // Convert each label to a SimpleGrantedAuthority
                     .collect(Collectors.toList());    // Collect into a list
    }

    public boolean isAdmin() {
        return labels.contains(ROLE.Admin.toString());
    }

    public boolean isPracownik() {
        return labels.contains(ROLE.Pracownik.toString());
    }

    public boolean isNormalUzytkownik() {
        return !isAdmin() && !isPracownik();
    }

    /** Sprawdza, czy zalogowany użytkownik ma dostęp do prywatnych treści danego użytkownika. 
     *  Admin ma prawo do wszystkiego, a pracownicy do użytkowników i samych siebie.
     * @param targetUzyt Użytkownik, do którego danych się dostaje
     * @param connectedUser Połączony zalogowany użytkownik
     * @return boolean
    */
    public boolean hasAuthenticationRights(Uzytkownik targetUzyt, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        if(uzyt.isAdmin()){
            return true;
        }else if (uzyt.isPracownik()) {
            return targetUzyt.isNormalUzytkownik() || uzyt.getEmail().equals(targetUzyt.getEmail());
        } else  {
            return uzyt.getEmail().equals(targetUzyt.getEmail());
        }
    }
    
    @Override
    public String toString() {
        return "Uzytkownik{" +
                "id=" + id +
                ", uzytId='" + uzytId + '\'' +
                ", labels=" + labels +
                ", nazwa='" + nazwa + '\'' +
                ", email='" + email + '\'' +
                ", haslo='" + haslo + '\'' +
                ", avatar='" + avatar + '\'' +
                ", dataUtworzenia=" + dataUtworzenia +
                ", ban=" + ban +
        //        ", posty=" + posty +
          //      ", komentarze=" + komentarze +
                ", oceny=" + oceny +
                ", ustawienia=" + ustawienia +
         //       ", rozmowyPrywatne=" + rozmowyPrywatne +
          //      ", blokowaniUzytkownicy=" + blokowaniUzytkownicy +
         //       ", blokujacyUzytkownicy=" + blokujacyUzytkownicy +
                '}';
    }


}
