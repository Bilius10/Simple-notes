import { TestBed } from '@angular/core/testing';

import { WalletUserService } from './wallet-user-service';

describe('WalletUserService', () => {
  let service: WalletUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WalletUserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
