package com.example.yukka.handler.exceptions;

/**
 * Wyjątek BlockedUzytkownikException jest rzucany, gdy użytkownik jest zablokowany.
 * 
 * @param message wiadomość opisująca szczegóły wyjątku
 */
public class BlockedUzytkownikException extends RuntimeException {
    public BlockedUzytkownikException (String message) {
        super(message);
    }

}
