import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import {  } from 'rxjs';
import { RoslinaResponse } from '../../../../services/models';
import { PostCardComponent } from "../../../post/components/post-card/post-card.component";
import { LulekComponent } from "../../components/lulek/lulek.component";
import { DzialkaResponse } from '../../../../services/models/dzialka-response';
import { ActivatedRoute } from '@angular/router';
import { DzialkaService } from '../../../../services/services';

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

  @ViewChild('canvas', { static: true }) canvasElement!: ElementRef;
  tiles: { image: string, x: number, y: number, clickable: boolean, showRoslinaBox?: boolean, roslina?: RoslinaResponse }[] = [];

  scale : number = 1;

  constructor(
    private route: ActivatedRoute,
    private dzialkaService: DzialkaService
  ) {}


  ngOnInit() {
    this.route.params.subscribe(params => {
      this.numer = Number(params['numer']);
      if (this.numer) {
        // Na razie dane są sztywne
        this.getDzialkaByNumer(this.numer);
        this.route.snapshot.data['numer'] = this.numer;
      }
    });



    this.generatePlaceholderTiles();
  }


  getDzialkaByNumer(numer: number): void {
    console.log('getDzialkaByNumer');
    this.dzialkaService.getDzialkaOfUzytkownikByNumer( { numer: 2, 'uzytkownik-nazwa': "Piotr Wiśniewski" }).subscribe({
      next: (dzialka) => {
        this.dzialka = dzialka;
        console.log(dzialka);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }


  // Uwaga, to są placeholdery. W prawidłowej implementacji obrazy są już załadowane base64 oprócz dirt i grass
  // Samo dirt i grass zostaną przeniesione do backendu czy coś
  generatePlaceholderTiles() {
    const images = {
      grass: 'assets/tiles/grass.png',
      dirt: 'assets/tiles/dirt.png'
    };

    for (let y = 0; y < 20; y++) {
      for (let x = 0; x < 20; x++) {
        //const isEdge = x < 2 || x >= 18 || y < 2 || y >= 18;
        const isEveryXthTile = (x + y * 20) % 24 === 0
        this.tiles.push({
          image: images.dirt,
          x,
          y,
          clickable: true,
          showRoslinaBox: isEveryXthTile,
          roslina: isEveryXthTile ? { nazwa: 'Jakaś roślina', obraz: 'assets/tiles/plant.png' } : undefined
        });
      }
    }
  }

  onTileClick(tile: { image: string, x: number, y: number, clickable: boolean }) {
    console.log(`Koordynaty kafelka: (${tile.x}, ${tile.y})`);
   // tile.image = 'assets/tiles/water.png';
  }

  onZoomChange(event: Event) {
    const zoomLevel = (event.target as HTMLInputElement).value;
    this.scale = Number(zoomLevel);
    this.canvasElement.nativeElement.style.transform = `scale(${this.scale})`;
  }



}
