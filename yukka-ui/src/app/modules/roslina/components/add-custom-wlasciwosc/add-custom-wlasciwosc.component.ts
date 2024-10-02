import { Component, EventEmitter, Input, Output } from '@angular/core';
import { WlasciwoscResponse, WlasciwoscWithRelations } from '../../../../services/models';
import { WlasciwoscProcessService } from '../../services/wlasciwosc-service/wlasciwosc.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WlasciwoscEtykiety } from '../../enums/wlasciwosc-etykiety';

@Component({
  selector: 'app-add-custom-wlasciwosc',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-custom-wlasciwosc.component.html',
  styleUrl: './add-custom-wlasciwosc.component.css'
})
export class AddCustomWlasciwoscComponent {
  @Input() wlasciwosciResponse: WlasciwoscResponse[] = [];
  @Output() customWlasciwoscAdded = new EventEmitter<WlasciwoscWithRelations>();

  selectedWlasciwoscType: WlasciwoscResponse | null = null;
  customWlasciwoscName: string = '';

  constructor(private wlasciwoscProcessService: WlasciwoscProcessService) {}


  deployWlasciwosci(): WlasciwoscResponse[] {
    return this.wlasciwosciResponse.filter(wlasciwosc =>
      wlasciwosc.etykieta !==  WlasciwoscEtykiety.OKRES_KWITNIENIA && wlasciwosc.etykieta !== WlasciwoscEtykiety.OKRES_OWOCOWANIA
    );
  }


  addCustomWlasciwosc(): void {
    if (this.selectedWlasciwoscType && this.customWlasciwoscName.trim()) {
      let customWlasciwosc: WlasciwoscWithRelations = {
        etykieta: this.selectedWlasciwoscType.etykieta,
        nazwa: this.customWlasciwoscName.trim().toLowerCase(),
        relacja: ''
      };
      customWlasciwosc = this.wlasciwoscProcessService.addRelacjaToWlasciwoscCauseIAmTooLazyToChangeTheBackend(customWlasciwosc);
      this.customWlasciwoscAdded.emit(customWlasciwosc);
      this.customWlasciwoscName = '';
    }
  }

}
