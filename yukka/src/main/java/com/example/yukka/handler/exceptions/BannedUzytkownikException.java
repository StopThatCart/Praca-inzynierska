package com.example.yukka.handler.exceptions;

/**
 * Wyjątek BannedUzytkownikException jest rzucany, gdy użytkownik jest zbanowany.
 * 
 * @param message wiadomość opisująca szczegóły wyjątku
 */
public class BannedUzytkownikException extends RuntimeException {

    public BannedUzytkownikException(String message) {
        super(message);
    }

}
