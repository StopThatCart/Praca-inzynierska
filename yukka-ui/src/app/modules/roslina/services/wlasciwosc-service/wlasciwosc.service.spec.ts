import { TestBed } from '@angular/core/testing';

import { WlasciwoscProcessService } from './wlasciwosc.service';

describe('WlasciwoscService', () => {
  let service: WlasciwoscProcessService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WlasciwoscProcessService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
