package com.example.yukka.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

/**
 * Klasa konfiguracyjna BeansConfig.
 * 
 * @return instancja BCryptPasswordEncoder
 */
@Configuration
@RequiredArgsConstructor
public class BeansConfig {
    
    /**
     * Tworzy i zwraca obiekt PasswordEncoder używający algorytmu BCrypt do kodowania haseł.
     * 
     * @return instancja BCryptPasswordEncoder do kodowania haseł
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
