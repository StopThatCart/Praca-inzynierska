import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PostResponse, RoslinaResponse, ZasadzonaRoslinaResponse } from '../../../../services/models';
import bootstrap, { Offcanvas } from 'bootstrap';
import { DzialkaModes } from '../../models/dzialka-modes';

@Component({
  selector: 'app-lulek',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lulek.component.html',
  styleUrl: './lulek.component.css'
})
export class LulekComponent {
  @Input() zasadzonaRoslina: ZasadzonaRoslinaResponse = {};
  @Input() offcanvasId: string = '';
  @Input() mode: string = '';
  @Input() editMode: DzialkaModes = DzialkaModes.BrakEdycji;
  @Output() roslinaClick = new EventEmitter<ZasadzonaRoslinaResponse>();

  private _roslinaObraz: string | undefined;

  dzialkaModes = DzialkaModes;

  getRoslina(): RoslinaResponse {
    return this.zasadzonaRoslina.roslina || {};
  }

  setRoslina(roslina: RoslinaResponse) {
    this.zasadzonaRoslina = roslina;
  }

  getRoslinaObraz(): string | undefined {
    if(this.zasadzonaRoslina.roslina && this.zasadzonaRoslina.roslina.obraz) {
      return 'data:image/jpeg;base64,' + this.zasadzonaRoslina.obraz;
    }
    return this._roslinaObraz;
  }

  onRoslinaClick() {
    if(this.mode !== DzialkaModes.Pan && this.editMode === DzialkaModes.BrakEdycji) {
      this.roslinaClick.emit(this.zasadzonaRoslina);
    }

  }
}
