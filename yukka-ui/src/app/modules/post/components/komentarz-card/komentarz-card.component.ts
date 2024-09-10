import { Component, Input, OnInit } from '@angular/core';
import { KomentarzResponse, OcenaRequest } from '../../../../services/models';

import { KomentarzService } from '../../../../services/services';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-komentarz-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './komentarz-card.component.html',
  styleUrls: ['./komentarz-card.component.css']
})
export class KomentarzCardComponent implements OnInit {
  @Input()  komentarz:KomentarzResponse = {};
  @Input() depth: number = 0;
  private _komentarzObraz: string | undefined;
  private _komentarzAvatar: string | undefined;
  showOdpowiedzi: boolean = false;

  constructor(private router: Router, private komentarzService: KomentarzService) {}

  ngOnInit() {
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

  addOcenaToKomentarz(komentarzId: string | undefined, ocena: boolean) {
    if (komentarzId) {
      let ocenaRequest: OcenaRequest = { lubi: ocena, ocenialnyId: komentarzId };
      this.komentarzService.addOcenaToKomentarz({ body: ocenaRequest }).subscribe({
        next: (komentarz) => {
          console.log('Ocena dodana');
          console.log(komentarz);
          this.komentarz.ocenyLubi = komentarz.ocenyLubi;
          this.komentarz.ocenyNieLubi = komentarz.ocenyNieLubi;
        },
        error: (error: HttpErrorResponse) => {
          if (error.status === 403) {
            this.router.navigate(['/login']);
          } else {
            console.error('Wystąpił błąd:', error);
          }
        }
      });
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
