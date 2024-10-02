import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RouterModule, Routes } from '@angular/router';

import { authGuard } from '../../services/guard/auth/auth.guard';
import { pracownikGuard } from '../../services/guard/pracownik/pracownik.guard';
import { OgrodResolverService } from './services/ogrod-resolver.service';
import { DzialkiListComponent } from './pages/dzialki-list/dzialki-list.component';

const routes: Routes = [
  {
    path: '',
    data: { breadcrumb: 'Ogród' },
   // component: RoslinaComponent,
    children: [
      {
        path: ':uzytkownik-nazwa',
        component: DzialkiListComponent,
        pathMatch: 'full',
        data: { breadcrumb: (data: any) =>`${data.ogrod.nazwa}` },
        resolve: { ogrod: OgrodResolverService }
      }//,
      /*
      {
        path: 'dodaj',
        component: AddRoslinaPageComponent,
        canActivate: [pracownikGuard],
        data: { breadcrumb: 'Dodawanie rośliny' }
      },*/
      /*
      {
        path: ':uzytkownikNazwa',
       // data: { breadcrumb: (data: any) =>`${data.dzialki.}` },
        //resolve: { dzialki: OgrodResolverService },
        children:[
          {
            path: '',
            component: RoslinaPageComponent
          },
          {
            path: ':nazwa-lacinska/aktualizuj',
            component: UpdateRoslinaPageComponent,
            data: { breadcrumb: 'Aktualizuj' },
            canActivate: [pracownikGuard]
          },
          {
            path: ':nazwa-lacinska/obraz',
            component: UploadRoslinaObrazPageComponent,
            data: { breadcrumb: 'Zmień obraz' },
            canActivate: [pracownikGuard]
          }
        ]
      },*/

    ]
  }
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OgrodRoutingModule { }
