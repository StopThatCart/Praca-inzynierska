import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoslinaListComponent } from './pages/roslina-list/roslina-list.component';
import { CommonModule } from '@angular/common';
import { RoslinaPageComponent } from './pages/roslina-page/roslina-page.component';
import { RoslinaResolverService } from './services/roslina-resolver.service';
import { AddRoslinaPageComponent } from './pages/add-roslina-page/add-roslina-page.component';
import { authGuard } from '../../services/guard/auth/auth.guard';
import { pracownikGuard } from '../../services/guard/pracownik/pracownik.guard';
import { UpdateRoslinaPageComponent } from './pages/update-roslina-page/update-roslina-page.component';
import { UploadRoslinaObrazPageComponent } from './pages/upload-roslina-obraz-page/upload-roslina-obraz-page.component';

const routes: Routes = [
  {
    path: '',
    data: { breadcrumb: 'Katalog roślin' },
   // component: RoslinaComponent,
    children: [
      {
        path: '',
        component: RoslinaListComponent
      },
      {
        path: 'dodaj',
        component: AddRoslinaPageComponent,
        canActivate: [pracownikGuard],
        data: { breadcrumb: 'Dodawanie rośliny' }
      },
      {
        path: ':nazwa-lacinska',
        data: { breadcrumb: (data: any) =>`${data.roslina.nazwaLacinska}` },
        resolve: { roslina: RoslinaResolverService },
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
      },

    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RoslinaRoutingModule { }
