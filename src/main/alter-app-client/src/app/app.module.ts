import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClientModule} from "@angular/common/http";
import {NgSelectModule} from '@ng-select/ng-select';
import {AppComponent} from './app.component';
import {PageLoginComponent} from './components/page-login/page-login.component';
import {AuthService} from "./services/auth.service";
import {UserService} from "./services/user.service";
import {PageHomeComponent} from './components/page-home/page-home.component';
import {PageNotFoundComponent} from './components/page-not-found/page-not-found.component';
import {AppRoutingModule} from "./app-routing.module";
import {FormsModule} from "@angular/forms";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import { NavbarComponent } from './components/navbar/navbar.component';
import {CourService} from "./services/cour.service";
import {PromotionService} from "./services/promotion.service";
import {ModelsService} from "./services/models.service";
import {PageSearchComponent} from './components/page-search/page-search.component';
import {PageHistoryComponent} from './components/page-history/page-history.component';
import {PageParametersComponent} from './components/page-parameters/page-parameters.component';
import {PageCalendarModelsComponent} from './components/page-calendar-models/page-calendar-models.component';
import {PageModulesRequirementComponent} from './components/page-modules-requirement/page-modules-requirement.component';
import {PageIndependantModulesComponent} from './components/page-independant-modules/page-independant-modules.component';
import {ModuleService } from './services/module.service';
import {TitreService } from './services/titre.service';
import {ParameterService} from "./services/parameter.service";
import {FormationService} from "./services/formation.service";
import {LieuService} from "./services/lieu.service";
import {StagiaireService} from "./services/stagiaire.service";
import {EntrepriseService} from "./services/entreprise.service";
import { AddElementComponent } from './components/modal/add-element/add-element.component';
import { DispenseElementComponent } from './components/modal/dispense-element/dispense-element.component';

@NgModule({
  declarations: [
    AppComponent,
    PageLoginComponent,
    PageHomeComponent,
    PageNotFoundComponent,
    NavbarComponent,
    PageSearchComponent,
    PageHistoryComponent,
    PageParametersComponent,
    PageCalendarModelsComponent,
    PageModulesRequirementComponent,
    PageIndependantModulesComponent,
    AddElementComponent,
    DispenseElementComponent,
  ],
  imports: [
    NgSelectModule,
    NgbModule.forRoot(),
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [
    AuthService,
    UserService,
    CourService,
    PromotionService,
    ModelsService,
    ModuleService,
    TitreService,
    ParameterService,
    FormationService,
    LieuService,
    StagiaireService,
    EntrepriseService
  ],
  entryComponents: [
    AddElementComponent,
    DispenseElementComponent,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
