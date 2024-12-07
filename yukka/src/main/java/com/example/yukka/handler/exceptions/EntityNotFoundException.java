package com.example.yukka.handler.exceptions;

/**
 * Wyjątek EntityNotFoundException jest rzucany, gdy żądana encja nie zostanie znaleziona.
 * 
 * @param message Szczegółowa wiadomość opisująca przyczynę wyjątku.
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}