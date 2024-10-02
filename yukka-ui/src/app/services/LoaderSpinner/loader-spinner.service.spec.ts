import { TestBed } from '@angular/core/testing';

import { LoaderSpinnerService } from './loader-spinner.service';

describe('LoaderSpinnerService', () => {
  let service: LoaderSpinnerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoaderSpinnerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
