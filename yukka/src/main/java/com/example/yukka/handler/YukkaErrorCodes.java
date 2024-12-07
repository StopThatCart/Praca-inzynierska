package com.example.yukka.handler;

import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

import lombok.Getter;

/**
 * Enum YukkaErrorCodes reprezentuje kody błędów używane w aplikacji.
 * Każdy kod błędu zawiera numer kodu, status HTTP oraz opis błędu.
 * 
 * <ul>
 *   <li><strong>NO_CODE</strong> - Brak kodu błędu</li>
 *   <li><strong>INCORRECT_CURRENT_PASSWORD</strong> - Aktualne hasło jest niepoprawne</li>
 *   <li><strong>NEW_PASSWORD_DOES_NOT_MATCH</strong> - Nowe hasło nie pasuje</li>
 *   <li><strong>ACCOUNT_BANNED</strong> - Konto użytkownika jest zbanowane</li>
 *   <li><strong>ACCOUNT_DISABLED</strong> - Konto użytkownika jest wyłączone</li>
 *   <li><strong>BAD_CREDENTIALS</strong> - Niepoprawny login lub hasło</li>
 *   <li><strong>ENTITY_NOT_FOUND</strong> - Nie znaleziono obiektu w bazie danych</li>
 *   <li><strong>ENTITY_ALREADY_EXISTS</strong> - Obiekt już istnieje w bazie danych</li>
 *   <li><strong>BLOCKED_UZYTKOWNIK</strong> - Ty albo użytkownik się blokujecie</li>
 *   <li><strong>FORBIDDEN_EXCEPTION</strong> - Brak uprawnień do wykonania operacji</li>
 * </ul>
 * 
 * Każdy element enum zawiera:
 * <ul>
 *   <li>code - numer kodu błędu</li>
 *   <li>httpStatus - status HTTP związany z błędem</li>
 *   <li>description - opis błędu</li>
 * </ul>
 */
public enum YukkaErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "Żadnego kodu"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Aktualne hasło jest niepoprawne"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "Nowe hasło nie pasuje"),
    ACCOUNT_BANNED(302, FORBIDDEN, "Twoje konto jest zbanowane."),
    
    ACCOUNT_DISABLED(303, FORBIDDEN, "Konto użytkownika jest wyłączone"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Niepoprawny login lub hasło"),
    ENTITY_NOT_FOUND(305, NOT_FOUND, "Nie znaleziono obiektu w bazie danych"),
    ENTITY_ALREADY_EXISTS(306, BAD_REQUEST, "Obiekt już istnieje w bazie danych"),
    BLOCKED_UZYTKOWNIK(308, FORBIDDEN, "Ty albo użytkownik się blokujecie"),
    FORBIDDEN_EXCEPTION(403, FORBIDDEN, "Nie masz uprawnień do wykonania tej operacji"),
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
