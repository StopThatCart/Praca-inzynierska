import { Injectable } from '@angular/core';
import { RoslinaRequest, RoslinaResponse, WlasciwoscResponse, WlasciwoscWithRelations } from '../../../../services/models';
import { getRelacjaByEtykieta } from '../../enums/roslina-relacje';

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
        w.etykieta = 'Siła wzrostu';
      }

      if (w.etykieta === 'Okres') {
        const okresKwitnienia = { ...w, etykieta: 'Okres kwitnienia', nazwy: this.miesiace };
        const okresDojrzewania = { ...w, etykieta: 'Okres owocowania', nazwy: this.miesiace };
        processedWlasciwosci.push(okresKwitnienia, okresDojrzewania);
      } else if (w.etykieta === 'Kolor') {
        const kolorKwiatow = { ...w, etykieta: 'Kolor kwiatów', nazwy: w.nazwy };
        const kolorLisci = { ...w, etykieta: 'Kolor liści', nazwy: w.nazwy };
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
        wlasciwosci.push({ etykieta: 'Kolor kwiatów', nazwa: kolor, relacja: getRelacjaByEtykieta('Kolor kwiatów') });
      });
    }

    if (roslina.koloryLisci) {
      roslina.koloryLisci.forEach(kolor => {
        wlasciwosci.push({ etykieta: 'Kolor liści', nazwa: kolor, relacja: getRelacjaByEtykieta('Kolor liści') });
      });
    }

    if (roslina.okresyKwitnienia) {
      roslina.okresyKwitnienia.forEach(okres => {
        wlasciwosci.push({ etykieta: 'Okres kwitnienia', nazwa: okres, relacja: getRelacjaByEtykieta('Okres kwitnienia') });
      });
    }

    if (roslina.okresyOwocowania) {
      roslina.okresyOwocowania.forEach(okres => {
        wlasciwosci.push({ etykieta: 'Okres owocowania', nazwa: okres, relacja: getRelacjaByEtykieta('Okres owocowania') });
      });
    }

    if (roslina.silyWzrostu) {
      roslina.silyWzrostu.forEach(sila => {
        wlasciwosci.push({ etykieta: 'Siła wzrostu', nazwa: sila, relacja: getRelacjaByEtykieta('Siła wzrostu') });
      });
    }

    if (roslina.gleby) {
      roslina.gleby.forEach(gleba => {
        wlasciwosci.push({ etykieta: 'Gleba', nazwa: gleba, relacja: getRelacjaByEtykieta('Gleba') });
      });
    }

    if (roslina.grupy) {
      roslina.grupy.forEach(grupa => {
        wlasciwosci.push({ etykieta: 'Grupa', nazwa: grupa, relacja: getRelacjaByEtykieta('Grupa') });
      });
    }

    if (roslina.kwiaty) {
      roslina.kwiaty.forEach(kwiat => {
        wlasciwosci.push({ etykieta: 'Kwiat', nazwa: kwiat, relacja: getRelacjaByEtykieta('Kwiat') });
      });
    }

    if (roslina.odczyny) {
      roslina.odczyny.forEach(odczyn => {
        wlasciwosci.push({ etykieta: 'Odczyn', nazwa: odczyn, relacja: getRelacjaByEtykieta('Odczyn') });
      });
    }

    if (roslina.owoce) {
      roslina.owoce.forEach(owoc => {
        wlasciwosci.push({ etykieta: 'Owoc', nazwa: owoc, relacja: getRelacjaByEtykieta('Owoc') });
      });
    }

    if (roslina.podgrupa) {
      roslina.podgrupa.forEach(podgrupa => {
        wlasciwosci.push({ etykieta: 'Podgrupa', nazwa: podgrupa, relacja: getRelacjaByEtykieta('Podgrupa') });
      });
    }

    if (roslina.pokroje) {
      roslina.pokroje.forEach(pokroj => {
        wlasciwosci.push({ etykieta: 'Pokrój', nazwa: pokroj, relacja: getRelacjaByEtykieta('Pokrój') });
      });
    }

    if (roslina.stanowiska) {
      roslina.stanowiska.forEach(stanowisko => {
        wlasciwosci.push({ etykieta: 'Stanowisko', nazwa: stanowisko, relacja: getRelacjaByEtykieta('Stanowisko') });
      });
    }

    if (roslina.walory) {
      roslina.walory.forEach(walor => {
        wlasciwosci.push({ etykieta: 'Walor', nazwa: walor, relacja: getRelacjaByEtykieta('Walor') });
      });
    }

    if (roslina.wilgotnosci) {
      roslina.wilgotnosci.forEach(wilgotnosc => {
        wlasciwosci.push({ etykieta: 'Wilgotność', nazwa: wilgotnosc, relacja: getRelacjaByEtykieta('Wilgotność') });
      });
    }

    if (roslina.zastosowania) {
      roslina.zastosowania.forEach(zastosowanie => {
        wlasciwosci.push({ etykieta: 'Zastosowanie', nazwa: zastosowanie, relacja: getRelacjaByEtykieta('Zastosowanie') });
      });
    }

    if (roslina.zimozielonosci) {
      roslina.zimozielonosci.forEach(zimozielonosc => {
        wlasciwosci.push({ etykieta: 'Zimozieloność', nazwa: zimozielonosc, relacja: getRelacjaByEtykieta('Zimozieloność') });
      });
    }

    return wlasciwosci;
  }

}
