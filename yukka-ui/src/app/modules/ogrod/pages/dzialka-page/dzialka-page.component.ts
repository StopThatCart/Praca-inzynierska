import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dzialka-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dzialka-page.component.html',
  styleUrl: './dzialka-page.component.css'
})
export class DzialkaPageComponent implements OnInit  {
  @ViewChild('canvas', { static: true }) canvasElement!: ElementRef;
  tiles: { color: string }[] = [];


  ngOnInit() {
    this.generateTiles();
  }

  generateTiles() {
    const colors = ['#FF5733', '#33FF57', '#3357FF', '#F3FF33', '#FF33F6'];
    for (let i = 0; i < 400; i++) {
      this.tiles.push({ color: colors[i % colors.length] });
    }
  }

}
