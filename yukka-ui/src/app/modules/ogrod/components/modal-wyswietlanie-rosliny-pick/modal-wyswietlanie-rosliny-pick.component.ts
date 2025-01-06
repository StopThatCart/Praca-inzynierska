import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { WyswietlanieRoslinyOpcjeComponent } from "../wyswietlanie-rosliny-opcje/wyswietlanie-rosliny-opcje.component";
import { Modal } from 'bootstrap';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { WyswietlanieRosliny } from '../../../social/models/WyswietlanieRosliny';
import { ZasadzonaRoslinaResponse } from '../../../../services/models';
import { ErrorMsgComponent } from "../../../../components/error-msg/error-msg.component";

@Component({
  selector: 'app-modal-wyswietlanie-rosliny-pick',
  standalone: true,
  imports: [WyswietlanieRoslinyOpcjeComponent, FormsModule, CommonModule, ErrorMsgComponent],
  templateUrl: './modal-wyswietlanie-rosliny-pick.component.html',
  styleUrl: './modal-wyswietlanie-rosliny-pick.component.css'
})
export class ModalWyswietlanieRoslinyPickComponent {
  @Input() selectedRoslina: ZasadzonaRoslinaResponse | undefined;

  selectedWyswietlanie: string = '';
  errorMsg: Array<string> = [];

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
    if(this.selectedRoslina) {
      if(this.selectedWyswietlanie === '') {
        this.closePickerModal();
        return;
      }
      console.log(this.selectedWyswietlanie);
      this.selectedRoslina.wyswietlanie = this.selectedWyswietlanie;
      this.obrazChange.emit(this.selectedWyswietlanie);
      this.closePickerModal();
    }
  }

  onWyswietlanieChange($event: String) {
    this.selectedWyswietlanie = $event.toString();
    console.log('Wyswietlanie:', this.selectedWyswietlanie);
  }



}
