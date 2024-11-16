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

import { AddCustomWlasciwoscComponent } from '../../components/add-custom-wlasciwosc/add-custom-wlasciwosc.component';
import { BreadcrumbComponent } from '../../../../components/breadcrumb/breadcrumb.component';

@Component({
  selector: 'app-update-roslina-page',
  standalone: true,
  imports: [CommonModule, FormsModule,
    WlasciwoscDropdownComponent,
    WysokoscInputComponent,
    WlasciwoscTagComponent,
    AddCustomWlasciwoscComponent,
    BreadcrumbComponent],
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

  nazwaLacinska: string = '';

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
      const nazwaLacinska = params['nazwa-lacinska'];
      if (nazwaLacinska) {
        this.getRoslinaByNazwaLacinska(nazwaLacinska);
        this.route.snapshot.data['nazwa-lacinska'] = nazwaLacinska;
      }
    });
  }

  getRoslinaByNazwaLacinska(nazwaLacinska: string): void {
    this.roslinaService.findByNazwaLacinska({ 'nazwa-lacinska': nazwaLacinska }).subscribe({
      next: (roslina) => {
        this.roslina = roslina;

        this.request = this.wlasciwoscProcessService.convertRoslinaResponseToRequest(roslina);
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

  onCustomWlasciwoscAdded(customWlasciwosc: WlasciwoscWithRelations): void {
    this.request.wlasciwosci.push(customWlasciwosc);
    this.wlasciwoscTagComponent.updateSortedWlasciwosci(this.request.wlasciwosci);
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

  updateRoslina(): void {
    if(!this.roslina.nazwaLacinska) {
      return;
    }
    this.errorMsg = [];
    this.message = '';

    this.request.nazwaLacinska = this.request.nazwaLacinska.toLowerCase();

    this.roslinaService.updateRoslina({
      'nazwa-lacinska':this.roslina.nazwaLacinska,
      body: this.request })
      .subscribe({
        next: () => {
          this.message = 'Roślina została zaaktualizowana';
          //this.getRoslinaByNazwaLacinska(this.request.nazwaLacinska);
          this.router.navigate(['/rosliny', this.request.nazwaLacinska]);
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
