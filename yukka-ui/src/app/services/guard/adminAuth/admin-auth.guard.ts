import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { TokenService } from '../../token/token.service';

export const adminAuthGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  if (!tokenService.isTokenValid()) {
    router.navigate(['/login']);
    return false;
  }

  const userRoles = tokenService.userRoles;
  if (userRoles.includes('Admin')) {
    return true;
  }

  router.navigate(['']);
  return false;
};
