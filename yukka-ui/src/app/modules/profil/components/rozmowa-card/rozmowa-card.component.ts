import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { RozmowaPrywatnaResponse, UzytkownikResponse } from '../../../../services/models';
import { RozmowaPrywatnaService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from '../../../../services/token/token.service';
import { CommonModule } from '@angular/common';
import { acceptRozmowaPrywatna } from '../../../../services/fn/rozmowa-prywatna/accept-rozmowa-prywatna';
import { error } from 'console';

@Component({
  selector: 'app-rozmowa-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './rozmowa-card.component.html',
  styleUrl: './rozmowa-card.component.css'
})
export class RozmowaCardComponent implements OnInit {
  @Input() rozmowa: RozmowaPrywatnaResponse = {};
  @Input() isBlokowany: boolean = false;
  @Input() isBlokujacy: boolean = false;
  @Output() onReject = new EventEmitter<string>();

  private _avatar: string | undefined;
  selectedUzyt: UzytkownikResponse | undefined;

  constructor(
    private rozService : RozmowaPrywatnaService,
    private router: Router,
    private route: ActivatedRoute,
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
      this.router.navigate([this.selectedUzyt?.nazwa], { relativeTo: this.route });
     // this.router.navigate(['profil/rozmowy', this.selectedUzyt?.nazwa]);
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

  isNadawca(): boolean | undefined {
    if (this.rozmowa.nadawca) {
      return this.rozmowa.nadawca === this.tokenService.uuid;
    }
    return undefined;
  }


  acceptRozmowaPrywatna(event : Event) {
    event.stopPropagation();
    if(this.selectedUzyt && this.selectedUzyt.nazwa) {
      this.rozService.acceptRozmowaPrywatna({ 'uzytkownik-nazwa': this.selectedUzyt.nazwa }).subscribe({
        next: (res) => {
          this.rozmowa.aktywna = true;
        },
        error: (err) => {
          console.error('Error while accepting rozmowa prywatna: ', err);
        }
      });
    }
  }

  rejectRozmowaPrywatna(event : Event) {
    event.stopPropagation();
    if(this.selectedUzyt && this.selectedUzyt.nazwa) {

      this.rozService.rejectRozmowaPrywatna({ "uzytkownik-nazwa": this.selectedUzyt.nazwa }).subscribe({
        next: () => {
          this.onReject.emit(this.selectedUzyt?.nazwa);
        },
        error: (err) => {
          console.error('Error while rejecting rozmowa prywatna: ', err);
        }
      });
    }
  }



}
