import { Component, Input } from '@angular/core';
import { RozmowaPrywatnaService, UzytkownikService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from '../../../../services/token/token.service';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-blok-button',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './blok-button.component.html',
  styleUrl: './blok-button.component.css'
})
export class BlokButtonComponent {
  @Input() odbiorcaNazwa: string | undefined;
  @Input() zablokowany: boolean = false;

  errorMsg: Array<string> = [];

  constructor(
      private rozService: RozmowaPrywatnaService,
      private uzytService: UzytkownikService,
      private router: Router,
      private route: ActivatedRoute,
      private tokenService: TokenService,
      private errorHandlingService: ErrorHandlingService
    ) {}

  zablokujUzyt() {
    if(!confirm("Czy aby na pewno chcesz zablokować użytkownika?")) {
      return;
    }

    if (this.odbiorcaNazwa) {
      this.errorMsg = [];
      this.uzytService.setBlokUzytkownik({ nazwa: this.odbiorcaNazwa, blok: true }).subscribe({
        next: (res) => {
          if(res) {
            window.location.reload();
          }
        },
        error: (err) => {
          this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
        }
      });

    }
  }

  odblokujUzyt() {
    if(!confirm("Czy aby na pewno chcesz odblokować użytkownika?")) {
      return;
    }

    if (this.odbiorcaNazwa) {
      this.errorMsg = [];
      this.uzytService.setBlokUzytkownik({ nazwa: this.odbiorcaNazwa, blok: false }).subscribe({
        next: (res) => {
          if(res) {
            window.location.reload();
          }
        },
        error: (err) => {
          this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
        }
      });
    }
  }
}
