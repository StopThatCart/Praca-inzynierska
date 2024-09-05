import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { WlasciwoscWithRelations } from '../../../../services/models';
import { RoslinaRelacje } from '../../enums/roslina-relacje';

@Component({
  selector: 'app-wlasciwosc-tag',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './wlasciwosc-tag.component.html',
  styleUrl: './wlasciwosc-tag.component.css'
})
export class WlasciwoscTagComponent implements OnChanges {
  @Input() wlasciwosci: WlasciwoscWithRelations[] = [];
  sortedWlasciwosci: WlasciwoscWithRelations[] = [];

  @Output() wlasciwoscRemoved = new EventEmitter<number>();

  // Na siłe wklejone, ale działa
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['wlasciwosci']) {
      this.sortedWlasciwosci = [...this.wlasciwosci].map(w => {
        if (w.relacja === RoslinaRelacje.MA_OKRES_KWITNIENIA) {
          w.etykieta = 'Okres kwitnienia';
        } else if (w.relacja === RoslinaRelacje.MA_OKRES_OWOCOWANIA) {
          w.etykieta = 'Okres owocowania';
        } else if (w.relacja === RoslinaRelacje.MA_KOLOR_KWIATOW) {
          w.etykieta = 'Kolor kwiatów';
        } else if (w.relacja === RoslinaRelacje.MA_KOLOR_LISCI) {
          w.etykieta = 'Kolor liści';
        }
        return w;
      }).sort((a, b) => {
        if (a.etykieta && b.etykieta) {
          return a.etykieta.localeCompare(b.etykieta);
        }
        return 0;
      });
    }
  }

  updateSortedWlasciwosci(wlasciwosci: WlasciwoscWithRelations[]): void {
    this.sortedWlasciwosci = [...wlasciwosci].map(w => {
      if (w.relacja === RoslinaRelacje.MA_OKRES_KWITNIENIA) {
        w.etykieta = 'Okres kwitnienia';
      } else if (w.relacja === RoslinaRelacje.MA_OKRES_OWOCOWANIA) {
        w.etykieta = 'Okres owocowania';
      } else if (w.relacja === RoslinaRelacje.MA_KOLOR_KWIATOW) {
        w.etykieta = 'Kolor kwiatów';
      } else if (w.relacja === RoslinaRelacje.MA_KOLOR_LISCI) {
        w.etykieta = 'Kolor liści';
      }
      return w;
    }).sort((a, b) => {
      if (a.etykieta && b.etykieta) {
        return a.etykieta.localeCompare(b.etykieta);
      }
      return 0;
    });
  }

  removeWlasciwosc(wlasciwosc: WlasciwoscWithRelations): void {
    console.log('Usuwam wlasciwosc', wlasciwosc);

    let index = this.sortedWlasciwosci.findIndex(w =>
      w.etykieta === wlasciwosc.etykieta
      && w.nazwa === wlasciwosc.nazwa
      && w.relacja === wlasciwosc.relacja);
    if(index === -1) {
      console.log('Nie znaleziono wlasciwosci do usuniecia');
      return;
    }
    this.sortedWlasciwosci.splice(index, 1);
    this.wlasciwoscRemoved.emit(index);
  }
}