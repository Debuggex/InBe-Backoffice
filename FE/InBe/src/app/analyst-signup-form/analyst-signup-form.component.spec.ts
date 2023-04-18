import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalystSignupFormComponent } from './analyst-signup-form.component';

describe('AnalystSignupFormComponent', () => {
  let component: AnalystSignupFormComponent;
  let fixture: ComponentFixture<AnalystSignupFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AnalystSignupFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AnalystSignupFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
