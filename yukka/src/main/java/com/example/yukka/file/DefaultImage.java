package com.example.yukka.file;

public enum DefaultImage {
    AVATAR("uzytkownik.obraz.default.name"),
    ROSLINA("roslina.obraz.default.name"),
    POWIADOMIENIA("powiadomienia.obraz.default.name");

    private final String propertyName;

    DefaultImage(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
