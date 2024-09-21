import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RoslinaService } from '../../../../services/services/roslina.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RoslinaResponse } from '../../../../services/models';

@Component({
  selector: 'app-upload-roslina-obraz-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './upload-roslina-obraz-page.component.html',
  styleUrl: './upload-roslina-obraz-page.component.css'
})
export class UploadRoslinaObrazPageComponent implements OnInit {
  roslina: RoslinaResponse | null = null;
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
      const nazwaLacinska = params['nazwaLacinska'];
      if (nazwaLacinska) {
        this.getRoslinaByNazwaLacinska(nazwaLacinska);
        this.route.snapshot.data['nazwaLacinska'] = nazwaLacinska;
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
        this.roslina = null;
        this.errorMsg.push('Nie znaleziono rośliny o podanej nazwie łacińskiej.');
      }
    });
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

    this.roslinaService.updateRoslinaObraz({
      "nazwa-lacinska": this.nazwaLacinska,
      body: { file: this.wybranyPlik } })
      .subscribe({
        next: () => { this.afterUploadRoslina(); },
        error: (error) => {
          this.message = 'Błąd podczas aktualizacji rośliny';
          this.handleErrors(error);
        }
      });
  }

  afterUploadRoslina(): void {
    this.message = 'Roślina została zaaktualizowana';
    this.clearImage();
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
