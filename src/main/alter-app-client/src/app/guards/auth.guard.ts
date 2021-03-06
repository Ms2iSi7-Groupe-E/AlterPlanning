import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {AuthService} from "../services/auth.service";

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(next:  ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (!AuthService.isAuthenticated()) {
      this.router.navigate(['/login'], {
        queryParams: {info: "Vous devez être authentifié pour accéder à cette page"}
      });
      return false;
    }

    return true;
  }
}
