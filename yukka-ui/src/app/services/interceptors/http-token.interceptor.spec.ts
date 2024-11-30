import { TestBed } from '@angular/core/testing';
import { httpTokenInterceptor } from './http-token.interceptor';

describe('httpTokenInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      httpTokenInterceptor
      ]
  }));

  it('should be created', () => {
    const interceptor: typeof httpTokenInterceptor = TestBed.inject(httpTokenInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
