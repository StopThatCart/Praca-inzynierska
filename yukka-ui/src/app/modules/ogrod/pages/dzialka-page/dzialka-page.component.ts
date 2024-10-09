import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { RoslinaBoxComponent } from "../../components/roslina-box/roslina-box.component";
import { DragDropModule, DragRef, Point } from '@angular/cdk/drag-drop';
import { ScrollingModule } from '@angular/cdk/scrolling';



@Component({
  selector: 'app-dzialka-page',
  standalone: true,
  imports: [RoslinaBoxComponent, DragDropModule, ScrollingModule],
  templateUrl: './dzialka-page.component.html',
  styleUrl: './dzialka-page.component.css'
})
export class DzialkaPageComponent implements OnInit, OnDestroy {

  @ViewChild('boundary', { static: true }) boundaryElement!: ElementRef;
  private scrollInterval: any;
  private scrollThreshold = 60;
  private scrollSpeed = 1; // Prędkość przewijania
  private scrollFactor = 0.9; // Czynnik przewijania
  private isScrolling = false;

  ngOnInit() {
    if (this.boundaryElement) {
      this.boundaryElement.nativeElement.addEventListener('mousedown', this.onMouseDown);
      this.boundaryElement.nativeElement.addEventListener('mouseup', this.onMouseUp);
      this.boundaryElement.nativeElement.addEventListener('mouseleave', this.onMouseUp);
    }
  }

  ngOnDestroy() {
    if (this.boundaryElement) {
      this.boundaryElement.nativeElement.removeEventListener('mousedown', this.onMouseDown);
      this.boundaryElement.nativeElement.removeEventListener('mouseup', this.onMouseUp);
      this.boundaryElement.nativeElement.removeEventListener('mouseleave', this.onMouseUp);
      this.boundaryElement.nativeElement.removeEventListener('mousemove', this.smoothScroll);
    }
  }

  onMouseDown = (event: MouseEvent) => {
    this.isScrolling = true;
    this.boundaryElement.nativeElement.addEventListener('mousemove', this.smoothScroll);

    this.smoothScroll(event);
  };

  onMouseUp = () => {
    this.isScrolling = false;
    this.boundaryElement.nativeElement.addEventListener('mousemove', this.smoothScroll);

  };


  smoothScroll = (event: MouseEvent) => {
    return;
    if (!this.isScrolling) return;

    const boundaryElement = this.boundaryElement.nativeElement;
    if (!boundaryElement) return;

    const rect = boundaryElement.getBoundingClientRect();

    let x = 0;
    let y = 0;

    if (event.clientY < rect.top + this.scrollThreshold) {
      y = -this.scrollSpeed;
    } else if (event.clientY > rect.bottom - this.scrollThreshold) {
      y = this.scrollSpeed;
    }

    if (event.clientX < rect.left + this.scrollThreshold) {
      x = -this.scrollSpeed;
    } else if (event.clientX > rect.right - this.scrollThreshold) {
      x = this.scrollSpeed;
    }

    boundaryElement.scrollTop += y;
    boundaryElement.scrollLeft += x;

    setTimeout(() => this.smoothScroll(event), 16);
  };
}
