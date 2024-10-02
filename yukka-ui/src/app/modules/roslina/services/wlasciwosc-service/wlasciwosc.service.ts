import { Injectable } from '@angular/core';
import { RoslinaRequest, RoslinaResponse, WlasciwoscResponse, WlasciwoscWithRelations } from '../../../../services/models';
import { getRelacjaByEtykieta } from '../../enums/roslina-relacje';
import { WlasciwoscEtykiety } from '../../enums/wlasciwosc-etykiety';

@Injectable({
  providedIn: 'root'
})
export class WlasciwoscProcessService {
  private miesiace: string[] = [
    'styczeń', 'luty', 'marzec', 'kwiecień', 'maj', 'czerwiec',
    'lipiec', 'sierpień', 'wrzesień', 'październik', 'listopad', 'grudzień'
  ];

  processWlasciwosciResponse(wlasciwosci: WlasciwoscResponse[]): WlasciwoscResponse[] {
    const processedWlasciwosci: WlasciwoscResponse[] = [];

    wlasciwosci.forEach(w => {
      if (w.etykieta === 'SilaWzrostu') {
        w.etykieta = WlasciwoscEtykiety.SILA_WZROSTU;
      }

      if (w.etykieta === 'Zimozielonosc') {
        w.etykieta = WlasciwoscEtykiety.ZIMOZIELONOSC;
      }


      if (w.etykieta === 'Okres') {
        const okresKwitnienia = { ...w, etykieta: WlasciwoscEtykiety.OKRES_KWITNIENIA, nazwy: this.miesiace };
        const okresDojrzewania = { ...w, etykieta: WlasciwoscEtykiety.OKRES_OWOCOWANIA, nazwy: this.miesiace };
        processedWlasciwosci.push(okresKwitnienia, okresDojrzewania);
      } else if (w.etykieta === 'Kolor') {
        const kolorKwiatow = { ...w, etykieta: WlasciwoscEtykiety.KOLOR_KWIATOW, nazwy: w.nazwy };
        const kolorLisci = { ...w, etykieta: WlasciwoscEtykiety.KOLOR_LISCI, nazwy: w.nazwy };
        processedWlasciwosci.push(kolorKwiatow, kolorLisci);
      } else {
        processedWlasciwosci.push(w);
      }
    });

    processedWlasciwosci.sort((a, b) => {
      if (a.etykieta && b.etykieta) {
        return a.etykieta.localeCompare(b.etykieta, 'pl', { sensitivity: 'base' });
      } else {
        return 0;
      }
    });

    return processedWlasciwosci;
  }

  addRelacjaToWlasciwoscCauseIAmTooLazyToChangeTheBackend(wlasciwosc: WlasciwoscWithRelations): WlasciwoscWithRelations {
    const relacja = wlasciwosc.etykieta ? getRelacjaByEtykieta(wlasciwosc.etykieta) : undefined;
    return { ...wlasciwosc, relacja };
  }


  convertRoslinaResponseToRequest(roslina: RoslinaResponse): RoslinaRequest {
    return {
      nazwa: roslina.nazwa || '',
      nazwaLacinska: roslina.nazwaLacinska || '',
      obraz: roslina.obraz || '',
      opis: roslina.opis || '',
      wysokoscMin: roslina.wysokoscMin || 0,
      wysokoscMax: roslina.wysokoscMax || 100,
      wlasciwosci: this.convertWlasciwosci(roslina)
    };
  }

  private convertWlasciwosci(roslina: RoslinaResponse): WlasciwoscWithRelations[] {
    const wlasciwosci: WlasciwoscWithRelations[] = [];

    if (roslina.koloryKwiatow) {
      roslina.koloryKwiatow.forEach(kolor => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.KOLOR_KWIATOW, nazwa: kolor, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.KOLOR_KWIATOW) });
      });
    }

    if (roslina.koloryLisci) {
      roslina.koloryLisci.forEach(kolor => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.KOLOR_LISCI, nazwa: kolor, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.KOLOR_LISCI) });
      });
    }

    if (roslina.okresyKwitnienia) {
      roslina.okresyKwitnienia.forEach(okres => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.OKRES_KWITNIENIA, nazwa: okres, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.OKRES_KWITNIENIA) });
      });
    }

    if (roslina.okresyOwocowania) {
      roslina.okresyOwocowania.forEach(okres => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.OKRES_OWOCOWANIA, nazwa: okres, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.OKRES_OWOCOWANIA) });
      });
    }

    if (roslina.silyWzrostu) {
      roslina.silyWzrostu.forEach(sila => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.SILA_WZROSTU, nazwa: sila, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.SILA_WZROSTU) });
      });
    }

    if (roslina.gleby) {
      roslina.gleby.forEach(gleba => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.GLEBA, nazwa: gleba, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.GLEBA) });
      });
    }

    if (roslina.grupy) {
      roslina.grupy.forEach(grupa => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.GRUPA, nazwa: grupa, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.GRUPA) });
      });
    }

    if (roslina.kwiaty) {
      roslina.kwiaty.forEach(kwiat => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.KWIAT, nazwa: kwiat, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.KWIAT) });
      });
    }

    if (roslina.odczyny) {
      roslina.odczyny.forEach(odczyn => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.ODCZYN, nazwa: odczyn, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.ODCZYN) });
      });
    }

    if (roslina.owoce) {
      roslina.owoce.forEach(owoc => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.OWOC, nazwa: owoc, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.OWOC) });
      });
    }

    if (roslina.podgrupa) {
      roslina.podgrupa.forEach(podgrupa => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.PODGRUPA, nazwa: podgrupa, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.PODGRUPA) });
      });
    }

    if (roslina.pokroje) {
      roslina.pokroje.forEach(pokroj => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.POKROJ, nazwa: pokroj, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.POKROJ) });
      });
    }

    if (roslina.stanowiska) {
      roslina.stanowiska.forEach(stanowisko => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.STANOWISKO, nazwa: stanowisko, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.STANOWISKO) });
      });
    }

    if (roslina.walory) {
      roslina.walory.forEach(walor => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.WALOR, nazwa: walor, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.WALOR) });
      });
    }

    if (roslina.wilgotnosci) {
      roslina.wilgotnosci.forEach(wilgotnosc => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.WILGOTNOSC, nazwa: wilgotnosc, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.WILGOTNOSC) });
      });
    }

    if (roslina.zastosowania) {
      roslina.zastosowania.forEach(zastosowanie => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.ZASTOSOWANIE, nazwa: zastosowanie, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.ZASTOSOWANIE) });
      });
    }

    if (roslina.zimozielonosci) {
      roslina.zimozielonosci.forEach(zimozielonosc => {
        wlasciwosci.push({ etykieta: WlasciwoscEtykiety.ZIMOZIELONOSC, nazwa: zimozielonosc, relacja: getRelacjaByEtykieta(WlasciwoscEtykiety.ZIMOZIELONOSC) });
      });
    }

    return wlasciwosci;
  }

}
