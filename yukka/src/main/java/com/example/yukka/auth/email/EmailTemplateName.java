package com.example.yukka.auth.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("aktywacja_konta")
    ;


    private final String name;
    EmailTemplateName(String name) {
        this.name = name;
    }
}
