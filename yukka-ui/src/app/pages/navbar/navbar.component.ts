import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '../../services/token/token.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  constructor(private router: Router,
    private tokenService: TokenService) {

  }

  toLogin() {
    this.router.navigate(['login']);
  }

  toKatalog() {
    this.router.navigate(['katalog']);
  }

  toSpolecznosc() {
    this.router.navigate(['spolecznosc']);
  }

  isLogged() {
    return this.tokenService.isTokenValid();
  }
  logout() {
    this.tokenService.clearToken();
    this.router.navigate(['login']);
  }
}
