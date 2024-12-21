import { Component, Inject, PLATFORM_ID, signal } from '@angular/core';
import { AuthRequest } from '../../services/models/auth-request';
import { CommonModule, isPlatformServer } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthenticationService } from '../../services/services';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TokenService } from '../../services/token/token.service';
import { ErrorHandlingService } from '../../services/error-handler/error-handling.service';
import { ErrorMsgComponent } from "../../components/error-msg/error-msg.component";
import { LoadingComponent } from "../../components/loading/loading.component";
import { NavbarComponent } from '../../components/navbar/navbar.component';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ErrorMsgComponent, RouterModule, LoadingComponent],
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
    private route: ActivatedRoute,
    private authService: AuthenticationService,
    private tokenService: TokenService,
    private errorHandlingService: ErrorHandlingService,
    @Inject(PLATFORM_ID) platformId: Object) {
      this.isServer = isPlatformServer(platformId);

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
          this.tokenService.refreshToken = res.refreshToken as string;
          this.router.navigate(['']);

        },
        error: (err) => {
          console.log(err);
          this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
        }
    });
  }

  register() {
    this.router.navigate(['register']);
  }

  redirectToZmianaHasla() {
    this.router.navigate(['zmiana-hasla-email']);
  }


}
