import { Component } from '@angular/core';
import { ErrorMsgComponent } from "../../components/error-msg/error-msg.component";
import { EmailRequest } from '../../services/models';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services';
import { ErrorHandlingService } from '../../services/error-handler/error-handling.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import e from 'express';

@Component({
  selector: 'app-zmiana-hasla-email',
  standalone: true,
  imports: [CommonModule, FormsModule, ErrorMsgComponent],
  templateUrl: './zmiana-hasla-email.component.html',
  styleUrl: './zmiana-hasla-email.component.css'
})
export class ZmianaHaslaEmailComponent {
  errorMsg: Array<string> = [];
  message = '';

  email: string = '';

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private errorHandlingService: ErrorHandlingService
  ) {}


  sendZmianaHaslaEmail() {
    this.errorMsg = [];
    this.message = '';

    this.authService.sendResetPasswordEmail({
      email: this.email
    }).subscribe({
      next: () => {
        this.message = 'Na podany adres email został wysłany link do zmiany hasła.';
        this.redirectToZmianaHasla();
      },
      error: (error) => {
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
      }
    });
  }

  redirectToZmianaHasla() {
    this.router.navigate(['zmiana-hasla']);
  }
}
