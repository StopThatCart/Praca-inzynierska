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

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    @Autowired
    private Bucket bucket;



    @Override
    protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request, @SuppressWarnings("null") HttpServletResponse response, @SuppressWarnings("null") FilterChain filterChain)
            throws ServletException, IOException {
        // if (!request.getServletPath().contains("/rosliny/szukaj")) {
        //     filterChain.doFilter(request, response);
        //     return;
        // }


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
