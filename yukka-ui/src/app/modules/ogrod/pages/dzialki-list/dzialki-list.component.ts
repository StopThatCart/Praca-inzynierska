import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { DzialkaService, OgrodService } from '../../../../services/services';
import { TokenService } from '../../../../services/token/token.service';
import { ActivatedRoute, Router } from '@angular/router';
import { OgrodResponse } from '../../../../services/models';

@Component({
  selector: 'app-dzialki-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dzialki-list.component.html',
  styleUrl: './dzialki-list.component.css'
})
export class DzialkiListComponent implements OnInit {
  ogrodResponse : OgrodResponse = {};
  uzytNazwa: string | undefined;


  constructor(private ogrodService : OgrodService,
    private dzialkaService: DzialkaService,
    private router: Router,
    private route: ActivatedRoute,
    private tokenService : TokenService
  ) {}


  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.uzytNazwa = params['uzytkownik-nazwa'];
      if (this.uzytNazwa) {
        this.route.snapshot.data['uzytkownik-nazwa'] = this.uzytNazwa;
      }
    });

    this.getOgrod();

  }




  getOgrod() {
    console.log('getOgrod');

    if(!this.uzytNazwa) {
      return;
    }

    this.ogrodService.getDzialki({ "uzytkownik-nazwa": this.uzytNazwa })
    .subscribe({
        next: (ogrod) => {
          this.ogrodResponse = ogrod;
          console.log(this.ogrodResponse);
        },
        error: (error) => {
          console.error('Error fetching ogrod:', error);
        }
      });
  }

  goToDzialkaPage(numer: number | undefined) {
    if(!numer) return;
    this.router.navigate(['dzialka', numer], { relativeTo: this.route });
  }

}
