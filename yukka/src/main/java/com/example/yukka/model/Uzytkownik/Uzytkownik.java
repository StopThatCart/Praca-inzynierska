package com.example.yukka.model.uzytkownik;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.DynamicLabels;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.yukka.authorities.ROLE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Entity
@Node
//@EntityListeners(AuditingEntityListener.class)
public class Uzytkownik implements UserDetails, Principal{
    @Id @GeneratedValue
    private Long id;

    @DynamicLabels
    @Builder.Default
    private List<String> labels = new ArrayList<>();

    @Property("nazwa")
    private String nazwa;

    @Property("email")
    private String email;

    @Property("haslo")
    private String haslo;

    @CreatedDate
    @Property("data_utworzenia")
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();

    @Property("ban")
    @Builder.Default
    private boolean ban = false;

    @Relationship(type = "MA_USTAWIENIA", direction = Relationship.Direction.OUTGOING)
    // Daj List jak nie działa
    private MaUstawienia ustawienia;


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

    @Override
    public String toString() {
        return "Uzytkownik [id=" + id + ", labels=" + labels + ", nazwa=" + nazwa + ", email=" + email + ", haslo="
                + haslo + ", createdDate=" + createdDate + ", banned=" + ban + "]";
    }


}
