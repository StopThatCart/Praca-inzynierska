import { TestBed } from '@angular/core/testing';

import { PowiadomieniaSyncService } from './powiadomienia-sync.service';

describe('PowiadomieniaSyncService', () => {
  let service: PowiadomieniaSyncService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PowiadomieniaSyncService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
