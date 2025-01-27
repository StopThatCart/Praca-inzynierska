import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CechaDropdownComponent } from '../../components/cecha-dropdown/cecha-dropdown.component';
import { CechaTagComponent } from '../../components/cecha-tag/cecha-tag.component';
import { WysokoscInputComponent } from '../../components/wysokosc-input/wysokosc-input.component';
import { RoslinaRequest, RoslinaResponse, CechaResponse, CechaWithRelations } from '../../../../services/models';
import { RoslinaService, RoslinaWlasnaService } from '../../../../services/services';
import { CechaProcessService } from '../../services/cecha-service/cecha.service';
import { ActivatedRoute, Router } from '@angular/router';

import { AddCustomCechaComponent } from '../../components/add-custom-cecha/add-custom-cecha.component';
import { BreadcrumbComponent } from '../../../../components/breadcrumb/breadcrumb.component';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

@Component({
  selector: 'app-update-roslina-page',
  standalone: true,
  imports: [CommonModule, FormsModule,
    CechaDropdownComponent,
    WysokoscInputComponent,
    CechaTagComponent,
    AddCustomCechaComponent,
    BreadcrumbComponent],
  templateUrl: './update-roslina-page.component.html',
  styleUrl: './update-roslina-page.component.css'
})
export class UpdateRoslinaPageComponent {
  cechyResponse: CechaResponse[] = [];
  message = '';
  errorMsg: Array<string> = [];

  @ViewChild(CechaTagComponent) cechaTagComponent!: CechaTagComponent;

  request: RoslinaRequest = {
    uuid: '',
    nazwa: '',
    nazwaLacinska: '',
    obraz: '',
    opis: '',
    wysokoscMin: 0,
    wysokoscMax: 100,
    cechy: [] as CechaWithRelations[],
  };

  roslina: RoslinaResponse = {};

  wybranyObraz: any;
  wybranyPlik: any;

  selectedCechaType: CechaResponse | null = null;
  customCechaName: string = '';

  constructor(
    private roslinaService: RoslinaService,
    private roslinaWlasnaService: RoslinaWlasnaService,
    private cechaProcessService: CechaProcessService,
    private errorHandlingService: ErrorHandlingService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.fetchCechy();
    this.route.params.subscribe(params => {
      const uuid = params['uuid'];
      if (uuid) {
        this.getRoslinaByUuid(uuid);
        this.route.snapshot.data['uuid'] = uuid;
      }
    });
  }

  getRoslinaByUuid(uuid: string): void {
    this.roslinaService.findByUuid({ uuid: uuid }).subscribe({
      next: (roslina) => {
        this.roslina = roslina;
        this.request = this.cechaProcessService.convertRoslinaResponseToRequest(roslina);
        this.errorMsg = [];
      },
      error: (err) => {
        this.roslina = {};
        this.errorMsg.push('Nie znaleziono rośliny o podanym id.');
      }
    });
  }

  onWysokoscMinChange(min: number) {
    this.request.wysokoscMin = min;
  }

  onWysokoscMaxChange(max: number) {
    this.request.wysokoscMax = max;
  }

  onCechaToggled(cechy: CechaWithRelations[]): void {
    this.request.cechy = cechy;
    this.cechaTagComponent.updateSortedCechy(cechy);
  }

  onCechaRemoved(index: number): void {
   // console.log('Removing cecha at index:', index);
    this.request.cechy.splice(index, 1);
   // console.log('Request after removing:', this.request);
  }

  onCustomCechaAdded(customCecha: CechaWithRelations): void {
    this.request.cechy.push(customCecha);
    this.cechaTagComponent.updateSortedCechy(this.request.cechy);
  }

  fetchCechy(): void {
    this.roslinaService.getCechyWithRelations().subscribe({
      next: (response) => {
        this.cechyResponse = response;
        this.cechyResponse = this.cechaProcessService.processCechyResponse(this.cechyResponse);
      },
      error: (error) => {
        this.message = 'Błąd podczas pobierania cech';
      }
    });
  }

  updateRoslina(): void {
    if(!this.roslina.uuid) {
      return;
    } else if (!this.roslina.nazwaLacinska) return;

    this.errorMsg = [];
    this.message = '';

    this.request.nazwaLacinska = this.request.nazwaLacinska.toLowerCase();

    if (this.roslina.roslinaUzytkownika) {
      this.updateRoslinaWlasna(this.request);
      return;
    }
    
    this.roslinaService.updateRoslina({ uuid: this.roslina.uuid, body: this.request }).subscribe({
      next: () => {
        this.message = 'Roślina została zaaktualizowana';
        this.router.navigate(['/rosliny', this.roslina.uuid]);
      },
      error: (error) => {
        this.message = 'Błąd podczas aktualizacji rośliny';
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
      }
    });
  }

  updateRoslinaWlasna(request: RoslinaRequest): void {
    this.roslinaWlasnaService.update({ body: request }).subscribe({
      next: () => {
        this.message = 'Roślina została zaaktualizowana';
        this.router.navigate(['/rosliny', this.roslina.uuid]);
      },
      error: (error) => {
        this.message = 'Błąd podczas aktualizacji rośliny';
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
      }
    });
  }

}
