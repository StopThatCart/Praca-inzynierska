import { Component, OnInit } from '@angular/core';
import { BlokResponse, StatystykiDto, UzytkownikResponse } from '../../../../services/models';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { UzytkownikService } from '../../../../services/services';
import { TokenService } from '../../../../services/token/token.service';
import { CommonModule } from '@angular/common';
import { RozmowaPrywatnaService } from '../../../../services/services/rozmowa-prywatna.service';
import { ZgloszenieButtonComponent } from "../../components/zgloszenie-button/zgloszenie-button.component";
import { TypPowiadomienia } from '../../models/TypPowiadomienia';
import { BanButtonComponent } from "../../components/ban-button/ban-button.component";
import { UsunKontoButtonComponent } from "../../components/usun-konto-button/usun-konto-button.component";
import { BlokButtonComponent } from "../../components/blok-button/blok-button.component";
import { isBlok } from '../../../../services/fn/uzytkownik/is-blok';
import { LoadingComponent } from '../../../../components/loading/loading.component';

@Component({
  selector: 'app-profil-page',
  standalone: true,
  imports: [CommonModule, RouterModule, ZgloszenieButtonComponent, BanButtonComponent, UsunKontoButtonComponent, BlokButtonComponent, LoadingComponent],
  templateUrl: './profil-page.component.html',
  styleUrl: './profil-page.component.css'
})
export class ProfilPageComponent implements OnInit {

  uzyt: UzytkownikResponse = {};
  nazwa: string | undefined;
  private _avatar: string | undefined;
  statystyki: StatystykiDto = {};

  errorMsg: string | null = null;


  isLoading: boolean = true;

  blokResponse: BlokResponse = {};
  zaproszony: boolean | undefined;
  isZaproszonyChecked: boolean = false;
  zaproszenieWyslane: boolean = false;

  typPowiadomienia = TypPowiadomienia;

  constructor(
    private tokenService: TokenService,
    private router: Router,
    private route: ActivatedRoute,
    private uzytService: UzytkownikService,
    private rozService: RozmowaPrywatnaService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.nazwa = params['nazwa'];
      if (this.nazwa) {
        this.getUzytkownikByNazwa(this.nazwa);
        this.route.snapshot.data['nazwa'] = this.nazwa;
      }
    });

  }

  getUzytkownikByNazwa(nazwa: string): void {
    this.uzytService.findByNazwa({ nazwa: nazwa }).subscribe({
      next: (uzyt) => {
        this.uzyt = uzyt;
        this.errorMsg = null;
        
        
      },
      error: (err) => {
        this.errorMsg = 'Nie znaleziono użytkownika o podanej nazwie.';
      }, 
      complete: () => {
        if (this.errorMsg && this.errorMsg.length > 0) {
        } else {
          this.getStatystykiOfUzytkownik(nazwa);
          this.getBlok();
        }
      }
    });
  }

  getAvatar(): string | undefined {
    if(this.uzyt && this.uzyt.avatar) {
      return 'data:image/jpeg;base64,' + this.uzyt.avatar;
    }
    return this._avatar;
  }

  getStatystykiOfUzytkownik(nazwa: string): void {
    //if(!this.tokenService.isTokenValid()) return;
    if(!this.uzyt.aktywowany) return;

    this.uzytService.getStatystykiOfUzytkownik({ nazwa: nazwa }).subscribe({
      next: (statystyki) => {
        this.statystyki = statystyki;
        this.errorMsg = null;
      },
      error: (err) => {
        console.log('Error: ', err);
      }
    });
  }

  getOcenyPozytywne(): number {
    let oceny = 0;
    if(!this.uzyt) {
      return oceny;
    } else if(this.statystyki.komentarzeOcenyPozytywne !== undefined && this.statystyki.postyOcenyPozytywne !== undefined) {
      oceny = (this.statystyki.komentarzeOcenyPozytywne + this.statystyki.postyOcenyPozytywne);
    }
    return oceny;
  }

  getOcenyNegatywne(): number {
    let oceny = 0;
    if(!this.uzyt) {
      return oceny;
    } else if(this.statystyki.komentarzeOcenyNegatywne !== undefined && this.statystyki.postyOcenyNegatywne !== undefined) {
      oceny = (this.statystyki.komentarzeOcenyNegatywne + this.statystyki.postyOcenyNegatywne);
    }
    return oceny;
  }

  isTokenAvailable(): boolean {
    return this.tokenService.isTokenValid();
  }

  isCurrentUser(): boolean {
    if(this.tokenService) {
      if(this.tokenService.nazwa === this.uzyt.nazwa) {
        return true;
      }
    }
    return false;
  }

  isPracownikOrAdmin(): boolean {
    if(this.tokenService) {
      if(this.tokenService.isPracownik() || this.tokenService.isAdmin()) {
        return true;
      }
    }
    return false;
  }

  isProfilUzytkownikPracownik(): boolean {
    if(this.uzyt && this.uzyt.labels?.includes('Pracownik')) {
      return true;
    }
    return false;
  }

  isProfilUzytkownikAdmin(): boolean {
    if(this.uzyt && this.uzyt.labels?.includes('Admin')) {
      return true;
    }
    return false;
  }

  checkIfZaproszony(): void {
    if(this.zaproszony) {
      return;
    }
  }

  isZaproszony(): boolean {
    if(!this.uzyt.aktywowany) return false;

    if (this.isZaproszonyChecked) {
      return this.zaproszony || false;
    }

    if (this.zaproszony) {
      this.isZaproszonyChecked = true;
      return this.zaproszony;
    }

    if (this.tokenService.token && this.uzyt.nazwa && this.tokenService.nazwa !== this.uzyt.nazwa) {
      this.rozService.findRozmowaPrywatnaByNazwa({ 'uzytkownik-nazwa': this.uzyt.nazwa })
        .subscribe({
          next: (roz) => {
            if (roz) {
              this.zaproszony = true;
            }
            this.isZaproszonyChecked = true;
            return this.zaproszony;
          },
          error: (err) => {
            if (err.status === 404 || err.error.businessErrorCode === 305) {
              this.isZaproszonyChecked = true;
              return null;
            }
            this.isZaproszonyChecked = true;
            return false;
          }
      });
    }
    return false;
  }

  getBlok() {
    if (this.tokenService.token && this.uzyt.nazwa && this.tokenService.nazwa !== this.uzyt.nazwa) {
      this.uzytService.isBlok({ nazwa: this.uzyt.nazwa }).subscribe({
        next: (res) => {
          this.blokResponse = res;
        },
        error: (err) => {
          console.log('Error: ', err.error);
          return false;
        }, 
        complete: () => {
          this.isLoading = false;
        }
      });
    } else {
      this.isLoading = false;
    }
    return false;
  }

  isBlok(): boolean {
    if (this.blokResponse.obojeBlokujacy) {
      return true;
    } else if (this.blokResponse.blok) {
      return true;
    }
    return false;
  }

  isBlokowany(): boolean {
    if (!this.blokResponse) return false;

    if (this.blokResponse.blok && this.blokResponse.blokujacy === this.tokenService.nazwa) {
      return true;
    }
    return false;
  }

  zaprosDoRozmowaPrywatna() {
    if(!this.uzyt.aktywowany) return;
    if (this.uzyt.nazwa) {
      this.rozService.inviteToRozmowaPrywatna({ 'uzytkownik-nazwa': this.uzyt.nazwa })
        .subscribe({
          next: (res) => {
            if(res) {
              console.log('Zaproszenie do rozmowy prywatnej wysłane');
              this.zaproszenieWyslane = true;
            } else {
              console.log('Coś poszło nie tak.');
            }
          },
          error: (err) => {
            console.log('Error: ', err.error);
          }
        });
    }
  }

}
