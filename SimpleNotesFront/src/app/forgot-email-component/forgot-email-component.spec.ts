import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForgotEmailComponent } from './forgot-email-component';

describe('ForgotEmailComponent', () => {
  let component: ForgotEmailComponent;
  let fixture: ComponentFixture<ForgotEmailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ForgotEmailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ForgotEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
