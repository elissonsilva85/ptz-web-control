import { TestBed } from '@angular/core/testing';

import { RcpService } from './rcp.service';

describe('RcpService', () => {
  let service: RcpService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RcpService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
