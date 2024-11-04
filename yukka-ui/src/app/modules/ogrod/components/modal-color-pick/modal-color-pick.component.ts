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


  @ViewChild('colorPickerModal') colorPickerModal!: ElementRef;

  constructor(
    private dzialkaService: DzialkaService,
    private tokenService: TokenService
  ) { }

  openColorPickerModal() {
    const modalElement = this.colorPickerModal.nativeElement;
    const modal = new Modal(modalElement);
    modal.show();
  }

  closeColorPickerModal() {
    const modalElement = this.colorPickerModal.nativeElement;
    const modal = Modal.getInstance(modalElement);
    modal?.hide();
  }

  confirm() {
    console.log('confirmColorChange', this.selectedColor);
    this.confirmColorChange.emit(this.selectedColor);
    this.closeColorPickerModal();
  }

  changeRoslinaKafelekKolor(color: string) {
    // TODO
   // this.dzialkaService.
    this.selectedColor = color;
  }

}
