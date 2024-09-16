import { Component, Input } from '@angular/core';
import { KomentarzSimpleResponse } from '../../../../services/models/komentarz-simple-response';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-wiadomosc-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './wiadomosc-card.component.html',
  styleUrl: './wiadomosc-card.component.css'
})
export class WiadomoscCardComponent {
  @Input() wiadomosc: KomentarzSimpleResponse = {};

  private _komentarzObraz: string | undefined;
  private _komentarzAvatar: string | undefined;

  constructor() {}


  getKomentarz(): KomentarzSimpleResponse {
    return this.wiadomosc;
  }

  setKomentarz(wiadomosc: KomentarzSimpleResponse) {
    this.wiadomosc = wiadomosc;
  }


  getKomentarzObraz(): string | undefined {
    if(this.wiadomosc.obraz) {
      return 'data:image/jpeg;base64,' + this.wiadomosc.obraz;
    }
    return this._komentarzObraz;
  }

  getKomentarzAvatar(): string | undefined {
    if(this.wiadomosc.avatar) {
      return 'data:image/jpeg;base64,' + this.wiadomosc.avatar;
    }
    return this._komentarzAvatar;
  }

}
