import { CanActivateFn, Router } from '@angular/router';

import { inject } from '@angular/core';
import { TokenService } from '../../token/token.service';

export const pracownikGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  if (!tokenService.isTokenValid()) {
    router.navigate(['/login']);
    return false;
  }

  const userRoles = tokenService.userRoles;
  if (userRoles.includes('Admin') || userRoles.includes('Pracownik')) {
    return true;
  }

  router.navigate(['']);
  return false;
};
