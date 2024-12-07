package com.example.yukka.security.rateLimiter;
import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;


/**
 * Konfiguracja ogranicznika szybkości (Rate Limiter). Jest ona używana do limitowania liczby żądań HTTP na sekundę dla danego użytkownika.
 */
@Configuration
public class RateLimiterConfig {
    @SuppressWarnings("deprecation")
    @Bean
    // Odnawia  refill / 60 tokenów na sekundę
    public Bucket createBucket() {
        int refill = 60;
        Bandwidth limit = Bandwidth.classic(300, Refill.greedy(refill, Duration.ofMinutes(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }
}
