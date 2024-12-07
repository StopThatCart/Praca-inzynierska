package com.example.yukka.handler.exceptions;

/**
 * Wyjątek ForbiddenException jest używany do sygnalizowania, że dostęp do określonego zasobu
 * lub operacji jest zabroniony. Dziedziczy po klasie RuntimeException.
 *
 * @param message Szczegółowa wiadomość opisująca przyczynę wyjątku.
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException (String message) {
        super(message);
    }
}
