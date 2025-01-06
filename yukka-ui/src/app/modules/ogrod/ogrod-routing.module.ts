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
import { RoslinyUzytkownikaPageComponent } from './pages/rosliny-uzytkownika-page/rosliny-uzytkownika-page.component';
import { NotFoundComponent } from '../../components/not-found/not-found.component';
import { MoveRoslinaToOtherDzialkaComponent } from './pages/move-roslina-to-other-dzialka/move-roslina-to-other-dzialka.component';
import { RoslinaResolverService } from '../roslina/services/roslina-resolver.service';

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
                path: 'dodawanie/:roslina-id',
                component: AddRoslinaToDzialkaComponent,
                data: { breadcrumb: (data: any) => `Dodawanie rośliny ${data.roslina.nazwa}` },
                resolve: { roslina: RoslinaResolverService }
              },
              {
                path: ':numer',
                data: { breadcrumb: (data: any) =>`nr. ${data.dzialka.numer}` },
                resolve: { dzialka: DzialkaResolverService },
                children: [
                  {
                    path: '',
                    component: DzialkaPageComponent,
                  },
                  {
                    path: 'przenoszenie/:roslina-id',
                    component: MoveRoslinaToOtherDzialkaComponent,
                    data: { breadcrumb: (data: any) => `Przenoszenie rośliny ${data.roslina.nazwa}` },
                    resolve: { roslina: RoslinaResolverService }
                  },
                ]
              // canActivate: [pracownikGuard],

              }
            ]
          },
          {
            path: 'rosliny',
            component: RoslinyUzytkownikaPageComponent,
            data: { breadcrumb: 'Rośliny' },
          },
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
