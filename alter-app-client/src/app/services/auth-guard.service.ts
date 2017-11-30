import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {AuthService} from "./auth.service";

@Injectable()
export class AuthGuardService implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(next:  ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (!AuthService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return false;
    }

    return true;
  }
}
