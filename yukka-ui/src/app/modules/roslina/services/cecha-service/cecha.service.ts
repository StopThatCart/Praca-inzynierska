import { Injectable } from '@angular/core';
import { RoslinaRequest, RoslinaResponse, CechaResponse, CechaWithRelations } from '../../../../services/models';
import { getRelacjaByEtykieta } from '../../models/roslina-relacje';
import { CechaEtykiety } from '../../models/cecha-etykiety';

@Injectable({
  providedIn: 'root'
})
export class CechaProcessService {
  private miesiace: string[] = [
    'styczeń', 'luty', 'marzec', 'kwiecień', 'maj', 'czerwiec',
    'lipiec', 'sierpień', 'wrzesień', 'październik', 'listopad', 'grudzień'
  ];

  processCechyResponse(cechy: CechaResponse[]): CechaResponse[] {
    const processedCechy: CechaResponse[] = [];

    cechy.forEach(w => {
      if (w.etykieta === 'SilaWzrostu') {
        w.etykieta = CechaEtykiety.SILA_WZROSTU;
      }

      if (w.etykieta === 'Zimozielonosc') {
        w.etykieta = CechaEtykiety.ZIMOZIELONOSC;
      }

      if (w.etykieta === 'Wilgotnosc') {
        w.etykieta = CechaEtykiety.WILGOTNOSC;
      }

      if (w.etykieta === 'Pokroj') {
        w.etykieta = CechaEtykiety.POKROJ;
      }

      if (w.etykieta === 'Okres') {
        const okresKwitnienia = { ...w, etykieta: CechaEtykiety.OKRES_KWITNIENIA, nazwy: this.miesiace };
        const okresDojrzewania = { ...w, etykieta: CechaEtykiety.OKRES_OWOCOWANIA, nazwy: this.miesiace };
        processedCechy.push(okresKwitnienia, okresDojrzewania);
      } else if (w.etykieta === 'Kolor') {
        const kolorKwiatow = { ...w, etykieta: CechaEtykiety.KOLOR_KWIATOW, nazwy: w.nazwy };
        const kolorLisci = { ...w, etykieta: CechaEtykiety.KOLOR_LISCI, nazwy: w.nazwy };
        processedCechy.push(kolorKwiatow, kolorLisci);
      } else {
        processedCechy.push(w);
      }
    });

    processedCechy.sort((a, b) => {
      if (a.etykieta && b.etykieta) {
        return a.etykieta.localeCompare(b.etykieta, 'pl', { sensitivity: 'base' });
      } else {
        return 0;
      }
    });

    return processedCechy;
  }

  addRelacjaToCecha(wlasciwosc: CechaWithRelations): CechaWithRelations {
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
      cechy: this.convertCechy(roslina)
    };
  }

  private convertCechy(roslina: RoslinaResponse): CechaWithRelations[] {
    const cechy: CechaWithRelations[] = [];

    if (roslina.koloryKwiatow) {
      roslina.koloryKwiatow.forEach(kolor => {
        cechy.push({ etykieta: CechaEtykiety.KOLOR_KWIATOW, nazwa: kolor, relacja: getRelacjaByEtykieta(CechaEtykiety.KOLOR_KWIATOW) });
      });
    }

    if (roslina.koloryLisci) {
      roslina.koloryLisci.forEach(kolor => {
        cechy.push({ etykieta: CechaEtykiety.KOLOR_LISCI, nazwa: kolor, relacja: getRelacjaByEtykieta(CechaEtykiety.KOLOR_LISCI) });
      });
    }

    if (roslina.okresyKwitnienia) {
      roslina.okresyKwitnienia.forEach(okres => {
        cechy.push({ etykieta: CechaEtykiety.OKRES_KWITNIENIA, nazwa: okres, relacja: getRelacjaByEtykieta(CechaEtykiety.OKRES_KWITNIENIA) });
      });
    }

    if (roslina.okresyOwocowania) {
      roslina.okresyOwocowania.forEach(okres => {
        cechy.push({ etykieta: CechaEtykiety.OKRES_OWOCOWANIA, nazwa: okres, relacja: getRelacjaByEtykieta(CechaEtykiety.OKRES_OWOCOWANIA) });
      });
    }

    if (roslina.silyWzrostu) {
      roslina.silyWzrostu.forEach(sila => {
        cechy.push({ etykieta: CechaEtykiety.SILA_WZROSTU, nazwa: sila, relacja: getRelacjaByEtykieta(CechaEtykiety.SILA_WZROSTU) });
      });
    }

    if (roslina.gleby) {
      roslina.gleby.forEach(gleba => {
        cechy.push({ etykieta: CechaEtykiety.GLEBA, nazwa: gleba, relacja: getRelacjaByEtykieta(CechaEtykiety.GLEBA) });
      });
    }

    if (roslina.grupy) {
      roslina.grupy.forEach(grupa => {
        cechy.push({ etykieta: CechaEtykiety.GRUPA, nazwa: grupa, relacja: getRelacjaByEtykieta(CechaEtykiety.GRUPA) });
      });
    }

    if (roslina.kwiaty) {
      roslina.kwiaty.forEach(kwiat => {
        cechy.push({ etykieta: CechaEtykiety.KWIAT, nazwa: kwiat, relacja: getRelacjaByEtykieta(CechaEtykiety.KWIAT) });
      });
    }

    if (roslina.odczyny) {
      roslina.odczyny.forEach(odczyn => {
        cechy.push({ etykieta: CechaEtykiety.ODCZYN, nazwa: odczyn, relacja: getRelacjaByEtykieta(CechaEtykiety.ODCZYN) });
      });
    }

    if (roslina.owoce) {
      roslina.owoce.forEach(owoc => {
        cechy.push({ etykieta: CechaEtykiety.OWOC, nazwa: owoc, relacja: getRelacjaByEtykieta(CechaEtykiety.OWOC) });
      });
    }

    if (roslina.podgrupa) {
      roslina.podgrupa.forEach(podgrupa => {
        cechy.push({ etykieta: CechaEtykiety.PODGRUPA, nazwa: podgrupa, relacja: getRelacjaByEtykieta(CechaEtykiety.PODGRUPA) });
      });
    }

    if (roslina.pokroje) {
      roslina.pokroje.forEach(pokroj => {
        cechy.push({ etykieta: CechaEtykiety.POKROJ, nazwa: pokroj, relacja: getRelacjaByEtykieta(CechaEtykiety.POKROJ) });
      });
    }

    if (roslina.stanowiska) {
      roslina.stanowiska.forEach(stanowisko => {
        cechy.push({ etykieta: CechaEtykiety.STANOWISKO, nazwa: stanowisko, relacja: getRelacjaByEtykieta(CechaEtykiety.STANOWISKO) });
      });
    }

    if (roslina.walory) {
      roslina.walory.forEach(walor => {
        cechy.push({ etykieta: CechaEtykiety.WALOR, nazwa: walor, relacja: getRelacjaByEtykieta(CechaEtykiety.WALOR) });
      });
    }

    if (roslina.wilgotnosci) {
      roslina.wilgotnosci.forEach(wilgotnosc => {
        cechy.push({ etykieta: CechaEtykiety.WILGOTNOSC, nazwa: wilgotnosc, relacja: getRelacjaByEtykieta(CechaEtykiety.WILGOTNOSC) });
      });
    }

    if (roslina.zastosowania) {
      roslina.zastosowania.forEach(zastosowanie => {
        cechy.push({ etykieta: CechaEtykiety.ZASTOSOWANIE, nazwa: zastosowanie, relacja: getRelacjaByEtykieta(CechaEtykiety.ZASTOSOWANIE) });
      });
    }

    if (roslina.zimozielonosci) {
      roslina.zimozielonosci.forEach(zimozielonosc => {
        cechy.push({ etykieta: CechaEtykiety.ZIMOZIELONOSC, nazwa: zimozielonosc, relacja: getRelacjaByEtykieta(CechaEtykiety.ZIMOZIELONOSC) });
      });
    }

    return cechy;
  }


  setRoslinaCechy(roslina: RoslinaResponse): { name: string, value: string }[] {
    const createCecha = (name: string, value: string | string[] | number | undefined): { name: string, value: string } | null => {
      if (Array.isArray(value)) {
        value = value.join(', ');
      }
      if (value) {
        return { name, value: value.toString() };
      }
      return null;
    };

    const cechy = [
      createCecha(CechaEtykiety.GRUPA, roslina.grupy),
      createCecha(CechaEtykiety.PODGRUPA, roslina.podgrupa),
      createCecha(CechaEtykiety.FORMA, roslina.formy),
      createCecha(CechaEtykiety.GLEBA, roslina.gleby),
      createCecha(CechaEtykiety.KOLOR_KWIATOW, roslina.koloryKwiatow),
      createCecha(CechaEtykiety.KOLOR_LISCI, roslina.koloryLisci),
      createCecha(CechaEtykiety.KWIAT, roslina.kwiaty),
      createCecha(CechaEtykiety.ODCZYN, roslina.odczyny),
      createCecha(CechaEtykiety.OKRES_KWITNIENIA, roslina.okresyKwitnienia),
      createCecha(CechaEtykiety.OKRES_OWOCOWANIA, roslina.okresyOwocowania),
      createCecha(CechaEtykiety.OWOC, roslina.owoce),
      createCecha(CechaEtykiety.POKROJ, roslina.pokroje),
      createCecha(CechaEtykiety.SILA_WZROSTU, roslina.silyWzrostu),
      createCecha(CechaEtykiety.STANOWISKO, roslina.stanowiska),
      createCecha(CechaEtykiety.WALOR, roslina.walory),
      createCecha(CechaEtykiety.WILGOTNOSC, roslina.wilgotnosci),
      createCecha('Wysokość', roslina.wysokoscMin && roslina.wysokoscMax ? `Od ${roslina.wysokoscMin} m do ${roslina.wysokoscMax} m` : ''),
      createCecha(CechaEtykiety.ZASTOSOWANIE, roslina.zastosowania),
      createCecha(CechaEtykiety.ZIMOZIELONOSC, roslina.zimozielonosci),
    ].filter(w => w !== null);

    return cechy as { name: string, value: string }[];
  }

  getRoslinaCechaPary(cechy: { name: string, value: string }[]): { name: string, value: string }[][] {
    const pary = [];
    for (let i = 0; i < cechy.length; i += 2) {
      pary.push(cechy.slice(i, i + 2));
    }
    return pary;
  }

}
