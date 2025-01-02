import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { CommonModule } from '@angular/common';
import { PostListComponent } from './pages/post-list/post-list.component';
import { PostyPageComponent } from './pages/posty-page/posty-page.component';
import { PostResolverService } from './services/post-resolver-service.service';
import { UzytkownikListComponent } from './pages/uzytkownik-list/uzytkownik-list.component';
import { AddUzytkownikComponent } from '../profil/pages/add-uzytkownik/add-uzytkownik.component';
import { adminAuthGuard } from '../../services/guard/adminAuth/admin-auth.guard';


const routes: Routes = [
  {
    path: '',
    redirectTo: 'posty',
    pathMatch: 'full'
  },
  {
    path: '',
    data: { breadcrumb: 'Społeczność' },
   // component: RoslinaComponent,
    children: [
      {
        path: 'uzytkownicy',
        data: { breadcrumb: 'Użytkownicy' },
        children: [
          {
            path: '',
            component: UzytkownikListComponent,
          },
          {
            path: 'dodaj',
            component: AddUzytkownikComponent,
            canActivate: [adminAuthGuard],
            data: { breadcrumb: 'Dodawanie użytkownika' }
          },
        ]
      },
      {
        path: 'posty',
        children: [
          {
            path: '',
            component: PostListComponent,
            data: { breadcrumb: 'Posty' }
          },
          {
            path: ':postId',
            component: PostyPageComponent,
            data: { breadcrumb: (data: any) =>`${data.post.tytul}` },
            resolve: { post: PostResolverService }
          }
        ]
      },
      {
        path: ':postId',
        component: PostyPageComponent,
        data: { breadcrumb: (data: any) =>`${data.post.tytul}` },
        resolve: { post: PostResolverService }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PostRoutingModule { }
