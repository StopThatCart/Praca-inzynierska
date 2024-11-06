import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PowiadomienieResponse } from '../../../../services/models';
import { PowiadomienieService } from '../../../../services/services';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { PowiadomieniaDropdownComponent } from '../powiadomienia-dropdown/powiadomienia-dropdown.component';
import { TypPowiadomienia } from '../../enums/TypPowiadomienia';
import { PowiadomieniaSyncService } from '../../services/powiadomieniaSync/powiadomienia-sync.service';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { TokenService } from '../../../../services/token/token.service';

@Component({
  selector: 'app-powiadomienie-card',
  standalone: true,
  imports: [CommonModule, RouterModule, NgbDropdownModule],
  templateUrl: './powiadomienie-card.component.html',
  styleUrl: './powiadomienie-card.component.css'
})
export class PowiadomienieCardComponent {
  @Input() pow: PowiadomienieResponse = {};
  @Input() isOnDropdown: boolean = false;
  @Output() powiadomienieUsuniete = new EventEmitter<PowiadomienieResponse>();
  @Output() powiadomieniePrzeczytane = new EventEmitter<PowiadomienieResponse>();

  private _avatar: string | undefined;

  constructor(
    private powiadomienieService : PowiadomienieService,
    private powiadomieniaSyncService: PowiadomieniaSyncService,
    private tokenService: TokenService,
    private router: Router
  ) {}

  getPowiadomienie(): PowiadomienieResponse {
    return this.pow;
  }

  setPowiadomienie(pow: PowiadomienieResponse) {
    this.pow = pow;
  }


  getAvatar(): string | undefined {
    if(this.pow.avatar) {
      return 'data:image/jpeg;base64,' + this.pow.avatar;
    }
    return this._avatar;
  }

  goToTarget() {
    console.log('goToTarget');
    console.log('pow: ', this.pow);
    if (this.pow.odnosnik && this.pow.typ) {
      // TODO: Nawigacja na bazie typu powiadomienia
      switch (this.pow.typ) {
        case TypPowiadomienia.GRATULACJE:
        case TypPowiadomienia.SPECJALNE:
        case TypPowiadomienia.BAN:
        case TypPowiadomienia.ZAPROSZENIE_ODRUCONE:
          break;
        case TypPowiadomienia.KOMENTARZ_POST:
        case TypPowiadomienia.POLUBIENIA_POST:
          this.router.navigate(['/posty', this.pow.odnosnik]);
          break;
        case TypPowiadomienia.ZAPROSZENIE:
          this.router.navigate(['profil/rozmowy']);
          break;
        case TypPowiadomienia.ZAPROSZENIE_ZAAKCEPTOWANE:
        case TypPowiadomienia.WIADOMOSC_PRYWATNA:
          this.router.navigate(['profil', this.tokenService.nazwa ,'rozmowy', this.pow.odnosnik]);
          break;

        case TypPowiadomienia.OWOCOWANIE_ROSLIN:
        case TypPowiadomienia.OWOCOWANIE_ROSLIN_TERAZ:
        case TypPowiadomienia.KWITNIENIE_ROSLIN_TERAZ:
        case TypPowiadomienia.PODLEWANIE_ROSLIN:
          //this.router.navigate(['/ogrod', this.pow.odnosnik]);
          throw new Error('NotImplementedYet: Typ powiadomienia nie jest obsługiwany');
          break;
        default:
          throw new Error('NotImplementedYet: Typ powiadomienia nie jest obsługiwany');
      }
    }
  }

  onHover() {
    console.log('onHover');
    if (!this.pow.przeczytane) {

      this.setPrzeczytane();
    }
  }

  setPrzeczytane() {
    if (this.pow.id) {
      this.powiadomienieService.setPowiadomieniePrzeczytane({ id: this.pow.id }).subscribe({
        next: (pow) => {
          if(pow) {
            console.log('Powiadomienie set: ', pow);
            this.pow.przeczytane = pow.przeczytane;
            this.powiadomieniePrzeczytane.emit(this.pow);
            this.powiadomieniaSyncService.notifyPowiadomieniePrzeczytane(this.pow);
          }
        },
        error: (error) => {
          console.error('Error setting powiadomienie as przeczytane:', error);
        }
      });
    }
  }

  remove(event : Event) {
    event.stopPropagation();
    if (this.pow.id) {
      this.powiadomienieService.remove1({ id: this.pow.id }).subscribe({
        next: () => {
          this.powiadomienieUsuniete.emit(this.pow);
          this.powiadomieniaSyncService.notifyPowiadomienieUsuniete(this.pow);
        },
        error: (error) => {
          console.error('Error removing powiadomienie:', error);
        }
      });
    }
  }




}
