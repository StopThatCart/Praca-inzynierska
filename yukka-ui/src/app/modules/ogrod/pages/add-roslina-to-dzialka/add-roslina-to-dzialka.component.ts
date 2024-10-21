import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { DzialkaResponse, DzialkaRoslinaRequest, Pozycja, RoslinaResponse, ZasadzonaRoslinaResponse } from '../../../../services/models';
import { DzialkaService, RoslinaService } from '../../../../services/services';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Tile, TileUtils } from '../../models/Tile';
import { DzialkaModes } from '../../models/dzialka-modes';
import { TokenService } from '../../../../services/token/token.service';

@Component({
  selector: 'app-add-roslina-to-dzialka',
  standalone: true,
  imports: [CommonModule, FormsModule],
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

  request : DzialkaRoslinaRequest = {
    numerDzialki: 1,
    pozycje: [],
    x: -1,
    y: -1,
    obraz: ''
  };

  // Potem się zrobi z tego osobny komponent
  @ViewChild('fileInput') fileInput!: ElementRef;
  wybranyObraz: any;
  wybranyPlik: any;

  onFileSelected(event: any) {
    this.wybranyPlik = event.target.files[0];

     if (this.wybranyPlik) {
       this.request.obraz = this.wybranyPlik.name;

       const reader = new FileReader();
       reader.onload = () => {
         this.wybranyObraz = reader.result as string;
       };
       reader.readAsDataURL(this.wybranyPlik);
     }
  }

  clearImage() {
    this.wybranyObraz = null;
    this.wybranyPlik = null;
    this.request.obraz = '';
    this.fileInput.nativeElement.value = '';
  }

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
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.initializeTiles();
    this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.getRoslinaById(id);
        this.route.snapshot.data['id'] = id;

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

  getRoslinaById(id: number): void {
    this.errorMsg = [];
    this.roslinaService.findById({ 'id': id }).subscribe({
      next: (roslina) => {
        this.roslina = roslina;
        this.request.roslinaId = roslina.roslinaId || undefined;
        this.request.nazwaLacinska = roslina.nazwaLacinska || undefined;
        console.log(roslina);
      },
      error: (err) => {
        this.roslina = undefined;
        console.log(err);
        this.errorMsg.push('Nie znaleziono rośliny o podanym id.');
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
          roslina: undefined,
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
          tile.roslinaId = zasadzonaRoslina.roslina?.id;
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
    if(this.request.obraz === '') {

      this.dzialkaService.saveRoslinaToDzialka1$Json({ body: this.request }).subscribe({
        next: () => {
          this.goToDzialka();
        },
        error: (error) => {
          this.message = 'Błąd podczas dodawania rośliny';
          console.log(error);
        }
      });
    } else {
      this.dzialkaService.saveRoslinaToDzialka1$FormData({ body: { request: this.request, file: this.wybranyPlik } }).subscribe({
        next: () => {
          this.goToDzialka();
        },
        error: (error) => {
          this.message = 'Błąd podczas dodawania rośliny';
          console.log(error);
        }
      });
    }

  }

  goToDzialka() {
    const nazwa = this.tokenService.nazwa;
    this.router.navigate(['ogrod', this.tokenService.nazwa, 'dzialka', this.request.numerDzialki]);
  }

  onTileClick(tile: Tile) {
    console.log('x:', tile.x, 'y:', tile.y);

    if(tile.roslinaId  && tile.roslinaId !== this.roslina?.id) {
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

    if (tile.roslinaId === this.roslina.id) {
      TileUtils.clearTile(tile);
      this.request.pozycje = this.request.pozycje.filter(p => p.x !== tile.x || p.y !== tile.y);
    } else if (!this.request.pozycje.some(p => p.x === tile.x && p.y === tile.y)) {
      tile.roslinaId = this.roslina.id;
      tile.backgroundColor = this.selectColor;
      this.request.pozycje.push({ x: tile.x, y: tile.y });
    }
  }

  addRoslinaPozycja(tile: Tile) {
    if (!this.roslina) return;
    this.tiles.forEach(t => {
      if (t.roslina === this.roslina) {
        TileUtils.clearTile(t);
        this.request.pozycje = this.request.pozycje.filter(p => p.x !== t.x || p.y !== t.y);
      }
    });

      tile.roslinaId = this.roslina.id;
      tile.roslina = this.roslina;
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



}
