import { TestBed } from '@angular/core/testing';

import { DzialkaResolverService } from './dzialka-resolver.service';

describe('DzialkaResolverService', () => {
  let service: DzialkaResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DzialkaResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
