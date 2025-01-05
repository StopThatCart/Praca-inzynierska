import { Component, ElementRef, ViewChild } from '@angular/core';
import { EdycjaNavComponent } from '../../components/edycja-nav/edycja-nav.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from '../../../../services/token/token.service';
import { UzytkownikResponse } from '../../../../services/models';
import { UzytkownikService } from '../../../../services/services';
import { ImageUploadComponent } from "../../../../components/image-upload/image-upload.component";
import { ErrorMsgComponent } from "../../../../components/error-msg/error-msg.component";
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

@Component({
  selector: 'app-edycja-avatar-page',
  standalone: true,
  imports: [CommonModule, FormsModule, EdycjaNavComponent, ImageUploadComponent, ErrorMsgComponent],
  templateUrl: './edycja-avatar-page.component.html',
  styleUrl: './edycja-avatar-page.component.css'
})
export class EdycjaAvatarPageComponent {
  uzyt: UzytkownikResponse = {};
  nazwa: string = '';
  private _avatar: string | undefined;

  wybranyPlik: any;

  message = '';
  errorMsg: Array<string> = [];

  @ViewChild('fileInput') fileInput!: ElementRef;

  constructor(
    private route : ActivatedRoute,
    private router: Router,
    private tokenService: TokenService,
    private uzytService: UzytkownikService,
  private errorHandlingService: ErrorHandlingService) {

  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.nazwa = params['nazwa'];
      if (this.nazwa) {
        this.getUzytkownikByNazwa(this.nazwa);
        this.route.snapshot.data['nazwa'] = this.nazwa;
      }
    });
  }

  getUzytkownikByNazwa(nazwa: string): void {
    this.uzytService.findByNazwa({ nazwa: nazwa }).subscribe({
      next: (uzyt) => {
        this.uzyt = uzyt;
        this.errorMsg = [];
      },
      error: (err) => {
        this.errorMsg.push('Nie znaleziono użytkownika o podanej nazwie.');
      }
    });
  }

  getAvatar(): string | undefined {
    if(this.uzyt && this.uzyt.avatar) {
      return 'data:image/jpeg;base64,' + this.uzyt.avatar;
    }
    return this._avatar;
  }

  onFileSelected(file: File) {
    this.wybranyPlik = file;
  }

  clearImage() {
    this.wybranyPlik = null;
  }

  uploadRoslinaObraz(): void {
    this.errorMsg = [];
    this.message = '';

    if (!this.wybranyPlik) {
      this.errorMsg.push('Nie wybrano pliku.');
      return;
    }
    console.log("nazwa łacińska: " + this.nazwa);

    this.uzytService.updateAvatar ({ body: { file: this.wybranyPlik } })
      .subscribe({
        next: () => {
          this.router.navigate(['profil', this.nazwa]);
          this.message = 'Yippie';
        },
        error: (error) => {
          this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
        }
      });
  }
}
