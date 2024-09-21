import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoslinaListComponent } from './pages/roslina-list/roslina-list.component';
import { CommonModule } from '@angular/common';
import { RoslinaPageComponent } from './pages/roslina-page/roslina-page.component';
import { RoslinaResolverService } from './services/roslina-resolver.service';
import { AddRoslinaPageComponent } from './pages/add-roslina-page/add-roslina-page.component';
import { authGuard } from '../../services/guard/auth/auth.guard';
import { pracownikGuard } from '../../services/guard/pracownik/pracownik.guard';

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
        path: ':nazwaLacinska',
        component: RoslinaPageComponent,
        data: { breadcrumb: (data: any) =>`${data.roslina.nazwaLacinska}` },
        resolve: { roslina: RoslinaResolverService }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RoslinaRoutingModule { }
