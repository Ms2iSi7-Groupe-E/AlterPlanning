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
  formations = [];
  cours = [];
  mois = [];

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

            // chargement des cours
            this.calendarService.getCoursForCalendarInGeneration(id).subscribe(
              resC => {
                this.cours = resC;
                this.loadElements();
              },
              errC => {
                console.error(errC);
              }
            );
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

    // pour tous les cours
    // determine la liste des formations
    // determine le premier et le dernier jour
    let iJourMin = 0;
    let iJourMax = 0;
    this.cours.forEach((c) => {

      // determine si la formation doit etre prise en compte
      if (this.formations.filter( p => p.codeFormation === c.codeFormation ).length === 0 ) {
        this.formations.push( {"codeFormation": c.codeFormation, "libelleFormation": c.libelleFormation,
        "libelleFormationLong": c.libelleFormationLong, "color": ""} );
      }

      // mise a jour du jour min et max
      const iDateDebut = parseInt( moment(c.debut).format("X"), 10 );
      const iDateFin = parseInt( moment(c.fin).format("X"), 10 );
      if ( iDateDebut < iJourMin || iJourMin === 0 ) {
        iJourMin = iDateDebut;
      }
      if ( iDateFin > iJourMax || iJourMax === 0 ) {
        iJourMax = iDateFin;
      }
    });

    // pour tous les jours de la periode du calendrier
    for ( let iDay = iJourMin; iDay < iJourMax; iDay += 86400 ) {
      const oDay = moment.unix( iDay );
      const sKeyMonth = oDay.format( "YYYY-MM" );
      const sKeyDay = oDay.format( "D" );

      // determine si le jour est dans le weenend
      if ( parseInt( oDay.format( "e" ), 10 ) > 4 ) {
        continue;
      }

      // determine le mois existe
      if ( !(sKeyMonth in this.mois) ) {
        this.mois[ sKeyMonth ] = { "libelle": oDay.format( "MMMM" ), "jours": [] };
      }

      // determine si le jour du mois existe
      if ( !(sKeyDay in this.mois[ sKeyMonth ][ "jours" ]) ) {
        this.mois[ sKeyMonth ][ "jours" ][ sKeyDay ] = { "lettre": oDay.format( "ddd" ).substring(0, 1), "jour": sKeyDay,
        "cours": [] };
      } else {

        // ce jour est deja pris en compte
        continue;
      }

      // recupere les cours concernes par ce jour
      const cours = [];
      this.cours.forEach((c) => {
        const iDateDebut = parseInt( moment(c.debut).format("X"), 10 );
        const iDateFin = parseInt( moment(c.fin).format("X"), 10 );
        if ( iDay >= iDateDebut && iDay <= ( iDateFin - 86400 ) ) {
          cours.push( c );
        }
      });

      // ajout des cours
      this.mois[ sKeyMonth ][ "jours" ][ sKeyDay ][ "cours" ] = cours;
    }

    console.log( iJourMin );
    console.log( iJourMax );
    console.log( this.mois );

    /*for ( let i = 0; i < this.mois.length; i++ ) {

      console.log( this.mois[ i ] );
    }*/

    // initialisation des styles
    this.parameterService.getParamters().subscribe(
      res => {
        // recupere les parametres
        this.parameters = res;

        // recupere les couleurs des lieux et des formations
        const colorsLieux = [];
        const colorsFormations = [];
        this.parameters.filter(p => p.key.startsWith("COURSE")).forEach((param) => {
          colorsLieux.push( param.value );
        });
        this.parameters.filter(p => p.key.startsWith("FORMATION")).forEach((param) => {
          colorsFormations.push( param.value );
        });

        // pour tous les lieux
        this.calendar.constraints.forEach((c) => {
          if ( c.constraintType !== "LIEUX" ) {
            return;
          }

          // recupere les informations du lieux
          this.lieuService.getLieuById( c.constraintValue ).subscribe(
            resL => {
              this.lieux.push( {"libelle": resL.libelle, "color": colorsLieux[ this.lieux.length ],
                "libelleLieuLong": resL.libelle + ", " + resL.adresse + ", " + resL.cp + " " + resL.ville } );
            },
            errL => {
              console.error(errL);
            }
          );
        });

        // applique les couleurs des formations
        this.formations.forEach((f, index) => {
          f.color = colorsFormations[ index ];
        });
      },
      err => {
        console.error(err);
      }
    );
  }

  // retourne les cles d'un tableau
  objectKeys(obj) {
      return Object.keys(obj);
  }

  // recupere la couleur d'un lieu de formation
  getColorLieuFormation( codeLieu ) {
    return 'FFFFFF';
  }
}
