import { Component } from '@angular/core';
import { PowiadomienieCardComponent } from "../powiadomienie-card/powiadomienie-card.component";

@Component({
  selector: 'app-powiadomienia-dropdown',
  standalone: true,
  imports: [PowiadomienieCardComponent],
  templateUrl: './powiadomienia-dropdown.component.html',
  styleUrl: './powiadomienia-dropdown.component.css'
})
export class PowiadomieniaDropdownComponent {

}
