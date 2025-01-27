import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PostResponse, RoslinaResponse, ZasadzonaRoslinaResponse } from '../../../../services/models';
import bootstrap, { Offcanvas } from 'bootstrap';
import { DzialkaModes } from '../../models/dzialka-modes';

@Component({
  selector: 'app-roslina-tile-circle',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './roslina-tile-circle.component.html',
  styleUrl: './roslina-tile-circle.component.css'
})
export class RoslinaTileCircleComponent {
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
    let baza = 'data:image/jpeg;base64,';
    if(this.zasadzonaRoslina) {
      if(this.zasadzonaRoslina.obraz) {
        return baza + this.zasadzonaRoslina.obraz
      }else if(this.zasadzonaRoslina.roslina && this.zasadzonaRoslina.roslina.obraz) {
        return baza + this.zasadzonaRoslina.roslina.obraz
      }

    }
    return this._roslinaObraz;
  }

  onRoslinaClick() {
    if(this.mode !== DzialkaModes.Pan && this.editMode === DzialkaModes.BrakEdycji) {
      this.roslinaClick.emit(this.zasadzonaRoslina);
      console.log(this.zasadzonaRoslina);
    }

  }
}
