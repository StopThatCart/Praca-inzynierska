import { Component, Inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from './pages/footer/footer.component';
import { NavbarComponent } from './pages/navbar/navbar.component';
import { SpinnerComponent } from "./services/LoaderSpinner/spinner/spinner.component";
import { DOCUMENT } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, FooterComponent, SpinnerComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {

  constructor(@Inject(DOCUMENT) private document: Document) {}

  ngOnInit(): void {
    this.document.documentElement.lang = 'pl';
  }

  title = 'yukka-ui';
}
