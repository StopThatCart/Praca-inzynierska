import { TestBed } from '@angular/core/testing';

import { PostResolverServiceService } from './post-resolver-service.service';

describe('PostResolverServiceService', () => {
  let service: PostResolverServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostResolverServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
