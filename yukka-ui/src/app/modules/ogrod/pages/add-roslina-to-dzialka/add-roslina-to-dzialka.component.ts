import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { ColorPickerModule } from 'ngx-color-picker';
import { DzialkaResponse, DzialkaRoslinaRequest, Pozycja, RoslinaResponse, ZasadzonaRoslinaResponse } from '../../../../services/models';
import { DzialkaService, RoslinaService } from '../../../../services/services';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Tile, TileUtils } from '../../models/Tile';
import { DzialkaModes } from '../../models/dzialka-modes';
import { TokenService } from '../../../../services/token/token.service';
import { ErrorHandlingService } from '../../../../services/error-handler/error-handling.service';
import { ErrorMsgComponent } from '../../../../components/error-msg/error-msg.component';
import { ImageUploadComponent } from "../../../../components/image-upload/image-upload.component";
import { WyswietlanieRosliny } from '../../../post/enums/WyswietlanieRosliny';
import { WyswietlanieRoslinyOpcjeComponent } from "../../components/wyswietlanie-rosliny-opcje/wyswietlanie-rosliny-opcje.component";

@Component({
  selector: 'app-add-roslina-to-dzialka',
  standalone: true,
  imports: [CommonModule, FormsModule, ColorPickerModule, ErrorMsgComponent, ImageUploadComponent, WyswietlanieRoslinyOpcjeComponent],
  templateUrl: './add-roslina-to-dzialka.component.html',
  styleUrl: './add-roslina-to-dzialka.component.css'
})
export class AddRoslinaToDzialkaComponent implements OnInit {
  roslina: RoslinaResponse | undefined;
  private _roslinaObraz: string | undefined;
  dzialki: DzialkaResponse[] = [];

  dzialka: DzialkaResponse | undefined;

  message = '';
  errorMsg: Array<string> = [];

  wyswietlanieOpcje = WyswietlanieRosliny;
  request : DzialkaRoslinaRequest = {
    roslinaId: '',
    numerDzialki: 1,
    pozycje: [],
    x: -1,
    y: -1,
    kolor: '#ffffff',
    wyswietlanie: WyswietlanieRosliny.TEKSTURA_KOLOR,
    obraz: ''
  };

  wybranyPlik: any = null;
  wybranaTekstura: any = null;

  tiles: Tile[] = [];
  rowColls = 20;

  DzialkaModes = DzialkaModes;
  mode: DzialkaModes = DzialkaModes.EditRoslinaKafelki;

  selectColor: string = '#32a852';
  takenColor: string = '#ff0000';
  roslinaPosColor: string = '#0dcaf0';

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
      if (roslinaId) {
        this.getRoslinaByRoslinaId(roslinaId);
        this.route.snapshot.data['roslinaId'] = roslinaId;

        this.getPozycjeInDzialki();


      }
    });
  }

  getPozycjeInDzialki() {
    this.dzialkaService.getPozycjeInDzialki().subscribe({
      next: (dzialki) => {
        this.dzialki = dzialki;

        this.loadDzialka();

        console.log(dzialki);
      },
      error: (err) => {
        this.dzialki = [];
        console.log(err);
        this.errorMsg.push('Wystąpił błąd podczas pobierania dzialek.');
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

    this.dzialka = this.dzialki.find(d => d.numer === this.request.numerDzialki);
    if (this.dzialka) {
      this.updateTilesWithRoslina(this.dzialka.zasadzoneRosliny);

      const existingRoslina = this.dzialka.zasadzoneRosliny?.find(roslina =>
       (this.roslina?.roslinaId && roslina.roslina?.roslinaId === this.roslina?.roslinaId)
        || (this.roslina?.nazwaLacinska && roslina.roslina?.nazwaLacinska === this.roslina?.nazwaLacinska));

      if (existingRoslina) {
        this.errorMsg.push('Roślina już istnieje w tej działce.');
        //console.log('Roślina już istnieje w tej działce.');



        this.mode = DzialkaModes.BrakEdycji;
      } else {
        if(this.mode === DzialkaModes.BrakEdycji) {
          this.mode = DzialkaModes.EditRoslinaKafelki;
        }
      }
    }
  }

  getRoslinaByRoslinaId(roslinaId: string): void {
    this.errorMsg = [];
    if(!roslinaId) return;
    this.roslinaService.findByRoslinaId({ 'roslina-id': roslinaId }).subscribe({
      next: (roslina) => {
        this.roslina = roslina;
        if (roslina.roslinaId) {
          this.request.roslinaId = roslina.roslinaId;
        }
        console.log(roslina);
      },
      error: (err) => {
        this.roslina = undefined;
        console.log(err);
        this.errorMsg.push('Nie znaleziono rośliny o podanym roslinaId.');
      }
    });
  }

  getRoslinaObraz(): string | undefined {
    if(this.roslina && this.roslina.obraz) {
      return 'data:image/jpeg;base64,' + this.roslina.obraz;
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

  addRoslinaToDzialka() {
    if(this.mode === DzialkaModes.BrakEdycji) return;
    console.log('DODAWANIE ROŚLINY DO DZIAŁKI ŁEŁOŁEŁO');
    console.log(this.request);

    if(this.request.x === -1 || this.request.y === -1) {
      this.errorMsg = [];
      this.errorMsg.push('Nie wybrano pozycji rośliny.');
    }
    if(this.request.pozycje.length <= 0) {
      this.errorMsg = [];
      this.errorMsg.push('Nie wybrano żadnych kafelków.');
      return;
    }

    this.message = '';
    this.errorMsg = [];

    this.dzialkaService.saveRoslinaToDzialka({ body: { request: this.request, obraz: this.wybranyPlik, tekstura: this.wybranaTekstura } }).subscribe({
      next: () => {
        this.goToDzialka();
      },
      error: (error) => {
        //this.message = 'Błąd podczas dodawania rośliny';
        this.errorMsg = this.errorHandlingService.handleErrors(error, this.errorMsg);
        console.log(error);
      }
    });

  }

  goToDzialka() {
    const nazwa = this.tokenService.nazwa;
    this.router.navigate(['ogrod', this.tokenService.nazwa, 'dzialka', this.request.numerDzialki]);
  }

  onTileClick(tile: Tile) {
    console.log('x:', tile.x, 'y:', tile.y);

    if(tile.roslinaId  && tile.roslinaId !== this.roslina?.roslinaId) {
      console.log('Ten kafelek jest zajęty przez inną roślinę.');
      return;
    }

    if(this.mode === DzialkaModes.EditRoslinaKafelki) {
      this.addTile(tile);
    } else if(this.mode === DzialkaModes.EditRoslinaPozycja) {
      this.addRoslinaPozycja(tile);
    }
  }

  addTile(tile: Tile) {
    if (!this.roslina) return;
    console.log(tile);

    if (tile.roslinaId === this.roslina.roslinaId) {
      TileUtils.clearTile(tile);
      this.request.pozycje = this.request.pozycje.filter(p => p.x !== tile.x || p.y !== tile.y);
    } else if (!this.request.pozycje.some(p => p.x === tile.x && p.y === tile.y)) {
      tile.roslinaId = this.roslina.roslinaId;
      tile.backgroundColor = this.selectColor;
      this.request.pozycje.push({ x: tile.x, y: tile.y });
    }
  }

  addRoslinaPozycja(tile: Tile) {
    if (!this.roslina) return;
    this.tiles.forEach(t => {
      if (t.zasadzonaRoslina === this.roslina) {
        TileUtils.clearTile(t);
        this.request.pozycje = this.request.pozycje.filter(p => p.x !== t.x || p.y !== t.y);
      }
    });

      tile.roslinaId = this.roslina.roslinaId;
      tile.zasadzonaRoslina = this.roslina;
      tile.backgroundColor = this.roslinaPosColor;

      if (!this.request.pozycje.some(p => p.x === tile.x && p.y === tile.y)) {
        this.request.pozycje.push({ x: tile.x, y: tile.y });
      }

      this.request.x = tile.x;
      this.request.y = tile.y;

  }

  changeEditMode(mode: DzialkaModes): void {
    this.mode = mode;
    console.log('pozycje:', this.request.pozycje);
  }

  onFileSelected(file: File) {
    this.wybranyPlik = file;
  }

  clearImage() {
    this.wybranyPlik = null;
  }

  onTeksturaSelected(file: File) {
    this.wybranaTekstura = file;
  }

  clearTekstura() {
    this.wybranaTekstura = null;
  }

}
