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

import lombok.RequiredArgsConstructor;


@Configuration
//@EnableWebMvc
@EnableSpringDataWebSupport()
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter authFilter;
    private final BanCheckFilter banCheckFilter;
    private final Neo4JAuthenticationProvider authenticationProvider;

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
                    auth.requestMatchers("/rosliny/szukaj",
                                        "/rosliny/wlasciwosci",
                                        "/rosliny/{nazwa-lacinska}"
                    ).permitAll()
                    .requestMatchers( 
                                    //"/**",
                                  "/auth/test",
                                  "/komentarze/oceny",
                                  "/posty/oceny",
                                  "/rozmowy/**",
                                  "/dzialki/**"
                    ).authenticated()


                    .requestMatchers("/uzytkownicy/pracownik/ban/{email}/{ban}")
                        .hasAnyRole(ROLE.Admin.toString(), ROLE.Pracownik.toString())
                    .requestMatchers("/uzytkownicy/blok/{nazwa}/{blok}").authenticated()
                    .requestMatchers("/uzytkownicy/blokowani").authenticated()
                    .requestMatchers(HttpMethod.PATCH,"/uzytkownicy/avatar").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/uzytkownicy/{email}",
                                                                    "/uzytkownicy"
                    ).authenticated()



                    .requestMatchers(HttpMethod.POST, "/posty").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/posty/{post-id}").authenticated()
                    .requestMatchers("/posty/uzytkownik/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/komentarze/{komentarz-id}").authenticated()
                    .requestMatchers(HttpMethod.PATCH, "/komentarze/{komentarz-id}").authenticated()

                    .requestMatchers(HttpMethod.DELETE, "/rosliny/{nazwa-lacinska}").authenticated()
                    .requestMatchers(HttpMethod.POST, "/rosliny").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/rosliny").authenticated()

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
                    .requestMatchers("/admin/**").hasRole(ROLE.Admin.toString())
                    .requestMatchers("/pracownik/**").hasAnyRole(ROLE.Admin.toString(), ROLE.Pracownik.toString())
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers( "/favicon.ico").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                     // .requestMatchers("/rest/neo4j/rozmowy").hasRole(ROLE.Admin.toString())


                      .anyRequest().permitAll())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
              //  .addFilterBefore(banCheckFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
