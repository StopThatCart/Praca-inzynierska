package com.example.yukka.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.yukka.Authorities.ROLE;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = false)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter authFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> 
                    auth.requestMatchers( 
                      //"/**",
                                  "/auth/test"
                      )
                      .authenticated()
                      .requestMatchers("/admin/**").hasRole(ROLE.Admin.toString())
                      .requestMatchers("/pracownik/**").hasAnyRole(ROLE.Admin.toString(), ROLE.Pracownik.toString())
                      .anyRequest().permitAll())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Password Encoding

}
