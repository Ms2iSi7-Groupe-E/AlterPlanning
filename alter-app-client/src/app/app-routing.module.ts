import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {PageNotFoundComponent} from "./components/page-not-found/page-not-found.component";
import {HomeComponent} from "./components/home/home.component";
import {AuthGuard} from "./guards/auth.guard";
import {GuestGuard} from "./guards/guest.guard";

// Define the routes
const ROUTES: Routes = [
  {
    path: '',
    canActivate: [AuthGuard],
    component: HomeComponent
  },
  {
    path: 'login',
    canActivate: [GuestGuard],
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
    AuthGuard,
    GuestGuard
  ]
})
export class AppRoutingModule {}
