import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { RoslinaResponse, ZasadzonaRoslinaResponse } from '../../../../services/models';
import { CommonModule } from '@angular/common';
import { DzialkaModes } from '../../models/dzialka-modes';

@Component({
  selector: 'app-offcanvas-roslina',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './offcanvas-roslina.component.html',
  styleUrl: './offcanvas-roslina.component.css'
})
export class OffcanvasRoslinaComponent {
  @Input() zasadzonaRoslina: ZasadzonaRoslinaResponse | undefined;
  @Input() mode: string = '';
  @Input() editMode: string = '';
  @Output() roslinaPozycjaEdit = new EventEmitter<ZasadzonaRoslinaResponse>();
  @Output() roslinaKafelkiEdit = new EventEmitter<ZasadzonaRoslinaResponse>();

  @ViewChild('offcanvasBottom', { static: true }) offcanvasBottom!: ElementRef;


  private _roslinaObraz: string | undefined;

  getRoslina(): RoslinaResponse | undefined {
    return this.zasadzonaRoslina;
  }

  setRoslina(roslina: RoslinaResponse) {
    this.zasadzonaRoslina = roslina;
  }

  getRoslinaObraz(): string | undefined {
    if(this.zasadzonaRoslina?.roslina?.obraz) {
      return 'data:image/jpeg;base64,' + this.zasadzonaRoslina.roslina.obraz;
    }
    return this._roslinaObraz;
  }

  sendRoslinaPozycjaEditEvent() {
    if(this.editMode && this.editMode !== DzialkaModes.Edit) {
      this.roslinaPozycjaEdit.emit(this.zasadzonaRoslina);
    }
  }

  sendRoslinaKafelkiEditEvent() {
    if(this.editMode && this.editMode !== DzialkaModes.Edit) {
      this.roslinaKafelkiEdit.emit(this.zasadzonaRoslina);
    }
  }



}
