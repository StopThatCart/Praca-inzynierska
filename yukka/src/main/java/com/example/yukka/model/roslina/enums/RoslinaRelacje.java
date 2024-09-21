package com.example.yukka.model.roslina.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;


public enum RoslinaRelacje {
  //  MA_WLASCIWOSC("wlasciwosci"),
    MA_FORME("formy"),
    MA_GLEBE("gleby"),
    MA_GRUPE("grupy"),
    MA_KOLOR_LISCI("koloryLisci"),
    MA_KOLOR_KWIATOW("koloryKwiatow"),
    MA_KWIAT("kwiaty"),
    MA_ODCZYNY("odczyny"),
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

    private static final Map<String, Function<Roslina, Set<Wlasciwosc>>> getters = new HashMap<>();
    private static final Map<String, BiConsumer<Roslina, Set<Wlasciwosc>>> setters = new HashMap<>();

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

    public Set<Wlasciwosc> getWlasciwosci(Roslina roslina) {
        return getters.get(property).apply(roslina);
    }

    public void setWlasciwosci(Roslina roslina, Set<Wlasciwosc> wlasciwosci) {
        setters.get(property).accept(roslina, wlasciwosci);
    }
}
