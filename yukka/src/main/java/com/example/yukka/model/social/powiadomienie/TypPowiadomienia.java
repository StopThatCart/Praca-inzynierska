package com.example.yukka.model.social.powiadomienie;

public enum TypPowiadomienia {
    KOMENTARZ_POST("{uzytkownikNazwa} odpowiedzial na twój komentarz pod postem {tytul}"),
    WIADOMOSC_PRYWATNA("{uzytkownikNazwa} odpowiedzial w wiadomosci prywatnej {tytul}"),
    POLUBIENIA_POST("Twój post {tytul} zebrał ponad {iloscPolubien} polubień!"),
    OWOCOWANIE_ROSLIN("Za {data} twoje rośliny {nazwyRoslin} zaczną owocować!"),
    PODLEWANIE_ROSLIN("Za {data} powinieneś podlać następujące rośliny: {nazwyRoslin}"),
    USUNIECIE_KOMENTARZA("Usunięto twój komentarz pod postem {tytul}"),
    BAN("Zostałeś zbanowany aż do {data}"),
    ODBANOWANIE("Zostałeś odbanowany"),
    OCENA_ROSLINY("Ktoś ocenił twoją roślinę {tytul} {odnosnik}"),
    USUNIECIE_ROSLINY("Twoja roślina została usunięta bo tak"),
    GRATULACJE("Gratulacje użytkowniku! Zyskałeś ponad {iloscPolubien} polubień!");

    private final String template;

    TypPowiadomienia(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
