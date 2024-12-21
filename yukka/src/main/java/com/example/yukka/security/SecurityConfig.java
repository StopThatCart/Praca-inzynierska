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


/**
 * Konfiguracja zabezpieczeń aplikacji.
 * 
 * <ul>
 * <li><strong>authFilter</strong>: Filtr JWT do uwierzytelniania żądań.</li>
 * <li><strong>authenticationProvider</strong>: Dostawca uwierzytelniania Neo4J.</li>
 * <li><strong>rateLimitFilter</strong>: Filtr ograniczający szybkość żądań.</li>
 * </ul>
 * 
 * <ul>
 * <li><strong>authManager</strong>: Konfiguracja menedżera uwierzytelniania.</li>
 * <li><strong>corsConfigurationSource</strong>: Konfiguracja CORS dla aplikacji.</li>
 * <li><strong>securityFilterChain</strong>: Konfiguracja łańcucha filtrów zabezpieczeń.</li>
 * </ul>
 */
@Configuration
//@EnableWebMvc
@EnableSpringDataWebSupport()
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter authFilter;
    private final Neo4JAuthenticationProvider authenticationProvider;
    private final com.example.yukka.security.rateLimiter.RateLimitFilter rateLimitFilter;

    
    /**
     * Konfiguruje i zwraca obiekt <strong>AuthenticationManager</strong>.
     *
     * @param http obiekt <strong>HttpSecurity</strong> używany do konfiguracji zabezpieczeń HTTP
     * @return skonfigurowany obiekt <strong>AuthenticationManager</strong>
     * @throws Exception jeśli wystąpi błąd podczas konfiguracji menedżera uwierzytelniania
     * <ul>
     *   <li><strong>http</strong>: obiekt <strong>HttpSecurity</strong> używany do konfiguracji zabezpieczeń HTTP</li>
     *   <li><strong>authenticationManagerBuilder</strong>: obiekt <strong>AuthenticationManagerBuilder</strong> używany do budowy menedżera uwierzytelniania</li>
     * </ul>
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        return authenticationManagerBuilder.build();
    }

    /**
     * Konfiguracja źródła CORS.
     * @return Źródło konfiguracji CORS.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
     //   configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(HttpHeaders.ORIGIN, HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT, HttpHeaders.AUTHORIZATION, "x-auth-token", "X-Refresh-Token"));
        configuration.setExposedHeaders(List.of("x-auth-token", "X-Refresh-Token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    /**
     * Konfiguracja łańcucha filtrów bezpieczeństwa.
     *
     * <ul>
     *   <li><strong>cors</strong>: Włącza obsługę CORS z domyślnymi ustawieniami.</li>
     *   <li><strong>csrf</strong>: Wyłącza ochronę CSRF.</li>
     *   <li><strong>authorizeHttpRequests</strong>: Konfiguruje autoryzację żądań HTTP:
     *   </li>
     *   <li><strong>sessionManagement</strong>: Ustawia politykę tworzenia sesji na STATELESS.</li>
     *   <li><strong>authenticationProvider</strong>: Ustawia dostawcę uwierzytelniania.</li>
     *   <li><strong>addFilterBefore</strong>: Dodaje filtry przed UsernamePasswordAuthenticationFilter.</li>
     * </ul>
     *
     * @param http Obiekt HttpSecurity do konfiguracji.
     * @return Skonfigurowany łańcuch filtrów bezpieczeństwa.
     * @throws Exception W przypadku błędów konfiguracji.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
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
                    .requestMatchers("/admin/**",
                                    "powiadomienia/admin")
                                    .hasAuthority(ROLE.Admin.toString())
                    .requestMatchers("/pracownik/**",
                                    "powiadomienia/pracownik")
                                    .hasAnyAuthority(ROLE.Admin.toString(), ROLE.Pracownik.toString())
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers( "/favicon.ico").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                      .anyRequest().authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
