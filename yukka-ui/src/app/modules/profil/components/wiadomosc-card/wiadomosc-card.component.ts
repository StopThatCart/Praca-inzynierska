import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { KomentarzSimpleResponse } from '../../../../services/models/komentarz-simple-response';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { KomentarzRequest } from '../../../../services/models';
import { KomentarzService } from '../../../../services/services';
import { TokenService } from '../../../../services/token/token.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

@Component({
  selector: 'app-wiadomosc-card',
  standalone: true,
  imports: [CommonModule,  FormsModule],
  templateUrl: './wiadomosc-card.component.html',
  styleUrl: './wiadomosc-card.component.css'
})
export class WiadomoscCardComponent implements OnInit {
  @Input() wiadomosc: KomentarzSimpleResponse = {};
  @Output() onRemove = new EventEmitter<any>();
  private _komentarzObraz: string | undefined;
  private _komentarzAvatar: string | undefined;

  canEdit: boolean = false;
  isEditing: boolean = false;
  editedOpis: string = '';

  errorMsg: Array<string> = [];

  private request: KomentarzRequest = {
    opis: '',
    targetId: this.wiadomosc.komentarzId ?? '',
    obraz: ''
  };

  constructor(
    private router: Router,
    private komentarzService: KomentarzService,
    private tokenService: TokenService,
    private errorHandlingService: ErrorHandlingService
  ) {}

  ngOnInit() {
    if(this.tokenService.isTokenValid()) {
      if (this.wiadomosc.uzytkownikNazwa && this.tokenService.hasAuthenticationRights(this.wiadomosc.uzytkownikNazwa)) {
        this.canEdit = true;
      }
    }
  }

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

  getKomentarzObrazSrcSet(): string | undefined {
    if (this.wiadomosc.obraz) {
      return `
        data:image/jpeg;base64,${this.wiadomosc.obraz} 1x,
        data:image/jpeg;base64,${this.wiadomosc.obraz} 2x
      `;
    }
    return undefined;
  }

  getKomentarzAvatar(): string | undefined {
    if(this.wiadomosc.avatar) {
      return 'data:image/jpeg;base64,' + this.wiadomosc.avatar;
    }
    return this._komentarzAvatar;
  }

  getKomentarzAvatarSrcSet(): string | undefined {
    if (this.wiadomosc.avatar) {
      return `
        data:image/jpeg;base64,${this.wiadomosc.avatar} 1x,
        data:image/jpeg;base64,${this.wiadomosc.avatar} 2x
      `;
    }
    return undefined;
  }

  goToProfil() {
    if (this.wiadomosc.uzytkownikNazwa) {
      this.router.navigate([`/profil/${this.wiadomosc.uzytkownikNazwa}`]);
    }
  }


  startEditing() {
    this.isEditing = true;
    this.editedOpis = this.wiadomosc.opis || '';
  }

  cancelEditing() {
    this.errorMsg = [];
    this.isEditing = false;
    this.editedOpis = '';
  }

  confirmEditing() {
    this.errorMsg = [];
    if (this.wiadomosc.komentarzId) {
      if(this.editedOpis === this.wiadomosc.opis) {
        this.isEditing = false;
        return;
      }

      this.request.targetId = this.wiadomosc.komentarzId;
      this.request.opis = this.editedOpis;
      this.komentarzService.updateKomentarz({
        'komentarz-id': this.wiadomosc.komentarzId,
        body: this.request
      }).subscribe({
        next: (res) => {
          console.log('Komentarz zaktualizowany');
          console.log(res);
          this.wiadomosc.opis = res.opis;
          this.wiadomosc.edytowany = res.edytowany;
          this.isEditing = false;
        },
        error: (err) => {
          this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
          this.cancelEditing();
        }
      });
    }
  }

  deleteKomentarz() {
    this.errorMsg = [];
    if(confirm("Czy aby na pewno chcesz usunąć ten komentarz?")) {
      if (this.wiadomosc.komentarzId) {
        this.komentarzService.removeKomentarz({ 'komentarz-id': this.wiadomosc.komentarzId }).subscribe({
          next: (res) => {
            console.log('Komentarz usunięty');
            console.log(res);
            this.onRemove.emit(this.wiadomosc.komentarzId);
          },
          error: (err) => {
            this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
          }
        });
      }
    }
  }

}
