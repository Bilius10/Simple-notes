import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PendingFriendRequestsComponent } from './pending-friend-requests-component';

describe('PendingFriendRequestsComponent', () => {
  let component: PendingFriendRequestsComponent;
  let fixture: ComponentFixture<PendingFriendRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PendingFriendRequestsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PendingFriendRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
