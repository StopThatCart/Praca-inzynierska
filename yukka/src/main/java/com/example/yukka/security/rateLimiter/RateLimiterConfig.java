package com.example.yukka.security.rateLimiter;
import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;


@Configuration
public class RateLimiterConfig {

        
        /** 
         * @return Bucket
         */
        /*
     *  - W zwykłych stronach wali 10 requestów na refresh
	    - Przy przejściu do innych stron tylko wymagane komponenty są aktualizowane/pobierane(3-5 razy)
        
        - Przy cosekundowej nawigacji powinno wysyłać do 250 requestów na minutę
        - Za to przy odświeżaniu strony co sekundę to w ciągu minuty powinno być ok 500 requestów
        - Nawet jeśli to dosyć wysoki próg, to i tak zabezpiecza przed podstawowymi atakami DDoS
        - No chyba że będzie atak z wielu źródeł, to wtedy trochę gorzej
     */

     
    @SuppressWarnings("deprecation")
    @Bean
    // Odnawia  refill / 60 tokenów na sekundę
    public Bucket createBucket() {
        int refill = 60;
        Bandwidth limit = Bandwidth.classic(300, Refill.greedy(refill, Duration.ofMinutes(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }
}
