import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RoslinaResponse } from '../../../../services/models';
import { RoslinaService } from '../../../../services/services';
import { switchMap } from 'rxjs/operators';
import { BreadcrumbComponent } from "../../../../pages/breadcrumb/breadcrumb.component";
import { TokenService } from '../../../../services/token/token.service';


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

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private roslinaService: RoslinaService,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    this.checkRoles();
    this.route.params.subscribe(params => {
      const nazwaLacinska = params['nazwaLacinska'];
      if (nazwaLacinska) {
        this.getRoslinaByNazwaLacinska(nazwaLacinska);
        this.route.snapshot.data['nazwaLacinska'] = nazwaLacinska;
      }
    });

  }


  private checkRoles() {
    this.isAdminOrPracownik = this.tokenService.isAdmin() || this.tokenService.isPracownik();
  }


  goToUpdateRoslina() {
    if(this.isAdminOrPracownik) {

    }
  }

  removeRoslina() {
    if(this.isAdminOrPracownik) {

    }
  }

  getRoslinaByNazwaLacinska(nazwaLacinska: string): void {
    this.roslinaService.findByNazwaLacinska({ 'nazwa-lacinska': nazwaLacinska }).subscribe({
      next: (roslina) => {
        this.roslina = roslina;
        this.setRoslinaWlasciwosci();
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

  setRoslinaWlasciwosci(): void {
    if (this.roslina) {
      const createWlasciwosc = (name: string, value: string | string[] | number | undefined): { name: string, value: string } | null => {
        if (Array.isArray(value)) {
          value = value.join(', ');
        }
        if (value) {
          return { name, value: value.toString() };
        }
        return null;
      };

      const wlasciwosci = [
        createWlasciwosc('Grupa', this.roslina.grupy),
        createWlasciwosc('Podgrupa', this.roslina.podgrupa),
        createWlasciwosc('Forma', this.roslina.formy),
        createWlasciwosc('Gleba', this.roslina.gleby),
        createWlasciwosc('Kolor Kwiatów', this.roslina.koloryKwiatow),
        createWlasciwosc('Kolor Liści', this.roslina.koloryLisci),
        createWlasciwosc('Kwiat', this.roslina.kwiaty),
        createWlasciwosc('Nagroda', this.roslina.nagrody),
        createWlasciwosc('Odczyn', this.roslina.odczyny),
        createWlasciwosc('Okres Kwitnienia', this.roslina.okresyKwitnienia),
        createWlasciwosc('Okres Owocowania', this.roslina.okresyOwocowania),
        createWlasciwosc('Owoc', this.roslina.owoce),
        createWlasciwosc('Pokrój', this.roslina.pokroje),
        createWlasciwosc('Siła Wzrostu', this.roslina.silyWzrostu),
        createWlasciwosc('Stanowisko', this.roslina.stanowiska),
        createWlasciwosc('Walory', this.roslina.walory),
        createWlasciwosc('Wilgotność', this.roslina.wilgotnosci),
        createWlasciwosc('Wysokość', this.roslina.wysokoscMin && this.roslina.wysokoscMax ? `Od ${this.roslina.wysokoscMin} m do ${this.roslina.wysokoscMax} m` : ''),
        createWlasciwosc('Zastosowanie', this.roslina.zastosowania),
        createWlasciwosc('Zimozieloność liści/igieł', this.roslina.zimozielonosci),
      ].filter(w => w !== null);

      this.roslinaWlasciwosci = wlasciwosci as { name: string, value: string }[];
    }
  }

  getRoslinaWlasciwoscPary(): { name: string, value: string }[][] {
    const pary = [];
    for (let i = 0; i < this.roslinaWlasciwosci.length; i += 2) {
      pary.push(this.roslinaWlasciwosci.slice(i, i + 2));
    }
    return pary;
  }

  trackByIndex(index: number, item: any): number {
    return index;
  }

}
