import { Component } from '@angular/core';
import { EdycjaNavComponent } from "../../components/edycja-nav/edycja-nav.component";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from '../../../../services/token/token.service';

@Component({
  selector: 'app-edycja-profil-page',
  standalone: true,
  imports: [CommonModule, FormsModule, EdycjaNavComponent],
  templateUrl: './edycja-profil-page.component.html',
  styleUrl: './edycja-profil-page.component.css'
})
export class EdycjaProfilPageComponent {


  constructor(
    private tokenService: TokenService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  editProfil() {

  }
}
