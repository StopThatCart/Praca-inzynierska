import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { RoslinaResponse } from '../../../../services/models';
import { RoslinaService } from '../../../../services/services';
import { BreadcrumbComponent } from '../../../../components/breadcrumb/breadcrumb.component';
import { TokenService } from '../../../../services/token/token.service';
import { RoslinaCechyContainerComponent } from "../../components/roslina-cechy-container/roslina-cechy-container.component";


@Component({
  selector: 'app-roslina-page',
  standalone: true,
  imports: [CommonModule, RouterModule, BreadcrumbComponent, RoslinaCechyContainerComponent],
  templateUrl: './roslina-page.component.html',
  styleUrl: './roslina-page.component.css'
})
export class RoslinaPageComponent implements OnInit {
  roslina: RoslinaResponse | undefined;
  private _roslinaObraz: string | undefined;

  errorMessage: string | null = null;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private roslinaService: RoslinaService,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const uuid = params['uuid'];
      if (uuid) {
        this.getRoslinaByUuid(uuid);
        this.route.snapshot.data['uuid'] = uuid;
      }
    });

  }

  isLoggedIn(): boolean {
    return this.tokenService.isTokenValid();
  }

  isPracownik(): boolean {
    return this.tokenService.isPracownik();
  }

  isAutor(): boolean | undefined {
    return this.roslina?.roslinaUzytkownika && this.tokenService.nazwa === this.roslina.autor;
  }


  goToUpdateRoslina() {
    if ((this.isPracownik() || this.isAutor()) && this.roslina?.uuid) {
      return ['/rosliny', this.roslina.uuid ,'aktualizuj'];
    }
    return undefined;
  }

  goToUploadRoslinaObraz() {
    if ((this.isPracownik() || this.isAutor()) && this.roslina?.uuid) {
      return ['/rosliny', this.roslina.uuid ,'obraz'];
    }
    return undefined;
  }

  goToAddRoslinaToDzialka() {
    if (this.roslina?.uuid) {
      return ['/ogrod', this.tokenService.nazwa, 'dzialka', 'dodawanie', this.roslina.uuid];
    }
    return undefined;
    
  }

  removeRoslina() {
    if(!(this.isPracownik() || this.isAutor()) || !this.roslina?.uuid) {
      return;
    }

    if(confirm("Czy na pewno chcesz usunąć roślinę?")) {
      this.roslinaService.deleteRoslina({ uuid: this.roslina?.uuid }).subscribe({
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
        this.errorMessage = null;
      },
      error: (err) => {
        this.errorMessage = 'Nie znaleziono rośliny o podanej nazwie łacińskiej.';
      }
    });
  }

  getRoslinaByUuid(uuid: string): void {
    this.roslinaService.findByUuid({ uuid: uuid }).subscribe({
      next: (roslina) => {
        this.roslina = roslina;
        this.errorMessage = null;
      },
      error: (err) => {
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
}
