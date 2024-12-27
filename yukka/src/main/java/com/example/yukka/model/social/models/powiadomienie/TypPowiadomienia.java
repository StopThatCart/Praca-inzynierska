package com.example.yukka.model.social.models.powiadomienie;

/**
 * TypPowiadomienia enum reprezentuje różne typy powiadomień w aplikacji.
 * Każdy typ powiadomienia ma przypisany szablon wiadomości.
 * 
 * <ul>
 *   <li><strong>KOMENTARZ_POST</strong>: {uzytkownikNazwa} odpowiedział(a) na twój komentarz pod postem {tytul}</li>
 *   <li><strong>POLUBIENIA_POST</strong>: Twój post {tytul} zebrał ponad {iloscPolubien} polubień!</li>
 *   <li><strong>ZAPROSZENIE</strong>: {uzytkownikNazwa} zaprosił(a) cię do rozmowy!</li>
 *   <li><strong>ZAPROSZENIE_ODRUCONE</strong>: {uzytkownikNazwa} odrzucił(a) twoje zaproszenie.</li>
 *   <li><strong>ZAPROSZENIE_ZAAKCEPTOWANE</strong>: {uzytkownikNazwa} zaakceptował(a) twoje zaproszenie.</li>
 *   <li><strong>WIADOMOSC_PRYWATNA</strong>: {uzytkownikNazwa} odpowiedział(a) tobie w wiadomosci prywatnej</li>
 *   <li><strong>OWOCOWANIE_ROSLIN</strong>: Za {data} twoje rośliny {nazwyRoslin} zaczną owocować!</li>
 *   <li><strong>OWOCOWANIE_ROSLIN_TERAZ</strong>: W aktualnym miesiącu twoje rośliny {nazwyRoslin} powinny owocować!</li>
 *   <li><strong>KWITNIENIE_ROSLIN_TERAZ</strong>: W aktualnym miesiącu twoje rośliny {nazwyRoslin} powinny kwitnąć!</li>
 *   <li><strong>USUNIECIE_KOMENTARZA</strong>: Usunięto twój komentarz pod postem {tytul}</li>
 *   <li><strong>BAN</strong>: Zostałeś zbanowany aż do {okres} za {tytul}</li>
 *   <li><strong>ODBANOWANIE</strong>: Twoje konto zostało odbanowane</li>
 *   <li><strong>USUNIECIE_ROSLINY</strong>: Twoja roślina została usunięta bo tak</li>
 *   <li><strong>GRATULACJE</strong>: Gratulacje użytkowniku! Zyskałeś(aś) ponad {iloscPolubien} polubień!</li>
 *   <li><strong>ZGLOSZENIE</strong>: {uzytkownikNazwa} wysłał(a) zgłoszenie na {zglaszany}: {tytul}</li>
 *   <li><strong>ZGLOSZENIE_POST</strong>: {uzytkownikNazwa} zgłosił(a) post {zglaszany} z powodu: {tytul}</li>
 *   <li><strong>ZGLOSZENIE_KOMENTARZ</strong>: {uzytkownikNazwa} zgłosił(a) komentarz {zglaszany} pod postem z powodu: {tytul}</li>
 *   <li><strong>SPECJALNE</strong>: {tytul}</li>
 * </ul>
 */
public enum TypPowiadomienia {
    KOMENTARZ_POST("{uzytkownikNazwa} odpowiedział(a) na twój komentarz pod postem {tytul}"),
    POLUBIENIA_POST("Twój post {tytul} zebrał ponad {iloscPolubien} polubień!"),
    ZAPROSZENIE("{uzytkownikNazwa} zaprosił(a) cię do rozmowy!"),
    ZAPROSZENIE_ODRUCONE("{uzytkownikNazwa} odrzucił(a) twoje zaproszenie."),
    ZAPROSZENIE_ZAAKCEPTOWANE("{uzytkownikNazwa} zaakceptował(a) twoje zaproszenie."),
    WIADOMOSC_PRYWATNA("{uzytkownikNazwa} odpowiedział(a) tobie w wiadomosci prywatnej"),
    OWOCOWANIE_ROSLIN("Za {data} twoje rośliny {nazwyRoslin} zaczną owocować!"),
    OWOCOWANIE_ROSLIN_TERAZ("W aktualnym miesiącu twoje rośliny {nazwyRoslin} powinny owocować!"),
    KWITNIENIE_ROSLIN_TERAZ("W aktualnym miesiącu twoje rośliny {nazwyRoslin} powinny kwitnąć!"),
    USUNIECIE_KOMENTARZA("Usunięto twój komentarz pod postem {tytul}"),
    BAN("Zostałeś zbanowany aż do {okres} za {tytul}"),
    ODBANOWANIE("Twoje konto zostało odbanowane"),
    USUNIECIE_ROSLINY("Twoja roślina została usunięta bo tak"),
    GRATULACJE("Gratulacje użytkowniku! Zyskałeś(aś) ponad {iloscPolubien} polubień!"),
    ZGLOSZENIE("{uzytkownikNazwa} wysłał(a) zgłoszenie na {zglaszany}: {tytul}"),
    ZGLOSZENIE_POST("{uzytkownikNazwa} zgłosił(a) post {zglaszany} z powodu: {tytul}"),
    ZGLOSZENIE_KOMENTARZ("{uzytkownikNazwa} zgłosił(a) komentarz {zglaszany} pod postem z powodu: {tytul}"),
    SPECJALNE("{tytul}"),
    SPECJALNE_PRACOWNICY("{tytul}");

    private final String template;

    TypPowiadomienia(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
