import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { TokenService } from '../../services/token/token.service';
import { CommonModule } from '@angular/common';
import e from 'express';
import { PowiadomienieCardComponent } from "../../modules/profil/components/powiadomienie-card/powiadomienie-card.component";
import { PowiadomieniaDropdownComponent } from "../../modules/profil/components/powiadomienia-dropdown/powiadomienia-dropdown.component";
import { UzytkownikService } from '../../services/services';
import { UzytkownikResponse } from '../../services/models';

import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { BellNotifComponent } from "../../modules/profil/components/bell-notif/bell-notif.component";
@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, NgbDropdownModule, PowiadomieniaDropdownComponent, BellNotifComponent],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  private _avatar: string | undefined;
  loggedIn: boolean = false;

  constructor(private router: Router,
    private tokenService: TokenService,
    private uzytService: UzytkownikService) {
    this.loggedIn = this.tokenService.isTokenValid();
  }

  ngOnInit(): void {

    this.loadAvatar();


    if (!this.tokenService.isTokenValid()) {
      this.loggedIn = false;
      this._avatar = undefined;
    }
  }

  public loadAvatar() {
   //  console.log('loadAvatar');
    if (this.tokenService.isTokenValid()) {
      this.uzytService.getAvatar().subscribe({
        next: (file) => {
          this._avatar = file.content;
        }
      });
    }
  }

  getUzytkownikNazwa(): string {
    if(this.tokenService.nazwa) {
    return this.tokenService.nazwa;
    } else {
      return 'Brak nazwy';
    }
  }

  getUzytAvatar(): string | undefined {
    //console.log('getUzytAvatar');
    if(this._avatar) {
      return 'data:image/jpeg;base64,' + this._avatar;
    } else {
      this.loadAvatar();
    }
    return this._avatar;
  }

  toLogin() {
    this.router.navigate(['login']);
  }

  toKatalog() {
    this.router.navigate(['rosliny']);
  }

  toOgrod() {
    const nazwa = this.tokenService.nazwa;
    if (nazwa) {
      //this.router.navigate([`ogrod/${nazwa}/dzialka/2`]);
      //this.router.navigate([`ogrod/${nazwa}`]);
    }
    else {
     // this.router.navigate([`ogrod/test/2`]);
    }
  }

  toSpolecznosc() {
    this.router.navigate(['spolecznosc']);
  }

  isLogged() {
    return this.tokenService.isTokenValid();
  }

  logout() {
    this._avatar = undefined;
    this.tokenService.clearToken();
    this.router.navigate(['login']);
  }


  // Routing
  goToProfil() {
    const nazwa = this.tokenService.nazwa;
    if (nazwa) {
      this.router.navigate([`/profil/${nazwa}`]);
    }
  }

  goToPowiadomieniaPage() {
    const nazwa = this.tokenService.nazwa;
    if (nazwa) {
      console.log('goToPowiadomieniaPage - start');
      this.router.navigate([`/profil/${nazwa}/powiadomienia`]);
    }

  }

  goToRozmowy() {
    const nazwa = this.tokenService.nazwa;
    if (nazwa) {
      this.router.navigate([`profil/${nazwa}/rozmowy`]);
    }
  }

  goToUstawienia() {
    const nazwa = this.tokenService.nazwa;
    if (nazwa) {
      this.router.navigate([`profil/${nazwa}/ustawienia`]);
    }
  }

  goToPosty() {
    const nazwa = this.tokenService.nazwa;
    if (nazwa) {
      this.router.navigate([`profil/${nazwa}/posty`]);
    }
  }

  goToKomentarze() {
    const nazwa = this.tokenService.nazwa;
    if (nazwa) {
      this.router.navigate([`profil/${nazwa}/komentarze`]);
    }
  }


  // Nie ma errora, chyba że przenosi się ze strony z działką
  testReferenceErrors() {
    //this.router.navigate(['ogrod/Piotr%20Wiśniewski/dzialka/2/przenoszenie/2732c898-a990-47b0-a48b-9d380c23a4e8']);
    this.router.navigate(['ogrod', this.tokenService.nazwa, 'dzialka', 2, 'przenoszenie', "2732c898-a990-47b0-a48b-9d380c23a4e8"]);
  }


}
