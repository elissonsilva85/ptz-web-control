import { TestBed } from '@angular/core/testing';

import { StepByStepService } from './step-by-step.service';

describe('StepByStepService', () => {
  let service: StepByStepService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StepByStepService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
