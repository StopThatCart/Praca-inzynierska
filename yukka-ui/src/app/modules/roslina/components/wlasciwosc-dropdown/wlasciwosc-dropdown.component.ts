import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WlasciwoscResponse, WlasciwoscWithRelations } from '../../../../services/models';
import { getRelacjaByEtykieta } from '../../enums/roslina-relacje';

@Component({
  selector: 'app-wlasciwosc-dropdown',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './wlasciwosc-dropdown.component.html',
  styleUrl: './wlasciwosc-dropdown.component.css'
})
export class WlasciwoscDropdownComponent {
  @Input() wlasciwosciResponse: WlasciwoscResponse[] = [];

  @Input() selectedWlasciwosci: WlasciwoscWithRelations[] = [];

  wysokoscMin: number = 0.0;
  wysokoscMax: number = 100.0;
  wysokoscMinLimit: number = 0.0;
  wysokoscMaxLimit: number = 100.0;

  private miesiace: string[] = [
    'styczeń', 'luty', 'marzec', 'kwiecień', 'maj', 'czerwiec',
    'lipiec', 'sierpień', 'wrzesień', 'październik', 'listopad', 'grudzień'
  ];

  @Output() wysokoscMinChange = new EventEmitter<number>();
  @Output() wysokoscMaxChange = new EventEmitter<number>();
  @Output() selectedWlasciwosciChange = new EventEmitter<WlasciwoscWithRelations[]>();

  ngOnChanges() {
    this.wlasciwosciResponse = this.processWlasciwosciResponse(this.wlasciwosciResponse);
  }

  isSelected(wlasciwosc: WlasciwoscWithRelations, nazwa: string): boolean {
    return this.selectedWlasciwosci.some(
      selected => selected.etykieta === wlasciwosc.etykieta && selected.nazwa === nazwa
    );
  }

  toggleWlasciwosc(wlasciwosc: WlasciwoscWithRelations, nazwa: string): void {
    const index = this.selectedWlasciwosci.findIndex(
      selected => selected.etykieta === wlasciwosc.etykieta && selected.nazwa === nazwa
    );

    if (index === -1) {
      const wlasciwoscWithRelacja = this.addRelacjaToWlasciwoscCauseIAmTooLazyToChangeTheBackend(wlasciwosc);
      if(wlasciwoscWithRelacja.relacja == undefined) {
        console.error('Relacja '+ wlasciwoscWithRelacja.relacja  + ' nie została znaleziona dla etykiety: ' + wlasciwosc.etykieta);
        return;
      }
      this.selectedWlasciwosci.push({ ...wlasciwoscWithRelacja, nazwa });
      console.log(this.selectedWlasciwosci);
    } else {
      this.selectedWlasciwosci.splice(index, 1);
    }

    this.selectedWlasciwosciChange.emit(this.selectedWlasciwosci);
  }



  onWysokoscMinChange() {
    if (this.wysokoscMin < this.wysokoscMinLimit) {
      this.wysokoscMin = this.wysokoscMinLimit;
    }
    if (this.wysokoscMin > this.wysokoscMax) {
      this.wysokoscMax = this.wysokoscMin;
    }
    this.wysokoscMinChange.emit(this.wysokoscMin);
  }

  onWysokoscMaxChange() {
    if (this.wysokoscMax > this.wysokoscMaxLimit) {
      this.wysokoscMax = this.wysokoscMaxLimit;
    }
    if (this.wysokoscMax < this.wysokoscMin) {
      this.wysokoscMin = this.wysokoscMax;
    }
    this.wysokoscMaxChange.emit(this.wysokoscMax);
  }


  private processWlasciwosciResponse(wlasciwosci: WlasciwoscResponse[]): WlasciwoscResponse[] {
    const processedWlasciwosci: WlasciwoscResponse[] = [];

    wlasciwosci.forEach(w => {
      if (w.etykieta === 'SilaWzrostu') {
        w.etykieta = 'Siła wzrostu';
      }

      if (w.etykieta === 'Okres') {
        const okresKwitnienia = { ...w, etykieta: 'Okres kwitnienia', nazwy: this.miesiace };
        const okresDojrzewania = { ...w, etykieta: 'Okres owocowania', nazwy: this.miesiace };

       // console.log(okresKwitnienia);
        processedWlasciwosci.push(okresKwitnienia, okresDojrzewania);
      } else if (w.etykieta === 'Kolor') {
        const okresKwitnienia = { ...w, etykieta: 'Kolor kwiatów', nazwy: w.nazwy };
        const okresDojrzewania = { ...w, etykieta: 'Kolor liści', nazwy: w.nazwy };

        //console.log(okresKwitnienia);
        processedWlasciwosci.push(okresKwitnienia, okresDojrzewania);
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


  private addRelacjaToWlasciwoscCauseIAmTooLazyToChangeTheBackend(wlasciwosc: WlasciwoscWithRelations): WlasciwoscWithRelations {
    const relacja = wlasciwosc.etykieta ? getRelacjaByEtykieta(wlasciwosc.etykieta) : undefined;
    return { ...wlasciwosc, relacja };
  }

}


