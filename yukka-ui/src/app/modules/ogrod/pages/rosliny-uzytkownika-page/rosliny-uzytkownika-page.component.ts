import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PaginationComponent } from '../../../../components/pagination/pagination.component';
import { SpinnerComponent } from '../../../../services/LoaderSpinner/spinner/spinner.component';
import { RoslinaCardComponent } from '../../../roslina/components/roslina-card/roslina-card.component';
import { WlasciwoscDropdownComponent } from '../../../roslina/components/wlasciwosc-dropdown/wlasciwosc-dropdown.component';
import { WlasciwoscTagComponent } from '../../../roslina/components/wlasciwosc-tag/wlasciwosc-tag.component';
import { WysokoscInputComponent } from '../../../roslina/components/wysokosc-input/wysokosc-input.component';
import { Convert } from '../../../../services/converts/wlasciwosc-with-relations-convert';
import { PageResponseRoslinaResponse, RoslinaRequest, UzytkownikRoslinaRequest, WlasciwoscResponse, WlasciwoscWithRelations } from '../../../../services/models';
import { RoslinaService, UzytkownikRoslinaService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from '../../../../services/token/token.service';
import { WlasciwoscProcessService } from '../../../roslina/services/wlasciwosc-service/wlasciwosc.service';

@Component({
  selector: 'app-rosliny-uzytkownika-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RoslinaCardComponent, WlasciwoscTagComponent, WlasciwoscDropdownComponent, SpinnerComponent, PaginationComponent, WysokoscInputComponent],
  templateUrl: './rosliny-uzytkownika-page.component.html',
  styleUrl: './rosliny-uzytkownika-page.component.css'
})
export class RoslinyUzytkownikaPageComponent {
  canAddRoslina: boolean = false;
  uzytNazwa: string | undefined;

  roslinaResponse: PageResponseRoslinaResponse = {};
  wlasciwosciResponse: WlasciwoscResponse[] = [];
  isLoading = false;
  message = '';

  request: UzytkownikRoslinaRequest = {
    nazwa: '',
   // nazwaLacinska: '',
    obraz: '',
    opis: '',
    wysokoscMin: 0,
    wysokoscMax: 100,
    wlasciwosci: [

    ] as WlasciwoscWithRelations[],
  };
  page = 1;
  size = 12;
  roslinaCount: number = 0;

  @ViewChild(WlasciwoscTagComponent) wlasciwoscTagComponent!: WlasciwoscTagComponent;

  constructor(
    private roslinaService: RoslinaService,
    private uzytkownikRoslinaService: UzytkownikRoslinaService,
    private wlasciwoscProcessService: WlasciwoscProcessService,
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
      //this.request.nazwaLacinska = params['nazwaLacinska'] || '';

      // Uwaga: uważaj na api-gen, bo trzeba ręcznie tworzyć konwersję na JSON
      // Tutaj link dla przyszłego mnie. https://app.quicktype.io/
      let wlasciwosci2 = params['wlasciwosci'] ? JSON.parse(params['wlasciwosci']) : [];

      this.request.wlasciwosci = Convert.toWlasciwoscWithRelationsArray(JSON.stringify(wlasciwosci2));

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
    this.uzytkownikRoslinaService.findAllRoslinyOfUzytkownik({
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

    this.roslinaService.getWlasciwosciWithRelations()
    .subscribe({
      next: (wlasciwosci) => {
        this.wlasciwosciResponse = this.wlasciwoscProcessService.processWlasciwosciResponse(wlasciwosci);
      },
      error: (error) => {
        console.error('Error fetching wlasciwosci:', error);
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

  // Paginacja

  goToPage(page: number) {
    const wlasciwosciJson = Convert.wlasciwoscWithRelationsArrayToJson(this.request.wlasciwosci);

    this.router.navigate(['ogrod', this.uzytNazwa, 'rosliny'], {
      queryParams: {
        page: page,
        wysokoscMin: this.request.wysokoscMin,
        wysokoscMax: this.request.wysokoscMax,
        nazwa: this.request.nazwa,
       // nazwaLacinska: this.request.nazwaLacinska,
        wlasciwosci: wlasciwosciJson
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
