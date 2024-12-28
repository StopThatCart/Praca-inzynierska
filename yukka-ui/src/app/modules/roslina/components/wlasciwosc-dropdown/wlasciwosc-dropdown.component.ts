import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WlasciwoscKatalogResponse, WlasciwoscResponse, WlasciwoscWithRelations } from '../../../../services/models';
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
  @Input() wlasciwosciKatalogResponse: WlasciwoscKatalogResponse[] = [];

  @Input() selectedWlasciwosci: WlasciwoscWithRelations[] = [];
  @Input() textColor: string = 'black';

  @Output() selectedWlasciwosciChange = new EventEmitter<WlasciwoscWithRelations[]>();

  constructor(private wlasciwoscProcessService: WlasciwoscProcessService) {}

  ngOnChanges() {

  }

  isSelected(wlasciwosc: WlasciwoscWithRelations, nazwa: string | undefined): boolean {
    return this.selectedWlasciwosci.some(
      selected => selected.etykieta === wlasciwosc.etykieta && selected.nazwa === nazwa
    );
  }

  toggleWlasciwosc(wlasciwosc: WlasciwoscKatalogResponse | WlasciwoscWithRelations, nazwa: string | undefined): void {
    const index = this.selectedWlasciwosci.findIndex(
      selected => selected.etykieta === wlasciwosc.etykieta && selected.nazwa === nazwa
    );

    console.log(wlasciwosc);

    if (index === -1) {
      const newWlasciwoscWithRelacja = { ...wlasciwosc };
      if ('nazwyLiczbaRoslin' in newWlasciwoscWithRelacja) {
        delete newWlasciwoscWithRelacja.nazwyLiczbaRoslin;
      }

      const wlasciwoscWithRelacja = this.wlasciwoscProcessService.addRelacjaToWlasciwoscCauseIAmTooLazyToChangeTheBackend(newWlasciwoscWithRelacja);
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

  isWlasciwoscKatalogResponseArray(array: any[]): array is WlasciwoscKatalogResponse[] {
    let res = array.length > 0 && 'nazwyLiczbaRoslin' in array[0];
    //console.log(res);
    return res;
  }

}


