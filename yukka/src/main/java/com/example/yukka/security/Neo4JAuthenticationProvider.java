package com.example.yukka.security;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.yukka.handler.exceptions.BannedUzytkownikException;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
//@Configuration
public class Neo4JAuthenticationProvider implements AuthenticationProvider {
   // private final Driver driver;

  //  @Value("${spring.data.neo4j.database}")
  //  private String dbName;
    
    private final UzytkownikRepository uzytkownikRepository;
    private final PasswordEncoder passwordEncoder;

    
    /** 
     * @param authentication
     * @return Authentication
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String nameOrEmail = authentication.getName();
        String haslo = authentication.getCredentials().toString();
        System.out.println("nazwa: " + nameOrEmail);
        Optional<Uzytkownik> uzytOpt = uzytkownikRepository.findByNameOrEmail(nameOrEmail);

        if (uzytOpt.isEmpty()) {
            System.out.println("Nie ma takiego uzytkownika");
            throw new BadCredentialsException("Niepoprawny login lub hasło.");
        }

        Uzytkownik uzyt = uzytOpt.get();

        if (!uzyt.isAktywowany()) {
                log.info("login [" + nameOrEmail + "] nieudany: konto nieaktywowane");
                throw new IllegalArgumentException("Konto nie zostało jeszcze aktywowane. Sprawdź swoje wiadomości w poczcie email albo zarejestruj się ponownie.");
        } else if (uzyt.isBan()) {
            log.info("login [" + nameOrEmail + "] nieudany: konto zbanowane");
            throw new BannedUzytkownikException("Konto zostało zbanowane do " + uzyt.getBanDo());
        }

        if(passwordEncoder.matches(haslo, uzyt.getHaslo())) {
            log.info("login [" + nameOrEmail + "] udany");
            final UserDetails principal = uzyt;
            return new UsernamePasswordAuthenticationToken(principal, haslo, uzyt.getAuthorities());
        } else {
            log.info("login [" + nameOrEmail + "] nieudany: Niepoprawny login lub hasło");
            throw new BadCredentialsException("Niepoprawny login lub hasło.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
