import { Injectable } from '@angular/core';
import { WlasciwoscResponse, WlasciwoscWithRelations } from '../../../../services/models';
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
}
