import { Component, Inject, PLATFORM_ID, signal } from '@angular/core';
import { AuthRequest } from '../../services/models/auth-request';
import { CommonModule, isPlatformServer } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthenticationService } from '../../services/services';
import { Router } from '@angular/router';
import { TokenService } from '../../services/token/token.service';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  isUser = signal(false)
  isServer = false;


  authRequest: AuthRequest = {email: 'piotr@email.pl', haslo: 'piotr12345678'};
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService,
    @Inject(PLATFORM_ID) platformId: Object) {
      this.isServer = isPlatformServer(platformId);
      console.log('aqui')

      const userToken = this.tokenService.token;
      if(userToken){
        this.isUser.set(true)
      }
  }

  login() {
    this.errorMsg = [];
    this.authService.login({
      body: this.authRequest
    }).subscribe( {
        next: (res) => {
          console.log(res);
          this.tokenService.token = res.token as string;
          this.router.navigate(['']);
        },
        error: (err) => {
          console.log(err);
           if (err.error.validationErrors) {
            this.errorMsg = err.error.validationErrors;
          } else if (typeof err.error === 'string') {
            this.errorMsg.push(err.error);
          } else if (err.error.error) {
            this.errorMsg.push(err.error.error);
          } else {
            this.errorMsg.push(err.message);
          }
        }
    });
  }

  register() {
    this.router.navigate(['register']);
  }


}
