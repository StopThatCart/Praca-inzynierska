import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoslinaListComponent } from './pages/roslina-list/roslina-list.component';
import { CommonModule } from '@angular/common';

const routes: Routes = [
  {
    path: '',
   // component: RoslinaComponent,
    children: [
      {
        path: '',
        component: RoslinaListComponent
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
