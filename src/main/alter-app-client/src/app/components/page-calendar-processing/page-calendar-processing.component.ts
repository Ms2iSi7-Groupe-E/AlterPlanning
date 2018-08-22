import { Component, OnInit } from '@angular/core';
import {CalendarService} from "../../services/calendar.service";
import {ActivatedRoute, Router} from "@angular/router";
import * as moment from "moment";
import "moment/locale/fr";
import {ParameterService} from "../../services/parameter.service";
import {LieuService} from "../../services/lieu.service";
import {ParameterModel} from "../../models/parameter.model";

@Component({
  selector: 'app-page-calendar-processing',
  templateUrl: './page-calendar-processing.component.html',
  styleUrls: ['./page-calendar-processing.component.scss']
})
export class PageCalendarProcessingComponent implements OnInit {

  error;
  calendar;
  parameters = [];
  lieux = [];

  constructor(private route: ActivatedRoute,
              private router: Router,
              private calendarService: CalendarService,
              private parameterService: ParameterService,
              private lieuService: LieuService) {
    moment.locale("fr");
  }

  ngOnInit() {
    this.route.paramMap.subscribe(p => {
      if (p['params'] && p['params'].id) {
        const id = p['params'].id;
        this.calendarService.getCalendar(id).subscribe(
          res => {
            this.calendar = res;
            this.loadElements();
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

  // chargement des elements du calendriers
  loadElements() {

    // initialisation des styles
    this.parameterService.getParamters().subscribe(
      res => {
        // recupere les parametres
        this.parameters = res;

        // recupere les couleurs des lieux
        const colorsLieux = [];
        this.parameters.filter(p => p.key.startsWith("COURSE")).forEach((param) => {
          colorsLieux.push( param.value );
        });

        // pour tous les lieux
        this.calendar.constraints.forEach((c) => {
          if ( c.constraintType !== "LIEUX" ) {
            return;
          }

          // recpere les informations du lieux
          this.lieuService.getLieuById( c.constraintValue ).subscribe(
            resL => {
              this.lieux.push( {"libelle": resL.libelle, "color": colorsLieux[ this.lieux.length ] } );
            },
            errL => {
              console.error(errL);
            }
          );
        });

        /*
        angular.forEach(values, function(value, key) {
  this.push(key + ': ' + value);
}, log);*/
       /* this.formation_colors = this.parameters.filter(p => p.key.startsWith("FORMATION"));
        this.course_colors = this.parameters.filter(p => p.key.startsWith("COURSE"));
        this.switch_colors = this.parameters.filter(p => p.key.startsWith("SWITCH_COLOR"));
        this.switch_values = this.parameters.filter(p => p.key.startsWith("SWITCH_VALUE")).map(sv => {
          return {
            key: sv.key,
            value: parseInt(sv.value, 10)
          };
        });*/

      },
      err => {
        console.error(err);
      }
    );

    // determine les lieux du calendrier
  }

}
