import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RoslinaService } from '../../../../services/services/roslina.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RoslinaResponse } from '../../../../services/models';
import { BreadcrumbComponent } from '../../../../components/breadcrumb/breadcrumb.component';
import { RoslinaWlasnaService } from '../../../../services/services';
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

  uuid: string = '';

  wybranyPlik: any = null;

  message = '';
  errorMsg: Array<string> = [];

  constructor(
    private roslinaService: RoslinaService,
    private roslinaWlasnaService: RoslinaWlasnaService,
    private errorHandlingService: ErrorHandlingService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.uuid = params['uuid'];
      if (this.uuid) {
        this.getRoslinaByUuid(this.uuid);
        this.route.snapshot.data['uuid'] = this.uuid;
      }
    });
  }

  getRoslinaByUuid(uuid: string): void {
    this.roslinaService.findByUuid({ uuid: uuid }).subscribe({
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
      this.uploadRoslinaWlasnaObraz();
      return;
    }

    this.roslinaService.updateRoslinaObraz({
      uuid: this.uuid,
      body: { file: this.wybranyPlik } })
      .subscribe({
        next: () => {
          this.router.navigate(['/rosliny', this.uuid]);
        },
        error: (error) => {
          this.message = 'Błąd podczas aktualizacji rośliny';
          this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
        }
      });
  }

  uploadRoslinaWlasnaObraz(): void {
    this.roslinaWlasnaService.updateObraz({
      uuid: this.uuid,
      body: { file: this.wybranyPlik } })
      .subscribe({
        next: () => {
          this.router.navigate(['/rosliny', this.roslina.uuid]);
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
