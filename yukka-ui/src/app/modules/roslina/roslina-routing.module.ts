import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoslinaListComponent } from './pages/roslina-list/roslina-list.component';
import { CommonModule } from '@angular/common';
import { RoslinaPageComponent } from './roslina-page/roslina-page.component';
import { RoslinaResolverService } from './services/roslina-resolver.service';

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
        path: ':nazwaLacinska',
        component: RoslinaPageComponent,
        data: { breadcrumb: (data: any) =>`${data.roslina.nazwaLacinska}` },
        resolve: { roslina: RoslinaResolverService }
      },
      {
        path: 'page/:page',
        component: RoslinaListComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RoslinaRoutingModule { }
