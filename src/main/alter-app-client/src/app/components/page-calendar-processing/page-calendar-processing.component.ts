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
  afficher = false;

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
          this.colorsLieux.push( { "key" : param.key, "value" : param.value, "codeLieu" : "" } );
        });
        this.parameters.filter(p => p.key.startsWith("FORMATION")).forEach((param) => {
          this.colorsFormations.push( { "key" : param.key, "value" : param.value, "codeFormation" : "" } );
        });

        // recupere le code de foramtion
        this.formations.forEach((f, index) => {
          if ( index < 6 ) {
            this.colorsFormations[ index ].codeFormation = f.codeFormation;
          } else {

            // generation tournante d'une couleur
            this.colorsFormations.push( { "key" : "FORMATION_COLOR_" + ( index + 1 ),
              "value" : this.colorsFormations[ index % 6 ].value, "codeFormation" : f.codeFormation } );
          }
        });

        // pour tous les lieux
        let iIndexColorLieu = 0;
        let iCountLieux = 0;
        this.calendar.constraints.forEach((c) => {
          if ( c.constraintType !== "LIEUX" ) {
            return;
          }
          iCountLieux++;

          // recupere le code du lieu
          if ( iIndexColorLieu < 6 ) {
            this.colorsLieux[ iIndexColorLieu ].codeLieu = c.constraintValue;
          } else {

            // generation tournante d'une couleur
            this.colorsLieux.push( { "key" : "COURSE_COLOR_" + ( iIndexColorLieu + 1 ),
              "value" : this.colorsLieux[ iIndexColorLieu % 6 ].value, "codeLieu" : c.constraintValue } );
          }
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
            },
            () => {
              iCountLieux--;

              // si c'est le dernier lieu
              if ( iCountLieux === 0 ) {

                // generation des cours
                this.generationCours();
              }
            }
          );
        });
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
    const mois = [];
    const semaines = [];
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
      if ( semaines.length === 0 || iNumDay === 0 ) {
        semaines.push( { "jours": [], "class" : "select_empty", "anchor" : sKeyMonth + '-' + sKeyDay } );
      }

      // positionne le jour dans la semaine
      semaines[ semaines.length - 1 ].jours.push( sKeyMonth + '-' + sKeyDay );

      // determine le mois existe
      if ( !(sKeyMonth in mois) ) {
        mois[ sKeyMonth ] = { "libelle": oDay.format( "MMMM" ), "jours": [], "lieux": [], "formations": [] };
      }

      // determine si le jour du mois existe
      if ( !(sKeyDay in mois[ sKeyMonth ][ "jours" ]) ) {
        mois[ sKeyMonth ][ "jours" ][ sKeyDay ] = { "lettre": oDay.format( "ddd" ).substring(0, 1), "jour": sKeyDay,
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

          // determine si un cours similaire existe sur le meme lieu pour le meme jour
          const cj = cours.find( fcj => {
            return fcj.codeLieu.toString() === c.codeLieu.toString() && fcj.idCours === c.idCours
              && fcj.anneeMois === c.anneeMois && fcj.jour === c.jour;
          });
          if ( !cj ) {
            c.anneeMois = sKeyMonth;
            c.jour = sKeyDay;
            cours.push( c );
          }
        }
      });

      // ajout des cours
      mois[ sKeyMonth ][ "jours" ][ sKeyDay ][ "cours" ] = cours;

      // pour tous les cours du mois
      cours.forEach((c) => {

        // determine la formation existe dans ce mois
        const isFormationMois = mois[ sKeyMonth ][ "formations" ].find( f => {
          return c.codeFormation === f.codeFormation;
        } );
        if ( !isFormationMois ) {
          mois[ sKeyMonth ][ "formations" ].push( this.formations.filter( f => f.codeFormation === c.codeFormation )[ 0 ] );
        }

        // determine les lieux du mois
        const isLieuMois = mois[ sKeyMonth ][ "lieux" ].find( l => {
          return c.codeLieu.toString() === l.codeLieu.toString();
        } );
        if ( !isLieuMois ) {
          mois[ sKeyMonth ][ "lieux" ].push( this.lieux.filter( l => l.codeLieu.toString() === c.codeLieu.toString() )[ 0 ] );
        }
      });
    }
    this.mois = mois;
    this.semaines = semaines;
    this.afficher = true;
  }

  // placement d'un cours
  placementCours(c) {
    c = Object.assign( {}, c );

    // si il y a un cours deja place sur ce jour
    if ( this.mois[ c.anneeMois ].jours[ c.jour ].cplace != null ) {
      this.deplacementCours( this.mois[ c.anneeMois ].jours[ c.jour ].cplace );
    }

    // pour tous les mois
    const mois = Object.assign( {}, this.mois );
    Object.keys(mois).forEach( km => {

      // pour tous les jours du mois
      Object.keys(mois[km].jours).forEach( kj => {

        // pour tous les cours du jour
        let bUpdateCours = false;
        const newCours = [];
        mois[km].jours[kj].cours.forEach( cj => {

          // si c'est le cours concerne pas le deplacement
          if ( cj.idCours === c.idCours && cj.codeLieu.toString() === c.codeLieu.toString() ) {
            bUpdateCours = true;

            // placement du cours
            mois[km].jours[kj].cplace = cj;

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
          mois[km].jours[kj].cours = newCours;
          bUpdateCours = false;
        }
      });
    });
    this.mois = mois;
  }

  // deplacement d'un cours
  deplacementCours(c) {
    c = Object.assign( {}, c );

    // pour tous les mois
    const mois = Object.assign( {}, this.mois );
    Object.keys(mois).forEach( km => {

      // pour tous les jours du mois
      Object.keys(mois[km].jours).forEach( kj => {

        // determine si le jour contient le cours a deplacer
        if ( mois[km].jours[kj].cplace != null && mois[km].jours[kj].cplace.idCours === c.idCours
            && mois[km].jours[kj].cplace.codeLieu.toString() === c.codeLieu.toString()) {

          mois[km].jours[kj].cours.push( c );
          mois[km].jours[kj].cplace = null;

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
    this.mois = mois;
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

  // traitement des redimentionnements
  onResizeWindow(e) {

    // determine la taille d'un element
    const iSizeOneWeek = ( window.innerHeight - 250 ) / this.semaines.length;
    const divs = document.querySelectorAll("div.select_empty, div.select_week");
    for ( let i = 0; i < divs.length; i++ ) {
      divs[ i ][ "style" ].height = iSizeOneWeek + "px";
    }
  }
}
