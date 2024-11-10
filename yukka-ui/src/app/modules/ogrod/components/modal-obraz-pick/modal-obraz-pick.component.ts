import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { Modal } from 'bootstrap';
import { ImageUploadComponent } from "../../../../components/image-upload/image-upload.component";

@Component({
  selector: 'app-modal-obraz-pick',
  standalone: true,
  imports: [ImageUploadComponent],
  templateUrl: './modal-obraz-pick.component.html',
  styleUrl: './modal-obraz-pick.component.css'
})
export class ModalObrazPickComponent {

  @Input() selectedColor: string = '#ffffff';
  @Output() obrazChange = new EventEmitter<any>();

  wybranyPlik: any = null;

  @ViewChild(ImageUploadComponent) imageUpload!: ImageUploadComponent;
  @ViewChild('pickerModal') pickerModal!: ElementRef;

  constructor() { }

  openPickerModal() {
    const modalElement = this.pickerModal.nativeElement;
    const modal = new Modal(modalElement);
    modal.show();
  }

  closePickerModal() {
    const modalElement = this.pickerModal.nativeElement;
    const modal = Modal.getInstance(modalElement);
    modal?.hide();
  }

  confirm() {
    console.log('confirmObrazZmiana');
    this.obrazChange.emit(this.wybranyPlik);
    this.imageUpload.clearImage();
    this.closePickerModal();
  }

  delet() {
    if(confirm('Czy na pewno chcesz usunąć ten plik?')) {
      console.log('deletObraz');
      this.obrazChange.emit(null);

      this.imageUpload.clearImage();
      this.closePickerModal();
    }
  }

  onFileSelected(file: File) {
    this.wybranyPlik = file;
  }

  clearImage() {
    this.wybranyPlik = null;
  }
}
