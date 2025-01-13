import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CechaDropdownComponent } from '../../components/cecha-dropdown/cecha-dropdown.component';
import { RoslinaRequest, CechaWithRelations, CechaResponse } from '../../../../services/models';
import { RoslinaService, RoslinaWlasnaService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { CechaProcessService } from '../../services/cecha-service/cecha.service';
import { WysokoscInputComponent } from "../../components/wysokosc-input/wysokosc-input.component";
import { CechaTagComponent } from "../../components/cecha-tag/cecha-tag.component";
import { AddCustomCechaComponent } from '../../components/add-custom-cecha/add-custom-cecha.component';
import { ErrorMsgComponent } from "../../../../components/error-msg/error-msg.component";
import { TokenService } from '../../../../services/token/token.service';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { ImageUploadComponent } from "../../../../components/image-upload/image-upload.component";

@Component({
  selector: 'app-add-roslina-page',
  standalone: true,
  imports: [CommonModule, FormsModule, CechaDropdownComponent,
    WysokoscInputComponent,
    AddCustomCechaComponent,
    CechaTagComponent, ErrorMsgComponent, ImageUploadComponent],
  templateUrl: './add-roslina-page.component.html',
  styleUrl: './add-roslina-page.component.css'
})
export class AddRoslinaPageComponent implements OnInit {
  cechyResponse: CechaResponse[] = [];
  message = '';
  errorMsg: Array<string> = [];

  @ViewChild(CechaTagComponent) cechaTagComponent!: CechaTagComponent;

  doKatalogu: boolean = false;
  request: RoslinaRequest = {
    nazwa: '',
    nazwaLacinska: '',
    obraz: '',
    opis: '',
    wysokoscMin: 0,
    wysokoscMax: 100,
    cechy: [] as CechaWithRelations[],
  };

  wybranyPlik: any;

  selectedCechaType: CechaResponse | null = null;
  customCechaName: string = '';

  constructor(
    private roslinaService: RoslinaService,
    private roslinaWlasnaService: RoslinaWlasnaService,
    private cechaProcessService: CechaProcessService,
    private errorHandlingService : ErrorHandlingService,
    private tokenService: TokenService,
    private router: Router,
    private route : ActivatedRoute
  ) {}

  onFileSelected(file: File) {
    this.wybranyPlik = file;
    
    if (this.wybranyPlik) {
      this.request.obraz = this.wybranyPlik.name;
    }
  }

  clearImage() {
     this.wybranyPlik = null;
     this.request.obraz = '';
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['doKatalogu'] !== undefined) {
        this.doKatalogu = params['doKatalogu'] === 'true';
      }
      console.log('doKatalogu:', this.doKatalogu);
    });
    this.fetchCechy();
  }

  onWysokoscMinChange(min: number) {
    this.request.wysokoscMin = min;
  }

  onWysokoscMaxChange(max: number) {
    this.request.wysokoscMax = max;
  }

  onCechaToggled(cechy: CechaWithRelations[]): void {
    console.log('Cechy toggled:', cechy);
    this.request.cechy = cechy;
    this.cechaTagComponent.updateSortedCechy(cechy);
  }

  onCechaRemoved(index: number): void {
    console.log('Removing cecha at index:', index);
    this.request.cechy.splice(index, 1);
    console.log('Request after removing:', this.request);
  }

  onCustomCechaAdded(customCecha: CechaWithRelations): void {
    this.request.cechy.push(customCecha);
    this.cechaTagComponent.updateSortedCechy(this.request.cechy);
  }


  fetchCechy(): void {
    this.roslinaService.getCechyWithRelations().subscribe({
      next: (response) => {
        this.cechyResponse = response;
        console.log("Konwertowanie cech");
        this.cechyResponse = this.cechaProcessService.processCechyResponse(this.cechyResponse);
      },
      error: (error) => {
        this.message = 'Błąd podczas pobierania cech';
      }
    });
  }

  addCustomCecha(): void {
    if (this.selectedCechaType && this.customCechaName.trim()) {
      let customCecha: CechaWithRelations = {
        etykieta: this.selectedCechaType.etykieta,
        nazwa: this.customCechaName.trim().toLowerCase(),
        relacja: ''
      };
      customCecha = this.cechaProcessService.addRelacjaToCecha(customCecha);
      this.request.cechy.push(customCecha);
      this.cechaTagComponent.updateSortedCechy(this.request.cechy);
      this.customCechaName = '';
    }
  }

  isAddingDoKatalogu(): boolean {
    if (this.doKatalogu && this.tokenService.isNormalUzytkownik()) {
      this.doKatalogu = false;
      this.route.snapshot.data['doKatalogu'] = 'false';
    }

    return this.doKatalogu && !this.tokenService.isNormalUzytkownik();
  }

  addRoslina(): void {
    this.errorMsg = [];
    this.message = '';
    this.request.nazwaLacinska = this.request.nazwaLacinska.toLowerCase();

    let leFile = null;
    if (this.request.obraz !== '') {
      leFile = this.wybranyPlik;
    }

    if (!this.isAddingDoKatalogu()) {
      this.addRoslinaWlasna(this.request, leFile);
      return;
    }
    console.log('Dodawanie rośliny: ', this.request);
    this.roslinaService.saveRoslina({ body: { request: this.request, file: leFile } }).subscribe({
      next: (roslina) => {
        this.router.navigate(['rosliny', roslina.uuid]);
        //this.afterAddRoslina();
      },
      error: (error) => {
        this.message = 'Błąd podczas dodawania rośliny';
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
      }
    });
  }


  addRoslinaWlasna(request: RoslinaRequest, leFile: any) {
    console.log('Dodawanie rośliny użytkownika: ', request);

    this.roslinaWlasnaService.save({ body: { request: request, file: leFile } }).subscribe({
      next: (roslina) => {
        //this.afterAddRoslina();
       // console.log("roslinka", roslina);
        this.router.navigate(['rosliny', roslina.uuid]);
      },
      error: (error) => {
        this.message = 'Błąd podczas dodawania rośliny';
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
      }
    });
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
      cechy: [] as CechaWithRelations[],
    };
    this.clearImage();
  }
}
