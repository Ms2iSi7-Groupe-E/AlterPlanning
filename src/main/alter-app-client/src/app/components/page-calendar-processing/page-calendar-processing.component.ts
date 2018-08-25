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
  colorsLieux = [];
  colorsFormations = [];
  cours = [];
  mois = [];
  semaines = [];

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
    this.cours.forEach((c) => {

      // determine si la formation doit etre prise en compte
      if (this.formations.filter( p => p.codeFormation === c.codeFormation ).length === 0 ) {
        this.formations.push( {"codeFormation": c.codeFormation, "libelleFormation": c.libelleFormation,
        "libelleFormationLong": c.libelleFormationLong, "color": ""} );
      }
    });

    // initialisation des styles
    this.parameterService.getParamters().subscribe(
      res => {
        // recupere les parametres
        this.parameters = res;

        // recupere les couleurs des lieux et des formations
        this.parameters.filter(p => p.key.startsWith("COURSE")).forEach((param) => {
          this.colorsLieux.push( param );
        });
        this.parameters.filter(p => p.key.startsWith("FORMATION")).forEach((param) => {
          this.colorsFormations.push( param );
        });

        // recupere le code de foramtion
        this.formations.forEach((f, index) => {
          this.colorsFormations[ index ].codeFormation = f.codeFormation;
        });

        // pour tous les lieux
        let iIndexColorLieu = 0;
        this.calendar.constraints.forEach((c) => {
          if ( c.constraintType !== "LIEUX" ) {
            return;
          }

          // recupere le code du lieu
          this.colorsLieux[ iIndexColorLieu ].codeLieu = c.constraintValue;
          iIndexColorLieu++;

          // recupere les informations du lieux
          this.lieuService.getLieuById( c.constraintValue ).subscribe(
            resL => {
              this.lieux.push( {"libelle": resL.libelle,
                "libelleLieuLong": resL.libelle + ", " + resL.adresse + ", " + resL.cp + " " + resL.ville,
                "codeLieu": c.constraintValue } );
            },
            errL => {
              console.error(errL);
            }
          );
        });

        // generation des cours
        this.generationCours();
      },
      err => {
        console.error(err);
      }
    );
  }

  // generation des cours
  generationCours() {

    // pour tous les cours
    // determine le premier et le dernier jour
    let iJourMin = 0;
    let iJourMax = 0;
    this.cours.forEach((c) => {

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
      const iNumDay = parseInt( oDay.format( "e" ), 10 );

      // determine si le jour est dans le weenend
      if ( iNumDay > 4 ) {
        continue;
      }

      // determine si c'est une nouvelle semaine
      if ( this.semaines.length === 0 || iNumDay === 0 ) {
        this.semaines.push( { "jours": [], "class" : "select_empty", "anchor" : sKeyMonth + '-' + sKeyDay } );
      }

      // positionne le jour dans la semaine
      this.semaines[ this.semaines.length - 1 ].jours.push( sKeyMonth + '-' + sKeyDay );

      // determine le mois existe
      if ( !(sKeyMonth in this.mois) ) {
        this.mois[ sKeyMonth ] = { "libelle": oDay.format( "MMMM" ), "jours": [] };
      }

      // determine si le jour du mois existe
      if ( !(sKeyDay in this.mois[ sKeyMonth ][ "jours" ]) ) {
        this.mois[ sKeyMonth ][ "jours" ][ sKeyDay ] = { "lettre": oDay.format( "ddd" ).substring(0, 1), "jour": sKeyDay,
        "cours": [], "cplace": null };
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

    console.log( this.semaines );
  }

  // placement d'un cours
  placementCours(c) {

    // pour tous les mois
    Object.keys(this.mois).forEach( km => {

      // pour tous les jours du mois
      Object.keys(this.mois[km].jours).forEach( kj => {

        // pour tous les cours du jour
        let bUpdateCours = false;
        const newCours = [];
        this.mois[km].jours[kj].cours.forEach( cj => {

          // si c'est le cours concerne pas le deplacement
          if ( cj.idCours === c.idCours ) {
            bUpdateCours = true;

            // si il y a un cours deja place
            if ( this.mois[km].jours[kj].cplace != null ) {
              this.deplacementCours( this.mois[km].jours[kj].cplace );
            }

            // placement du cours
            this.mois[km].jours[kj].cplace = c;

            // recherche la semaine concernee
            this.semaines.forEach( s => {

              // determine si le jour et contenu dans cette semaine
              const sj = s.jours.find( j => {
                return j === (km + '-' + kj);
              });
              if ( sj ) {
                s.class = 'select_week';
              }
            });

          } else {

            // ne prendre en compte le cours que s'il ne fait pas partie du cours recherche
            newCours.push( cj );
          }
        });

        // si les cours de ce jour doivent etre mise a jour
        if ( bUpdateCours ) {
          this.mois[km].jours[kj].cours = newCours;
          bUpdateCours = false;
        }
      });
    });
  }

  // deplacement d'un cours
  deplacementCours(c) {

    // pour tous les mois
    Object.keys(this.mois).forEach( km => {

      // pour tous les jours du mois
      Object.keys(this.mois[km].jours).forEach( kj => {

        // determine si le jour contient le cours a deplacer
        if ( this.mois[km].jours[kj].cplace != null && this.mois[km].jours[kj].cplace.idCours === c.idCours ) {
          this.mois[km].jours[kj].cours.push( c );
          this.mois[km].jours[kj].cplace = null;

          // recherche la semaine concernee
          this.semaines.forEach( s => {

            // determine si le jour et contenu dans cette semaine
            const sj = s.jours.find( j => {
              return j === (km + '-' + kj);
            });
            if ( sj ) {
              s.class = 'select_empty';
            }
          });
        }
      });
    });
  }

  // retourne les cles d'un tableau
  objectKeys(obj) {
    return Object.keys(obj);
  }

  // recupere la couleur d'un lieu de formation
  getColorLieuFormation( codeLieu ) {
    const color = this.colorsLieux.find( c => {
       return c.codeLieu.toString() === codeLieu.toString();
     } );
    return color ? color.value : '';
  }

  // recupere la couleur d'une formation
  getColorFormation( codeFormation ) {
    const color = this.colorsFormations.find( c => {
      return c.codeFormation.toString() === codeFormation.toString();
    } );
   return color ? color.value : '';
  }

  // click sur une semaine
  clickSemaine( s ) {
    document.getElementsByName( s.anchor )[ 0 ].scrollIntoView();
    window.scrollTo( 0, window.scrollY - 60 );
  }
}
