import { Component, Inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from './components/footer/footer.component';

import { SpinnerComponent } from "./services/LoaderSpinner/spinner/spinner.component";
import { DOCUMENT } from '@angular/common';
import { NavbarComponent } from './components/navbar/navbar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, FooterComponent, SpinnerComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  // constructor(@Inject(DOCUMENT) private document: Document) {}

  // ngOnInit(): void {
  //   this.document.documentElement.lang = 'pl';
  // }

  title = 'yukka-ui';
}
