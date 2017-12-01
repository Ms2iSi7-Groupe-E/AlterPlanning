import { TestBed, inject } from '@angular/core/testing';

import { GuestGuard } from './guest.guard';

describe('GuestGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GuestGuard]
    });
  });

  it('should be created', inject([GuestGuard], (service: GuestGuard) => {
    expect(service).toBeTruthy();
  }));
});
