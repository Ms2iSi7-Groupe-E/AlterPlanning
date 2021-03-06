import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {CalendarService} from "../../services/calendar.service";
import {CalendarStates} from "../../models/enums/calendar-states";
import {StagiaireService} from "../../services/stagiaire.service";
import {EntrepriseService} from "../../services/entreprise.service";
import {ModuleService} from "../../services/module.service";
import {PromotionService} from "../../services/promotion.service";
import {FormationService} from "../../services/formation.service";
import {SearchKeys} from "../../models/enums/search-keys";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ConfirmComponent} from "../modal/confirm/confirm.component";
import {SearchQueryModel} from "../../models/search-query.model";

@Component({
  selector: 'app-page-search',
  templateUrl: './page-search.component.html',
  styleUrls: ['./page-search.component.scss']
})
export class PageSearchComponent implements OnInit {

  QUERY_KEYS = SearchKeys;
  CALENDAR_STATE = CalendarStates;

  stagiaires = [];
  entreprises = [];
  promotions = [];
  modules = [];
  formations = [];

  validated = false;
  searching = false;

  selectedState = "ALL";
  selectedStagiaire = null;
  selectedEntreprise = null;
  selectedModule = null;
  selectedFormation = null;
  selectedPromotion = null;
  selectedDateDebut = null;
  selectedDateFin = null;

  calendars = [];

  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private modalService: NgbModal,
              private calendarService: CalendarService,
              private stagiaireService: StagiaireService,
              private entrepriseService: EntrepriseService,
              private moduleService: ModuleService,
              private promotionService: PromotionService,
              private formationService: FormationService) { }

  ngOnInit() {
    this.stagiaireService.getStagiaires().subscribe(res => this.stagiaires = res, console.error);
    this.entrepriseService.getEntreprises().subscribe(res => this.entreprises = res, console.error);
    this.moduleService.getModules().subscribe(res => this.modules = res, console.error);
    this.promotionService.getPromotions().subscribe(res => this.promotions = res, console.error);
    this.formationService.getFormations().subscribe(res => this.formations = res, console.error);

    this.handleQueryParams();
  }

  search() {
    this.calendars = [];
    this.validated = true;
    this.searching = true;
    this.addParamToRoute(this.QUERY_KEYS.validated, true);

    let getCalendarObservable = null;

    if (this.allFieldsAreClear) {
      getCalendarObservable = this.calendarService.getCalendars();
    } else {
      const queryParams = new SearchQueryModel();

      if (this.selectedState !== 'ALL') {
        queryParams.state = this.selectedState;
      }

      if (this.selectedStagiaire != null) {
        queryParams.codeStagiaire = this.selectedStagiaire;
      }

      if (this.selectedEntreprise != null) {
        queryParams.codeEntreprise = this.selectedEntreprise;
      }

      if (this.selectedModule != null) {
        queryParams.idModule = this.selectedModule;
      }

      if (this.selectedFormation != null) {
        queryParams.codeFormation = this.selectedFormation;
      }

      if (this.selectedPromotion != null) {
        queryParams.codePromotion = this.selectedPromotion;
      }

      if (this.selectedDateDebut != null) {
        const sd = new Date(this.selectedDateDebut.year, this.selectedDateDebut.month - 1, this.selectedDateDebut.day);
        queryParams.startDate = sd.getTime().toString();
      }

      if (this.selectedDateFin != null) {
        const ed = new Date(this.selectedDateFin.year, this.selectedDateFin.month - 1, this.selectedDateFin.day);
        queryParams.endDate = ed.getTime().toString();
      }

      getCalendarObservable = this.calendarService.searchCalendars(queryParams);
    }

    if (getCalendarObservable !== null) {
      getCalendarObservable.subscribe(res => {
        this.calendars = res;
        this.searching = false;
      }, err => {
        console.error(err);
        this.searching = false;
      });
    } else {
      console.error('impossible de construire l\'observable de recherche');
      this.searching = false;
    }
  }

  get allFieldsAreClear() {
    return this.selectedStagiaire === null && this.selectedState === "ALL"
      && this.selectedEntreprise === null && this.selectedModule === null
      && this.selectedFormation === null && this.selectedPromotion === null
      && this.selectedDateDebut === null && this.selectedDateFin === null;
  }

  addParamToRoute(key, value) {
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams: {
        ...this.activatedRoute.snapshot.queryParams,
        [key]: value,
      }
    });
  }

  handleQueryParams() {
    const queryParams: Params = Object.assign({}, this.activatedRoute.snapshot.queryParams);

    this.selectedState = queryParams.state ? queryParams.state : "ALL";
    this.selectedStagiaire = queryParams.stagiaire ? parseInt(queryParams.stagiaire, 10) : null;
    this.selectedEntreprise = queryParams.entreprise ? parseInt(queryParams.entreprise, 10) : null;
    this.selectedModule = queryParams.module ? parseInt(queryParams.module, 10) : null;
    this.selectedFormation = queryParams.formation ? queryParams.formation : null;
    this.selectedPromotion = queryParams.promotion ? queryParams.promotion : null;

    const dateDeb = queryParams.startDate ? new Date(parseInt(queryParams.startDate, 10)) : null;
    const dateFin = queryParams.endDate ? new Date(parseInt(queryParams.endDate, 10)) : null;

    this.selectedDateDebut = !dateDeb ? null : {
      year: dateDeb.getFullYear(),
      month: dateDeb.getMonth() + 1,
      day: dateDeb.getDate()
    };

    this.selectedDateFin = !dateFin ? null : {
      year: dateFin.getFullYear(),
      month: dateFin.getMonth() + 1,
      day: dateFin.getDate()
    };

    if (queryParams.validated) {
      if (queryParams.validated === 'true') {
        this.search();
      }
    } else {
      this.addParamToRoute(this.QUERY_KEYS.validated, false);
    }
  }

  selectedValueChange(key: SearchKeys) {
    switch (key) {
      case SearchKeys.state:
        this.addParamToRoute(key, this.selectedState);
        break;

      case SearchKeys.stagiaire:
        this.addParamToRoute(key, this.selectedStagiaire);
        break;

      case SearchKeys.entreprise:
        this.addParamToRoute(key, this.selectedEntreprise);
        break;

      case SearchKeys.module:
        this.addParamToRoute(key, this.selectedModule);
        break;

      case SearchKeys.formation:
        this.addParamToRoute(key, this.selectedFormation);
        break;

      case SearchKeys.promotion:
        this.addParamToRoute(key, this.selectedPromotion);
        break;

      case SearchKeys.startDate:
        const dateDeb = !this.selectedDateDebut ? null :
          new Date(this.selectedDateDebut.year, this.selectedDateDebut.month - 1, this.selectedDateDebut.day);
        const valueDeb = dateDeb ? dateDeb.getTime() : null;
        this.addParamToRoute(key, valueDeb);
        break;

      case SearchKeys.endDate:
        const dateFin = !this.selectedDateFin ? null :
          new Date(this.selectedDateFin.year, this.selectedDateFin.month - 1, this.selectedDateFin.day);
        const valueFin = dateFin ? dateFin.getTime() : null;
        this.addParamToRoute(key, valueFin);
        break;
    }
  }

  getCalendarStagiairePrenom(calendar) {
    return calendar.stagiaire ? calendar.stagiaire.prenom : '';
  }

  getCalendarStagiaireNom(calendar) {
    return calendar.stagiaire ? calendar.stagiaire.nom : '';
  }

  getCalendarEntrepriseName(calendar) {
    return calendar.entreprise ? calendar.entreprise.raisonSociale : '';
  }

  getCalendarPeriode(calendar) {
    const debut = calendar.startDate ? new Date(calendar.startDate) : null;
    const fin = calendar.endDate ? new Date(calendar.endDate) : null;
    const debutStr = debut ? debut.toLocaleDateString() : null;
    const finStr = fin ? fin.toLocaleDateString() : null;

    let periode = '';

    if (debutStr && finStr) {
      periode = debutStr + ' au ' + finStr;
    } else if (debutStr && !finStr) {
      periode = debutStr + ' au ...';
    } else if (!debutStr && finStr) {
      periode = '... au ' + finStr;
    }
    return periode;
  }

  getCalendarState(calendar) {
    if (calendar.state === CalendarStates.DRAFT) {
      return 'Brouillon';
    } else if (calendar.state === CalendarStates.PROPOSAL) {
      return 'Proposition';
    } else if (calendar.state === CalendarStates.VALIDATED) {
      return 'Confirmé';
    } else {
      return '';
    }
  }

  getCalendarCssClass(calendar) {
    if (calendar.state === CalendarStates.DRAFT) {
      return 'stat_brouillon';
    } else if (calendar.state === CalendarStates.PROPOSAL) {
      return 'stat_proposition';
    } else if (calendar.state === CalendarStates.VALIDATED) {
      return 'stat_confirme';
    } else {
      return '';
    }
  }

  deleteCalendar(calendar) {
    const modalRef = this.modalService.open(ConfirmComponent, { size: 'sm' });
    modalRef.componentInstance.text = "Voulez-vous vraiment supprimer ce calendrier ?";
    modalRef.componentInstance.validate.subscribe(() => {
      this.calendarService.deleteCalendar(calendar.id).subscribe(() => {
        this.calendars = this.calendars.filter(c => c.id !== calendar.id);
      }, console.error);
    });
  }
}
