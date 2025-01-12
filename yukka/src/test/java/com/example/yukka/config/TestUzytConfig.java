package com.example.yukka.config;

import java.util.List;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

@TestConfiguration
public class TestUzytConfig {
    
    /** 
     * @param uzytkownikRepository
     * @param passwordEncoder
     * @return List<Uzytkownik>
     */
    @Bean
    @Primary
    public List<Uzytkownik> createTestUsers(UzytkownikRepository uzytkownikRepository, PasswordEncoder passwordEncoder) {
        Uzytkownik admin = Uzytkownik.builder()
                .uuid("admin-id")
                .nazwa("admin")
                .email("admin@example.com")
                .haslo(passwordEncoder.encode("adminpass"))
                .labels(List.of("Admin"))
                .build();

        Uzytkownik pracownik = Uzytkownik.builder()
                .uuid("pracownik-id")
                .nazwa("pracownik")
                .email("pracownik@example.com")
                .haslo(passwordEncoder.encode("pracownikpass"))
                .labels(List.of("Pracownik"))
                .build();

        Uzytkownik normalnyUzytkownik = Uzytkownik.builder()
                .uuid("uzytkownik-id")
                .nazwa("uzytkownik")
                .email("uzytkownik@example.com")
                .haslo(passwordEncoder.encode("uzytkownikpass"))
                .labels(List.of("Uzytkownik"))
                .build();
        
        Uzytkownik normalnyUzytkownik2 = Uzytkownik.builder()
                .uuid("uzytkownik-id2")
                .nazwa("uzytkownik2")
                .email("uzytkownik2@example.com")
                .haslo(passwordEncoder.encode("uzytkownikpass2"))
                .labels(List.of("Uzytkownik"))
                .build();

        uzytkownikRepository.save(admin);
        uzytkownikRepository.save(pracownik);
        uzytkownikRepository.save(normalnyUzytkownik);
        uzytkownikRepository.save(normalnyUzytkownik2);

        return List.of(admin, pracownik, normalnyUzytkownik);
    }
}
