import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WlasciwoscDropdownComponent } from '../../components/wlasciwosc-dropdown/wlasciwosc-dropdown.component';
import { WlasciwoscTagComponent } from '../../components/wlasciwosc-tag/wlasciwosc-tag.component';
import { WysokoscInputComponent } from '../../components/wysokosc-input/wysokosc-input.component';
import { RoslinaRequest, RoslinaResponse, WlasciwoscResponse, WlasciwoscWithRelations } from '../../../../services/models';
import { RoslinaService } from '../../../../services/services';
import { WlasciwoscProcessService } from '../../services/wlasciwosc-service/wlasciwosc.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-update-roslina-page',
  standalone: true,
  imports: [CommonModule, FormsModule, WlasciwoscDropdownComponent, WysokoscInputComponent, WlasciwoscTagComponent],
  templateUrl: './update-roslina-page.component.html',
  styleUrl: './update-roslina-page.component.css'
})
export class UpdateRoslinaPageComponent {
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

  roslina: RoslinaResponse = {};

  wybranyObraz: any;
  wybranyPlik: any;

  selectedWlasciwoscType: WlasciwoscResponse | null = null;
  customWlasciwoscName: string = '';

  constructor(
    private roslinaService: RoslinaService,
    private wlasciwoscProcessService: WlasciwoscProcessService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.fetchWlasciwosci();
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
        // TODO: przerobienie response na request
        this.errorMsg = [];
      },
      error: (err) => {
        this.roslina = {};
        this.errorMsg.push('Nie znaleziono rośliny o podanej nazwie łacińskiej.');
      }
    });
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

  updateRoslina(): void {
    this.errorMsg = [];
    this.message = '';
    this.request.nazwaLacinska = this.request.nazwaLacinska.toLowerCase();
    this.roslinaService.updateRoslina({ body: this.request }).subscribe({
      next: () => {
        this.afterUpdateRoslina();
      },
      error: (error) => {
        this.message = 'Błąd podczas aktualizacji rośliny';
        this.handleErrors(error);
      }
    });
  }

  afterUpdateRoslina(): void {
    this.message = 'Roślina została zaaktualizowana';
    this.request = {
      nazwa: '',
      nazwaLacinska: '',
      obraz: '',
      opis: '',
      wysokoscMin: 0,
      wysokoscMax: 100,
      wlasciwosci: [] as WlasciwoscWithRelations[],
    };
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
