import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { IndeksusComponent } from './pages/indeksus/indeksus.component';
import { RegisterComponent } from './pages/register/register.component';
import { Post } from './services/models/post';
import { ProfilModule } from './modules/profil/profil.module';
import { loggedInGuard } from './services/guard/loggedIn/logged-in.guard';
import { AktywacjaKontaComponent } from './pages/aktywacja-konta/aktywacja-konta.component';
import { ZmianaHaslaComponent } from './pages/zmiana-hasla/zmiana-hasla.component';
import { ZmianaHaslaEmailComponent } from './pages/zmiana-hasla-email/zmiana-hasla-email.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { ForbiddenComponent } from './components/forbidden/forbidden.component';
import { AktywacjaKontaRefreshComponent } from './pages/aktywacja-konta-refresh/aktywacja-konta-refresh.component';

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
    path: 'aktywacja-konta',
    component: AktywacjaKontaComponent,
    canActivate: [loggedInGuard],
    data: { breadcrumb: 'Aktywacja konta' }
  },
  {
    path: 'aktywacja-konta-refresh',
    component: AktywacjaKontaRefreshComponent,
    canActivate: [loggedInGuard],
    data: { breadcrumb: 'Ponowne wysyłanie emaila' }
  },
  {
    path: 'zmiana-hasla',
    component: ZmianaHaslaComponent,
    canActivate: [loggedInGuard],
    data: { breadcrumb: 'Reset hasła' }
  },
  {
    path: 'zmiana-hasla-email',
    component: ZmianaHaslaEmailComponent,
    canActivate: [loggedInGuard],
    data: { breadcrumb: 'Reset hasła' }
  },
  {
    path: 'rosliny',
    loadChildren: () => import('./modules/roslina/roslina.module').then(m => m.RoslinaModule)
  },
  {
    path: 'social',
    loadChildren: () => import('./modules/social/social.module').then(m => m.SocialModule)
  },
  {
    path: 'profil',
    loadChildren: () => import('./modules/profil/profil.module').then(m => m.ProfilModule)
  },
  {
    path: 'ogrod',
    loadChildren: () => import('./modules/ogrod/ogrod.module').then(m => m.OgrodModule)
  },
  { path: '404', component: NotFoundComponent },
  { path: '403', component: ForbiddenComponent },
  { path: '**', pathMatch: 'full', redirectTo: '404' }


];
