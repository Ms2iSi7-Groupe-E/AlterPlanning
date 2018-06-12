import { Component, OnInit } from '@angular/core';
import {CalendarService} from "../../services/calendar.service";
import {CalendarStates} from "../../models/enums/calendar-states";

@Component({
  selector: 'app-page-search',
  templateUrl: './page-search.component.html',
  styleUrls: ['./page-search.component.scss']
})
export class PageSearchComponent implements OnInit {

  calendars = [];

  constructor(private calendarService: CalendarService) { }

  ngOnInit() {
    // Get stagaires
    // Get entreprises
    // Get promotions
    // Get cursus
    // Get Modules
  }

  search() {
    // For the moment get all calendar for mock
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
