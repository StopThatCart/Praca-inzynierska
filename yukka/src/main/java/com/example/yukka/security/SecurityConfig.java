package com.example.yukka.security;

import java.util.Arrays;
import java.util.List;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.yukka.authorities.ROLE;
import com.example.yukka.security.jwt.JwtFilter;

import lombok.RequiredArgsConstructor;


@Configuration
//@EnableWebMvc
@EnableSpringDataWebSupport()
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter authFilter;
   // private final BanCheckFilter banCheckFilter;
    private final Neo4JAuthenticationProvider authenticationProvider;

    private final com.example.yukka.security.rateLimiter.RateLimitFilter rateLimitFilter;

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        return authenticationManagerBuilder.build();
    }

     @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
     //   configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(HttpHeaders.ORIGIN, HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT, HttpHeaders.AUTHORIZATION, "x-auth-token"));
        configuration.setExposedHeaders(List.of("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //.cors(cors -> cors.disable())
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> 
                    auth.requestMatchers("/rosliny",
                    "/rosliny/szukaj"
                                        
                    ).permitAll()
                    .requestMatchers(HttpMethod.GET, 
                    "/posty/**",
                    "/rosliny/**",
                    "/uzytkownicy/**"
                    ).permitAll()


                    .requestMatchers("/pracownicy/**").hasAnyAuthority(ROLE.Admin.toString(), ROLE.Pracownik.toString())

                    .requestMatchers(HttpMethod.DELETE, "/rosliny/{roslina-id}").hasAnyAuthority(ROLE.Admin.toString(), ROLE.Pracownik.toString())
                    .requestMatchers(HttpMethod.POST, "/rosliny").hasAnyAuthority(ROLE.Admin.toString(), ROLE.Pracownik.toString())
                    .requestMatchers(HttpMethod.PUT, "/rosliny/{nazwa-lacinska}/**").hasAnyAuthority(ROLE.Admin.toString(), ROLE.Pracownik.toString())

                    // Do swaggera
                    .requestMatchers("/v2/api-docs",
                                    "/v3/api-docs",
                                    "/v3/api-docs/**",
                                    "/swagger-resources",
                                    "/swagger-resources/**",
                                    "/configuration/ui",
                                    "/configuration/security",
                                    "/swagger-ui/**",
                                    "/webjars/**",
                                    "/swagger-ui.html").permitAll()
                    .requestMatchers("/admin/**").hasAuthority(ROLE.Admin.toString())
                    .requestMatchers("/pracownik/**").hasAnyAuthority(ROLE.Admin.toString(), ROLE.Pracownik.toString())
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers( "/favicon.ico").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                     // .requestMatchers("/rest/neo4j/rozmowy").hasAuthority(ROLE.Admin.toString())


                      .anyRequest().authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
              //  .addFilterBefore(banCheckFilter, UsernamePasswordAuthenticationFilter.class)
                
                .build();
    }

}
