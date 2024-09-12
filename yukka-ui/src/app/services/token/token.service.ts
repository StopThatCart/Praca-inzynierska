import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Uzytkownik } from '../models';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
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

  clearToken() {
    if (this.isLocalStorageAvailable()) {
      localStorage.removeItem('token');
    }
  }

  isTokenValid() {
    const token = this.token;
    if (!token) {
      return false;
    }
    // decode the token
    const jwtHelper = new JwtHelperService();
    // check expiry date
    const isTokenExpired = jwtHelper.isTokenExpired(token);
    if (isTokenExpired) {
      this.clearToken();
      return false;
    }
    return true;
  }

  isTokenNotValid() {
    return !this.isTokenValid();
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

  get avatar(): string {
    const token = this.token;
    if (token) {
      const jwtHelper = new JwtHelperService();
      const decodedToken = jwtHelper.decodeToken(token);
      if (decodedToken.Avatar) {
        return 'data:image/jpeg;base64,' + decodedToken.Avatar;
      }
      return decodedToken.Avatar;
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
