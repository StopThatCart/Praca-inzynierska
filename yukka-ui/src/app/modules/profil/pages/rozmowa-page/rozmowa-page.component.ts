import { Component } from '@angular/core';
import { EdycjaNavComponent } from "../../components/edycja-nav/edycja-nav.component";
import { AddKomentarzCardComponent } from "../../../post/components/add-komentarz-card/add-komentarz-card.component";
import { WiadomoscCardComponent } from "../../components/wiadomosc-card/wiadomosc-card.component";
import { PageResponseRozmowaPrywatnaResponse, RozmowaPrywatna, RozmowaPrywatnaResponse, UzytkownikResponse } from '../../../../services/models';
import { RozmowaPrywatnaService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from '../../../../services/token/token.service';
import { CommonModule } from '@angular/common';
import { TypKomentarza } from '../../../post/enums/TypKomentarza';

@Component({
  selector: 'app-rozmowa-page',
  standalone: true,
  imports: [CommonModule, EdycjaNavComponent, AddKomentarzCardComponent, WiadomoscCardComponent],
  templateUrl: './rozmowa-page.component.html',
  styleUrl: './rozmowa-page.component.css'
})
export class RozmowaPageComponent {
  rozmowa: RozmowaPrywatnaResponse = {};
  odbiorcaNazwa: string | undefined;

  rozmowaUzyt : UzytkownikResponse = {};

  private _avatar: string | undefined;

  errorMessage: string | null = null;

  public TypKomentarza = TypKomentarza;
  constructor(
    private rozService: RozmowaPrywatnaService,
    private router: Router,
    private route: ActivatedRoute,
    private tokenService: TokenService
  ) {

  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.odbiorcaNazwa = params['nazwa'];
      if (this.odbiorcaNazwa) {
        this.getRozmowa(this.odbiorcaNazwa);
        this.route.snapshot.data['nazwa'] = this.odbiorcaNazwa;
      }
    });
   // this.findAllPowiadomienia();
   // console.log(this.powResponse);
  }

  getRozmowa(nazwa: string): void {
    this.rozService.getRozmowaPrywatna({ nazwa: nazwa }).subscribe({
      next: (rozmowa) => {
        this.rozmowa = rozmowa;
        this.errorMessage = null;
        if(this.rozmowa.uzytkownicy) {
          const otherUzyt = this.rozmowa.uzytkownicy.find(user => user.nazwa === nazwa);
          if (otherUzyt) {
            this.rozmowaUzyt = otherUzyt;
            this._avatar = 'data:image/jpeg;base64,' + otherUzyt.avatar;
          }
        }
      },
      error: (err) => {
        this.errorMessage = 'Nie znaleziono rozmowy o podanym ID.';
      }
    });
  }

  getAvatar(): string | undefined {
    return this._avatar;
  }

  getNazwa(): string | undefined {
    return this.rozmowaUzyt.nazwa;
  }






}
