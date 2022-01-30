import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StepByStepActionComponent } from './step-by-step-action.component';

describe('StepByStepActionComponent', () => {
  let component: StepByStepActionComponent;
  let fixture: ComponentFixture<StepByStepActionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StepByStepActionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StepByStepActionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
