import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { CommonModule } from '@angular/common';
import { ProfilPageComponent } from './pages/profil-page/profil-page.component';
import { ProfilResolverService } from './services/profil-resolver-service.service';
import { PowiadomieniaPageComponent } from './pages/powiadomienia-page/powiadomienia-page.component';
import { authGuard } from '../../services/guard/auth.guard';

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
        path: ':nazwa',
        component: ProfilPageComponent,
        pathMatch: 'full',
        data: { breadcrumb: (data: any) =>`${data.uzytkownik.nazwa}` },
        resolve: { post: ProfilResolverService }
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
