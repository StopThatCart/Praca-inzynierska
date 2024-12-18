import { Component, EventEmitter, Input, Output, TemplateRef, ViewChild } from '@angular/core';
import { PowiadomienieResponse } from '../../../../services/models';
import { PowiadomienieService } from '../../../../services/services';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { PowiadomieniaDropdownComponent } from '../powiadomienia-dropdown/powiadomienia-dropdown.component';
import { TypPowiadomienia } from '../../enums/TypPowiadomienia';
import { PowiadomieniaSyncService } from '../../services/powiadomieniaSync/powiadomienia-sync.service';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { TokenService } from '../../../../services/token/token.service';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

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

  errorMsg : Array<string> = [];
  private _avatar: string | undefined;

  constructor(
    private powiadomienieService : PowiadomienieService,
    private powiadomieniaSyncService: PowiadomieniaSyncService,
    private errorHandlingService: ErrorHandlingService,
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
        case TypPowiadomienia.ZGLOSZENIE:
          this.router.navigate(['profil', this.pow.odnosnik]);
          break;
        case TypPowiadomienia.KOMENTARZ_POST:
        case TypPowiadomienia.POLUBIENIA_POST:
        case TypPowiadomienia.ZGLOSZENIE_KOMENTARZ:
        case TypPowiadomienia.ZGLOSZENIE_POST:
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
          this.router.navigate(['/ogrod', this.pow.odnosnik]);
          //throw new Error('NotImplementedYet: Typ powiadomienia nie jest obsługiwany');
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
      this.errorMsg = [];
      this.powiadomienieService.setPowiadomieniePrzeczytane({ id: this.pow.id }).subscribe({
        next: (pow) => {
          if(pow) {
            if( pow === null) {
              console.error('Powiadomienie nie istnieje');
              return;
            }
            console.log('Powiadomienie set: ', pow);
            this.pow.przeczytane = pow.przeczytane;
            this.powiadomieniePrzeczytane.emit(this.pow);
            this.powiadomieniaSyncService.notifyPowiadomieniePrzeczytane(this.pow);
          }
        },
        error: (error) => {
          this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
          alert(this.errorMsg);
          console.error('Error setting powiadomienie as przeczytane:', error);
        }
      });
    }
  }

  remove(event : Event) {
    this.errorMsg = [];
    event.stopPropagation();
    if (this.pow.id) {
      if(this.pow.typ === TypPowiadomienia.SPECJALNE || this.pow.typ === TypPowiadomienia.ZGLOSZENIE) {
        console.log('Nie można usunąć powiadomienia specjalnego');
        this.powiadomienieService.ukryjPowiadomienie({ id: this.pow.id }).subscribe({
          next: () => {
            this.powiadomienieUsuniete.emit(this.pow);
            this.powiadomieniaSyncService.notifyPowiadomienieUsuniete(this.pow);
          },
          error: (error) => {
            this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
            alert(this.errorMsg);
            console.error('Error :', error);
          }
        });
      } else {
        this.powiadomienieService.removePowiadomienie({ id: this.pow.id }).subscribe({
          next: () => {
            this.powiadomienieUsuniete.emit(this.pow);
            this.powiadomieniaSyncService.notifyPowiadomienieUsuniete(this.pow);
          },
          error: (error) => {
            this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
            alert(this.errorMsg);
            console.error('Error :', error);
          }
        });
      }
    }
  }

}
