import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {AuthGuardService} from "./services/auth-guard.service";
import {PageNotFoundComponent} from "./components/page-not-found/page-not-found.component";
import {HomeComponent} from "./components/home/home.component";
import {GuestGuardService} from "./services/guest-guard.service";

// Define the routes
const ROUTES: Routes = [
  {
    path: '',
    canActivate: [AuthGuardService],
    component: HomeComponent
  },
  {
    path: 'login',
    canActivate: [GuestGuardService],
    component: LoginComponent
  },
  {
    path: '**',
    component: PageNotFoundComponent
  }
];

@NgModule({
  imports: [ RouterModule.forRoot(ROUTES,{useHash: true}) ],
  exports: [ RouterModule ],
  providers: [
    AuthGuardService,
    GuestGuardService
  ]
})
export class AppRoutingModule {}
