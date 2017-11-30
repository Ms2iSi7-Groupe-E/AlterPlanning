import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {AuthService} from "./auth.service";

@Injectable()
export class GuestGuardService implements CanActivate {

  constructor(private router: Router) {}

  canActivate(next:  ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (AuthService.isAuthenticated()) {
      this.router.navigate(['/']);
      return false;
    }

    return true;
  }
}
