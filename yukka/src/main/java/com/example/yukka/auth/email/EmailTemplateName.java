package com.example.yukka.auth.email;

import lombok.Getter;

/**
 * Enum reprezentujący różne nazwy szablonów emailowych używanych w aplikacji.
 * Każda stała wyliczeniowa odpowiada konkretnemu szablonowi emaila.
 */
@Getter
public enum EmailTemplateName {

    AKTYWACJA_KONTA("aktywacja_konta"),
    RESET_HASLO("reset_haslo"),
    ZMIANA_EMAIL("zmiana_email")
    ;


    private final String name;
    EmailTemplateName(String name) {
        this.name = name;
    }
}
