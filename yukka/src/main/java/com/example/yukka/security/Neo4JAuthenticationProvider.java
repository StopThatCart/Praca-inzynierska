package com.example.yukka.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
//@Configuration
public class Neo4JAuthenticationProvider implements AuthenticationProvider {
   // private final Driver driver;

  //  @Value("${spring.data.neo4j.database}")
  //  private String dbName;
    
    private final UzytkownikRepository uzytkownikRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String nameOrEmail = authentication.getName();
        String haslo = authentication.getCredentials().toString();

        Uzytkownik uzyt = uzytkownikRepository.findByNameOrEmail(nameOrEmail).get();
        if(uzyt == null){
            throw new BadCredentialsException("Niepoprawny login lub hasło.");
        }

        if(passwordEncoder.matches(haslo, uzyt.getHaslo())) {
            final UserDetails principal = uzyt;
            return new UsernamePasswordAuthenticationToken(principal, haslo, uzyt.getAuthorities());
        } else {
            throw new BadCredentialsException("Niepoprawny login lub hasło.");
        }

        // Possible to add more information from user
       // List<GrantedAuthority> authorities = new ArrayList<>();
        //.add();
       
        //return new UsernamePasswordAuthenticationToken(principal, haslo, uzyt.getAuthorities());
/* 
        try (Session session = driver.session(sessionConfig)) {
            if (nameOrEmail.contains("@")){
                query = "MATCH (n:Uzytkownik) WHERE n.email = $nameOrEmail AND n.haslo = $haslo RETURN n";
            }else {
                query = "MATCH (n:Uzytkownik) WHERE n.nazwa = $nameOrEmail AND n.haslo = $haslo RETURN n";
            }

            List<Record> results = session.run(query,
                    Map.of("nameOrEmail", nameOrEmail, "haslo", haslo)).list();
            
            if (results.isEmpty()) {
                return null;
            }

            Node user = results.get(0).get("n").asNode();

            System.out.println("\n\n\n USEEEEERR: " + user.toString() + "\n\n\n");

            // Possible to add more information from user
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            final UserDetails principal = new User(nameOrEmail, haslo, authorities);

            return new UsernamePasswordAuthenticationToken(principal, haslo, authorities);
        }
            */
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //return false;
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
