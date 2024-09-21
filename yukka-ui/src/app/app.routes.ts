import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { IndeksusComponent } from './pages/indeksus/indeksus.component';
import { RegisterComponent } from './pages/register/register.component';
import { Post } from './services/models/post';
import { ProfilModule } from './modules/profil/profil.module';
import { loggedInGuard } from './services/guard/loggedIn/logged-in.guard';

export const routes: Routes = [
  { path: '', component: IndeksusComponent },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [loggedInGuard],
    data: { breadcrumb: 'Logowanie' }
  },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [loggedInGuard],
    data: { breadcrumb: 'Rejestracja' }
  },
  {
    path: 'rosliny',
    loadChildren: () => import('./modules/roslina/roslina.module').then(m => m.RoslinaModule)
  },
  {
    path: 'posty',
    loadChildren: () => import('./modules/post/post.module').then(m => m.PostModule)
  },
  {
    path: 'profil',
    loadChildren: () => import('./modules/profil/profil.module').then(m => m.ProfilModule)
  },
  { path: '**', redirectTo: '' }
];
