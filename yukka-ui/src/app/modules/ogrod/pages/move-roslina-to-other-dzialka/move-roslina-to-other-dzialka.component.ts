import { Component } from '@angular/core';
import { ErrorMsgComponent } from "../../../../components/error-msg/error-msg.component";
import { WyswietlanieRoslinyOpcjeComponent } from "../../components/wyswietlanie-rosliny-opcje/wyswietlanie-rosliny-opcje.component";
import { DzialkaTilePickerComponent } from "../../components/dzialka-tile-picker/dzialka-tile-picker.component";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DzialkaResponse, DzialkaRoslinaRequest, MoveRoslinaRequest, Pozycja, RoslinaResponse, ZasadzonaRoslinaResponse } from '../../../../services/models';
import { Tile, TileUtils } from '../../models/Tile';
import { DzialkaModes } from '../../models/dzialka-modes';
import { WyswietlanieRosliny } from '../../../post/models/WyswietlanieRosliny';
import { DzialkaService, RoslinaService } from '../../../../services/services';
import { TokenService } from '../../../../services/token/token.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';

@Component({
  selector: 'app-move-roslina-to-other-dzialka',
  standalone: true,
  imports: [CommonModule, FormsModule, ErrorMsgComponent,  DzialkaTilePickerComponent],
  templateUrl: './move-roslina-to-other-dzialka.component.html',
  styleUrl: './move-roslina-to-other-dzialka.component.css'
})
export class MoveRoslinaToOtherDzialkaComponent {
 // roslina: RoslinaResponse | undefined;
  zasadzonaRoslina: ZasadzonaRoslinaResponse | undefined;
  private _roslinaObraz: string | undefined;
  dzialki: DzialkaResponse[] = [];

  dzialka: DzialkaResponse | undefined;

  message = '';
  errorMsg: Array<string> = [];
  moveRequest : MoveRoslinaRequest = {
      numerDzialki: -1,
      pozycje: [],
      x: -1,
      y: -1,
      xnowy: -1,
      ynowy: -1
  };

  request : DzialkaRoslinaRequest = {
    roslinaId: '',
    numerDzialki: -1,
    pozycje: [],
    x: -1,
    y: -1,
    kolor: '#ffffff',
    wyswietlanie: WyswietlanieRosliny.TEKSTURA_KOLOR
  };


  tiles: Tile[] = [];
  rowColls = 20;

  DzialkaModes = DzialkaModes;
  mode: DzialkaModes = DzialkaModes.EditRoslinaKafelki;

  takenColor: string = '#ff0000';

  constructor(
    private dzialkaService: DzialkaService,
    private roslinaService: RoslinaService,
    private tokenService: TokenService,
    private router: Router,
    private route: ActivatedRoute,
    private errorHandlingService: ErrorHandlingService
  ) { }

  ngOnInit(): void {
    this.initializeTiles();
    this.route.params.subscribe(params => {
      const roslinaId = params['roslinaId'];
      const numerDzialki = Number(params['numer']);
      if (roslinaId && numerDzialki) {
        this.getRoslinaInDzialka(numerDzialki, roslinaId);
        this.route.snapshot.data['roslinaId'] = roslinaId;
        this.route.snapshot.data['numer'] = numerDzialki;

        this.request.numerDzialki = numerDzialki;
        this.moveRequest.numerDzialki = numerDzialki;

        this.getPozycjeInDzialki(numerDzialki);
      }
    });
  }

  getRoslinaInDzialka(numerDzialki: number, roslinaId: string): void {
    this.errorMsg = [];
    if(!numerDzialki || !roslinaId) return;
    this.dzialkaService.getRoslinaInDzialkaByRoslinaId({ numer: numerDzialki, 'roslina-id': roslinaId }).subscribe({
      next: (zasadzonaRoslina) => {
        this.zasadzonaRoslina = zasadzonaRoslina;
        if (zasadzonaRoslina.x && zasadzonaRoslina.y) {
          this.moveRequest.x = zasadzonaRoslina!.x;
          this.moveRequest.y = zasadzonaRoslina!.y;
        }
        console.log(zasadzonaRoslina);
      },
      error: (err) => {
        console.log(err);
        this.errorMsg.push('Nie znaleziono rośliny o podanym id.');
      }
    });
  }

  getPozycjeInDzialki(numerDzialki: number) {
    this.dzialkaService.getPozycjeInDzialki().subscribe({
      next: (dzialki) => {
        this.dzialki = dzialki.filter(dzialka => dzialka.numer !== numerDzialki);
        if (this.dzialki.length > 0 && this.dzialki[0].numer) {
          this.request.numerDzialki = this.dzialki[0].numer;
        }
        this.loadDzialka();
      },
      error: (err) => {
        this.dzialki = [];
        console.log(err);
        this.errorMsg.push('Wystąpił błąd podczas pobierania dzialek.');
        this.scrollToErrorHeader();
      }
    });
  }

  onDzialkaChange(numerDzialki: number) {
    this.request.numerDzialki = numerDzialki;
    this.loadDzialka();
  }

  loadDzialka() {
    this.errorMsg = [];
    this.clearTiles();
    this.request.pozycje = [];
    this.request.x = -1;
    this.request.y = -1;
    this.moveRequest.numerDzialkiNowy = this.request.numerDzialki;

    this.dzialka = this.dzialki.find(d => d.numer === this.request.numerDzialki);
    if (this.dzialka) {

      this.updateTilesWithRoslina(this.dzialka.zasadzoneRosliny);

      const existingRoslina = this.dzialka.zasadzoneRosliny?.find(roslina =>
       (this.zasadzonaRoslina?.roslina?.roslinaId && roslina.roslina?.roslinaId === this.zasadzonaRoslina?.roslina?.roslinaId));

      if (existingRoslina) {
        this.errorMsg.push('Roślina już istnieje w tej działce.');
        this.mode = DzialkaModes.BrakEdycji;
      } else {
        if(this.mode === DzialkaModes.BrakEdycji) {
          this.mode = DzialkaModes.EditRoslinaKafelki;
        }
      }
    }
  }


  getRoslinaObraz(): string | undefined {
    let baza = 'data:image/jpeg;base64,';
    if(this.zasadzonaRoslina) {
      if(this.zasadzonaRoslina.obraz) {
        return baza + this.zasadzonaRoslina.obraz
      }else if(this.zasadzonaRoslina.roslina && this.zasadzonaRoslina.roslina.obraz) {
        return baza + this.zasadzonaRoslina.roslina.obraz
      }
    }
    return this._roslinaObraz;
  }

  initializeTiles(): void {
    const tiles: Tile[] = [];
    for (let y = 0; y < this.rowColls; y++) {
      for (let x = 0; x < this.rowColls; x++) {
        tiles.push({
          x,
          y,
          clickable: true,
          hovered: false,
          backgroundColor: undefined
        });
      }
    }
    this.tiles = tiles;
  }

  clearTiles(): void {
    this.tiles.forEach(tile => {
      TileUtils.clearTile(tile);
    });
  }

  updateTilesWithRoslina(zasadzoneRosliny: ZasadzonaRoslinaResponse[] | undefined): void {
    if (!zasadzoneRosliny) return;
    zasadzoneRosliny.forEach(zasadzonaRoslina => {
      zasadzonaRoslina.pozycje?.forEach((pozycja: Pozycja) => {
        const tile = this.tiles.find(t => t.x === pozycja.x && t.y === pozycja.y);
        if (tile) {
          tile.roslinaId = zasadzonaRoslina.roslina?.roslinaId;
          tile.backgroundColor = this.takenColor;
        }
      });
    });
  }

  moveRoslinaToOtherDzialka() {
    if(this.mode === DzialkaModes.BrakEdycji) return;
    console.log(this.request);

    if(this.request.x === -1 || this.request.y === -1) {
      this.errorMsg = [];
      this.errorMsg.push('Nie wybrano pozycji rośliny.');
      this.scrollToErrorHeader();
      return;
    }else if(this.request.pozycje.length <= 0) {
      this.errorMsg = [];
      this.errorMsg.push('Nie wybrano żadnych kafelków.');
      this.scrollToErrorHeader();
      return;
    }

    this.moveRequest.xnowy = this.request.x;
    this.moveRequest.ynowy = this.request.y;
    this.moveRequest.pozycje = this.request.pozycje;

    this.message = '';
    this.errorMsg = [];

    // console.log(this.moveRequest);
    // console.log(this.request);

    this.dzialkaService.updateRoslinaPozycjaInDzialka({ body: this.moveRequest }).subscribe({
      next: () => {
        this.goToDzialka();
      },
      error: (error) => {
        //this.message = 'Błąd podczas dodawania rośliny';
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
        console.log(error);
        this.scrollToErrorHeader();
      }
    });

  }

  goToDzialka() {
    const nazwa = this.tokenService.nazwa;
    this.router.navigate(['ogrod', this.tokenService.nazwa, 'dzialka', this.moveRequest.numerDzialkiNowy]);
  }

  changeEditMode(mode: DzialkaModes): void {
    this.mode = mode;
    console.log('pozycje:', this.request.pozycje);
  }


  scrollToErrorHeader(): void {
    const element = document.getElementById('card-header');
    if (element) {
      element.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }


}
