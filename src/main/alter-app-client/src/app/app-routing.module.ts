import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PageLoginComponent} from "./components/page-login/page-login.component";
import {PageNotFoundComponent} from "./components/page-not-found/page-not-found.component";
import {PageCalendarProposalComponent} from "./components/page-calendar-proposal/page-calendar-proposal.component";
import {AuthGuard} from "./guards/auth.guard";
import {GuestGuard} from "./guards/guest.guard";
import { PageSearchComponent } from './components/page-search/page-search.component';
import { PageHistoryComponent } from './components/page-history/page-history.component';
import { PageParametersComponent } from './components/page-parameters/page-parameters.component';
import { PageCalendarModelsComponent } from './components/page-calendar-models/page-calendar-models.component';
import { PageIndependantModulesComponent } from './components/page-independant-modules/page-independant-modules.component';
import { PageModulesRequirementComponent } from './components/page-modules-requirement/page-modules-requirement.component';
import {PageCalendarProcessingComponent} from "./components/page-calendar-processing/page-calendar-processing.component";
import {PageCalendarDetailsComponent} from "./components/page-calendar-details/page-calendar-details.component";

// Define the routes
const ROUTES: Routes = [
  {
    path: '',
    redirectTo: '/propose-calendar',
    pathMatch: 'full'
  },
  {
    path: 'propose-calendar',
    canActivate: [AuthGuard],
    component: PageCalendarProposalComponent
  },
  {
    path: 'login',
    canActivate: [GuestGuard],
    component: PageLoginComponent
  },
  {
    path: 'search',
    canActivate: [AuthGuard],
    component: PageSearchComponent
  },
  {
    path: 'calendar/:id/processing',
    canActivate: [AuthGuard],
    component: PageCalendarProcessingComponent
  },
  {
    path: 'calendar/:id/details',
    canActivate: [AuthGuard],
    component: PageCalendarDetailsComponent
  },
  {
    path: 'calendar-models',
    canActivate: [AuthGuard],
    component: PageCalendarModelsComponent
  },
  {
    path: 'independant-modules',
    canActivate: [AuthGuard],
    component: PageIndependantModulesComponent
  },
  {
    path: 'modules-requirement',
    canActivate: [AuthGuard],
    component: PageModulesRequirementComponent
  },
  {
    path: 'history',
    canActivate: [AuthGuard],
    component: PageHistoryComponent
  },
  {
    path: 'parameters',
    canActivate: [AuthGuard],
    component: PageParametersComponent
  },
  {
    path: '**',
    redirectTo: '/404',
    pathMatch: 'full'
  },
  {
    path: '404',
    component: PageNotFoundComponent
  },
];

@NgModule({
  imports: [ RouterModule.forRoot(ROUTES, { useHash: true }) ],
  exports: [ RouterModule ],
  providers: [
    AuthGuard,
    GuestGuard
  ]
})
export class AppRoutingModule {}
