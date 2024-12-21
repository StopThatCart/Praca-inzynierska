package com.example.yukka.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Serwis JwtService zapewnia funkcjonalność do generowania i weryfikacji tokenów JWT.
 * 
 * <ul>
 *   <li><strong>secretKey</strong>: Klucz tajny używany do podpisywania tokenów JWT.</li>
 *   <li><strong>jwtExpiration</strong>: Czas wygaśnięcia tokenu JWT w milisekundach.</li>
 * </ul>
 * 
 * <ul>
 *   <li><strong>extractUsername</strong>: Wyodrębnia nazwę użytkownika z tokenu JWT.</li>
 *   <li><strong>extractClaim</strong>: Wyodrębnia określone roszczenie z tokenu JWT.</li>
 *   <li><strong>generateToken</strong>: Generuje token JWT dla podanych danych użytkownika.</li>
 *   <li><strong>buildToken</strong>: Buduje token JWT z dodatkowymi roszczeniami i czasem wygaśnięcia.</li>
 *   <li><strong>buildRefreshToken</strong>: Buduje token odświeżający JWT z czasem wygaśnięcia.</li>
 *   <li><strong>isTokenValid</strong>: Sprawdza, czy token JWT jest ważny dla podanych danych użytkownika.</li>
 *   <li><strong>isTokenExpired</strong>: Sprawdza, czy token JWT wygasł.</li>
 *   <li><strong>extractExpiration</strong>: Wyodrębnia datę wygaśnięcia z tokenu JWT.</li>
 *   <li><strong>extractAllClaims</strong>: Wyodrębnia wszystkie roszczenia z tokenu JWT.</li>
 *   <li><strong>getSignInKey</strong>: Pobiera klucz używany do podpisywania tokenów JWT.</li>
 * </ul>
 */
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt-refresh.expiration}")
    private long jwtRefreshExpiration;

    
    /** 
     * @param token
     * @return String
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildRefreshToken(userDetails, jwtRefreshExpiration);
    }

    @SuppressWarnings("deprecation")
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        var authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .claim("authorities", authorities)
                .signWith(getSignInKey())
                .compact();
    }


    @SuppressWarnings("deprecation")
    private String buildRefreshToken(UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @SuppressWarnings("deprecation")
    private Claims extractAllClaims(String token) {
        JwtParser parser = Jwts.parser()
        .setSigningKey(getSignInKey())
        .build();
       
        return parser.parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
