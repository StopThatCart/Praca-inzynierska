import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { TokenService } from '../../services/token/token.service';
import { CommonModule } from '@angular/common';
import e from 'express';
import { PowiadomienieCardComponent } from "../../modules/profil/components/powiadomienie-card/powiadomienie-card.component";
import { PowiadomieniaDropdownComponent } from "../../modules/profil/components/powiadomienia-dropdown/powiadomienia-dropdown.component";
import { UzytkownikService } from '../../services/services';
import { UzytkownikResponse } from '../../services/models';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, PowiadomienieCardComponent, PowiadomieniaDropdownComponent],
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
    this.tokenService.isLoggedIn.subscribe((status: boolean) => {
      this.loggedIn = status;
    });

    if (!this.tokenService.isTokenValid()) {
      this.loggedIn = false;
    }
  }

  private loadAvatar() {
   //  console.log('loadAvatar');
    if (this.tokenService.isTokenValid()) {
      this.uzytService.getAvatar().subscribe({
        next: (uzyt) => {
          this._avatar = uzyt.avatar;
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

  toSpolecznosc() {
    this.router.navigate(['spolecznosc']);
  }

  isLogged() {
    return this.loggedIn;
  }
  logout() {
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
      this.router.navigate([`/profil/powiadomienia`]);
    }

  }

  goToRozmowy() {
    this.router.navigate([`profil/rozmowy`]);
  }




}
