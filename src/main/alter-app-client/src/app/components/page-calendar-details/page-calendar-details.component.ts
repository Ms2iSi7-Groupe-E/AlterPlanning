import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CalendarService} from "../../services/calendar.service";
import {CalendarStates} from "../../models/enums/calendar-states";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {CalendarModelNameComponent} from "../modal/calendar-model-name/calendar-model-name.component";
import {CalendarModelService} from "../../services/calendar-model.service";
import {CalendarModelModel} from "../../models/calendar-model.model";
import {ConstraintTypes} from "../../models/enums/constraint-types";
import {CalendarStateModel} from "../../models/calendar-state.model";
import {ConfirmComponent} from "../modal/confirm/confirm.component";
import {UpdateStagiaireComponent} from "../modal/update-stagiaire/update-stagiaire.component";
import {UpdateEntrepriseComponent} from "../modal/update-entreprise/update-entreprise.component";

@Component({
  selector: 'app-page-calendar-details',
  templateUrl: './page-calendar-details.component.html',
  styleUrls: ['./page-calendar-details.component.scss']
})
export class PageCalendarDetailsComponent implements OnInit {

  CALENDAR_STATES = CalendarStates;
  error;
  calendar;
  lines = [];
  startDate;
  endDate;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private modalService: NgbModal,
              private calendarService: CalendarService,
              private calendarModelService: CalendarModelService) { }

  ngOnInit() {
    this.route.paramMap.subscribe(p => {
      if (p['params'] && p['params'].id) {
        const id = p['params'].id;
        this.calendarService.getCalendar(id).subscribe(
          res => {
            this.calendar = res;
            if (this.calendar.state === CalendarStates.DRAFT) {
              this.goToProcessingPage(res.id);
              return;
            }
            this.getLines();
          },
          err => {
            if (err['status'] === 404) {
              this.error = "Ce calendrier n'existe pas ou plus.";
            } else {
              this.error = "Une erreur est survenue lors de la récupération du calendrier";
              console.error(err);
            }
          });
      } else {
        this.error = "L'identifiant du calendrier est mal renseigné dans l'URL. Merci de contacter votre administrateur.";
      }
    });
  }

  goToProcessingPage(idCalendar) {
    this.router.navigate(['/calendar/' + idCalendar + '/processing']);
  }

  getLines() {
    this.calendarService.getLinesForCalendar(this.calendar.id).subscribe(res => {
      this.lines = res;
      this.startDate = this.lines[0].debut;
      this.endDate = this.lines[this.lines.length - 1].fin;
    }, console.error);
  }

  registerAsModel() {
    const modalRef = this.modalService.open(CalendarModelNameComponent, { size: 'lg' });
    modalRef.componentInstance.validate.subscribe(res => {
      const body = new CalendarModelModel();
      body.idCalendar = this.calendar.id;
      body.name = res.name;
      this.calendarModelService.addCalendarModel(body).subscribe(() => {
        this.calendar.isModel = true;
      }, console.error);
    });
  }

  changeStateToValidate() {
    const modalRef = this.modalService.open(ConfirmComponent, { size: 'sm' });
    modalRef.componentInstance.text = "Voulez-vous valider ce calendrier ?";
    modalRef.componentInstance.validate.subscribe(() => {
      const body = new CalendarStateModel();
      body.state = CalendarStates.VALIDATED;
      this.calendarService.changeStateCalendar(this.calendar.id, body).subscribe(() => {
        this.calendar.state = CalendarStates.VALIDATED;
      }, console.error);
    });
  }

  deleteCalendar() {
    const modalRef = this.modalService.open(ConfirmComponent, { size: 'sm' });
    modalRef.componentInstance.text = "Voulez-vous vraiment supprimer ce calendrier ?";
    modalRef.componentInstance.validate.subscribe(() => {
      this.calendarService.deleteCalendar(this.calendar.id).subscribe(() => {
        return this.router.navigate(['/']);
      }, console.error);
    });
  }

  proposeAlternative() {
    this.router.navigate(['/propose-calendar'], { queryParams: { fromCalendar: this.calendar.id } });
  }

  updateStagiaire() {
    const modalRef = this.modalService.open(UpdateStagiaireComponent, { size: 'lg' });
    if (this.calendar.stagiaire) {
      modalRef.componentInstance.codeStagiaire = this.calendar.stagiaire.codeStagiaire;
    }
    if (this.calendar.entreprise) {
      modalRef.componentInstance.codeEntreprise = this.calendar.entreprise.codeEntreprise;
    }
    modalRef.componentInstance.validate.subscribe((res) => {
      console.log(res);
    });
  }

  updateEntreprise() {
    const modalRef = this.modalService.open(UpdateEntrepriseComponent, { size: 'lg' });
    if (this.calendar.stagiaire) {
      modalRef.componentInstance.codeStagiaire = this.calendar.stagiaire.codeStagiaire;
    }
    if (this.calendar.entreprise) {
      modalRef.componentInstance.codeEntreprise = this.calendar.entreprise.codeEntreprise;
    }
    modalRef.componentInstance.validate.subscribe((res) => {
      console.log(res);
    });
  }

  get cursus() {
    if (this.calendar === null) {
      return '';
    }

    const constraints = this.calendar.constraints;
    const formations = constraints.filter(c => c.constraintType === ConstraintTypes.AJOUT_FORMATION);
    if (formations.length === 0) {
      return '';
    }

    return formations[0].constraintValue;
  }

  get state() {
    return this.calendar.state === CalendarStates.PROPOSAL ? 'Proposition' : 'Validé';
  }
}
