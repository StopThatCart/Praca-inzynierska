package com.example.yukka.model.roslina.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.cecha.Cecha;


/**
 * Enum RoslinaRelacje reprezentuje różne relacje i właściwości roślin.
 * Każda wartość enum posiada odpowiadającą jej właściwość, która jest używana do
 * pobierania i ustawiania odpowiednich właściwości rośliny.
 *
 * <ul>
 *   <li><strong>MA_FORME</strong> - formy</li>
 *   <li><strong>MA_GLEBE</strong> - gleby</li>
 *   <li><strong>MA_GRUPE</strong> - grupy</li>
 *   <li><strong>MA_KOLOR_LISCI</strong> - kolory liści</li>
 *   <li><strong>MA_KOLOR_KWIATOW</strong> - kolory kwiatów</li>
 *   <li><strong>MA_KWIAT</strong> - kwiaty</li>
 *   <li><strong>MA_ODCZYN</strong> - odczyny</li>
 *   <li><strong>MA_OKRES_KWITNIENIA</strong> - okresy kwitnienia</li>
 *   <li><strong>MA_OKRES_OWOCOWANIA</strong> - okresy owocowania</li>
 *   <li><strong>MA_OWOC</strong> - owoce</li>
 *   <li><strong>MA_PODGRUPE</strong> - podgrupa</li>
 *   <li><strong>MA_POKROJ</strong> - pokroje</li>
 *   <li><strong>MA_SILE_WZROSTU</strong> - siły wzrostu</li>
 *   <li><strong>MA_STANOWISKO</strong> - stanowiska</li>
 *   <li><strong>MA_WALOR</strong> - walory</li>
 *   <li><strong>MA_WILGOTNOSC</strong> - wilgotności</li>
 *   <li><strong>MA_ZASTOSOWANIE</strong> - zastosowania</li>
 *   <li><strong>MA_ZIMOZIELONOSC_LISCI</strong> - zimozieloności</li>
 * </ul>
 *
 * Enum zawiera również mapy getterów i setterów, które są używane do pobierania i ustawiania
 * odpowiednich właściwości rośliny na podstawie wartości enum.
 */
public enum RoslinaRelacje {
    MA_FORME("formy"),
    MA_GLEBE("gleby"),
    MA_GRUPE("grupy"),
    MA_KOLOR_LISCI("koloryLisci"),
    MA_KOLOR_KWIATOW("koloryKwiatow"),
    MA_KWIAT("kwiaty"),
    MA_ODCZYN("odczyny"),
    MA_OKRES_KWITNIENIA("okresyKwitnienia"),
    MA_OKRES_OWOCOWANIA("okresyOwocowania"),
    MA_OWOC("owoce"),
    MA_PODGRUPE("podgrupa"),
    MA_POKROJ("pokroje"),
    MA_SILE_WZROSTU("silyWzrostu"),
    MA_STANOWISKO("stanowiska"),
    MA_WALOR("walory"),
    MA_WILGOTNOSC("wilgotnosci"),
    MA_ZASTOSOWANIE("zastosowania"),
    MA_ZIMOZIELONOSC_LISCI("zimozielonosci");

    private final String property;

    private static final Map<String, Function<Roslina, Set<Cecha>>> getters = new HashMap<>();
    private static final Map<String, BiConsumer<Roslina, Set<Cecha>>> setters = new HashMap<>();

    static {
        getters.put("formy", Roslina::getFormy);
        getters.put("gleby", Roslina::getGleby);
        getters.put("grupy", Roslina::getGrupy);
        getters.put("koloryLisci", Roslina::getKoloryLisci);
        getters.put("koloryKwiatow", Roslina::getKoloryKwiatow);
        getters.put("kwiaty", Roslina::getKwiaty);
        getters.put("odczyny", Roslina::getOdczyny);
        getters.put("okresyKwitnienia", Roslina::getOkresyKwitnienia);
        getters.put("okresyOwocowania", Roslina::getOkresyOwocowania);
        getters.put("owoce", Roslina::getOwoce);
        getters.put("podgrupa", Roslina::getPodgrupa);
        getters.put("pokroje", Roslina::getPokroje);
        getters.put("silyWzrostu", Roslina::getSilyWzrostu);
        getters.put("stanowiska", Roslina::getStanowiska);
        getters.put("walory", Roslina::getWalory);
        getters.put("wilgotnosci", Roslina::getWilgotnosci);
        getters.put("zastosowania", Roslina::getZastosowania);
        getters.put("zimozielonosci", Roslina::getZimozielonosci);

        setters.put("formy", Roslina::setFormy);
        setters.put("gleby", Roslina::setGleby);
        setters.put("grupy", Roslina::setGrupy);
        setters.put("koloryLisci", Roslina::setKoloryLisci);
        setters.put("koloryKwiatow", Roslina::setKoloryKwiatow);
        setters.put("kwiaty", Roslina::setKwiaty);
        setters.put("odczyny", Roslina::setOdczyny);
        setters.put("okresyKwitnienia", Roslina::setOkresyKwitnienia);
        setters.put("okresyOwocowania", Roslina::setOkresyOwocowania);
        setters.put("owoce", Roslina::setOwoce);
        setters.put("podgrupa", Roslina::setPodgrupa);
        setters.put("pokroje", Roslina::setPokroje);
        setters.put("silyWzrostu", Roslina::setSilyWzrostu);
        setters.put("stanowiska", Roslina::setStanowiska);
        setters.put("walory", Roslina::setWalory);
        setters.put("wilgotnosci", Roslina::setWilgotnosci);
        setters.put("zastosowania", Roslina::setZastosowania);
        setters.put("zimozielonosci", Roslina::setZimozielonosci);
    }

    RoslinaRelacje(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    public Set<Cecha> getCechy(Roslina roslina) {
        return getters.get(property).apply(roslina);
    }

    public void setCechy(Roslina roslina, Set<Cecha> cechy) {
        setters.get(property).accept(roslina, cechy);
    }
}
