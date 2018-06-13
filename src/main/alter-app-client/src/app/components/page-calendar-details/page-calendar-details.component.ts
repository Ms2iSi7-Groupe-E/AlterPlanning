import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CalendarService} from "../../services/calendar.service";
import {CalendarStates} from "../../models/enums/calendar-states";

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
              private calendarService: CalendarService) { }

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

}
