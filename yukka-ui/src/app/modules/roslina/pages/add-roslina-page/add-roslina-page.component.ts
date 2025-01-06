import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WlasciwoscDropdownComponent } from '../../components/wlasciwosc-dropdown/wlasciwosc-dropdown.component';
import { WlasciwoscResponse } from '../../../../services/models/wlasciwosc-response';
import { RoslinaRequest, UzytkownikRoslinaRequest, WlasciwoscWithRelations } from '../../../../services/models';
import { RoslinaService, UzytkownikRoslinaService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { WlasciwoscProcessService } from '../../services/wlasciwosc-service/wlasciwosc.service';
import { WysokoscInputComponent } from "../../components/wysokosc-input/wysokosc-input.component";
import { WlasciwoscTagComponent } from "../../components/wlasciwosc-tag/wlasciwosc-tag.component";
import { AddCustomWlasciwoscComponent } from '../../components/add-custom-wlasciwosc/add-custom-wlasciwosc.component';
import { ErrorMsgComponent } from "../../../../components/error-msg/error-msg.component";
import { TokenService } from '../../../../services/token/token.service';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { ImageUploadComponent } from "../../../../components/image-upload/image-upload.component";

@Component({
  selector: 'app-add-roslina-page',
  standalone: true,
  imports: [CommonModule, FormsModule, WlasciwoscDropdownComponent,
    WysokoscInputComponent,
    AddCustomWlasciwoscComponent,
    WlasciwoscTagComponent, ErrorMsgComponent, ImageUploadComponent],
  templateUrl: './add-roslina-page.component.html',
  styleUrl: './add-roslina-page.component.css'
})
export class AddRoslinaPageComponent implements OnInit {
  wlasciwosciResponse: WlasciwoscResponse[] = [];
  message = '';
  errorMsg: Array<string> = [];

  @ViewChild(WlasciwoscTagComponent) wlasciwoscTagComponent!: WlasciwoscTagComponent;

  doKatalogu: boolean = false;
  request: RoslinaRequest = {
    nazwa: '',
    nazwaLacinska: '',
    obraz: '',
    opis: '',
    wysokoscMin: 0,
    wysokoscMax: 100,
    wlasciwosci: [] as WlasciwoscWithRelations[],
  };

  wybranyPlik: any;

  selectedWlasciwoscType: WlasciwoscResponse | null = null;
  customWlasciwoscName: string = '';

  constructor(
    private roslinaService: RoslinaService,
    private uzytkownikRoslinaService: UzytkownikRoslinaService,
    private wlasciwoscProcessService: WlasciwoscProcessService,
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
      this.addUzytkownikRoslina(this.request, leFile);
      return;
    }
    console.log('Dodawanie rośliny: ', this.request);
    this.roslinaService.saveRoslina1({ body: { request: this.request, file: leFile } }).subscribe({
      next: (roslina) => {
        this.router.navigate(['rosliny', roslina.roslinaId]);
        //this.afterAddRoslina();
      },
      error: (error) => {
        this.message = 'Błąd podczas dodawania rośliny';
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
      }
    });
  }


  addUzytkownikRoslina(request: RoslinaRequest, leFile: any) {
    console.log('Dodawanie rośliny użytkownika: ', request);
    let uzytRequest: UzytkownikRoslinaRequest = {
      nazwa: this.request.nazwa,
      opis: this.request.opis,
      //obraz: '',
      wysokoscMin: this.request.wysokoscMin,
      wysokoscMax: this.request.wysokoscMax,
      wlasciwosci: this.request.wlasciwosci,
    };

    this.uzytkownikRoslinaService.saveRoslina({ body: { request: uzytRequest, file: leFile } }).subscribe({
      next: (roslina) => {
        //this.afterAddRoslina();
       // console.log("roslinka", roslina);
        this.router.navigate(['rosliny', roslina.roslinaId]);
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
      wlasciwosci: [] as WlasciwoscWithRelations[],
    };
    this.clearImage();
  }
}
