import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { CommonModule } from '@angular/common';
import { ProfilPageComponent } from './pages/profil-page/profil-page.component';
import { ProfilResolverService } from './services/profill-resolver-service/profil-resolver-service.service';
import { PowiadomieniaPageComponent } from './pages/powiadomienia-page/powiadomienia-page.component';
import { authGuard } from '../../services/guard/auth.guard';
import { RozmowyListComponent } from './pages/rozmowy-list/rozmowy-list.component';
import { RozmowaPageComponent } from './pages/rozmowa-page/rozmowa-page.component';
import { RozmowaResolverService } from './services/rozmowa-resolver-service/rozmowa-resolver.service';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/',
    pathMatch: 'full'
  },
  {
    path: '',
    data: { breadcrumb: 'Profil' },
   // component: RoslinaComponent,
    children: [
      {
        path: 'powiadomienia',
        component: PowiadomieniaPageComponent,
        canActivate: [authGuard],
        data: { breadcrumb: 'Powiadomienia' }
      },
      {
        path: 'rozmowy',
        component: RozmowyListComponent,
        canActivate: [authGuard],
        data: { breadcrumb: 'Rozmowy' }
      },
      {
        path: 'rozmowy/:uzytkownikNazwa',
        component: RozmowaPageComponent,
        canActivate: [authGuard],
        data: { breadcrumb: (data: any) => `Rozmowa z ${data.uzytkownik.nazwa}` },
        resolve: { rozmowa: RozmowaResolverService }
      },
      {
        path: ':nazwa',
        component: ProfilPageComponent,
        pathMatch: 'full',
        data: { breadcrumb: (data: any) =>`${data.uzytkownik.nazwa}` },
        resolve: { profil: ProfilResolverService }
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
