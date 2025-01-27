import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CechaKatalogResponse, CechaResponse, CechaWithRelations } from '../../../../services/models';
import { CechaProcessService } from '../../services/cecha-service/cecha.service';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-cecha-dropdown',
  standalone: true,
  imports: [CommonModule, FormsModule, NgbDropdownModule],
  templateUrl: './cecha-dropdown.component.html',
  styleUrl: './cecha-dropdown.component.css'
})
export class CechaDropdownComponent {
  @Input() cechyResponse: CechaResponse[] = [];
  @Input() cechyKatalogResponse: CechaKatalogResponse[] = [];

  @Input() selectedCechy: CechaWithRelations[] = [];
  @Input() textColor: string = 'black';

  @Output() selectedCechyChange = new EventEmitter<CechaWithRelations[]>();

  constructor(private cechaProcessService: CechaProcessService) {}

  ngOnChanges() {

  }

  isSelected(cecha: CechaWithRelations, nazwa: string | undefined): boolean {
    return this.selectedCechy.some(
      selected => selected.etykieta === cecha.etykieta && selected.nazwa === nazwa
    );
  }

  toggleCecha(cecha: CechaKatalogResponse | CechaWithRelations, nazwa: string | undefined): void {
    const index = this.selectedCechy.findIndex(
      selected => selected.etykieta === cecha.etykieta && selected.nazwa === nazwa
    );

    if (index === -1) {
      const newCechaWithRelacja = { ...cecha };
      if ('nazwyLiczbaRoslin' in newCechaWithRelacja) {
        delete newCechaWithRelacja.nazwyLiczbaRoslin;
      }

      if ('nazwy' in newCechaWithRelacja) {
        delete newCechaWithRelacja.nazwy;
      }
      let cechus = { ...newCechaWithRelacja, nazwa };

      let cechaWithRelacja = this.cechaProcessService.addRelacjaToCecha(cechus);
      if(cechaWithRelacja.relacja == undefined) {
        console.error('Relacja '+ cechaWithRelacja.relacja  + ' nie została znaleziona dla etykiety: ' + cecha.etykieta);
        return;
      }
      
      
      //this.selectedCechy.push({ ...cechaWithRelacja, nazwa });
      this.selectedCechy.push({ ...cechaWithRelacja });
      //console.log(this.selectedCechy);
    } else {
      this.selectedCechy.splice(index, 1);
    }

    this.selectedCechyChange.emit(this.selectedCechy);
  }

  isCechaKatalogResponseArray(array: any[]): array is CechaKatalogResponse[] {
    let res = array.length > 0 && 'nazwyLiczbaRoslin' in array[0];
    //console.log(res);
    return res;
  }

}


