import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ErrorMsgComponent } from '../../components/error-msg/error-msg.component';
import { AuthenticationService } from '../../services/services';
import { ErrorHandlingService } from '../../services/error-handler/error-handling.service';

@Component({
  selector: 'app-aktywacja-konta-refresh',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ErrorMsgComponent],
  templateUrl: './aktywacja-konta-refresh.component.html',
  styleUrl: './aktywacja-konta-refresh.component.css'
})
export class AktywacjaKontaRefreshComponent {
  errorMsg: Array<string> = [];
  message = '';

  email = '';

  constructor(
      private router: Router,
      private authService: AuthenticationService,
      private errorHandlingService: ErrorHandlingService
  ) {}

  sendEmail() {
    if (!this.email) return;
    this.errorMsg = [];
    this.message = '';

    this.authService.confirmResend({ email: this.email }).subscribe({
      next: () => {
        this.message = 'Wysłano nowy link aktywacyjny na podany adres email';
      },
      error: (error) => {
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
      }
    });
  }
}
