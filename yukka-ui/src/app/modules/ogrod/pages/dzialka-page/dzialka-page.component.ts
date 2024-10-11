import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import html2canvas from 'html2canvas';
import {  } from 'rxjs';
import { RoslinaResponse } from '../../../../services/models';
import { PostCardComponent } from "../../../post/components/post-card/post-card.component";
import { LulekComponent } from "../../components/lulek/lulek.component";
import { DzialkaResponse } from '../../../../services/models/dzialka-response';
import { ActivatedRoute } from '@angular/router';
import { DzialkaService } from '../../../../services/services';
import { Tile } from '../../models/Tile';

@Component({
  selector: 'app-dzialka-page',
  standalone: true,
  imports: [CommonModule, PostCardComponent, LulekComponent],
  templateUrl: './dzialka-page.component.html',
  styleUrl: './dzialka-page.component.css'
})
export class DzialkaPageComponent implements OnInit  {
  dzialka: DzialkaResponse = {};
  numer: number | undefined;
  uzytNazwa: string | undefined;

  @ViewChild('canvas', { static: true }) canvasElement!: ElementRef;
  @ViewChild('slider', { static: true }) zoomEl!: ElementRef;
  @ViewChild('canvasElement', { static: true }) canvas!: ElementRef<HTMLCanvasElement>;
  tiles: Tile[] = [];

  scale : number = 1;

  private images = {
    grass: 'assets/tiles/grass.png',
    dirt: 'assets/tiles/dirt.png'
  };

  private placeholderColors = ['#FFCCCC', '#CCFFCC', '#CCCCFF'];

  constructor(
    private route: ActivatedRoute,
    private dzialkaService: DzialkaService
  ) {}


  ngOnInit() {
    // TODO: Dodawanie ładowania
    this.generateInitialTiles();

    this.route.params.subscribe(params => {
      this.numer = Number(params['numer']);
      this.uzytNazwa = params['uzytkownik-nazwa'];

      if (this.numer && this.uzytNazwa) {
        // Na razie dane są sztywne
        this.getDzialkaByNumer(this.numer, this.uzytNazwa);
        this.route.snapshot.data['numer'] = this.numer;
        this.route.snapshot.data['uzytkownik-nazwa'] = this.uzytNazwa;
      }
    });
  }


  getDzialkaByNumer(numer: number, uzytkownikNazwa: string): void {
    console.log('getDzialkaByNumer');
    this.dzialkaService.getDzialkaOfUzytkownikByNumer( { numer: 2, 'uzytkownik-nazwa': "Piotr Wiśniewski" }).subscribe({
      next: (dzialka) => {
        this.dzialka = dzialka;
        console.log(dzialka);
        this.processRosliny(dzialka);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  processRosliny(dzialka: DzialkaResponse): void {
    if (dzialka.zasadzoneRosliny) {
      dzialka.zasadzoneRosliny.forEach(zasadzonaRoslina => {
        if (zasadzonaRoslina.tabX && zasadzonaRoslina.tabY) {
          this.updateTilesWithRoslina(zasadzonaRoslina);
        }
      });
    }
  }

  updateTilesWithRoslina(zasadzonaRoslina: any): void {
    zasadzonaRoslina.tabX.forEach((x: number, index: number) => {
      const y = zasadzonaRoslina.tabY[index];
      const tile = this.tiles.find(t => t.x === x && t.y === y);
      if (tile) {
        if (zasadzonaRoslina.roslina && zasadzonaRoslina.roslina.id) {
          tile.roslinaId = zasadzonaRoslina.roslina.id;
          tile.backgroundColor = this.placeholderColors[zasadzonaRoslina.roslina.id % this.placeholderColors.length];
          const imageBlob = this.base64ToBlob(zasadzonaRoslina.obraz ?? '', this.images.grass);
          const imageUrl = URL.createObjectURL(imageBlob);
          // I dla tile ustawiasz obraz taki:
          tile.image = imageUrl || this.images.grass;
        }
        if (tile.x == zasadzonaRoslina.x && tile.y == zasadzonaRoslina.y) {
          tile.roslina = zasadzonaRoslina.roslina;
          if (tile.roslina && zasadzonaRoslina.obraz) {
            tile.roslina.obraz = zasadzonaRoslina.obraz;
          }
        }
      }
    });
  }



  generateInitialTiles(): void {
    const tiles: Tile[] = [];
    for (let y = 0; y < 22; y++) {
      for (let x = 0; x < 22; x++) {
        const isEdge = x < 2 || x >= 20 || y < 2 || y >= 20;
        tiles.push({
          image: isEdge ? this.images.grass : this.images.dirt,
          x: isEdge ? x - 2 : x,
          y: isEdge ? y - 2 : y,
          roslina: undefined,
          clickable: true,
          hovered: false
        });
      }
    }
    this.tiles = tiles;
  }


  // Uwaga, to są placeholdery. W prawidłowej implementacji obrazy są już załadowane base64 oprócz dirt i grass
  // Samo dirt i grass zostaną przeniesione do backendu czy coś
  generatePlaceholderTiles() {
    const tiles: Tile[] = [];
    for (let y = 0; y < 20; y++) {
      for (let x = 0; x < 20; x++) {
        //const isEdge = x < 2 || x >= 18 || y < 2 || y >= 18;
        const isEveryXthTile = (x + y * 20) % 24 === 0
        tiles.push({
          image: this.images.dirt,
          x,
          y,
          roslina: isEveryXthTile ? { nazwa: 'Jakaś roślina', obraz: 'assets/tiles/plant.png' } : undefined,
          clickable: true,
          hovered: false
        });
      }
    }
    this.tiles = tiles;
  }

  /*
  Jak wywali dla tileów protokół 431 to tym można to objejść
  */
  /*
  const imageBlob = this.base64ToBlob(zasadzonaRoslina.obraz ?? '', this.images.grass);
  const imageUrl = URL.createObjectURL(imageBlob);
  // I dla tile ustawiasz obraz taki:
    image: imageUrl || this.images.grass,
  */


  base64ToBlob(base64: string, contentType: string): Blob {
    const byteCharacters = atob(base64);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    return new Blob([byteArray], { type: contentType });
  }





  onTileClick(tile: Tile) {
    console.log(`Koordynaty kafelka: (${tile.x}, ${tile.y})
      RoslinaId: ${tile.roslinaId}
      Należy do rośliny: ${tile.roslina?.nazwa}
      Kolorek: ${tile.backgroundColor}`);
   // tile.image = 'assets/tiles/water.png';
  }

  onZoomChange(event: Event) {
    const zoomLevel = (event.target as HTMLInputElement).value;
    this.scale = Number(zoomLevel);
    this.canvasElement.nativeElement.style.transform = `scale(${this.scale})`;
    this.zoomEl.nativeElement.style.transform = `scale(${this.scale})`;
    document.documentElement.style.setProperty('--scale', this.scale.toString());
  }

  // Uwaga: Nie będzie działać chyba że podaż base64 dla defaultowych kafelków
  saveCanvasAsImage(): void {
    html2canvas(this.canvasElement.nativeElement).then(canvas => {
      const link = document.createElement('a');
      link.href = canvas.toDataURL('image/png');
      link.download = 'canvas-image.png';
      link.click();
    });
  }


}
