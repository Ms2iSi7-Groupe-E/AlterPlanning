import { TestBed, inject } from '@angular/core/testing';

import { GuestGuardService } from './guest-guard.service';

describe('GuestGuardService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GuestGuardService]
    });
  });

  it('should be created', inject([GuestGuardService], (service: GuestGuardService) => {
    expect(service).toBeTruthy();
  }));
});
