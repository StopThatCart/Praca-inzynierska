import { Injectable, ElementRef } from '@angular/core';
import html2canvas from 'html2canvas';
@Injectable({
  providedIn: 'root'
})
export class CanvasService {

  saveCanvasAsImage(canvas: ElementRef, overlay: ElementRef, onScaleChange: (scale: number) => void): void {
    onScaleChange(1);
    let canvasElement = canvas.nativeElement;
    let overlayElement = overlay.nativeElement;

    const originalCanvasTransition = canvasElement.style.transition;
    const originalOverlayTransition = overlayElement.style.transition;

    canvasElement.style.transition = 'none';
    overlayElement.style.transition = 'none';

    onScaleChange(1);
    canvasElement = canvas.nativeElement;
    overlayElement = overlay.nativeElement;

    html2canvas(canvasElement, { backgroundColor: null }).then(canvasImage => {
      html2canvas(overlayElement, { backgroundColor: null }).then(overlayImage => {
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

          canvas.nativeElement.style.transition = originalCanvasTransition;
          overlay.nativeElement.style.transition = originalOverlayTransition;
      });
    });
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

  getRandomColor(): string {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }
}
