package com.example.yukka.model.User;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.neo4j.core.schema.DynamicLabels;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@Entity
@Node
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal{
    @Id @GeneratedValue
    private Long id;

    @DynamicLabels
    private List<String> labels;

    @Property("nazwa")
    private String name;

    @Property("email")
    private String email;

    @Property("haslo")
    private String password;

    @CreatedDate
    @Property("data_utworzenia")
    private LocalDateTime createdDate;

    @Property("zbanowany")
    private boolean banned;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    public boolean isBanned() {
        return banned;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return labels.stream()
                     .map(SimpleGrantedAuthority::new) // Convert each label to a SimpleGrantedAuthority
                     .collect(Collectors.toList());    // Collect into a list
    }



}
