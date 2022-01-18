import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { YoutubeDialog } from './youtube.dialog';

describe('YoutubeComponent', () => {
  let component: YoutubeDialog;
  let fixture: ComponentFixture<YoutubeDialog>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ YoutubeDialog ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(YoutubeDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
