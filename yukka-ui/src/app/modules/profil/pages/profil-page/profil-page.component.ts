import { Component, OnInit } from '@angular/core';
import { StatystykiDto, Uzytkownik, UzytkownikResponse } from '../../../../services/models';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { PostService, UzytkownikService } from '../../../../services/services';
import { TokenService } from '../../../../services/token/token.service';
import { CommonModule } from '@angular/common';
import { RozmowaPrywatnaService } from '../../../../services/services/rozmowa-prywatna.service';
import { ZgloszenieButtonComponent } from "../../components/zgloszenie-button/zgloszenie-button.component";
import { TypPowiadomienia } from '../../models/TypPowiadomienia';
import { BanButtonComponent } from "../../components/ban-button/ban-button.component";
import { getStatystykiOfUzytkownik } from '../../../../services/fn/uzytkownik/get-statystyki-of-uzytkownik';
import { UsunKontoButtonComponent } from "../../components/usun-konto-button/usun-konto-button.component";

@Component({
  selector: 'app-profil-page',
  standalone: true,
  imports: [CommonModule, RouterModule, ZgloszenieButtonComponent, BanButtonComponent, UsunKontoButtonComponent],
  templateUrl: './profil-page.component.html',
  styleUrl: './profil-page.component.css'
})
export class ProfilPageComponent implements OnInit {
  uzyt: UzytkownikResponse = {};
  nazwa: string | undefined;
  private _avatar: string | undefined;
  statystyki: StatystykiDto = {};

  errorMsg: string | null = null;

  isCurrentUserBoi: boolean = false;



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
        this.getStatystykiOfUzytkownik(this.nazwa);
        this.getOcenyPozytywne();
      }
    });
    this.isCurrentUserBoi = this.isCurrentUser();
  }

  getUzytkownikByNazwa(nazwa: string): void {
    this.uzytService.findByNazwa({ nazwa: nazwa }).subscribe({
      next: (uzyt) => {
        this.uzyt = uzyt;
        this.errorMsg = null;
      },
      error: (err) => {
        this.errorMsg = 'Nie znaleziono użytkownika o podanej nazwie.';
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

    console.log('Pobieranie statystyk: ', nazwa);
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
    if (this.isZaproszonyChecked) {
      return this.zaproszony || false;
    }

    if (this.zaproszony) {
      this.isZaproszonyChecked = true;
      return this.zaproszony;
    }

    if (this.tokenService.token && this.uzyt.nazwa
      && this.tokenService.nazwa !== this.uzyt.nazwa) {

      this.rozService.getRozmowaPrywatna({ 'uzytkownik-nazwa': this.uzyt.nazwa })
        .subscribe({
          next: (roz) => {
            console.log('Rozmowa: ', roz);
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

  zaprosDoRozmowaPrywatna() {
    if (this.uzyt.nazwa) {
      this.rozService.inviteToRozmowaPrywatna({ 'uzytkownik-nazwa': this.uzyt.nazwa })
        .subscribe({
          next: (roz) => {
            if(roz) {
              console.log('Zaproszenie do rozmowy prywatnej wysłane: ', roz);
              this.zaproszenieWyslane = true;
            } else {
              console.log('Coś poszło nie tak.');
            }
          }
        });
    }
  }

  goToEdycjaProfil() {
    this.router.navigate(['edycja', 'profil'], { relativeTo: this.route });
  }

  goToRozmowa() {
    this.router.navigate(['rozmowy', this.uzyt.nazwa], { relativeTo: this.route });
  }



  goToRozmowy() {
    this.router.navigate(['rozmowy'], { relativeTo: this.route });
  }

  goToOgrod() {
    if (this.uzyt.nazwa) {
      this.router.navigate(['ogrod', this.uzyt.nazwa]);
    }
  }

  goToPosty() {
    if (this.uzyt.nazwa) {
      this.router.navigate(['posty'], { relativeTo: this.route });
    }
  }

  goToKomentarze() {
    if (this.uzyt.nazwa) {
      this.router.navigate(['komentarze'], { relativeTo: this.route });
    }
  }

}
