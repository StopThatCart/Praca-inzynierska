import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { WyswietlanieRoslinyOpcjeComponent } from "../wyswietlanie-rosliny-opcje/wyswietlanie-rosliny-opcje.component";
import { Modal } from 'bootstrap';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { WyswietlanieRosliny } from '../../../post/enums/WyswietlanieRosliny';

@Component({
  selector: 'app-modal-wyswietlanie-rosliny-pick',
  standalone: true,
  imports: [WyswietlanieRoslinyOpcjeComponent, FormsModule, CommonModule],
  templateUrl: './modal-wyswietlanie-rosliny-pick.component.html',
  styleUrl: './modal-wyswietlanie-rosliny-pick.component.css'
})
export class ModalWyswietlanieRoslinyPickComponent {

  @Input() selectedWyswietlanie: string = '';
  @Output() obrazChange = new EventEmitter<string>();

  @ViewChild('pickerModal') pickerModal!: ElementRef;

  wyswietlanieOpcje = WyswietlanieRosliny;

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
    console.log('confirmWyswietlanieZmiana');
    console.log(this.selectedWyswietlanie);
    this.obrazChange.emit(this.selectedWyswietlanie);
    this.closePickerModal();
  }



}
