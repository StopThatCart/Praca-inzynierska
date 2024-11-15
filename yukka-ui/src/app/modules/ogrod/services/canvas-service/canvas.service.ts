import { Injectable, ElementRef } from '@angular/core';
import html2canvas from 'html2canvas';
import { DzialkaModes } from '../../models/dzialka-modes';
@Injectable({
  providedIn: 'root'
})
export class CanvasService {
  private isPanning: boolean = false;
  private startX: number = 0;
  private startY: number = 0;
  private scrollLeft: number = 0;
  private scrollTop: number = 0;

  saveCanvasAsImage(canvas: ElementRef, 
    overlay: ElementRef, 
    backgroundCanvas: ElementRef , 
    onScaleChange: (scale: number) => void): Promise<void> {
    return new Promise((resolve) => {

    onScaleChange(1);
    let canvasElement = canvas.nativeElement;
    let overlayElement = overlay.nativeElement;
    let highlightCanvasElement = backgroundCanvas.nativeElement;

    const originalCanvasTransition = canvasElement.style.transition;
    const originalOverlayTransition = overlayElement.style.transition;
    const originalHighlightCanvasTransition = highlightCanvasElement.style.transition;

    canvasElement.style.transition = 'none';
    overlayElement.style.transition = 'none';
    highlightCanvasElement.style.transition = 'none';

    onScaleChange(1);
    canvasElement = canvas.nativeElement;
    overlayElement = overlay.nativeElement;
    highlightCanvasElement = backgroundCanvas.nativeElement;

    html2canvas(canvasElement, { backgroundColor: null }).then(canvasImage => {
      html2canvas(overlayElement, { backgroundColor: null }).then(overlayImage => {
        html2canvas(highlightCanvasElement, { backgroundColor: null }).then(highlightCanvasImage => {
          const combinedCanvas = document.createElement('canvas');
          combinedCanvas.width = canvasImage.width;
          combinedCanvas.height = canvasImage.height;

          const context = combinedCanvas.getContext('2d');
          if (context) {
              context.drawImage(canvasImage, 0, 0);
              context.drawImage(highlightCanvasImage, 0, 0);
              context.drawImage(overlayImage, 0, 0);

              const link = document.createElement('a');
              link.href = combinedCanvas.toDataURL('image/png');
              link.download = 'canvas-image.png';
              link.click();
          }

          canvas.nativeElement.style.transition = originalCanvasTransition;
          overlay.nativeElement.style.transition = originalOverlayTransition;
          backgroundCanvas.nativeElement.style.transition = originalHighlightCanvasTransition;
          
          resolve();
        });
      });
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

  onMouseDown(event: MouseEvent, canvasContainer: ElementRef, mode: string): void {
    if (mode !== DzialkaModes.Pan) return;
    this.isPanning = true;
    this.startX = event.pageX - canvasContainer.nativeElement.offsetLeft;
    this.startY = event.pageY - canvasContainer.nativeElement.offsetTop;
    this.scrollLeft = canvasContainer.nativeElement.scrollLeft;
    this.scrollTop = canvasContainer.nativeElement.scrollTop;
  }

  onMouseMove(event: MouseEvent, canvasContainer: ElementRef): void {
    if (!this.isPanning) return;
    event.preventDefault();
    const x = event.pageX - canvasContainer.nativeElement.offsetLeft;
    const y = event.pageY - canvasContainer.nativeElement.offsetTop;
    const walkX = x - this.startX;
    const walkY = y - this.startY;
    canvasContainer.nativeElement.scrollLeft = this.scrollLeft - walkX;
    canvasContainer.nativeElement.scrollTop = this.scrollTop - walkY;
    if (typeof document !== 'undefined') {
      document.body.style.userSelect = 'none';
    }
  }

  onMouseUp(): void {
    this.isPanning = false;
    if (typeof document !== 'undefined') {
      document.body.style.userSelect = '';
    }
  }


}
