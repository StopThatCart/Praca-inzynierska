import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RoslinaResponse } from '../../../../services/models';
import { RoslinaService, UzytkownikRoslinaService } from '../../../../services/services';
import { switchMap } from 'rxjs/operators';
import { BreadcrumbComponent } from '../../../../components/breadcrumb/breadcrumb.component';
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
    private uzytkownikRoslinaService: UzytkownikRoslinaService,
    private wlasciwoscProcessService: WlasciwoscProcessService,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    this.checkRoles();

    this.route.params.subscribe(params => {
      const roslinaId = params['roslina-id'];
      if (roslinaId) {
        this.getRoslinaByRoslinaId(roslinaId);
        //this.getRoslinaByNazwaLacinska(nazwaLacinska);
        this.route.snapshot.data['roslina-id'] = roslinaId;
      }
    });

  }


  private checkRoles() {
    this.isAdminOrPracownik = this.tokenService.isAdmin() || this.tokenService.isPracownik();

    this.isLoggedIn = this.tokenService.isTokenValid();
  }

  isAutor(): boolean | undefined {
    return this.roslina?.roslinaUzytkownika && this.tokenService.nazwa === this.roslina.autor;
  }


  goToUpdateRoslina() {
    if ((this.isAdminOrPracownik || this.isAutor()) && this.roslina?.roslinaId) {
      this.router.navigate(['rosliny', this.roslina.roslinaId ,'aktualizuj']);
    }
  }

  goToUploadRoslinaObraz() {
    if ((this.isAdminOrPracownik || this.isAutor()) && this.roslina?.roslinaId) {
      this.router.navigate(['rosliny', this.roslina.roslinaId ,'obraz']);
    }
  }

  goToAddRoslinaToDzialka() {
    if (this.roslina?.roslinaId) {
      this.router.navigate(['ogrod', this.tokenService.nazwa, 'dzialka', 'dodawanie', this.roslina.roslinaId]);
    }
  }

  // TODO
  removeRoslina() {
    if(!(this.isAdminOrPracownik || this.isAutor()) || !this.roslina?.roslinaId) {
      return;
    }

    if(confirm("Czy na pewno chcesz usunąć roślinę?")) {
      this.roslinaService.deleteRoslina({ 'roslina-id': this.roslina?.roslinaId }).subscribe({
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

  getRoslinaByRoslinaId(roslinaId: string): void {
    this.roslinaService.findByRoslinaId({ 'roslina-id': roslinaId }).subscribe({
      next: (roslina) => {
        this.roslina = roslina;
        this.roslinaWlasciwosci = this.wlasciwoscProcessService.setRoslinaWlasciwosci(roslina);
        this.errorMessage = null;
      },
      error: (err) => {
        this.roslina = null;
        this.errorMessage = 'Nie znaleziono rośliny o podanym id.';
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
