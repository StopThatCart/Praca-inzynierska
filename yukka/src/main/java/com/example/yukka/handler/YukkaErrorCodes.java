package com.example.yukka.handler;

import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

import lombok.Getter;

public enum YukkaErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "Żadnego kodu"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Aktualne hasło jest niepoprawne"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "Nowe hasło nie pasuje"),
    ACCOUNT_BANNED(302, FORBIDDEN, "Konto użytkownika jest zbanowane"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "Konto użytkownika jest wyłączone"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Niepoprawny login lub hasło"),
    ENTITY_NOT_FOUND(305, NOT_FOUND, "Nie znaleziono obiektu w bazie danych"),
    ENTITY_ALREADY_EXISTS(306, BAD_REQUEST, "Obiekt już istnieje w bazie danych"),
    ;

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    YukkaErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}
