import { Component, OnInit} from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { TokenService } from '../../services/token/token.service';
import { CommonModule } from '@angular/common';
import { PowiadomieniaDropdownComponent } from "../../modules/profil/components/powiadomienia-dropdown/powiadomienia-dropdown.component";
import { UzytkownikService } from '../../services/services';

import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, NgbDropdownModule, PowiadomieniaDropdownComponent],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  private _avatar: string | undefined;

  uzytNazwa: string | undefined;

  constructor(private router: Router,
    private tokenService: TokenService,
    private uzytService: UzytkownikService) {
  }

  ngOnInit(): void {

    this.loadAvatar();


    // if (!this.tokenService.isTokenValid()) {
    //   this._avatar = undefined;
    //   this.uzytNazwa = undefined;
    // }
  }

  public loadAvatar() {
   //  console.log('loadAvatar');
    if (this.tokenService.isTokenValid()) {
      this.uzytService.getAvatar().subscribe({
        next: (file) => {
          this._avatar = file.content;
          this.uzytNazwa = this.tokenService.nazwa;
        }
      });
    }
  }

  getUzytkownikNazwa(): string {
    if(this.uzytNazwa) {
    return this.uzytNazwa;
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
    this.router.navigate(['/login']);
  }

  toKatalog() {
    this.router.navigate(['/rosliny']);
  }

  toOgrod() {
    if (this.uzytNazwa) {
      //this.router.navigate([`ogrod/${nazwa}/dzialka/2`]);
      //this.router.navigate([`ogrod/${nazwa}`]);
    }
    else {
     // this.router.navigate([`ogrod/test/2`]);
    }
  }

  toSpolecznosc() {
    this.router.navigate(['/spolecznosc']);
  }

  isLogged() {
    return this.tokenService.isTokenValid();
  }

  logout() {
    this._avatar = undefined;
    this.uzytNazwa = undefined;
    this.tokenService.clearToken();
    this.tokenService.clearRefreshToken();
    this.router.navigate(['/login']);
  }


  // Routing
  goToProfil() {
    if (this.uzytNazwa) {
      this.router.navigate([`/profil/${this.uzytNazwa}`]);
    }
  }

  goToPowiadomieniaPage() {
    if (this.uzytNazwa) {
      console.log('goToPowiadomieniaPage - start');
      this.router.navigate([`/profil/${this.uzytNazwa}/powiadomienia`]);
    }

  }

  goToRozmowy() {
    if (this.uzytNazwa) {
      this.router.navigate([`/profil/${this.uzytNazwa}/rozmowy`]);
    }
  }

  goToUstawienia() {
    if (this.uzytNazwa) {
      this.router.navigate([`/profil/${this.uzytNazwa}/ustawienia`]);
    }
  }

  goToPosty() {
    if (this.uzytNazwa) {
      this.router.navigate([`/profil/${this.uzytNazwa}/posty`]);
    }
  }

  goToKomentarze() {
    if (this.uzytNazwa) {
      this.router.navigate([`/profil/${this.uzytNazwa}/komentarze`]);
    }
  }


  // Nie ma errora, chyba że przenosi się ze strony z działką
  testReferenceErrors() {
    //this.router.navigate(['ogrod/Piotr%20Wiśniewski/dzialka/2/przenoszenie/2732c898-a990-47b0-a48b-9d380c23a4e8']);
    this.router.navigate(['ogrod', this.uzytNazwa, 'dzialka', 2, 'przenoszenie', "2732c898-a990-47b0-a48b-9d380c23a4e8"]);
  }


}
