import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Modal } from 'bootstrap';

@Component({
  selector: 'app-modal-notatka-pick',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modal-notatka-pick.component.html',
  styleUrl: './modal-notatka-pick.component.css'
})
export class ModalNotatkaPickComponent {
  @Input() notatka: string | undefined;
  @Output() confirmChange = new EventEmitter<string>();

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
    console.log('confirmChange', this.notatka);
    this.confirmChange.emit(this.notatka);
    this.closePickerModal();
  }

}
