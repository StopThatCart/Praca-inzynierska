import { Component, Input, OnInit } from '@angular/core';
import { KomentarzRequest, KomentarzResponse, OcenaRequest } from '../../../../services/models';

import { KomentarzService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { TokenService } from '../../../../services/token/token.service';
import { FormsModule } from '@angular/forms';
import { AddKomentarzCardComponent } from "../add-komentarz-card/add-komentarz-card.component";
import { TypKomentarza } from '../../models/TypKomentarza';
import { ZgloszenieButtonComponent } from "../../../profil/components/zgloszenie-button/zgloszenie-button.component";
import { TypPowiadomienia } from '../../../profil/models/TypPowiadomienia';
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

@Component({
  selector: 'app-komentarz-card',
  standalone: true,
  imports: [CommonModule, FormsModule, AddKomentarzCardComponent, ZgloszenieButtonComponent, NgbTooltipModule],
  templateUrl: './komentarz-card.component.html',
  styleUrls: ['./komentarz-card.component.css']
})
export class KomentarzCardComponent implements OnInit {
  @Input()  komentarz:KomentarzResponse = {};
  @Input() depth: number = 0;

  typ: TypKomentarza = TypKomentarza.ODPOWIEDZ;
  typPowiadomienia = TypPowiadomienia;

  private _komentarzObraz: string | undefined;
  private _komentarzAvatar: string | undefined;
  showOdpowiedzi: boolean = false;

  canEdit: boolean = false;
  isEditing: boolean = false;
  editedOpis: string = '';

  isReplying: boolean = false;

  errorMsg: Array<string> = [];

  private request: KomentarzRequest = {
    opis: '',
    targetId: this.komentarz.komentarzId ?? '',
    obraz: ''
  };

  public TypKomentarza = TypKomentarza;
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private komentarzService: KomentarzService,
    private tokenService: TokenService,
    private errorHandlingService: ErrorHandlingService
  ) {}

  ngOnInit() {
    if(this.tokenService.isTokenValid()) {
      if (this.komentarz.uzytkownikNazwa && this.tokenService.hasAuthenticationRights(this.komentarz.uzytkownikNazwa)) {
        this.canEdit = true;
      }
    }

    if(this.komentarz.odpowiedzi) {
      if (this.komentarz.odpowiedzi.length >= 3) {
        this.showOdpowiedzi = false;
      } else if (this.depth >= 4 && this.komentarz.odpowiedzi.length > 0) {
        this.showOdpowiedzi = false;
      } else {
        this.showOdpowiedzi = true;
      }
    } else {
      this.showOdpowiedzi = true;
    }
  }

  getKomentarz(): KomentarzResponse {
    return this.komentarz;
  }

  setKomentarz(komentarz: KomentarzResponse) {
    this.komentarz = komentarz;
  }

  getKomentarzObraz(): string | undefined {
    if(this.komentarz.obraz) {
      return 'data:image/jpeg;base64,' + this.komentarz.obraz;
    }
    return this._komentarzObraz;
  }

  getKomentarzAvatar(): string | undefined {
    if(this.komentarz.avatar) {
      return 'data:image/jpeg;base64,' + this.komentarz.avatar;
    }
    return this._komentarzAvatar;
  }

  getPostIdFromPage(): string | undefined {
    let postId = this.route.snapshot.data['postId'];
    return postId;
  }

  goToProfil() {
    if (this.komentarz.uzytkownikNazwa) {
      this.router.navigate([`/profil/${this.komentarz.uzytkownikNazwa}`]);
    }
  }



  toggleReplying() {
    this.isReplying = !this.isReplying;
  }

  cancelOdpowiedz() {
    this.isReplying = false;
  }


  addOcenaToKomentarz(komentarzId: string | undefined, ocena: boolean) {
    this.errorMsg = [];
    if (komentarzId && this.tokenService) {
      // if(this.tokenService.nazwa === this.komentarz.uzytkownikNazwa) {
      //   return;
      // }
      let ocenaRequest: OcenaRequest = { lubi: ocena, ocenialnyId: komentarzId };
      this.komentarzService.addOcenaToKomentarz({ body: ocenaRequest }).subscribe({
        next: (komentarz) => {
          console.log('Ocena dodana');
          this.komentarz.ocenyLubi = komentarz.ocenyLubi;
          this.komentarz.ocenyNieLubi = komentarz.ocenyNieLubi;
        },
        error: (error: HttpErrorResponse) => {
          if (error.status === 403) {
            this.router.navigate(['/login']);
          } else {
            this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);;
          }
        }
      });
    }
  }

  startEditing() {
    this.isEditing = true;
    this.editedOpis = this.komentarz.opis || '';
  }

  cancelEditing() {
    this.errorMsg = [];
    this.isEditing = false;
    this.editedOpis = '';
  }

  confirmEditing() {
    this.errorMsg = [];
    if (this.komentarz.komentarzId) {
      if(this.editedOpis === this.komentarz.opis) {
        this.isEditing = false;
        return;
      }

      this.request.targetId = this.komentarz.komentarzId;
      this.request.opis = this.editedOpis;
      this.komentarzService.updateKomentarz({ body: this.request }).subscribe({
        next: (res) => {
          console.log('Komentarz zaktualizowany');
          console.log(res);
          this.komentarz.opis = res.opis;
          this.komentarz.edytowany = res.edytowany;
          this.isEditing = false;
        },
        error: (error) => {
          this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
          this.cancelEditing();
        }
      });
    }
  }

  deleteKomentarz() {
    this.errorMsg = [];
    if(confirm("Czy aby na pewno chcesz usunąć ten komentarz?")) {
      if (this.komentarz.komentarzId) {
        this.komentarzService.removeKomentarz({ 'komentarz-id': this.komentarz.komentarzId }).subscribe({
          next: (res) => {
            console.log('Komentarz usunięty');
            console.log(res);
            window.location.reload();
          },
          error: (error) => {
            this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
          }
        });
      }
    }
  }

  setStartMargin(): number {
    if(this.depth > 5) {
      return 0;
    }
    return Math.min(this.depth, 1);
  }

  toggleOdpowiedzi() {
    this.showOdpowiedzi = !this.showOdpowiedzi;
  }

}
