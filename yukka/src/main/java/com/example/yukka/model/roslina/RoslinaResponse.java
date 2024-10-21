package com.example.yukka.model.roslina;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoslinaResponse {
    private Long id;
    private String roslinaId;
    private String nazwa;
    private String nazwaLacinska;

    

    private String opis;
    private double wysokoscMin;
    private double wysokoscMax;
    private byte[] obraz;

    private String autor;
    private boolean roslinaUzytkownika;
    
    private Set<String> grupy;
    private Set<String> formy;
    private Set<String> gleby;
    private Set<String> koloryLisci;
    private Set<String> koloryKwiatow;
    private Set<String> kwiaty;
    private Set<String> odczyny;
    private Set<String> okresyKwitnienia;
    private Set<String> okresyOwocowania;
    private Set<String> owoce;
    private Set<String> podgrupa;
    private Set<String> pokroje;
    private Set<String> silyWzrostu;
    private Set<String> stanowiska;
    private Set<String> walory;
    private Set<String> wilgotnosci;
    private Set<String> zastosowania;
    private Set<String> zimozielonosci;
}
