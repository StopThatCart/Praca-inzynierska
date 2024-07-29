package com.example.yukka.model.Uzytkownik;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements  UserDetailsService {

    @Autowired
    private final UzytkownikRepository repository;

    @Override
   // @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return repository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Użytkownika nie znaleziono"));
    }

   
    public UserDetails loadUserByUsername2(String username) throws UsernameNotFoundException {
        Uzytkownik uzytkownik = repository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<GrantedAuthority> authorities = new HashSet<>();
        if (uzytkownik.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (uzytkownik.isPracownik()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_PRACOWNIK"));
        }

        return new org.springframework.security.core.userdetails.User(
            uzytkownik.getUsername(),
            uzytkownik.getPassword(),
            authorities
        );
    }
    public Collection<Uzytkownik> getAllUsers(){
        return repository.getAllUsers();
    }
}
