import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-error-msg',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './error-msg.component.html',
  styleUrl: './error-msg.component.css'
})
export class ErrorMsgComponent {
  @Input() errorMsg: string[] = [];
  @Input() message = '';
}
