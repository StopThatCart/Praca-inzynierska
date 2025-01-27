import { Component, Input } from '@angular/core';
import { ErrorMsgComponent } from '../../../../components/error-msg/error-msg.component';
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { PracownikService } from '../../../../services/services/pracownik.service';
import { TokenService } from '../../../../services/token/token.service';
import { Router } from '@angular/router';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

@Component({
  selector: 'app-usun-konto-button',
  standalone: true,
  imports: [FormsModule, NgbTooltipModule],
  templateUrl: './usun-konto-button.component.html',
  styleUrl: './usun-konto-button.component.css'
})
export class UsunKontoButtonComponent {

  @Input() usuwany: string | undefined;

  errorMsg: Array<string> = [];
  message: string = '';

  constructor(
    private pracownikService: PracownikService,
    private tokenService: TokenService,
    private router: Router,
    private errorHandlingService: ErrorHandlingService
  ) {}

  usunUzytkownika() {
    if (this.usuwany === undefined) return;

    if(!confirm("Czy na pewno chcesz usunąć tego użytkownika?")) return;

    this.errorMsg = [];
    this.message = '';

    this.pracownikService.remove( { 'uzytkownik-nazwa': this.usuwany }).subscribe({
      next: (res) => {
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error('Błąd podczas usuwania użytkownika');
        this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
      }
    });
  }
}
