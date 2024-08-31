import { Component, Input } from '@angular/core';
import { RoslinaResponse } from '../../../../services/models';
import { Roslina } from '../../../../services/models/roslina';

@Component({
  selector: 'app-roslina-card',
  standalone: true,
  imports: [],
  templateUrl: './roslina-card.component.html',
  styleUrl: './roslina-card.component.css'
})
export class RoslinaCardComponent {
  @Input()  roslina:RoslinaResponse = {};
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

  goToRoslina(nazwaLacinska: string | undefined) {
   console.log('nazwaLacinska', nazwaLacinska);
   // this.router.navigate(['/roslina', this.nazwaLacinska]);

  }


}
