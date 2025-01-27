import { Component, OnInit, ViewChild } from '@angular/core';
import { RoslinaService } from '../../../../services/services/roslina.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PageResponseRoslinaResponse, RoslinaRequest, CechaKatalogResponse, CechaWithRelations } from '../../../../services/models';
import { CommonModule } from '@angular/common';
import { RoslinaCardComponent } from "../../components/roslina-card/roslina-card.component";
import { CechaTagComponent } from "../../components/cecha-tag/cecha-tag.component";
import { CechaDropdownComponent } from "../../components/cecha-dropdown/cecha-dropdown.component";
import { FormsModule } from '@angular/forms';
import { Convert } from '../../../../services/converts/cecha-with-relations-convert';
import { PaginationComponent } from "../../../../components/pagination/pagination.component";
import { CechaProcessService } from '../../services/cecha-service/cecha.service';
import { WysokoscInputComponent } from "../../components/wysokosc-input/wysokosc-input.component";
import { TokenService } from '../../../../services/token/token.service';
import { LoadingComponent } from "../../../../components/loading/loading.component";
import { CryptKeyService } from '../../../../services/crypt-key/crypt-key.service';

@Component({
  selector: 'app-roslina-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RoslinaCardComponent, CechaTagComponent, CechaDropdownComponent, PaginationComponent, WysokoscInputComponent, LoadingComponent],
  templateUrl: './roslina-list.component.html',
  styleUrl: './roslina-list.component.css'
})
export class RoslinaListComponent implements OnInit{
  canAddRoslina: boolean = false;

  roslinaResponse: PageResponseRoslinaResponse = {};
  cechyResponse: CechaKatalogResponse[] = [];
  isLoading = false;
  message = '';

  request: RoslinaRequest = {
    nazwa: '',
    nazwaLacinska: '',
    obraz: '',
    opis: '',
    wysokoscMin: 0,
    wysokoscMax: 100,
    cechy: [
/*
      {
        etykieta: 'Kolor', relacja: 'MA_KOLOR_LISCI', nazwa: 'ciemnozielone'
      },
      {
        etykieta: 'Okres', relacja: 'MA_OKRES_OWOCOWANIA', nazwa: 'październik'
      },

      {
        etykieta: 'Okres', relacja: 'MA_OKRES_KWITNIENIA', nazwa: 'lipiec'
      },

      {
        etykieta: 'Gleba', relacja: 'MA_GLEBE', nazwa: 'przeciętna ogrodowa'
      },
      {
        etykieta: 'Gleba', relacja: 'MA_GLEBE', nazwa: 'próchniczna'
      }
*/
    ] as CechaWithRelations[],
  };
  // Na backendzie jest size 10, więc tutaj tylko testuję
  page = 1;
  size = 12;
  roslinaCount: number = 0;

  @ViewChild(CechaTagComponent) cechaTagComponent!: CechaTagComponent;

  constructor(
    private roslinaService: RoslinaService,
    private cechaProcessService: CechaProcessService,
    private cryptKeyService: CryptKeyService,
    private router: Router,
    private route: ActivatedRoute,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    this.checkRoles();

    this.route.queryParams.subscribe(params => {
      this.page = +params['page'] || 1;

      this.request.wysokoscMin = params['wysokoscMin'] || 0;
      this.request.wysokoscMax = params['wysokoscMax'] || 100;
      this.request.nazwa = params['nazwa'] || '';
      this.request.nazwaLacinska = params['nazwaLacinska'] || '';

      // Uwaga: uważaj na api-gen, bo trzeba ręcznie tworzyć konwersję na JSON
      // Tutaj link: https://app.quicktype.io/
      let cechy2 = [];
      if (params['cechy']) {
        const decryptedCechy = this.cryptKeyService.decrypt(params['cechy']);
        cechy2 = JSON.parse(decryptedCechy);
      }
      // let cechy2 = params['cechy'] ? JSON.parse(params['cechy']) : [];

      this.request.cechy = Convert.toCechaWithRelationsArray(JSON.stringify(cechy2));

      this.findAllRosliny();
    });
  }

  private checkRoles() {
    this.canAddRoslina = this.tokenService.isAdmin() || this.tokenService.isPracownik();
  }

  goToAddRoslina(doKatalogu: boolean) {
    if(this.canAddRoslina) {
      this.router.navigate(['rosliny/dodaj', doKatalogu]);
    }
  }

  findAllRosliny() {
    this.page = (Number.isInteger(this.page) && this.page >= 0) ? this.page : 1;

    this.isLoading = true;
    this.roslinaService.findAllRoslinyWithParameters({
      page: this.page - 1,
      size: this.size,
      body: this.request
    }).subscribe({
        next: (rosliny) => {
          this.roslinaResponse = rosliny;
          this.roslinaCount = rosliny.totalElements as number;
        },
        error: (error) => {
          console.error('Error fetching rosliny:', error);
          this.message = 'Wystąpił błąd podczas pobierania roślin.';
          this.isLoading = false;
        },
        complete: () => {
          this.isLoading = false;
        }
      });

    this.roslinaService.getCechyCountFromQuery({ body: this.request })
    .subscribe({
      next: (cechy) => {
        this.cechyResponse = this.cechaProcessService.processCechyResponse(cechy);
        //console.log('Cechy:', this.cechyResponse);
      },
      error: (error) => {
        console.error('Error fetching cechy:', error);
      }
    });
  }

  onSearch() {
   // console.log('Search:', this.request);
    this.goToFirstPage();
   // this.findAllRosliny();
  }

  onWysokoscMinChange(min: number) {
    this.request.wysokoscMin = min;
  }

  onWysokoscMaxChange(max: number) {
    this.request.wysokoscMax = max;
  }

  onCechaToggled(cechy: CechaWithRelations[]): void {
    //console.log('Cechy toggled:', cechy);
    this.request.cechy = cechy;
    this.cechaTagComponent.updateSortedCechy(cechy);
  }

  onCechaRemoved(index: number): void {
   // console.log('Removing cecha at index:', index);
    this.request.cechy.splice(index, 1);
   // console.log('Request after removing:', this.request);
  }

  // Paginacja

  goToPage(page: number) {
    const cechyJson = Convert.cechaWithRelationsArrayToJson(this.request.cechy);
    const encryptedCechy = this.cryptKeyService.encrypt(cechyJson);

    this.router.navigate(['/rosliny'], {
      queryParams: {
        page: page,
        wysokoscMin: this.request.wysokoscMin,
        wysokoscMax: this.request.wysokoscMax,
        nazwa: this.request.nazwa,
        nazwaLacinska: this.request.nazwaLacinska,
        cechy: encryptedCechy
      },
      queryParamsHandling: 'merge'
    });
  }

  goToFirstPage() {
    this.goToPage(1);
  }

  goToNextPage() {
    if (this.page < (this.roslinaResponse.totalPages as number)) {
      this.goToPage(this.page + 1);
    }
  }

  goToPreviousPage() {
    if (this.page > 1) {
      this.goToPage(this.page - 1);
    }
  }

  goToLastPage() {
    const lastPage = this.roslinaResponse.totalPages as number;
    this.goToPage(lastPage);
  }

  get isLastPage() {
    return this.page === (this.roslinaResponse.totalPages as number);
  }

}
