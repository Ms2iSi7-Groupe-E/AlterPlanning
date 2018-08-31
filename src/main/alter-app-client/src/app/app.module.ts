import {BrowserModule} from '@angular/platform-browser';
import {LOCALE_ID, NgModule} from '@angular/core';
import {HttpClientModule} from "@angular/common/http";
import {NgSelectModule} from '@ng-select/ng-select';
import {AppComponent} from './app.component';
import {PageLoginComponent} from './components/page-login/page-login.component';
import {AuthService} from "./services/auth.service";
import {UserService} from "./services/user.service";
import {PageCalendarProposalComponent} from './components/page-calendar-proposal/page-calendar-proposal.component';
import {PageNotFoundComponent} from './components/page-not-found/page-not-found.component';
import {AppRoutingModule} from "./app-routing.module";
import {FormsModule} from "@angular/forms";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {NgbDatepickerI18n, NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {NavbarComponent} from './components/navbar/navbar.component';
import {CourService} from "./services/cour.service";
import {PromotionService} from "./services/promotion.service";
import {ModelsService} from "./services/models.service";
import {PageSearchComponent} from './components/page-search/page-search.component';
import {PageHistoryComponent} from './components/page-history/page-history.component';
import {PageParametersComponent} from './components/page-parameters/page-parameters.component';
import {PageCalendarModelsComponent} from './components/page-calendar-models/page-calendar-models.component';
import {PageModulesRequirementComponent} from './components/page-modules-requirement/page-modules-requirement.component';
import {PageIndependantModulesComponent} from './components/page-independant-modules/page-independant-modules.component';
import {ModuleService} from './services/module.service';
import {TitreService} from './services/titre.service';
import {ParameterService} from "./services/parameter.service";
import {FormationService} from "./services/formation.service";
import {LieuService} from "./services/lieu.service";
import {StagiaireService} from "./services/stagiaire.service";
import {EntrepriseService} from "./services/entreprise.service";
import {AddElementComponent} from './components/modal/add-element/add-element.component';
import {DispenseElementComponent} from './components/modal/dispense-element/dispense-element.component';
import {DatepickerI18n} from "./components/custom/datepicker-i18n";
import { PageCalendarProcessingComponent } from './components/page-calendar-processing/page-calendar-processing.component';
import { PageCalendarDetailsComponent } from './components/page-calendar-details/page-calendar-details.component';
import {CalendarService} from "./services/calendar.service";
import {ColorPickerModule} from "ngx-color-picker";
import {DataTablesModule} from "angular-datatables";
import {HistoryService} from "./services/history.service";
import {CalendarModelService} from "./services/calendar-model.service";
import { CalendarModelNameComponent } from './components/modal/calendar-model-name/calendar-model-name.component';
import { HandleIndependantModuleComponent } from './components/modal/handle-independant-module/handle-independant-module.component';
import {IndependantModuleService} from "./services/independant-module.service";
import { ConfirmComponent } from './components/modal/confirm/confirm.component';

@NgModule({
  declarations: [
    AppComponent,
    PageLoginComponent,
    PageCalendarProposalComponent,
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
    PageCalendarProcessingComponent,
    PageCalendarDetailsComponent,
    CalendarModelNameComponent,
    HandleIndependantModuleComponent,
    ConfirmComponent,
  ],
  imports: [
    NgSelectModule,
    NgbModule.forRoot(),
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    ColorPickerModule,
    DataTablesModule
  ],
  providers: [
    { provide: LOCALE_ID, useValue: 'fr' },
    { provide: NgbDatepickerI18n, useClass: DatepickerI18n },
    AuthService,
    UserService,
    CourService,
    CalendarService,
    PromotionService,
    ModelsService,
    ModuleService,
    TitreService,
    ParameterService,
    FormationService,
    LieuService,
    StagiaireService,
    EntrepriseService,
    HistoryService,
    CalendarModelService,
    IndependantModuleService,
  ],
  entryComponents: [
    AddElementComponent,
    DispenseElementComponent,
    CalendarModelNameComponent,
    HandleIndependantModuleComponent,
    ConfirmComponent,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
