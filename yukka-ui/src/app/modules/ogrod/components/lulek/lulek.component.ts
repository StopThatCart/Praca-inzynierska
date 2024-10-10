import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { PostResponse, RoslinaResponse } from '../../../../services/models';

@Component({
  selector: 'app-lulek',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lulek.component.html',
  styleUrl: './lulek.component.css'
})
export class LulekComponent {
  @Input() roslina: RoslinaResponse = {};
  private _roslinaObraz: string | undefined;

  getRoslina(): RoslinaResponse {
    return this.roslina;
  }

  setRoslina(roslina: RoslinaResponse) {
    this.roslina = roslina;
  }

  getRoslinaObraz(): string | undefined {
    if(this.roslina.obraz) {
      return 'data:image/jpeg;base64,' + this.roslina.obraz;
    }
    return this._roslinaObraz;
  }
}
