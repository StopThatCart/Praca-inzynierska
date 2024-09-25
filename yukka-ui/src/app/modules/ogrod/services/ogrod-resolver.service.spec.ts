import { TestBed } from '@angular/core/testing';

import { OgrodResolverService } from './ogrod-resolver.service';

describe('OgrodResolverService', () => {
  let service: OgrodResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OgrodResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
