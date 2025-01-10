import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CechaResponse, CechaWithRelations } from '../../../../services/models';
import { CechaProcessService } from '../../services/cecha-service/cecha.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CechaEtykiety } from '../../models/cecha-etykiety';

@Component({
  selector: 'app-add-custom-cecha',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-custom-cecha.component.html',
  styleUrl: './add-custom-cecha.component.css'
})
export class AddCustomCechaComponent {
  @Input() cechyResponse: CechaResponse[] = [];
  @Output() customCechaAdded = new EventEmitter<CechaWithRelations>();

  selectedCechaType: CechaResponse | null = null;
  customCechaName: string = '';

  constructor(private cechaProcessService: CechaProcessService) {}


  deployCechy(): CechaResponse[] {
    return this.cechyResponse.filter(cecha =>
      cecha.etykieta !==  CechaEtykiety.OKRES_KWITNIENIA && cecha.etykieta !== CechaEtykiety.OKRES_OWOCOWANIA
    );
  }


  addCustomCecha(): void {
    if (this.selectedCechaType && this.customCechaName.trim()) {
      let customCecha: CechaWithRelations = {
        etykieta: this.selectedCechaType.etykieta,
        nazwa: this.customCechaName.trim().toLowerCase(),
        relacja: ''
      };
      customCecha = this.cechaProcessService.addRelacjaToCecha(customCecha);
      this.customCechaAdded.emit(customCecha);
      this.customCechaName = '';
    }
  }

}
