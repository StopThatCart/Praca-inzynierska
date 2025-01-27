import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { CechaWithRelations } from '../../../../services/models';
import { RoslinaRelacje } from '../../models/roslina-relacje';
import { CechaEtykiety } from '../../models/cecha-etykiety';

@Component({
  selector: 'app-cecha-tag',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cecha-tag.component.html',
  styleUrl: './cecha-tag.component.css'
})
export class CechaTagComponent implements OnChanges {
  @Input() cechy: CechaWithRelations[] = [];
  sortedCechy: CechaWithRelations[] = [];

  @Output() cechaRemoved = new EventEmitter<number>();

  // Na siłe wklejone, ale działa
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['cechy']) {
      this.updateSortedCechy(this.cechy);
    }
  }

  updateSortedCechy(cechy: CechaWithRelations[]): void {
    this.sortedCechy = [...cechy].map(w => {
      if (w.relacja === RoslinaRelacje.MA_OKRES_KWITNIENIA) {
        w.etykieta = CechaEtykiety.OKRES_KWITNIENIA;
      } else if (w.relacja === RoslinaRelacje.MA_OKRES_OWOCOWANIA) {
        w.etykieta =  CechaEtykiety.OKRES_OWOCOWANIA;
      } else if (w.relacja === RoslinaRelacje.MA_KOLOR_KWIATOW) {
        w.etykieta = CechaEtykiety.KOLOR_KWIATOW;
      } else if (w.relacja === RoslinaRelacje.MA_KOLOR_LISCI) {
        w.etykieta = CechaEtykiety.KOLOR_LISCI;
      }
      return w;
    }).sort((a, b) => {
      if (a.etykieta && b.etykieta) {
        return a.etykieta.localeCompare(b.etykieta);
      }
      return 0;
    });
  }

  removeCecha(cecha: CechaWithRelations): void {
    let index = this.sortedCechy.findIndex(w =>
      w.etykieta === cecha.etykieta
      && w.nazwa === cecha.nazwa
      && w.relacja === cecha.relacja);
    if(index === -1) {
     // console.log('Nie znaleziono cechy do usuniecia');
      return;
    }
    this.sortedCechy.splice(index, 1);
    this.cechaRemoved.emit(index);
  }
}
