import { CanActivateFn, Router } from '@angular/router';
import { TokenService } from '../../token/token.service';
import { inject } from '@angular/core';

export const profilGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  if (!tokenService.isTokenValid()) {
    router.navigate(['/login']);
    return false;
  }

  const tokenNazwa = tokenService.nazwa;
  const paramNazwa = route.paramMap.get('nazwa');

  if (tokenNazwa === paramNazwa) {
    return true;
  }

  const isAdminOrPracownik = tokenService.isAdmin() || tokenService.isPracownik();
  //const isTargetPracownik = route.snapshot.data['profil']?.labels?.includes('Pracownik');

  if (isAdminOrPracownik) {
    return true;
  }

  router.navigate(['/']);
  return false;
};
