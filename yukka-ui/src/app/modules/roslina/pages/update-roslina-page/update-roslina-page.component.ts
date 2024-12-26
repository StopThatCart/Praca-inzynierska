import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WlasciwoscDropdownComponent } from '../../components/wlasciwosc-dropdown/wlasciwosc-dropdown.component';
import { WlasciwoscTagComponent } from '../../components/wlasciwosc-tag/wlasciwosc-tag.component';
import { WysokoscInputComponent } from '../../components/wysokosc-input/wysokosc-input.component';
import { RoslinaRequest, RoslinaResponse, UzytkownikRoslinaRequest, WlasciwoscResponse, WlasciwoscWithRelations } from '../../../../services/models';
import { RoslinaService } from '../../../../services/services';
import { WlasciwoscProcessService } from '../../services/wlasciwosc-service/wlasciwosc.service';
import { ActivatedRoute, Router } from '@angular/router';

import { AddCustomWlasciwoscComponent } from '../../components/add-custom-wlasciwosc/add-custom-wlasciwosc.component';
import { BreadcrumbComponent } from '../../../../components/breadcrumb/breadcrumb.component';
import { UzytkownikRoslinaService } from '../../../../services/services/uzytkownik-roslina.service';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

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

  //roslinaId: string = '';

  wybranyObraz: any;
  wybranyPlik: any;

  selectedWlasciwoscType: WlasciwoscResponse | null = null;
  customWlasciwoscName: string = '';

  constructor(
    private roslinaService: RoslinaService,
    private uzytkownikRoslinaService: UzytkownikRoslinaService,
    private wlasciwoscProcessService: WlasciwoscProcessService,
    private errorHandlingService: ErrorHandlingService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.fetchWlasciwosci();
    this.route.params.subscribe(params => {
      const roslinaId = params['roslina-id'];
      if (roslinaId) {
        this.getRoslinaByRoslinaId(roslinaId);
        this.route.snapshot.data['roslina-id'] = roslinaId;
      }
    });
  }

  getRoslinaByRoslinaId(roslinaId: string): void {
    this.roslinaService.findByRoslinaId({ 'roslina-id': roslinaId }).subscribe({
      next: (roslina) => {
        this.roslina = roslina;

        this.request = this.wlasciwoscProcessService.convertRoslinaResponseToRequest(roslina);
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
    if(!this.roslina.roslinaId) {
      return;
    }
    this.errorMsg = [];
    this.message = '';

    if (this.roslina.roslinaUzytkownika) {
      this.updateUzytkownikRoslina(this.request);
      return;
    } else if (!this.roslina.nazwaLacinska) return;

    this.request.nazwaLacinska = this.request.nazwaLacinska.toLowerCase();

    this.roslinaService.updateRoslina({
      'nazwa-lacinska':this.roslina.nazwaLacinska,
      body: this.request })
      .subscribe({
        next: () => {
          this.message = 'Roślina została zaaktualizowana';
          //this.getRoslinaByNazwaLacinska(this.request.nazwaLacinska);
          this.router.navigate(['/rosliny', this.roslina.roslinaId]);
        },
        error: (error) => {
          this.message = 'Błąd podczas aktualizacji rośliny';
          this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
        }
      });
  }

  updateUzytkownikRoslina(request: RoslinaRequest): void {
    console.log("AKTUALIZACJA ROŚLINY UZYTKOWNIKA");
    let uzytRequest : UzytkownikRoslinaRequest = {
      roslinaId: this.roslina.roslinaId,
      nazwa: request.nazwa,
      obraz: '',
      opis: request.opis,
      wysokoscMin: request.wysokoscMin,
      wysokoscMax: request.wysokoscMax,
      wlasciwosci: request.wlasciwosci
    }

    console.log("Uzytkownik roslina request: ", uzytRequest);

    this.uzytkownikRoslinaService.updateRoslina1({ body: uzytRequest })
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

}
