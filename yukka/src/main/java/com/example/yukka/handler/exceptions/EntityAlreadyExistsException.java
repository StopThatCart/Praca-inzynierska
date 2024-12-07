package com.example.yukka.handler.exceptions;

/**
 * Wyjątek EntityAlreadyExistsException jest rzucany, gdy próba utworzenia nowego bytu
 * kończy się niepowodzeniem z powodu istnienia bytu o tych samych właściwościach.
 *
 * @param message Szczegółowa wiadomość opisująca przyczynę wyjątku.
 */
public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
