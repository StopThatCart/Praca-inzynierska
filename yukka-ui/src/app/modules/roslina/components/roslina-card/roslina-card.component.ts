import { Component, Input } from '@angular/core';
import { RoslinaResponse } from '../../../../services/models';
import { Roslina } from '../../../../services/models/roslina';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-roslina-card',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './roslina-card.component.html',
  styleUrl: './roslina-card.component.css'
})
export class RoslinaCardComponent {
  @Input()  roslina:RoslinaResponse = {};
  private _roslinaObraz: string | undefined;

  constructor(private router: Router) {}

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
    if (nazwaLacinska) {
      this.router.navigate(['/rosliny', nazwaLacinska]);
    }
  }


}
