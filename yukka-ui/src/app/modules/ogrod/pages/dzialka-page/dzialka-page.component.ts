import { Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import html2canvas from 'html2canvas';
import { MoveRoslinaRequest, Pozycja, RoslinaResponse, ZasadzonaRoslinaResponse } from '../../../../services/models';
import { PostCardComponent } from "../../../post/components/post-card/post-card.component";
import { LulekComponent } from "../../components/lulek/lulek.component";
import { DzialkaResponse } from '../../../../services/models/dzialka-response';
import { ActivatedRoute } from '@angular/router';
import { DzialkaService } from '../../../../services/services';
import { Tile, TileUtils } from '../../models/Tile';
import { FormsModule } from '@angular/forms';
import { ScaleSliderComponent } from "../../components/scale-slider/scale-slider.component";
import { CanvasService } from '../../services/canvas-service/canvas.service';
import { Offcanvas } from 'bootstrap';
import { OffcanvasRoslinaComponent } from "../../components/offcanvas-roslina/offcanvas-roslina.component";
import { DzialkaModes } from '../../models/dzialka-modes';
import { DzialkaRoslinaRequest } from '../../../../services/models/dzialka-roslina-request';

@Component({
  selector: 'app-dzialka-page',
  standalone: true,
  imports: [CommonModule, FormsModule, PostCardComponent, LulekComponent, ScaleSliderComponent, OffcanvasRoslinaComponent],
  templateUrl: './dzialka-page.component.html',
  styleUrl: './dzialka-page.component.css'
})
export class DzialkaPageComponent implements OnInit  {
  dzialka: DzialkaResponse = {};
  dzialkaBackup: DzialkaResponse = {};

  numer: number | undefined;
  uzytNazwa: string | undefined;
  selectedRoslina: ZasadzonaRoslinaResponse | undefined;

  moveRoslinaRequest: MoveRoslinaRequest = {
    numerDzialki: 0,
    pozycje: [],
    x: -1,
    y: -1
  };


  @ViewChild('tileSet', { static: true }) tileSet!: ElementRef; // Do usunięcia
  @ViewChild('slider', { static: true }) zoomEl!: ElementRef;

  @ViewChild('canvasContainer', { static: true }) canvasContainer!: ElementRef;
  @ViewChild('canvasElement', { static: true }) canvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('overlayBoi', { static: true }) overlay!: ElementRef;

  @ViewChild('backgroundCanvas', { static: true }) backgroundCanvas!: ElementRef<HTMLCanvasElement>;


  @ViewChild(OffcanvasRoslinaComponent) offcanvasBottom!: OffcanvasRoslinaComponent;
  tiles: Tile[] = [];

  scale : number = 1;
  tileSize: number = 64;
  rowColls: number = 20;
  canvasWidth: number = this.tileSize * this.rowColls;

  mode: DzialkaModes = DzialkaModes.Select;
  editMode: DzialkaModes = DzialkaModes.BrakEdycji;
  isSaving: boolean = false;

  setMode(mode: DzialkaModes): void {
    this.mode = mode;
  }

  changeEditMode(mode: DzialkaModes): void {
    this.editMode = mode;
    console.log('pozycje:', this.selectedRoslina?.pozycje);
  }

  createBackup(): void {
    console.log('Tworzę backup');

    let copy = structuredClone(this.dzialka);
    this.dzialkaBackup = copy;
  }

  cancelChanges(): void {
    console.log('Anuluję zmiany');
    if (this.dzialkaBackup) {
      console.log("dzialkaBackup:", this.dzialkaBackup);
      console.log("dzialka:", this.dzialka);
      this.dzialka = this.dzialkaBackup;
      this.dzialkaBackup = {};

      this.initializeTiles();
      this.processRosliny(this.dzialka);
    }
    this.editMode = DzialkaModes.BrakEdycji;
  }

  private images = {
    grass: 'assets/tiles/grass.png',
    dirt: 'assets/tiles/dirt.png'
  };

  DzialkaModes = DzialkaModes;

  constructor(
    private route: ActivatedRoute,
    private dzialkaService: DzialkaService,
    private canvasService: CanvasService
  ) {}


  ngOnInit() {
    // TODO: Dodawanie ładowania
    this.initializeTiles();
    this.drawChessboard();

    this.route.params.subscribe(params => {
      this.numer = Number(params['numer']);
      this.uzytNazwa = params['uzytkownik-nazwa'];
      this.moveRoslinaRequest.numerDzialki = this.numer;

      if (this.numer && this.uzytNazwa) {
        this.getDzialkaByNumer( this.numer, this.uzytNazwa);
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
          zasadzonaRoslina: undefined,
          //roslina: isEveryXthTile ? { nazwa: 'Jakaś roślina', obraz: 'assets/tiles/plant.png' } : undefined,
          clickable: true,
          hovered: false,
          backgroundColor: undefined
        });
      }
    }
    this.tiles = tiles;
  }

  drawChessboard(): void {
    const canvas = this.canvas.nativeElement;
    const backgroundCanvas = this.backgroundCanvas.nativeElement;
    const rows = this.rowColls;
    const cols = rows;

    const ctx = canvas.getContext('2d');
    const bgCtx = backgroundCanvas.getContext('2d');
    if (ctx && bgCtx) {
      console.log(canvas.width, canvas.height);
      for (let row = 0; row < rows; row++) {
        for (let col = 0; col < cols; col++) {
          const tile = TileUtils.findTile(this.tiles, col, row);
          if(!tile) {
            console.error('Nie znaleziono kafelka podczas generowania planszy');
            return;
          }

          this.drawTileTexture(tile, ctx);
          this.drawTileBackground(tile, bgCtx);

        }
      }
    }
  }

  private drawTileTexture(tile: Tile, ctx : CanvasRenderingContext2D): void {
    console.log('drawTileTexture');
    const img = new Image();

    if(ctx) {
      if(tile.image && tile.image !== this.images.dirt && tile.image !== this.images.grass) {
        img.src = 'data:image/jpeg;base64,' + tile.image;
      } else if(tile.image === this.images.dirt) {
        img.src = this.images.dirt;
      } else {
        img.src = this.images.grass;
      }

      img.onload = () => {
          ctx.drawImage(img, tile.x * this.tileSize, tile.y * this.tileSize, this.tileSize, this.tileSize);
      };
    }
  }

  private drawTileBackground(tile: Tile, ctx: CanvasRenderingContext2D): void {
    console.log('drawTileBackground');
    ctx.fillStyle = this.getRgbaColor(tile.backgroundColor);
    ctx.fillRect(tile.x * this.tileSize, tile.y * this.tileSize, this.tileSize, this.tileSize);
  }




  changeRoslinaPozycjaInDzialka(): void {
    console.log('changeRoslinaPozycjaInDzialka');
    if(!this.selectedRoslina?.pozycje || this.numer === undefined) return;

    this.moveRoslinaRequest.pozycje = this.selectedRoslina?.pozycje;

    this.moveRoslinaRequest.numerDzialki = this.numer;
    this.moveRoslinaRequest.xnowy = this.selectedRoslina.x;
    this.moveRoslinaRequest.ynowy = this.selectedRoslina.y;

    this.dzialkaService.updateRoslinaPositionInDzialka( { body: this.moveRoslinaRequest }).subscribe({
      next: (dzialka) => {
        this.editMode = DzialkaModes.BrakEdycji;
        this.selectedRoslina = undefined;
        this.dzialka = dzialka;
        console.log(dzialka);

        this.processRosliny(dzialka);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  getDzialkaByNumer(numer: number, uzytkownikNazwa: string): void {
    console.log('getDzialkaByNumer');
    this.dzialkaService.getDzialkaOfUzytkownikByNumer( { numer: numer, 'uzytkownik-nazwa': uzytkownikNazwa }).subscribe({
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



  onRoslinaDelete() {
    console.log('onRoslinaDelete');
    this.initializeTiles();
    this.drawChessboard();
    this.getDzialkaByNumer(this.numer!, this.uzytNazwa!);
  }

  onRoslinaKolorChange(kolor: string) {
    console.log('onRoslinaKolorChange');
    if(this.selectedRoslina) {
      this.selectedRoslina.kolor = kolor;
      this.updateTilesWithRoslina(this.selectedRoslina, false);
    }
  }

  processRosliny(dzialka: DzialkaResponse): void {
    console.log('processRosliny');
    if (dzialka.zasadzoneRosliny) {
      dzialka.zasadzoneRosliny.forEach(zasadzonaRoslina => {
        if (zasadzonaRoslina.pozycje) {
          this.updateTilesWithRoslina(zasadzonaRoslina, true);
        }
      });
    }
    this.createBackup();
  }

  updateTilesWithRoslina(zasadzonaRoslina: ZasadzonaRoslinaResponse, drawTekstury : boolean): void {
    const bgCtx = this.backgroundCanvas.nativeElement.getContext('2d');
    if (!bgCtx) return;

    zasadzonaRoslina.pozycje?.forEach((pozycja: Pozycja) => {
      if (pozycja.x === undefined || pozycja.y === undefined) {
        throw new Error('Nieprawidłowa pozycja rośliny');
      }

      const tile = TileUtils.findTile(this.tiles, pozycja.x, pozycja.y);
      if (tile) {
        if (zasadzonaRoslina.roslina && zasadzonaRoslina.roslina.id) {
          tile.roslinaId = zasadzonaRoslina.roslina.id;
          tile.backgroundColor = zasadzonaRoslina.kolor;
          if (tile.image === this.images.dirt || tile.image === zasadzonaRoslina.tekstura) {
            tile.image = zasadzonaRoslina.tekstura || this.images.dirt;
            if (drawTekstury) {
            this.drawTileTexture(tile, this.canvas.nativeElement.getContext('2d')!);
          }
          }

          //tile.backgroundColor = this.placeholderColors[zasadzonaRoslina.roslina.id % this.placeholderColors.length];
        }
        if (tile.x == zasadzonaRoslina.x && tile.y == zasadzonaRoslina.y) {
          tile.zasadzonaRoslina = zasadzonaRoslina;
          if (tile.zasadzonaRoslina && zasadzonaRoslina.obraz) {
            if (tile.zasadzonaRoslina.roslina) {
                tile.zasadzonaRoslina.roslina.obraz = zasadzonaRoslina.obraz;
            }
          }
        }
        this.drawTileBackground(tile, bgCtx);
      }
    });
    this.redrawBackgroundCanvas();
  }

  onTileClick(tile: Tile) {
    if(this.mode === DzialkaModes.Pan) {
      return;
    }
    if (this.editMode === DzialkaModes.EditRoslinaKafelki || this.editMode === DzialkaModes.EditRoslinaPozycja) {
      if(tile.zasadzonaRoslina) {
        console.log('Na tym kafelku znajduje się roślina.');
        return;
      }

      if(tile.roslinaId  && tile.roslinaId !== this.selectedRoslina?.roslina?.id) {
        console.log('Ten kafelek nie jest przypisany do tej rośliny.');
        return;
      }

      if(this.editMode === DzialkaModes.EditRoslinaKafelki) {
        this.changeRoslinaKafelek(tile);
      }else if (this.editMode === DzialkaModes.EditRoslinaPozycja) {
        this.changeRoslinaPozycja(tile);
      }
    }

    else {
      console.log(`Koordynaty kafelka: (${tile.x}, ${tile.y})
        RoslinaId: ${tile.roslinaId}
        Leży na nim roślina: ${tile.zasadzonaRoslina?.roslina?.nazwa}
        Kolorek: ${tile.backgroundColor}
        tekstura jest?: ${tile.image ? 'tak' : 'nie'}
        czy to ziemia?: ${tile.image === this.images.dirt ? 'tak' : 'nie'}
        `);
    }
   // tile.image = 'assets/tiles/water.png';
  }



  changeRoslinaKafelek(tile: Tile): void {
    if(!this.selectedRoslina) return;
    const index = this.selectedRoslina.pozycje?.findIndex(p => p.x === tile.x && p.y === tile.y);
      if (index !== undefined && index !== -1) {
        this.selectedRoslina.pozycje?.splice(index, 1);
        TileUtils.clearTile(tile);
        this.drawTileTexture(tile, this.canvas.nativeElement.getContext('2d')!);
      } else {
        this.selectedRoslina.pozycje?.push({ x: tile.x, y: tile.y });
        tile.roslinaId = this.selectedRoslina.roslina?.id;
        tile.backgroundColor = this.getRandomColor();
        tile.image = this.selectedRoslina.tekstura || this.images.dirt;
      }

      this.updateTilesWithRoslina(this.selectedRoslina, false);
  }


  changeRoslinaPozycja(tile: Tile): Tile | undefined {
    if(!this.selectedRoslina) return;
    const oldTile = TileUtils.findTile(this.tiles, this.selectedRoslina.x!, this.selectedRoslina.y!);
    if(oldTile) {
      console.log('Czyści stary kafelek');
      TileUtils.removeRoslina(oldTile);

    }

    tile.roslinaId = this.selectedRoslina.roslina?.id;
    tile.zasadzonaRoslina = this.selectedRoslina;
    tile.backgroundColor = this.selectedRoslina.kolor;
    tile.image = this.selectedRoslina.tekstura || this.images.dirt;

    if(this.selectedRoslina.pozycje?.findIndex(p => p.x === tile.x && p.y === tile.y) === -1) {
      this.selectedRoslina.pozycje?.push({ x: tile.x, y: tile.y });
    }
    this.selectedRoslina.x = tile.x;
    this.selectedRoslina.y = tile.y;

    this.updateTilesWithRoslina(this.selectedRoslina, false);

    return oldTile;
  }


  private redrawBackgroundCanvas(): void {
    const bgCtx = this.backgroundCanvas.nativeElement.getContext('2d');
    if (!bgCtx) return;

    bgCtx.clearRect(0, 0, this.backgroundCanvas.nativeElement.width, this.backgroundCanvas.nativeElement.height);

    for (const tile of this.tiles) {
      this.drawTileBackground(tile, bgCtx);
    }
  }

  onTileHover(tile: any, isHovering: boolean) {
    tile.hovered = isHovering;

    const bgCtx = this.backgroundCanvas.nativeElement.getContext('2d');
    if (!bgCtx) return;

    if (isHovering) {
      bgCtx.save();
      bgCtx.globalAlpha = 0.5;
      bgCtx.fillStyle = 'rgba(255, 255, 255, 0.5)';
      bgCtx.fillRect(tile.x * this.tileSize, tile.y * this.tileSize, this.tileSize, this.tileSize);
      bgCtx.restore();
    } else {
      this.redrawBackgroundCanvas();
    }
  }

  onRoslinaClick(roslina: ZasadzonaRoslinaResponse): void {
    if (this.mode === DzialkaModes.Select) {
      this.selectedRoslina = roslina;
      if(roslina.x && roslina.y) {
        this.moveRoslinaRequest.x = roslina.x;
        this.moveRoslinaRequest.y = roslina.y;
      }

      const offcanvasElement = this.offcanvasBottom.offcanvasBottom.nativeElement;
      if (offcanvasElement) {
        const bsOffcanvas = new Offcanvas(offcanvasElement);
        bsOffcanvas.show();
      }
    }
  }

  onRoslinaPozycjaEdit(roslina: RoslinaResponse): void {
    if(this.editMode !== DzialkaModes.BrakEdycji) return;
    this.editMode = DzialkaModes.EditRoslinaPozycja;
  }

  onMouseDown(event: MouseEvent): void {
    this.canvasService.onMouseDown(event, this.canvasContainer, this.mode);
  }

  @HostListener('document:mousemove', ['$event'])
  onMouseMove(event: MouseEvent): void {
    this.canvasService.onMouseMove(event, this.canvasContainer);
  }

  @HostListener('document:mouseup')
  onMouseUp(): void {
    this.canvasService.onMouseUp();
  }

  onScaleChange(newScale: number) {
    this.scale = newScale;
    this.applyScale(this.scale);
  }

  applyScale( scale: number ): void {
    if(scale >= 0.1 && scale <= 2.0) {
      const canvas = this.canvas.nativeElement;
      const overlay = this.overlay.nativeElement;
      const backgroundCanvas = this.backgroundCanvas.nativeElement;

      canvas.style.transform = `scale(${scale})`;
      overlay.style.transform = `scale(${scale})`;
      backgroundCanvas.style.transform = `scale(${scale})`;
    }
  }


  getRgbaColor(hex: string | undefined): string {
    return this.canvasService.getRgbaColor(hex);
  }

  getRandomColor(): string {
    return this.canvasService.getRandomColor();
  }

  saveCanvasAsImage(): void {
    if(confirm('Czy na pewno chcesz zapisać obraz działki?')) {
      this.isSaving = true;
      this.canvasService.saveCanvasAsImage(this.canvas, this.overlay, this.backgroundCanvas, this.onScaleChange.bind(this));
      this.isSaving = false;
    }
  }


}
