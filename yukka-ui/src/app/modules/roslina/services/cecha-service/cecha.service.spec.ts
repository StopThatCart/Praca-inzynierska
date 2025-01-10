import { TestBed } from '@angular/core/testing';

import { CechaProcessService } from './cecha.service';

describe('CechaService', () => {
  let service: CechaProcessService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CechaProcessService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
