import { TestBed } from '@angular/core/testing';

import { CryptKeyService } from './crypt-key.service';

describe('CryptKeyService', () => {
  let service: CryptKeyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CryptKeyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
