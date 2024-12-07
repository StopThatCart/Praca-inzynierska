package com.example.yukka.security.rateLimiter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.yukka.handler.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <strong>RateLimitFilter</strong> - Filtr ograniczający liczbę żądań HTTP.
 * 
 * <ul>
 * <li><strong>bucket</strong> - Obiekt typu Bucket używany do śledzenia i ograniczania liczby żądań.</li>
 * </ul>
 * 
 * <strong>Metody:</strong>
 * 
 * <ul>
 * <li><strong>doFilterInternal</strong> - Metoda filtrująca żądania HTTP.
 * <ul>
 * <li><strong>request</strong> - Obiekt HttpServletRequest reprezentujący żądanie HTTP.</li>
 * <li><strong>response</strong> - Obiekt HttpServletResponse reprezentujący odpowiedź HTTP.</li>
 * <li><strong>filterChain</strong> - Obiekt FilterChain używany do kontynuowania przetwarzania filtra.</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * <strong>Wyjątki:</strong>
 * <ul>
 * <li><strong>ServletException</strong> - Rzucany w przypadku błędów serwletu.</li>
 * <li><strong>IOException</strong> - Rzucany w przypadku błędów wejścia/wyjścia.</li>
 * </ul>
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {
    @Autowired
    private Bucket bucket;


    /**
     * Przefiltrowuje żądanie HTTP, aby sprawdzić, czy przekroczono limit żądań.
     * 
     * @param request żądanie HTTP
     * @param response odpowiedź HTTP
     * @param filterChain łańcuch filtrów
     * @throws ServletException w przypadku błędu serwletu
     * @throws IOException w przypadku błędu wejścia/wyjścia
     * 
     * <ul>
     *   <li><strong>request</strong>: żądanie HTTP</li>
     *   <li><strong>response</strong>: odpowiedź HTTP</li>
     *   <li><strong>filterChain</strong>: łańcuch filtrów</li>
     * </ul>
     */
    @Override
    protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request, @SuppressWarnings("null") HttpServletResponse response, @SuppressWarnings("null") FilterChain filterChain)
            throws ServletException, IOException {
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
            return;
        } else {
            ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                        .businessErrorCode(HttpStatus.TOO_MANY_REQUESTS.value())
                        .businessErrorDescription("Wykonano zbyt dużo operacji. Poczekaj.")
                        .build();
                
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(exceptionResponse);

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse);

            return;
        }
    }
}
