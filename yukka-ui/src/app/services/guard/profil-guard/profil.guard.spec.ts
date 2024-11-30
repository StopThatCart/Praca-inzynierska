import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { profilGuard } from './profil.guard';

describe('profilGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) =>
      TestBed.runInInjectionContext(() => profilGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
