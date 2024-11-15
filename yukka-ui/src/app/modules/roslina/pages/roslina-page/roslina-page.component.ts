import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RoslinaResponse } from '../../../../services/models';
import { RoslinaService } from '../../../../services/services';
import { switchMap } from 'rxjs/operators';
import { BreadcrumbComponent } from "../../../../pages/breadcrumb/breadcrumb.component";
import { TokenService } from '../../../../services/token/token.service';
import { WlasciwoscProcessService } from '../../services/wlasciwosc-service/wlasciwosc.service';


@Component({
  selector: 'app-roslina-page',
  standalone: true,
  imports: [CommonModule, BreadcrumbComponent],
  templateUrl: './roslina-page.component.html',
  styleUrl: './roslina-page.component.css'
})
export class RoslinaPageComponent implements OnInit {
  roslina: RoslinaResponse | null = null;
  roslinaWlasciwosci: { name: string, value: string }[] = [];
  private _roslinaObraz: string | undefined;

  errorMessage: string | null = null;


  isAdminOrPracownik: boolean = false;
  isLoggedIn: boolean = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private roslinaService: RoslinaService,
    private wlasciwoscProcessService: WlasciwoscProcessService,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    this.checkRoles();
    this.route.params.subscribe(params => {
      const nazwaLacinska = params['nazwa-lacinska'];
      if (nazwaLacinska) {
        this.getRoslinaByNazwaLacinska(nazwaLacinska);
        this.route.snapshot.data['nazwa-lacinska'] = nazwaLacinska;
      }
    });

  }


  private checkRoles() {
    this.isAdminOrPracownik = this.tokenService.isAdmin() || this.tokenService.isPracownik();
    this.isLoggedIn = this.tokenService.isTokenValid();
  }


  goToUpdateRoslina() {
    if (this.isAdminOrPracownik && this.roslina?.nazwaLacinska) {
      this.router.navigate(['aktualizuj'], { relativeTo: this.route });
    }
  }

  goToUploadRoslinaObraz() {
    if (this.isAdminOrPracownik && this.roslina?.nazwaLacinska) {
      this.router.navigate(['obraz'], { relativeTo: this.route });
    }
  }

  goToAddRoslinaToDzialka() {
    if (this.roslina?.nazwaLacinska) {
      this.router.navigate(['ogrod', this.tokenService.nazwa, 'dzialka', 'dodawanie', this.roslina.id]);
    }
  }

  removeRoslina() {
    if(!this.isAdminOrPracownik || !this.roslina?.nazwaLacinska) {
      return;
    }

    if(confirm("Czy na pewno chcesz usunąć roślinę?")) {
      this.roslinaService.deleteRoslina1({ 'nazwa-lacinska': this.roslina?.nazwaLacinska }).subscribe({
        next: () => {
          this.router.navigate(['..'], { relativeTo: this.route });
        },
        error: (err) => {
          this.errorMessage = 'Nie udało się usunąć rośliny.';
        }
      });
    }
  }

  getRoslinaByNazwaLacinska(nazwaLacinska: string): void {
    this.roslinaService.findByNazwaLacinska({ 'nazwa-lacinska': nazwaLacinska }).subscribe({
      next: (roslina) => {
        this.roslina = roslina;
        this.roslinaWlasciwosci = this.wlasciwoscProcessService.setRoslinaWlasciwosci(roslina);
        this.errorMessage = null;
      },
      error: (err) => {
        this.roslina = null;
        this.errorMessage = 'Nie znaleziono rośliny o podanej nazwie łacińskiej.';
      }
    });
  }

  getRoslinaObraz(): string | undefined {
    if(this.roslina && this.roslina.obraz) {
      return 'data:image/jpeg;base64,' + this.roslina.obraz;
    }
    return this._roslinaObraz;
  }

  getRoslinaWlasciwoscPary(): { name: string, value: string }[][] {
    return this.wlasciwoscProcessService.getRoslinaWlasciwoscPary(this.roslinaWlasciwosci);
  }

  trackByIndex(index: number, item: any): number {
    return index;
  }

}
