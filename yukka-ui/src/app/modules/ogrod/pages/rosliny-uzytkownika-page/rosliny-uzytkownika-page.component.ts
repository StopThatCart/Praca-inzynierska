import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PaginationComponent } from '../../../../components/pagination/pagination.component';
import { RoslinaCardComponent } from '../../../roslina/components/roslina-card/roslina-card.component';
import { CechaDropdownComponent } from '../../../roslina/components/cecha-dropdown/cecha-dropdown.component';
import { CechaTagComponent } from '../../../roslina/components/cecha-tag/cecha-tag.component';
import { WysokoscInputComponent } from '../../../roslina/components/wysokosc-input/wysokosc-input.component';
import { Convert } from '../../../../services/converts/cecha-with-relations-convert';
import { PageResponseRoslinaResponse, CechaKatalogResponse, CechaWithRelations, RoslinaRequest } from '../../../../services/models';
import { RoslinaWlasnaService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from '../../../../services/token/token.service';
import { CechaProcessService } from '../../../roslina/services/cecha-service/cecha.service';
import { LoadingComponent } from "../../../../components/loading/loading.component";
import { CryptKeyService } from '../../../../services/crypt-key/crypt-key.service';
import * as crypto from 'crypto';
@Component({
  selector: 'app-rosliny-uzytkownika-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RoslinaCardComponent, CechaTagComponent, CechaDropdownComponent, PaginationComponent, WysokoscInputComponent, LoadingComponent],
  templateUrl: './rosliny-uzytkownika-page.component.html',
  styleUrl: './rosliny-uzytkownika-page.component.css'
})
export class RoslinyUzytkownikaPageComponent {
  canAddRoslina: boolean = false;
  uzytNazwa: string | undefined;

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

    ] as CechaWithRelations[],
  };
  page = 1;
  size = 12;
  roslinaCount: number = 0;

  @ViewChild(CechaTagComponent) cechaTagComponent!: CechaTagComponent;

  constructor(
    private roslinaWlasnaService: RoslinaWlasnaService,
    private cechaProcessService: CechaProcessService,
    private cryptKeyService: CryptKeyService,
    private router: Router,
    private route: ActivatedRoute,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.uzytNazwa = params['uzytkownik-nazwa'];
      if (this.uzytNazwa) {
        this.route.snapshot.data['uzytkownik-nazwa'] = this.uzytNazwa;
      }
    });
    this.checkRoles();

    this.route.queryParams.subscribe(params => {
      this.page = +params['page'] || 1;

      this.request.wysokoscMin = params['wysokoscMin'] || 0;
      this.request.wysokoscMax = params['wysokoscMax'] || 100;
      this.request.nazwa = params['nazwa'] || '';
      this.request.nazwaLacinska = params['nazwaLacinska'] || '';

      let cechy2 = [];
      if (params['cechy']) {
        const decryptedCechy = this.cryptKeyService.decrypt(params['cechy']);
        cechy2 = JSON.parse(decryptedCechy);
      }

      this.request.cechy = Convert.toCechaWithRelationsArray(JSON.stringify(cechy2));

      this.findAllRosliny();
    });
  }

  private checkRoles() {
    this.canAddRoslina = //this.tokenService.isAdmin() || this.tokenService.isPracownik() ||
    this.tokenService.nazwa === this.uzytNazwa;
  }

  goToAddRoslina(doKatalogu: boolean) {
    if (this.canAddRoslina) {
      this.router.navigate(['rosliny/dodaj', doKatalogu]);
    }
  }

  findAllRosliny() {
   // console.log('Request:', this.request);
    this.page = (Number.isInteger(this.page) && this.page >= 0) ? this.page : 1;

    this.isLoading = true;
    this.roslinaWlasnaService.findAllRoslinyOfUzytkownik({
      page: this.page - 1,
      size: this.size,
      'uzytkownik-nazwa': this.uzytNazwa,
      body: this.request

    }).subscribe({
        next: (rosliny) => {
          this.roslinaResponse = rosliny;
          this.roslinaCount = rosliny.totalElements as number;
        },
        error: (error) => {
          console.error('Error fetching rosliny:', error);
          this.message = 'Wystąpił błąd podczas pobierania roślin.';
        },
        complete: () => {
          this.isLoading = false;
        }
      });

    this.roslinaWlasnaService.getUzytkownikCechyCountFromQuery({ body: this.request, 'uzytkownik-nazwa': this.uzytNazwa })
    .subscribe({
      next: (cechy) => {
        this.cechyResponse = this.cechaProcessService.processCechyResponse(cechy);
      },
      error: (error) => {
        console.error('Error fetching cechy:', error);
      }
    });
  }

  onSearch() {
   // console.log('Search:', this.request);
    this.goToFirstPage();
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

  // Paginacja

  goToPage(page: number) {
    const cechyJson = Convert.cechaWithRelationsArrayToJson(this.request.cechy);
    const encryptedCechy = this.cryptKeyService.encrypt(cechyJson);

    this.router.navigate(['ogrod', this.uzytNazwa, 'rosliny'], {
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
