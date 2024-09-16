import { Component, Input, OnInit } from '@angular/core';
import { RozmowaPrywatnaResponse, UzytkownikResponse } from '../../../../services/models';
import { RozmowaPrywatnaService } from '../../../../services/services';
import { Router } from '@angular/router';
import { TokenService } from '../../../../services/token/token.service';

@Component({
  selector: 'app-rozmowa-card',
  standalone: true,
  imports: [],
  templateUrl: './rozmowa-card.component.html',
  styleUrl: './rozmowa-card.component.css'
})
export class RozmowaCardComponent implements OnInit {
  @Input() rozmowa: RozmowaPrywatnaResponse = {};

  private _avatar: string | undefined;
  selectedUzyt: UzytkownikResponse | undefined;

  constructor(
    private rozService : RozmowaPrywatnaService,
    private router: Router,
    private tokenService: TokenService
  ) {}

  ngOnInit() {
    this.setSelectedUzyt();
  }

  getRozmowa(): RozmowaPrywatnaResponse {
    return this.rozmowa;
  }

  setRozmowa(rozmowa: RozmowaPrywatnaResponse) {
    this.rozmowa = rozmowa;
  }

  goToRozmowa() {
    if (this.selectedUzyt) {
      this.router.navigate(['profil/rozmowy', this.selectedUzyt?.nazwa]);
    }
  }

  getAvatar(): string | undefined {
    if(this.rozmowa.uzytkownicy) {
      if(this.tokenService) {
        const loggedUzytNazwa = this.tokenService.nazwa;
        const otherUzyt = this.rozmowa.uzytkownicy.find(user => user.nazwa !== loggedUzytNazwa);

        if (otherUzyt && otherUzyt.avatar) {
          return 'data:image/jpeg;base64,' + otherUzyt.avatar;
        }
      }
    }
    return this._avatar;
  }

  private setSelectedUzyt() {
    if (this.rozmowa.uzytkownicy) {
      const loggedUzytNazwa = this.tokenService.nazwa;
      this.selectedUzyt = this.rozmowa.uzytkownicy.find(user => user.nazwa !== loggedUzytNazwa);
    }
  }



}
