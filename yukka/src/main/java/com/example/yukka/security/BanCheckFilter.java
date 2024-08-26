package com.example.yukka.security;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.yukka.model.uzytkownik.Uzytkownik;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class BanCheckFilter extends OncePerRequestFilter {

    private static final List<String> PROTECTED_URLS = List.of(
            "/posty/oceny",
            "/komentarze/oceny",
            "/komentarze/{komentarz-id}",
            "/komentarze/odpowiedzi",
            "/komentarze/posty",
            "/comments/add",
            "/another/protected/url"
            );

    private static final Set<String> PROTECTED_METHODS = Set.of("POST", "PATCH", "PUT", "DELETE");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Uzytkownik) {
            Uzytkownik uzyt = (Uzytkownik) authentication.getPrincipal();

            String requestURI = request.getRequestURI();
            boolean isProtectedUrl = PROTECTED_URLS.stream().anyMatch(requestURI::startsWith);

            String method = request.getMethod();

            if (PROTECTED_METHODS.contains(method) && uzyt.isBan()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Operacja niedozwolona - Jesteś zbanowany.");
                return;
            }

          //  if (isProtectedUrl && uzyt.isBan()) {
         //       response.sendError(HttpServletResponse.SC_FORBIDDEN, "Operacja niedozwolona - Jesteś zbanowany.");
                return;
         //   }
        }

        filterChain.doFilter(request, response);
    }

}
