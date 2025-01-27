import { AfterViewInit, Component, ElementRef, EventEmitter, Input, Output, TemplateRef, ViewChild } from '@angular/core';
import { ColorPickerComponent, ColorPickerModule } from 'ngx-color-picker';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Modal } from 'bootstrap';
import { TokenService } from '../../../../services/token/token.service';
import { DzialkaService } from '../../../../services/services';

@Component({
  selector: 'app-modal-color-pick',
  standalone: true,
  imports: [CommonModule, FormsModule, ColorPickerModule],
  templateUrl: './modal-color-pick.component.html',
  styleUrl: './modal-color-pick.component.css'
})
export class ModalColorPickComponent {

  @Input() selectedColor: string = '#ffffff';
  @Output() confirmColorChange = new EventEmitter<string>();


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
    this.confirmColorChange.emit(this.selectedColor);
    this.closePickerModal();
  }

}
