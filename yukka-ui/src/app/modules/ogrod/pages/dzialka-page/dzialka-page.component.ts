import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import html2canvas from 'html2canvas';
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

  @ViewChild('tileSet', { static: true }) tileSet!: ElementRef; // Do usunięcia

  @ViewChild('slider', { static: true }) zoomEl!: ElementRef;

  @ViewChild('canvasContainer', { static: true }) canvasContainer!: ElementRef;
  @ViewChild('canvasElement', { static: true }) canvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('overlayBoi', { static: true }) overlay!: ElementRef;
  tiles: Tile[] = [];

  scale : number = 1;
  tileSize: number = 64;
  rowColls: number = 20;

  canvasWidth: number = this.tileSize * this.rowColls;

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
    this.initializeTiles();
    this.drawChessboard();
    console.log(this.tiles.length);
    //this.generateInitialTiles();

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

    // Uwaga, to są placeholdery. W prawidłowej implementacji obrazy są już załadowane base64 oprócz dirt i grass
  // Samo dirt i grass zostaną przeniesione do backendu czy coś
  initializeTiles(): void {
    const tiles: Tile[] = [];
    for (let y = 0; y < this.rowColls; y++) {
      for (let x = 0; x < this.rowColls; x++) {
        //const isEdge = x < 2 || x >= 18 || y < 2 || y >= 18;
        //const isEveryXthTile = (x + y * 20) % 44 === 0;
        tiles.push({
          image: this.images.dirt,
          x,
          y,
          roslina: undefined,
          //roslina: isEveryXthTile ? { nazwa: 'Jakaś roślina', obraz: 'assets/tiles/plant.png' } : undefined,
          clickable: true,
          hovered: false,
          backgroundColor: undefined
        });
      }
    }
    this.tiles = tiles;
  }

  getRandomColor(): string {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }


  drawChessboard(): void {
    const canvas = this.canvas.nativeElement;
    const overlay = this.overlay.nativeElement;
    const rows = this.rowColls;
    const cols = rows;

    const ctx = canvas.getContext('2d');
    if (ctx) {
      console.log(canvas.width, canvas.height);
      for (let row = 0; row < rows; row++) {
        for (let col = 0; col < cols; col++) {
          const tile = this.findTile(col, row);
          const img = new Image();
          img.src = tile.image || this.images.grass;
          img.onload = () => {
            ctx.drawImage(img, col * this.tileSize, row * this.tileSize, this.tileSize, this.tileSize);
            if (tile.roslina) {
              console.log('Roslina:', tile.roslina);
              tile.roslina.obraz = this.images.grass;  // TODO

              const roslinaImg = new Image();
              roslinaImg.src = tile.roslina.obraz || '';
              roslinaImg.onload = () => {
                ctx.drawImage(roslinaImg, col * this.tileSize, row * this.tileSize, this.tileSize, this.tileSize);
              };
            }

          };
        }
      }
    }
    //console.log(canvas.width, canvas.height);
    // Już jest to zrobione w overlay.
   // canvas.addEventListener('click', this.onCanvasClick.bind(this));
  }

  onCanvasClick(event: MouseEvent): void {
    const canvas = this.canvas.nativeElement;
    const rect = canvas.getBoundingClientRect();
    const x = (event.clientX - rect.left) / this.scale;
    const y = (event.clientY - rect.top) / this.scale;
    const col = Math.floor(x / this.tileSize);
    const row = Math.floor(y / this.tileSize);
    console.log(`Tile clicked: (${col}, ${row})`);
    const tile = this.findTile(col, row);
    if(tile) {
      this.onTileClick(tile);
    }
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

  getRgbaColor(hex: string | undefined): string {

    if (!hex) {
      return `transparent`;
    }
    const bigint = parseInt(hex.slice(1), 16);
    const r = (bigint >> 16) & 255;
    const g = (bigint >> 8) & 255;
    const b = bigint & 255;

    return `rgba(${r},${g},${b}, 0.5)`;
  }


  findTile(x: number, y: number): Tile  {
    const tile = this.tiles.find(t => t.x === x && t.y === y);
    if (!tile) {
      throw new Error(`Nie znaleziono kafelka na koordynatach(${x}, ${y})`);
    }
    return tile;
  }

  onTileClick(tile: Tile) {
    console.log(`Koordynaty kafelka: (${tile.x}, ${tile.y})
      RoslinaId: ${tile.roslinaId}
      Należy do rośliny: ${tile.roslina?.nazwa}
      Kolorek: ${tile.backgroundColor}`);
   // tile.image = 'assets/tiles/water.png';
  }

  onTileHover(tile: any, isHovering: boolean) {
    //console.log(`Kafelkek (${tile.x}, ${tile.y}) ${isHovering ? 'najechano' : 'zjechano'}`);
    tile.hovered = isHovering;
    // TODO: Przerób Tile[] na Tile[][] potem
    const tileElement = document.querySelector(`.tile-overlay[data-x="${tile.x}"][data-y="${tile.y}"]`) as HTMLElement;
    if (!tileElement) return;
    if (isHovering) {
      if(!tile.backgroundColor) {
        tileElement.style.backgroundColor = this.getRandomColor();
      } else {
        tileElement.style.backgroundColor = this.getRgbaColor(tile.backgroundColor);

      }

      tileElement.style.filter = 'brightness(1.2)';
    } else {
      if(!tile.backgroundColor) {
        tileElement.style.backgroundColor = 'transparent';
      }
      tileElement.style.filter = 'none';
    }

  }

  onZoomChange(event: Event) {
    const zoomLevel = (event.target as HTMLInputElement).value;
    this.scale = Number(zoomLevel);
    this.applyScale(this.scale);

    //this.canvas.nativeElement.style.transform = `scale(${this.scale})`;
  }

  applyScale( scale: number ): void {
    if(scale >= 0.1 && scale <= 2.0) {
      const canvas = this.canvas.nativeElement;
      const overlay = this.overlay.nativeElement;

      canvas.style.transform = `scale(${scale})`;
      overlay.style.transform = `scale(${scale})`;
    }
  }

  // Uwaga: Działa na Operze, ale na innych nie sprawdzałem
  saveCanvasAsImage(): void {
    this.applyScale(1);
    const canvas = this.canvas.nativeElement;
    const overlay = this.overlay.nativeElement;
    //const natEl = this.tileSet.nativeElement;
    //const natEl = this.canvasContainer.nativeElement;

    html2canvas(canvas, { backgroundColor: null }).then(canvasImage => {
      html2canvas(overlay, { backgroundColor: null }).then(overlayImage => {
          const combinedCanvas = document.createElement('canvas');
          combinedCanvas.width = canvasImage.width;
          combinedCanvas.height = canvasImage.height;

          const context = combinedCanvas.getContext('2d');
          if (context) {
              context.drawImage(canvasImage, 0, 0);
              context.drawImage(overlayImage, 0, 0);

              const link = document.createElement('a');
              link.href = combinedCanvas.toDataURL('image/png');
              link.download = 'canvas-image.png';
              link.click();
          }
      });
  });
  }


}
