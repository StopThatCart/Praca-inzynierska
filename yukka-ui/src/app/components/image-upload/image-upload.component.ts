import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, Output, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-image-upload',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './image-upload.component.html',
  styleUrl: './image-upload.component.css'
})
export class ImageUploadComponent {
  @Output() fileSelected = new EventEmitter<File>();
  @Output() clearImageEvent = new EventEmitter<void>();

  wybranyObraz: any;
  wybranyPlik: any;

  @ViewChild('fileInput') fileInput!: ElementRef;

  onFileSelected(event: any) {
    this.wybranyPlik = event.target.files[0];
    if (this.wybranyPlik) {
      const reader = new FileReader();
      reader.onload = () => {
        this.wybranyObraz = reader.result as string;
        this.fileSelected.emit(this.wybranyPlik);
      };
      reader.readAsDataURL(this.wybranyPlik);
    }
  }

  clearImage() {
    this.wybranyObraz = null;
    this.wybranyPlik = null;
    this.fileInput.nativeElement.value = '';
    this.clearImageEvent.emit();
  }
}
