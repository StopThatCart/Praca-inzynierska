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
    private final UzytkownikRepository uzytkownikRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Metoda <strong>authenticate</strong> służy do uwierzytelniania użytkownika na podstawie podanych danych logowania.
     *
     * @param authentication obiekt <strong>Authentication</strong> zawierający dane logowania użytkownika
     * @return obiekt <strong>Authentication</strong> reprezentujący uwierzytelnionego użytkownika
     * @throws AuthenticationException w przypadku niepowodzenia uwierzytelnienia
     * <ul>
     *   <li><strong>BadCredentialsException</strong> - gdy login lub hasło są niepoprawne</li>
     *   <li><strong>IllegalArgumentException</strong> - gdy konto użytkownika nie zostało aktywowane</li>
     *   <li><strong>BannedUzytkownikException</strong> - gdy konto użytkownika jest zbanowane</li>
     * </ul>
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String nameOrEmail = authentication.getName();
        String haslo = authentication.getCredentials().toString();
        Optional<Uzytkownik> uzytOpt = uzytkownikRepository.findByNameOrEmail(nameOrEmail);

        if (uzytOpt.isEmpty()) {
            throw new BadCredentialsException("Niepoprawny login lub hasło.");
        }

        Uzytkownik uzyt = uzytOpt.get();

        if (!uzyt.isAktywowany()) {
                log.info("login [" + nameOrEmail + "] nieudany: konto nieaktywowane");
                throw new IllegalArgumentException("Konto nie zostało jeszcze aktywowane. Sprawdź swoje wiadomości w poczcie email albo zarejestruj się ponownie.");
        } else if (uzyt.isBan()) {
            log.info("login [" + nameOrEmail + "] nieudany: konto zbanowane");
            throw new BannedUzytkownikException("Konto zostało zbanowane do " + uzyt.getBanDo() + " z powodu: " + uzyt.getBanPowod());
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


    /**
     * Sprawdza, czy dostarczona klasa uwierzytelniania jest obsługiwana przez ten dostawcę uwierzytelniania.
     *
     * @param authentication klasa uwierzytelniania do sprawdzenia
     * @return <ul>
     *             <li><strong>true</strong> - jeśli dostarczona klasa uwierzytelniania jest UsernamePasswordAuthenticationToken</li>
     *             <li><strong>false</strong> - w przeciwnym razie</li>
     *         </ul>
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
