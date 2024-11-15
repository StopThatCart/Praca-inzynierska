import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RouterModule, Routes } from '@angular/router';

import { authGuard } from '../../services/guard/auth/auth.guard';
import { pracownikGuard } from '../../services/guard/pracownik/pracownik.guard';
import { OgrodResolverService } from './services/ogrod-resolver.service';
import { DzialkiListComponent } from './pages/dzialki-list/dzialki-list.component';
import { DzialkaPageComponent } from './pages/dzialka-page/dzialka-page.component';
import { DzialkaResolverService } from './services/dzialka-resolver/dzialka-resolver.service';
import { AddRoslinaToDzialkaComponent } from './pages/add-roslina-to-dzialka/add-roslina-to-dzialka.component';

const routes: Routes = [
  {
    path: '',
    data: { breadcrumb: 'Ogród' },
   // component: RoslinaComponent,
    children: [
      {
        path: ':uzytkownik-nazwa',
        canActivate: [authGuard],
        data: { breadcrumb: (data: any) =>`${data.ogrod.nazwa}` },
        resolve: { ogrod: OgrodResolverService },
        children: [
          {
            path: '',
            component: DzialkiListComponent,
          },
          {
            path: 'dzialka',
           // canActivate: [pracownikGuard],
            data: { breadcrumb: (data: any) =>`Działka` },
            children: [
              {
                path: 'dodawanie/:id',
                component: AddRoslinaToDzialkaComponent,
                data: { breadcrumb: 'Dodawanie rośliny' }
              },
              {
                path: ':numer',
                component: DzialkaPageComponent,
              // canActivate: [pracownikGuard],
                data: { breadcrumb: (data: any) =>`nr. ${data.dzialka.numer}` },
                resolve: { dzialka: DzialkaResolverService },
              }
            ]
          }
        ]
      }//,
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
