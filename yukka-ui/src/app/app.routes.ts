import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { IndeksusComponent } from './pages/indeksus/indeksus.component';
import { RegisterComponent } from './pages/register/register.component';

export const routes: Routes = [
  { path: '', component: IndeksusComponent },
  {
    path: 'login',
    component: LoginComponent,
    data: { breadcrumb: 'Login' }
  },
  {
    path: 'register',
    component: RegisterComponent,
    data: { breadcrumb: 'Rejestracja' }
  },
  {
    path: 'rosliny',
    loadChildren: () => import('./modules/roslina/roslina.module').then(m => m.RoslinaModule)
  }
];
