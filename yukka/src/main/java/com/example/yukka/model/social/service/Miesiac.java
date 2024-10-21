package com.example.yukka.model.social.service;

public enum Miesiac {
    STYCZEŃ("styczeń"),
    LUTY("luty"),
    MARZEC("marzec"),
    KWIECIEŃ("kwiecień"),
    MAJ("maj"),
    CZERWIEC("czerwiec"),
    LIPIEC("lipiec"),
    SIERPIEŃ("sierpień"),
    WRZESIEŃ("wrzesień"),
    PAŹDZIERNIK("październik"),
    LISTOPAD("listopad"),
    GRUDZIEŃ("grudzień");

    @SuppressWarnings("unused")
    private final String nazwa;

    Miesiac(String nazwa) {
        this.nazwa = nazwa;
    }
}
