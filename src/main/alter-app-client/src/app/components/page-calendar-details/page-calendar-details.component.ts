import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CalendarService} from "../../services/calendar.service";
import {CalendarStates} from "../../models/enums/calendar-states";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {CalendarModelNameComponent} from "../modal/calendar-model-name/calendar-model-name.component";
import {CalendarModelService} from "../../services/calendar-model.service";
import {CalendarModelModel} from "../../models/calendar-model.model";

@Component({
  selector: 'app-page-calendar-details',
  templateUrl: './page-calendar-details.component.html',
  styleUrls: ['./page-calendar-details.component.scss']
})
export class PageCalendarDetailsComponent implements OnInit {

  error;
  calendar;

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
              this.router.navigate(['/calendar/' + res.id + '/processing']);
            }
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
}
