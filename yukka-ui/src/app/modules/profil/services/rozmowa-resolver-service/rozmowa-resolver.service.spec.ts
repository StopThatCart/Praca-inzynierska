import { TestBed } from '@angular/core/testing';

import { RozmowaResolverService } from './rozmowa-resolver.service';

describe('RozmowaResolverService', () => {
  let service: RozmowaResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RozmowaResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
