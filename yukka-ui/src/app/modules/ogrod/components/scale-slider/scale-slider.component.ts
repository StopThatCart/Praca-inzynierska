import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-scale-slider',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './scale-slider.component.html',
  styleUrl: './scale-slider.component.css'
})
export class ScaleSliderComponent {
  @Input() scale: number = 1.0;
  @Input() disabled: boolean = false;
  @Output() scaleChange = new EventEmitter<number>();

  onZoomChange(event: Event) {
    if (!this.disabled) {
      const zoomLevel = (event.target as HTMLInputElement).value;
      this.scale = Number(zoomLevel);
      this.scaleChange.emit(this.scale);
    }
  }

  changeZoom(zoom: number) {
    if (!this.disabled && this.scale + zoom >= 0.1 && this.scale + zoom <= 2.0) {
      this.scale = Number((this.scale + zoom).toFixed(1));
      this.scaleChange.emit(this.scale);
    }
  }
}
