package com.example.yukka.security.jwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.yukka.handler.ExceptionResponse;
import com.example.yukka.handler.YukkaErrorCodes;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * <p>Filtr JWT, który jest wykonywany raz na każde żądanie.</p>
 * 
 * @throws ServletException - W przypadku błędu serwletu.
 * @throws IOException - W przypadku błędu wejścia/wyjścia.
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UzytkownikService userDetailsService;

    
    /** 
     * @throws ServletException
     * @throws IOException
     */
    @Override
    @SuppressWarnings("null")
    protected void doFilterInternal(
             @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                Uzytkownik uzytkownik = (Uzytkownik) userDetails;
                if (uzytkownik.isBan()) {
                    response.setStatus(YukkaErrorCodes.ACCOUNT_BANNED.getCode());
                    response.setContentType("application/json");
                    
                    ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                            .businessErrorCode(YukkaErrorCodes.ACCOUNT_BANNED.getCode())
                            .businessErrorDescription(YukkaErrorCodes.ACCOUNT_BANNED.getDescription())
                            .build();
                    
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonResponse = objectMapper.writeValueAsString(exceptionResponse);
                    
                    response.getWriter().write(jsonResponse);
                    return;
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
