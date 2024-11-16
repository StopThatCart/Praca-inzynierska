import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RoslinaService } from '../../../../services/services/roslina.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RoslinaResponse } from '../../../../services/models';
import { BreadcrumbComponent } from '../../../../components/breadcrumb/breadcrumb.component';

@Component({
  selector: 'app-upload-roslina-obraz-page',
  standalone: true,
  imports: [CommonModule, FormsModule, BreadcrumbComponent],
  templateUrl: './upload-roslina-obraz-page.component.html',
  styleUrl: './upload-roslina-obraz-page.component.css'
})
export class UploadRoslinaObrazPageComponent implements OnInit {
  roslina: RoslinaResponse = {};
  private _roslinaObraz: string | undefined;

  nazwaLacinska: string = '';

  wybranyObraz: any;
  wybranyPlik: any;


  @ViewChild('fileInput') fileInput!: ElementRef;

  message = '';
  errorMsg: Array<string> = [];

  constructor(
    private roslinaService: RoslinaService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.nazwaLacinska = params['nazwa-lacinska'];
      if (this.nazwaLacinska) {
        this.getRoslinaByNazwaLacinska(this.nazwaLacinska);
        this.route.snapshot.data['nazwa-lacinska'] = this.nazwaLacinska;
      }
    });
  }

  getRoslinaByNazwaLacinska(nazwaLacinska: string): void {
    this.roslinaService.findByNazwaLacinska({ 'nazwa-lacinska': nazwaLacinska }).subscribe({
      next: (roslina) => {
        this.roslina = roslina;
        this.errorMsg = [];
      },
      error: (err) => {
        this.roslina = {};
        this.errorMsg.push('Nie znaleziono rośliny o podanej nazwie łacińskiej.');
      }
    });
  }

  getRoslinaObraz(): string | undefined {
    if(this.roslina.obraz) {
      return 'data:image/jpeg;base64,' + this.roslina.obraz;
    }
    return this._roslinaObraz;
  }

  onFileSelected(event: any) {
    this.wybranyPlik = event.target.files[0];

     if (this.wybranyPlik) {
       //this.request.obraz = this.wybranyPlik.name;
       const reader = new FileReader();
       reader.onload = () => {
         this.wybranyObraz = reader.result as string;
       };
       reader.readAsDataURL(this.wybranyPlik);
     }
  }

  clearImage() {
     this.wybranyObraz = null;
     this.wybranyPlik = null;
    // this.request.obraz = '';
     this.fileInput.nativeElement.value = '';
  }

  uploadRoslinaObraz(): void {
    this.errorMsg = [];
    this.message = '';

    console.log("nazwa łacińska: " + this.nazwaLacinska);

    this.roslinaService.updateRoslinaObraz({
      'nazwa-lacinska': this.nazwaLacinska,
      body: { file: this.wybranyPlik } })
      .subscribe({
        next: () => {
          //this.message = 'Roślina została zaaktualizowana';
         // this.clearImage();
          this.router.navigate(['/rosliny', this.nazwaLacinska]);
        },
        error: (error) => {
          this.message = 'Błąd podczas aktualizacji rośliny';
          this.handleErrors(error);
        }
      });
  }

  private handleErrors(err: any) {
    console.log(err);
    if(err.error.validationErrors) {
      this.errorMsg = err.error.validationErrors
    } else if (err.error.error) {
      this.errorMsg.push(err.error.error);
    } else {
      this.errorMsg.push(err.message);
    }
  }

}
