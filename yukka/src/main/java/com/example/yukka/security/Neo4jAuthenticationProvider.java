package com.example.yukka.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class Neo4jAuthenticationProvider implements AuthenticationProvider {

    private final Driver driver;

    public Neo4jAuthenticationProvider(Driver driver) {
        this.driver = driver;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
        String name = authentication.getName();
        String email = "jan123";
        String password = authentication.getCredentials().toString();
        try (Session session = driver.session()) {
            List<Record> results = session.run("MATCH (n:Uzytkownik) WHERE n.email = $email RETURN n",
                    Map.of("email", email)).list();

            if (results.isEmpty()) {
                return null;
            }

            Node user = results.get(0).get("n").asNode();
            // Possible to add more information from user
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            final UserDetails principal = new User(name, password, authorities);

            return new UsernamePasswordAuthenticationToken(principal, password, authorities);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
