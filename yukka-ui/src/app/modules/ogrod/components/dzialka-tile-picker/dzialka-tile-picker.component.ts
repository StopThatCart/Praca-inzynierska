import { Component, Input } from '@angular/core';
import { DzialkaRoslinaRequest, MoveRoslinaRequest, RoslinaRequest, RoslinaResponse } from '../../../../services/models';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ColorPickerModule } from 'ngx-color-picker';
import { WyswietlanieRosliny } from '../../../post/enums/WyswietlanieRosliny';
import { Tile, TileUtils } from '../../models/Tile';
import { DzialkaModes } from '../../models/dzialka-modes';

@Component({
  selector: 'app-dzialka-tile-picker',
  standalone: true,
  imports: [CommonModule,  FormsModule, ColorPickerModule],
  templateUrl: './dzialka-tile-picker.component.html',
  styleUrl: './dzialka-tile-picker.component.css'
})
export class DzialkaTilePickerComponent {

  @Input() roslina: RoslinaResponse | undefined;
  @Input() tiles: Tile[] = [];
  @Input() request: DzialkaRoslinaRequest = {
      roslinaId: '',
      numerDzialki: 1,
      pozycje: [],
      x: -1,
      y: -1,
      kolor: '#ffffff',
      wyswietlanie: WyswietlanieRosliny.TEKSTURA_KOLOR
    };

  @Input() isMove: boolean = false;
  @Input() mode: DzialkaModes = DzialkaModes.EditRoslinaKafelki;

  DzialkaModes = DzialkaModes;
  selectColor: string = '#32a852';
  roslinaPosColor: string = '#0dcaf0';

  constructor() { }


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

}
