import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WyswietlanieRosliny } from '../../../post/enums/WyswietlanieRosliny';

@Component({
  selector: 'app-wyswietlanie-rosliny-opcje',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './wyswietlanie-rosliny-opcje.component.html',
  styleUrl: './wyswietlanie-rosliny-opcje.component.css'
})
export class WyswietlanieRoslinyOpcjeComponent {

  @Input() wyswietlanie : string | undefined;

  wyswietlanieOpcje = WyswietlanieRosliny;

}
