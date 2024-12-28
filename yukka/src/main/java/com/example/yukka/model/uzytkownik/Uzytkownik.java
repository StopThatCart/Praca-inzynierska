package com.example.yukka.model.uzytkownik;

import java.security.Principal;
import java.time.LocalDate;
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

import com.example.yukka.auth.authorities.ROLE;
import com.example.yukka.model.ogrod.Ogrod;
import com.example.yukka.model.social.models.komentarz.Komentarz;
import com.example.yukka.model.social.models.ocenil.Ocenil;
import com.example.yukka.model.social.models.post.Post;
import com.example.yukka.model.social.models.rozmowaPrywatna.RozmowaPrywatna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Klasa reprezentująca użytkownika w systemie.
 * Implementuje interfejsy <strong>UserDetails</strong> oraz <strong>Principal</strong>.
 * 
 * <ul>
 * <li><strong>id</strong>: Unikalny identyfikator użytkownika.</li>
 * <li><strong>uzytId</strong>: Identyfikator użytkownika.</li>
 * <li><strong>labels</strong>: Lista etykiet przypisanych do użytkownika.</li>
 * <li><strong>nazwa</strong>: Nazwa użytkownika.</li>
 * <li><strong>email</strong>: Adres email użytkownika.</li>
 * <li><strong>haslo</strong>: Hasło użytkownika.</li>
 * <li><strong>avatar</strong>: Ścieżka do awatara użytkownika.</li>
 * <li><strong>dataUtworzenia</strong>: Data utworzenia konta użytkownika.</li>
 * <li><strong>aktywowany</strong>: Status aktywacji konta użytkownika.</li>
 * <li><strong>ban</strong>: Status bana użytkownika.</li>
 * <li><strong>banDo</strong>: Data wygaśnięcia bana.</li>
 * <li><strong>imie</strong>: Imię użytkownika.</li>
 * <li><strong>miasto</strong>: Miasto użytkownika.</li>
 * <li><strong>miejsceZamieszkania</strong>: Miejsce zamieszkania użytkownika.</li>
 * <li><strong>opis</strong>: Opis użytkownika.</li>
 * <li><strong>posty</strong>: Lista postów użytkownika.</li>
 * <li><strong>komentarze</strong>: Lista komentarzy użytkownika.</li>
 * <li><strong>oceny</strong>: Lista ocen użytkownika.</li>
 * <li><strong>ustawienia</strong>: Ustawienia użytkownika.</li>
 * <li><strong>ogrod</strong>: Ogród użytkownika.</li>
 * <li><strong>rozmowyPrywatne</strong>: Zestaw prywatnych rozmów użytkownika.</li>
 * <li><strong>blokowaniUzytkownicy</strong>: Zestaw użytkowników zablokowanych przez tego użytkownika.</li>
 * <li><strong>blokujacyUzytkownicy</strong>: Zestaw użytkowników, którzy zablokowali tego użytkownika.</li>
 * </ul>
 * 
 * Klasa zawiera również metody do sprawdzania ról użytkownika oraz uprawnień do dostępu do prywatnych treści.
 */
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

    @Property("aktywowany")
    @Builder.Default
    private boolean aktywowany = false;

    @Property("ban")
    @Builder.Default
    private boolean ban = false;

    @Property("banDo")
    private LocalDate banDo;

    @Property("imie")
    private String imie;

    @Property("miasto")
    private String miasto;

    @Property("miejsceZamieszkania")
    private String miejsceZamieszkania;

    @Property("opis")
    private String opis;

    @Relationship(type = "MA_POST", direction = Relationship.Direction.OUTGOING)
    private List<Post> posty;


    @Relationship(type = "SKOMENTOWAL", direction = Relationship.Direction.OUTGOING)
    private List<Komentarz> komentarze;
    
    @Relationship(type = "OCENIL", direction = Relationship.Direction.OUTGOING)
    private List<Ocenil> oceny;

    @Relationship(type = "MA_USTAWIENIA", direction = Relationship.Direction.OUTGOING)
    // Daj List jak nie działa
    private Ustawienia ustawienia;
    
    @Relationship(type = "MA_OGROD", direction = Relationship.Direction.OUTGOING)
    private Ogrod ogrod;

    @Relationship(type = "JEST_W_ROZMOWIE", direction = Relationship.Direction.OUTGOING)
    private Set<RozmowaPrywatna> rozmowyPrywatne;

    @ToString.Exclude
    @Relationship(type = "BLOKUJE", direction = Relationship.Direction.OUTGOING)
    private Set<Uzytkownik> blokowaniUzytkownicy;

    @ToString.Exclude
    @Relationship(type = "BLOKUJE", direction = Relationship.Direction.INCOMING)
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

    
    /** 
     * @return String
     */
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
        if (connectedUser == null) {
            throw new IllegalArgumentException("Użytkownik nie jest zalogowany");
        }
        Uzytkownik uzyt = (Uzytkownik) connectedUser.getPrincipal();

        if(uzyt.isAdmin()){
            return true;
        }else if (uzyt.isPracownik()) {
            return targetUzyt.isNormalUzytkownik() || uzyt.getEmail().equals(targetUzyt.getEmail());
        } else  {
            return uzyt.getEmail().equals(targetUzyt.getEmail());
        }
    }

    public boolean hasAuthenticationRights(Uzytkownik targetUzyt, Uzytkownik uzyt) {
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
