package com.example.yukka.auth.email;

import lombok.Getter;

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
