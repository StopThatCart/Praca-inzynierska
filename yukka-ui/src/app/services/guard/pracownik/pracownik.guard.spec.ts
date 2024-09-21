import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { pracownikGuard } from './pracownik.guard';

describe('pracownikGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => pracownikGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
