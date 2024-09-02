import { TestBed } from '@angular/core/testing';

import { RoslinaResolverService } from './roslina-resolver.service';

describe('RoslinaResolverService', () => {
  let service: RoslinaResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoslinaResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
