import {Component, OnInit} from '@angular/core';
import {CalendarService} from "../../services/calendar.service";
import {CalendarStates} from "../../models/enums/calendar-states";
import {StagiaireService} from "../../services/stagiaire.service";
import {EntrepriseService} from "../../services/entreprise.service";
import {ModuleService} from "../../services/module.service";
import {PromotionService} from "../../services/promotion.service";
import {FormationService} from "../../services/formation.service";

@Component({
  selector: 'app-page-search',
  templateUrl: './page-search.component.html',
  styleUrls: ['./page-search.component.scss']
})
export class PageSearchComponent implements OnInit {

  CALENDAR_STATE = CalendarStates;

  stagiaires = [];
  entreprises = [];
  promotions = [];
  modules = [];
  formations = [];

  selectedState = "ALL";
  selectedStagiaire = null;
  selectedEntreprise = null;
  selectedModule = null;
  selectedFormation = null;
  selectedPromotion = null;
  selectedDateDebut = null;
  selectedDateFin = null;

  calendars = [];

  constructor(private calendarService: CalendarService,
              private stagiaireService: StagiaireService,
              private entrepriseService: EntrepriseService,
              private moduleService: ModuleService,
              private promotionService: PromotionService,
              private formationService: FormationService) { }

  ngOnInit() {
    this.stagiaireService.getStagiaires().subscribe(res => {
      this.stagiaires = res;
    }, err => {
      console.error(err);
    });

    this.entrepriseService.getEntreprises().subscribe(res => {
      this.entreprises = res;
    }, err => {
      console.error(err);
    });

    this.moduleService.getModules().subscribe(res => {
      this.modules = res;
    }, err => {
      console.error(err);
    });

    this.promotionService.getPromotions().subscribe(res => {
      this.promotions = res;
    }, err => {
      console.error(err);
    });

    this.formationService.getFormations().subscribe(res => {
      this.formations = res;
    }, err => {
      console.error(err);
    });
  }

  search() {
    // TODO : For the moment get all calendar for mock
    this.calendarService.getCalendars().subscribe(res => {
      this.calendars = res;
    }, err => {
      console.error(err);
    });
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
    alert('Not yet implemented');
  }
}
