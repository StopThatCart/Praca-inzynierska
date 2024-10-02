import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { CommonModule } from '@angular/common';
import { ProfilPageComponent } from './pages/profil-page/profil-page.component';
import { ProfilResolverService } from './services/profill-resolver-service/profil-resolver-service.service';
import { PowiadomieniaPageComponent } from './pages/powiadomienia-page/powiadomienia-page.component';
import { authGuard } from '../../services/guard/auth/auth.guard';
import { RozmowyListComponent } from './pages/rozmowy-list/rozmowy-list.component';
import { RozmowaPageComponent } from './pages/rozmowa-page/rozmowa-page.component';
import { RozmowaResolverService } from './services/rozmowa-resolver-service/rozmowa-resolver.service';
import { UstawieniaPageComponent } from './pages/ustawienia-page/ustawienia-page.component';

const routes: Routes = [
  {
    path: '',
    data: { breadcrumb: 'Profil' },
   // component: RoslinaComponent,
    children: [
      {
        path: ':nazwa',
        //component: ProfilPageComponent,
        data: { breadcrumb: (data: any) =>`${data.uzytkownik.nazwa}` },
        resolve: { profil: ProfilResolverService },
        children:[
          {
            path: '',
            component: ProfilPageComponent,
          },
          {
            path: 'powiadomienia',
            component: PowiadomieniaPageComponent,
            canActivate: [authGuard],
            data: { breadcrumb: 'Powiadomienia' }
          },
          {
            path: 'ustawienia',
            component: UstawieniaPageComponent,
            canActivate: [authGuard],
            data: { breadcrumb: 'Ustawienia' }
          },
          {
            path: 'rozmowy',
            //component: RozmowyListComponent,
            canActivate: [authGuard],
            data: { breadcrumb: 'Rozmowy' },
            children: [
              {
                path: '',
                component: RozmowyListComponent,
              },
              {
                path: ':uzytkownik-nazwa',
                component: RozmowaPageComponent,
                data: { breadcrumb: (data: any) => `Rozmowa z ${data.uzytkownik.nazwa}` },
                resolve: { rozmowa: RozmowaResolverService }
              }
            ]
          }

        ]
      }

      /*,
      {
        path: ':nazwaUzytkownika/ustawienia',
        component: PostyPageComponent,
        data: { breadcrumb: (data: any) =>`${data.post.tytul}` },
        resolve: { post: PostResolverService }
      }*/
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProfilRoutingModule { }
