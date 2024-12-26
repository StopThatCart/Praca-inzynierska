import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WlasciwoscResponse, WlasciwoscWithRelations } from '../../../../services/models';
import { getRelacjaByEtykieta } from '../../models/roslina-relacje';
import { WlasciwoscProcessService } from '../../services/wlasciwosc-service/wlasciwosc.service';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-wlasciwosc-dropdown',
  standalone: true,
  imports: [CommonModule, FormsModule, NgbDropdownModule],
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

  @Output() wysokoscMinChange = new EventEmitter<number>();
  @Output() wysokoscMaxChange = new EventEmitter<number>();
  @Output() selectedWlasciwosciChange = new EventEmitter<WlasciwoscWithRelations[]>();

  constructor(private wlasciwoscProcessService: WlasciwoscProcessService) {}

  ngOnChanges() {

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
      const wlasciwoscWithRelacja = this.wlasciwoscProcessService.addRelacjaToWlasciwoscCauseIAmTooLazyToChangeTheBackend(wlasciwosc);
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

}


