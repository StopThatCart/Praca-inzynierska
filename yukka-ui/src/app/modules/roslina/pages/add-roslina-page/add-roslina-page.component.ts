import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WlasciwoscDropdownComponent } from '../../components/wlasciwosc-dropdown/wlasciwosc-dropdown.component';
import { WlasciwoscResponse } from '../../../../services/models/wlasciwosc-response';
import { RoslinaRequest, WlasciwoscWithRelations } from '../../../../services/models';
import { RoslinaService } from '../../../../services/services';
import { Router } from '@angular/router';
import { SpinnerComponent } from '../../../../services/LoaderSpinner/spinner/spinner.component';
import { WlasciwoscProcessService } from '../../services/wlasciwosc-service/wlasciwosc.service';
import { WysokoscInputComponent } from "../../components/wysokosc-input/wysokosc-input.component";
import { WlasciwoscTagComponent } from "../../components/wlasciwosc-tag/wlasciwosc-tag.component";

@Component({
  selector: 'app-add-roslina-page',
  standalone: true,
  imports: [CommonModule, FormsModule, WlasciwoscDropdownComponent, WysokoscInputComponent, WlasciwoscTagComponent],
  templateUrl: './add-roslina-page.component.html',
  styleUrl: './add-roslina-page.component.css'
})
export class AddRoslinaPageComponent implements OnInit {
  wlasciwosciResponse: WlasciwoscResponse[] = [];
  message = '';
  errorMsg: Array<string> = [];

  @ViewChild('fileInput') fileInput!: ElementRef;
  @ViewChild(WlasciwoscTagComponent) wlasciwoscTagComponent!: WlasciwoscTagComponent;

  request: RoslinaRequest = {
    nazwa: '',
    nazwaLacinska: '',
    obraz: '',
    opis: '',
    wysokoscMin: 0,
    wysokoscMax: 100,
    wlasciwosci: [] as WlasciwoscWithRelations[],
  };
  wybranyObraz: any;
  wybranyPlik: any;

  selectedWlasciwoscType: WlasciwoscResponse | null = null;
  customWlasciwoscName: string = '';

  constructor(
    private roslinaService: RoslinaService,
    private wlasciwoscProcessService: WlasciwoscProcessService,
    private router: Router
  ) {}

  onFileSelected(event: any) {
    this.wybranyPlik = event.target.files[0];

     if (this.wybranyPlik) {
       this.request.obraz = this.wybranyPlik.name;

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
     this.request.obraz = '';
     this.fileInput.nativeElement.value = '';
  }

  ngOnInit(): void {
    this.fetchWlasciwosci();
  }

  onWysokoscMinChange(min: number) {
    this.request.wysokoscMin = min;
  }

  onWysokoscMaxChange(max: number) {
    this.request.wysokoscMax = max;
  }

  onWlasciwoscToggled(wlasciwosci: WlasciwoscWithRelations[]): void {
    console.log('Wlasciwosci toggled:', wlasciwosci);
    this.request.wlasciwosci = wlasciwosci;
    this.wlasciwoscTagComponent.updateSortedWlasciwosci(wlasciwosci);
  }

  onWlasciwoscRemoved(index: number): void {
    console.log('Removing wlasciwosc at index:', index);
    this.request.wlasciwosci.splice(index, 1);
    console.log('Request after removing:', this.request);
  }

  fetchWlasciwosci(): void {
    this.roslinaService.getWlasciwosciWithRelations().subscribe({
      next: (response) => {
        this.wlasciwosciResponse = response;
        console.log("Konwertowanie wlasciwosci");
        this.wlasciwosciResponse = this.wlasciwoscProcessService.processWlasciwosciResponse(this.wlasciwosciResponse);
      },
      error: (error) => {
        this.message = 'Błąd podczas pobierania właściwości';
      }
    });
  }

  addCustomWlasciwosc(): void {
    if (this.selectedWlasciwoscType && this.customWlasciwoscName.trim()) {
      let customWlasciwosc: WlasciwoscWithRelations = {
        etykieta: this.selectedWlasciwoscType.etykieta,
        nazwa: this.customWlasciwoscName.trim().toLowerCase(),
        relacja: ''
      };
      customWlasciwosc = this.wlasciwoscProcessService.addRelacjaToWlasciwoscCauseIAmTooLazyToChangeTheBackend(customWlasciwosc);
      this.request.wlasciwosci.push(customWlasciwosc);
      this.wlasciwoscTagComponent.updateSortedWlasciwosci(this.request.wlasciwosci);
      this.customWlasciwoscName = '';
    }
  }

  addRoslina(): void {
    this.errorMsg = [];
    this.message = '';
    this.request.nazwaLacinska = this.request.nazwaLacinska.toLowerCase();
    if(this.request.obraz === '') {
      this.roslinaService.saveRoslina2$Json({ body: this.request }).subscribe({
        next: () => {
          this.afterAddRoslina();
        },
        error: (error) => {
          this.message = 'Błąd podczas dodawania rośliny';
          this.handleErrors(error);
        }
      });
    } else {
      this.roslinaService.saveRoslina2$FormData({ body: { request: this.request, file: this.wybranyPlik } }).subscribe({
        next: () => {
          this.afterAddRoslina();
        },
        error: (error) => {
          this.message = 'Błąd podczas dodawania rośliny';
          this.handleErrors(error);
        }
      });
    }

  }

  afterAddRoslina(): void {
    this.message = 'Roślina została dodana pomyślnie';
    this.request = {
      nazwa: '',
      nazwaLacinska: '',
      obraz: '',
      opis: '',
      wysokoscMin: 0,
      wysokoscMax: 100,
      wlasciwosci: [] as WlasciwoscWithRelations[],
    };
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
