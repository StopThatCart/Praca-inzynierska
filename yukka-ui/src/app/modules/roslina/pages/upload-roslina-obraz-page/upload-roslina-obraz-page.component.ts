import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RoslinaService } from '../../../../services/services/roslina.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RoslinaRequest, RoslinaResponse, UzytkownikRoslinaRequest } from '../../../../services/models';
import { BreadcrumbComponent } from '../../../../components/breadcrumb/breadcrumb.component';
import { UzytkownikRoslinaService } from '../../../../services/services';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { ImageUploadComponent } from "../../../../components/image-upload/image-upload.component";

@Component({
  selector: 'app-upload-roslina-obraz-page',
  standalone: true,
  imports: [CommonModule, FormsModule, BreadcrumbComponent, ImageUploadComponent],
  templateUrl: './upload-roslina-obraz-page.component.html',
  styleUrl: './upload-roslina-obraz-page.component.css'
})
export class UploadRoslinaObrazPageComponent implements OnInit {
  roslina: RoslinaResponse = {};
  private _roslinaObraz: string | undefined;

  roslinaId: string = '';

  wybranyPlik: any = null;

  message = '';
  errorMsg: Array<string> = [];

  constructor(
    private roslinaService: RoslinaService,
    private uzytkownikRoslinaService: UzytkownikRoslinaService,
    private errorHandlingService: ErrorHandlingService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.roslinaId = params['roslina-id'];
      if (this.roslinaId) {
        this.getRoslinaByRoslinaId(this.roslinaId);
        this.route.snapshot.data['roslina-id'] = this.roslinaId;
      }
    });
  }

  getRoslinaByRoslinaId(roslinaId: string): void {
    this.roslinaService.findByRoslinaId({ 'roslina-id': roslinaId }).subscribe({
      next: (roslina) => {
        this.roslina = roslina;
        this.errorMsg = [];
      },
      error: (err) => {
        this.roslina = {};
        this.errorMsg.push('Nie znaleziono rośliny o podanym id.');
      }
    });
  }

  getRoslinaObraz(): string | undefined {
    if(this.roslina.obraz) {
      return 'data:image/jpeg;base64,' + this.roslina.obraz;
    }
    return this._roslinaObraz;
  }

  onFileSelected(file: File) {
    this.wybranyPlik = file;
  }

  clearImage() {
    this.wybranyPlik = null;
  }

  onSubmit() {
    if (!this.wybranyPlik) return;
    this.uploadRoslinaObraz();
  }

  uploadRoslinaObraz(): void {
    this.errorMsg = [];
    this.message = '';

    if (this.roslina.roslinaUzytkownika) {
      this.uploadUzytkownikRoslinaObraz();
      return;
    } else if (!this.roslina.nazwaLacinska) return;

    this.roslinaService.updateRoslinaObraz({
      'nazwa-lacinska': this.roslina.nazwaLacinska,
      body: { file: this.wybranyPlik } })
      .subscribe({
        next: () => {
          //this.message = 'Roślina została zaaktualizowana';
         // this.clearImage();
          this.router.navigate(['/rosliny', this.roslinaId]);
        },
        error: (error) => {
          this.message = 'Błąd podczas aktualizacji rośliny';
          this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
        }
      });
  }

  uploadUzytkownikRoslinaObraz(): void {
    this.uzytkownikRoslinaService.updateObraz({
      roslinaId: this.roslinaId,
      body: { file: this.wybranyPlik } })
      .subscribe({
        next: () => {
          this.message = 'Roślina została zaaktualizowana';
          this.router.navigate(['/rosliny', this.roslina.roslinaId]);
        },
        error: (error) => {
          this.message = 'Błąd podczas aktualizacji rośliny';
          this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
        }
      });
  }

  delet() {
    if(confirm('Czy na pewno chcesz zmienić obraz na domyślny?')) {
      this.wybranyPlik = null;
      this.uploadRoslinaObraz();
    }
  }

}
