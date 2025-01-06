import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Uzytkownik, UzytkownikResponse } from '../models';
import { BehaviorSubject, switchMap } from 'rxjs';
import { AuthenticationService } from '../services';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor(private authService: AuthenticationService) { }

  private isLocalStorageAvailable(): boolean {
    return typeof localStorage !== 'undefined';
  }

  set token(token: string) {
    if (this.isLocalStorageAvailable()) {
      localStorage.setItem('token', token);
    }
  }

  get token(): string | null {
    if (this.isLocalStorageAvailable()) {
      return localStorage.getItem('token');
    }
    return null;
  }



  set refreshToken(token: string) {
    if (this.isLocalStorageAvailable()) {
      localStorage.setItem('refreshToken', token);
    }
  }

  get refreshToken(): string | null {
    if (this.isLocalStorageAvailable()) {
      return localStorage.getItem('refreshToken');
    }
    return null;
  }

  clearToken() {
    if (this.isLocalStorageAvailable()) {
      localStorage.removeItem('token');
    }
  }

  clearRefreshToken() {
    if (this.isLocalStorageAvailable()) {
      localStorage.removeItem('refreshToken');
    }
  }

  isTokenValid() {
    const token = this.token;
    const refreshToken = this.refreshToken;

    const jwtHelper = new JwtHelperService();
    const isTokenExpired = jwtHelper.isTokenExpired(token);
    const isRefreshTokenExpired = jwtHelper.isTokenExpired(refreshToken);


    if (!refreshToken) {
      this.clearToken();
      return false;
    }

    if (!token) {
      if (isRefreshTokenExpired) {
        this.clearRefreshToken();
        return false;
      }
      return this.getNewToken();
    }

    if (isRefreshTokenExpired) {
      this.clearRefreshToken();
      this.clearToken();
      return false;
    }

    if (isTokenExpired) {
      this.clearToken();
      if (!isRefreshTokenExpired) {
        return this.getNewToken();
      }
    }

    return true;
  }

  private getNewToken() : boolean {
    const jwtHelper = new JwtHelperService();
    if(!this.refreshToken || jwtHelper.isTokenExpired(this.refreshToken)) {
      return false;
    }
    //debugger;//
    this.authService.refreshToken({ 'X-Refresh-Token': this.refreshToken }).subscribe({
      next: (res) => {
        this.token = res.token as string;
        this.refreshToken = res.refreshToken as string;
        return true;
      },
      error: (err) => {
        console.log(err);
      }
    });

    return false;
  }

  isTokenNotValid() {
    return !this.isTokenValid();
  }

  isRefreshTokenValid() {
    const refreshToken = this.refreshToken;
    if (!refreshToken) {
      return false;
    }

    const jwtHelper = new JwtHelperService();
    const isRefreshTokenExpired = jwtHelper.isTokenExpired(refreshToken);

    if (isRefreshTokenExpired) {
      this.clearRefreshToken();
      return false;
    }

    return true;
  }

  get userRoles(): string[] {
    const token = this.token;
    if (token) {
      const jwtHelper = new JwtHelperService();
      const decodedToken = jwtHelper.decodeToken(token);
      return decodedToken.authorities;
    }
    return [];
  }

  get uzytId(): string {
    const token = this.token;
    if (token) {
      const jwtHelper = new JwtHelperService();
      const decodedToken = jwtHelper.decodeToken(token);
      return decodedToken.UzytId;
    }
    return '';
  }

  get nazwa(): string {
    const token = this.token;
    if (token) {
      const jwtHelper = new JwtHelperService();
      const decodedToken = jwtHelper.decodeToken(token);
      return decodedToken.Nazwa;
    }
    return '';
  }

  get email(): string {
    const token = this.token;
    if (token) {
      const jwtHelper = new JwtHelperService();
      const decodedToken = jwtHelper.decodeToken(token);
      return decodedToken.Email;
    }
    return '';
  }

  isAdmin(): boolean {
    return this.userRoles.includes('Admin');
  }

  isPracownik(): boolean {
    return this.userRoles.includes('Pracownik');
  }

  isNormalUzytkownik() {
    return !this.isAdmin() && !this.isPracownik();
  }

  isCurrentUzytkownik(targetUzyt: UzytkownikResponse): boolean {
    let hasRights = false;
    if(this.isAdmin()) {
      hasRights = true;
    } else if (this.isPracownik() && !targetUzyt.labels?.includes('Pracownik')) {
      hasRights = true;
    } else {
      hasRights = this.nazwa === targetUzyt.nazwa;
    }
    return hasRights;
  }

  hasAuthenticationRights(targetUzytNazwa: string): boolean {
    if(this.isAdmin()) {
        return true;
    }if (this.isPracownik()) {
        return true;
    } else {
        return this.nazwa === targetUzytNazwa;
    }
  }

}
