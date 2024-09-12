import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { CommonModule } from '@angular/common';
import { ProfilPageComponent } from './pages/profil-page/profil-page.component';
import { ProfilResolverService } from './services/profil-resolver-service.service';

const routes: Routes = [
  {
    path: '',
    data: { breadcrumb: 'Profil' },
   // component: RoslinaComponent,
    children: [
      {
        path: ':nazwa',
        component: ProfilPageComponent,
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
