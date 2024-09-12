import { TestBed } from '@angular/core/testing';

import { ProfilResolverService } from './profil-resolver-service.service';

describe('ProfilResolverService', () => {
  let service: ProfilResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProfilResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
