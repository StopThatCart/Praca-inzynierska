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
import { EdycjaProfilPageComponent } from './pages/edycja-profil-page/edycja-profil-page.component';
import { EdycjaAvatarPageComponent } from './pages/edycja-avatar-page/edycja-avatar-page.component';
import { ProfilPostyPageComponent } from './pages/profil-posty-page/profil-posty-page.component';
import { ProfilKomentarzePageComponent } from './pages/profil-komentarze-page/profil-komentarze-page.component';
import { EdycjaEmailPageComponent } from './pages/edycja-email-page/edycja-email-page.component';
import { profilGuard } from '../../services/guard/profil-guard/profil.guard';
import { EdycjaUsunKontoPageComponent } from './pages/edycja-usun-konto-page/edycja-usun-konto-page.component';
import { AddUzytkownikComponent } from './pages/add-uzytkownik/add-uzytkownik.component';
import { adminAuthGuard } from '../../services/guard/adminAuth/admin-auth.guard';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/404',
    pathMatch: 'full'
  },
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
            path: 'posty',
            component: ProfilPostyPageComponent,
            canActivate: [profilGuard],
            data: { breadcrumb: 'Posty' }
          },
          {
            path: 'komentarze',
            component: ProfilKomentarzePageComponent,
            canActivate: [profilGuard],
            data: { breadcrumb: 'Komentarze' }
          },
          {
            path: 'edycja',
            //component: UstawieniaPageComponent,
            canActivate: [authGuard],
            data: { breadcrumb: 'Edycja' },
            children: [
              {
                path: '',
                component: EdycjaProfilPageComponent,
              },
              {
                path: 'profil',
                component: EdycjaProfilPageComponent,
                data: { breadcrumb: `Edycja profilu` }
              },
              {
                path: 'avatar',
                component: EdycjaAvatarPageComponent,
                data: { breadcrumb: `Edycja avataru` }
              },
              {
                path: 'email',
                component: EdycjaEmailPageComponent,
                data: { breadcrumb: `Zmiana adresu email` }
              },
              {
                path: 'usun-konto',
                component: EdycjaUsunKontoPageComponent,
                data: { breadcrumb: `Usuwanie konta` }
              }
            ]
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
      },

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
