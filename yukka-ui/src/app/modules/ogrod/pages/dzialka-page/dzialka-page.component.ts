import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import {  } from 'rxjs';

@Component({
  selector: 'app-dzialka-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dzialka-page.component.html',
  styleUrl: './dzialka-page.component.css'
})
export class DzialkaPageComponent implements OnInit  {
  @ViewChild('canvas', { static: true }) canvasElement!: ElementRef;
  tiles: { image: string }[] = [];


  ngOnInit() {
    this.generateTiles();
  }

  generateTiles() {
    const images = ['assets/tiles/grass.png', 'assets/tiles/dirt.png'];
    for (let i = 0; i < 400; i++) {
      this.tiles.push({ image: images[i % images.length] });
    }
  }

}
