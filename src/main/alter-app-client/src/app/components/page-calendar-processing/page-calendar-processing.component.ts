import { Component, OnInit } from '@angular/core';
import {CalendarService} from "../../services/calendar.service";
import {ActivatedRoute, Router} from "@angular/router";
import * as moment from "moment";
import "moment/locale/fr";
import {ParameterService} from "../../services/parameter.service";
import {LieuService} from "../../services/lieu.service";
import {ParameterModel} from "../../models/parameter.model";
import {CalendatrCoursModel} from "../../models/calendar.cours.model";
import { ModuleService } from '../../services/module.service';

@Component({
  selector: 'app-page-calendar-processing',
  templateUrl: './page-calendar-processing.component.html',
  styleUrls: ['./page-calendar-processing.component.scss']
})
export class PageCalendarProcessingComponent implements OnInit {

  registerError;
  error;
  id = 0;
  calendar;
  parameters = [];
  lieux = [];
  formations = [];
  colorsLieux = [];
  colorsFormations = [];
  cours = [];
  coursIndependants = [];
  mois = [];
  semaines = [];
  afficher = false;
  messageNotification = '';
  infoNotification = '';
  totalHeureNotification = '';
  moduleWithRequirement = [];
  moduleLibelle = [];
  navmodPromotions = [];
  navmodPromotionsFilter = [];

  constructor(private route: ActivatedRoute,
              private router: Router,
              private calendarService: CalendarService,
              private parameterService: ParameterService,
              private lieuService: LieuService,
              private moduleService: ModuleService) {
    moment.locale("fr");
  }

  ngOnInit() {
    this.route.paramMap.subscribe(p => {
      if (p['params'] && p['params'].id) {
        this.id = p['params'].id;
        this.calendarService.getCalendar(this.id).subscribe(
          res => {
            this.calendar = res;

            // chargement des cours
            this.calendarService.getCoursForCalendarInGeneration(this.id).subscribe(
              resC => {
                this.cours = resC.cours;
                this.coursIndependants = resC.independantModules;
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

  // retourne ou charge la description d'un module
  getOrLoadDescModule( moduleId ) {
    if ( (moduleId in this.moduleLibelle) ) {
      return this.moduleLibelle[ moduleId ];
    }

    // determine si le libelle du module cible est present dans les cours
    const c = this.cours.find( fc => {
      return fc.idModule.toString() === moduleId;
    });
    if ( c ) {
      this.moduleLibelle[ moduleId ] = c.libelleModule;
      return;
    }

    // determine si le libelle du module cible doit etre charge
    this.moduleService.getModuleById( moduleId ).subscribe(
      res => {
        this.moduleLibelle[ moduleId ] = res.libelle;
      },
      err => {
        console.error(err);
      }
    );
  }

  // chargement des elements du calendriers
  loadElements() {

    // recupere la liste des pre-requis
    this.moduleService.getModulesWithRequirement().subscribe(
      res => {
        this.moduleWithRequirement = res;

        // determine les lignes de validation
        this.moduleWithRequirement.forEach( m => {
          m.validation = [];
          m.validation.push( { "modules": [] } );
          m.requirements.forEach( (r, i) => {
            m.validation[ m.validation.length - 1 ][ "modules" ].push( r );
            if ( i < (m.requirements.length - 1) && !m.requirements[ i + 1 ].or ) {
              m.validation.push( { "modules": [] } );
            }
            this.getOrLoadDescModule( r.moduleId.toString() );
          });
          this.getOrLoadDescModule( m.moduleId.toString() );
        });
      },
      err => {
        console.error(err);
      }
    );

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

            // generation aleatoire d'une couleur
            this.colorsFormations[ index ] = { "key" : "FORMATION_COLOR_" + ( index + 1 ),
              "value" : this.getRandomColor(), "codeFormation" : f.codeFormation };
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

    // pour tous les cours et les modules independants
    // determine le premier et le dernier jour
    let iJourMin = 0;
    let iJourMax = 0;
    this.cours.forEach( c => {

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
    this.coursIndependants.forEach( ci => {

      // mise a jour du jour min et max
      const iDateDebut = parseInt( moment(ci.startDate).format("X"), 10 );
      const iDateFin = parseInt( moment(ci.endDate).format("X"), 10 ) + ( parseInt( ci.hours, 10 ) * 3600 );
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
    const oDay = moment.unix( iJourMin - 86400 );
    while ( oDay.unix() < iJourMax ) {
      oDay.add( 1, 'days' );
      const iDay = oDay.unix();

      // recupere les cles des dates
      const sKeyMonth = oDay.format( "YYYY-MM" );
      const sKeyDay = oDay.format( "D" );
      const iNumDay = parseInt( oDay.format( "e" ), 10 );

      // determine si le jour est dans le weenend
      if ( iNumDay > 4 ) {
        continue;
      }

      // determine si c'est une nouvelle semaine
      if ( semaines.length === 0 || iNumDay === 0 ) {
        semaines.push( { "jours": [], "promotions": [], "class" : "select_empty",
          "hiddenClass": "", "anchor" : sKeyMonth + '-' + sKeyDay } );
      }

      // positionne le jour dans la semaine
      semaines[ semaines.length - 1 ].jours.push( { "anneeMois": sKeyMonth, "jour": sKeyDay } );

      // determine le mois existe
      if ( !(sKeyMonth in mois) ) {
        mois[ sKeyMonth ] = { "libelle": oDay.format( "MMMM" ), "annee": oDay.format( "YYYY" ),
          "jours": [], "lieux": [], "formations": [] };
      }

      // determine si le jour du mois existe
      if ( !(sKeyDay in mois[ sKeyMonth ][ "jours" ]) ) {
        mois[ sKeyMonth ][ "jours" ][ sKeyDay ] = { "lettre": oDay.format( "ddd" ).substring(0, 1), "jour": sKeyDay,
        "cours": [], "coursIndependants": [], "cplace": null, "color": "#ffffff" };
      } else {

        // ce jour est deja pris en compte
        continue;
      }

      // recupere les cours concernes par ce jour
      const cours = [];
      let promotions = [];
      this.cours.forEach( c => {
        const iDateDebut = parseInt( moment(c.debut).format("X"), 10 );
        const iDateFin = parseInt( moment(c.fin).format("X"), 10 );
        if ( iDay >= iDateDebut && iDay <= iDateFin ) {

          // determine si un cours similaire existe sur le meme lieu pour le meme jour
          const cj = cours.find( fcj => {
            return fcj.codeLieu.toString() === c.codeLieu.toString() && // fcj.idCours === c.idCours
              fcj.anneeMois === sKeyMonth && fcj.jour === sKeyDay &&
              fcj.idModule === c.idModule;
          });
          if ( !cj ) {
            c.anneeMois = sKeyMonth;
            c.jour = sKeyDay;
            c.show = true;
            c.indep = false;
            cours.push( c );
            promotions = promotions.concat( c.promotions );
          }
        }
      });
      mois[ sKeyMonth ][ "jours" ][ sKeyDay ][ "cours" ] = cours;

      // recupere les cours independants concernes par ce jour
      const coursIndependants = [];
      this.coursIndependants.forEach( ci => {
        const iDateDebut = parseInt( moment(ci.startDate).format("X"), 10 );
        const iDateFin = parseInt( moment(ci.endDate).format("X"), 10 ) + ( parseInt( ci.hours, 10 ) * 3600 );
        if ( iDay >= iDateDebut && iDay <= iDateFin ) {

          // recupere le cours independant
          ci.anneeMois = sKeyMonth;
          ci.jour = sKeyDay;
          ci.show = true;
          ci.indep = true;
          ci.promotions = [];
          coursIndependants.push( ci );
        }
      });
      mois[ sKeyMonth ][ "jours" ][ sKeyDay ][ "coursIndependants" ] = coursIndependants;

      // recuperation des promotions des jours de la semaines
      semaines[ semaines.length - 1 ].promotions = semaines[ semaines.length - 1 ].promotions.concat( promotions );

      // determine la couleur de fond du jour
      if ( cours.length + coursIndependants.length === 0 ) {
        mois[ sKeyMonth ][ "jours" ][ sKeyDay ][ "color" ] = "#e8e8e8";
      }

      // pour tous les cours du mois
      cours.forEach( c => {

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

      // pour tous les cours independants
      coursIndependants.forEach( ci => {

        // determine les lieux du mois
        const isLieuMois = mois[ sKeyMonth ][ "lieux" ].find( l => {
          return ci.codeLieu.toString() === l.codeLieu.toString();
        } );
        if ( !isLieuMois ) {
          mois[ sKeyMonth ][ "lieux" ].push( this.lieux.filter( l => l.codeLieu.toString() === ci.codeLieu.toString() )[ 0 ] );
        }
      });
    }

    // pour toutes les semaines, determine si il y a des semaines sans jour
    semaines.forEach( s => {
      let iNbrCours = 0;
      s.jours.forEach( sj => {
        iNbrCours += mois[ sj.anneeMois ][ "jours" ][ sj.jour ][ "cours" ].length +
          mois[ sj.anneeMois ][ "jours" ][ sj.jour ][ "coursIndependants" ].length;
      });

      // si il n'y a pas de cours cette semaine
      if ( iNbrCours === 0 ) {
        s.class = "select_week_empty";
      }
    });
    this.mois = mois;
    this.semaines = semaines;
    this.afficher = true;
    this.chargementCours();
  }

  // chargement des cours
  chargementCours() {

    // recuperation des infos de base du calendrier
    this.calendarService.getCalendarDetailsById(this.id).subscribe(
      res => {

        // gestion de l'affichage de la zone de notification
        if ( res.stagiaire ) {
          this.infoNotification += "Statgiaire : " + res.stagiaire.nom + " " + res.stagiaire.prenom;
        }
        if ( res.entreprise ) {
          this.infoNotification += ( this.infoNotification !== '' ? ', ' : '' ) + "Entreprise : " + res.entreprise.raisonSociale;
        }

        // placement des cours et des modules independants
        if ( res.cours.length + res.independantModules.length > 0 ) {

          // pour tous les mois
          Object.keys(this.mois).forEach( km => {

            // pour tous les jours du mois
            Object.keys(this.mois[km].jours).forEach( kj => {

              // si il y a des cours a positionner
              if (res.cours.length > 0 ) {
                this.mois[km].jours[kj].cours.filter( c => c.show ).forEach( cj => {

                  // determine si le cours doit etre positionne
                  const cs = res.cours.find( resC => resC.idCours === cj.idCours && resC.codeLieu.toString() === cj.codeLieu.toString() );
                  if ( cs ) {
                    this.placementCours( cj, false );
                  }
                });
              }

              // si il y a des modules independants a positionner
              if ( res.independantModules.length > 0 ) {
                this.mois[km].jours[kj].coursIndependants.filter( c => c.show ).forEach( cij => {

                  // determine si le cours doit etre positionne
                  const cs = res.independantModules.find( resC => resC.id === cij.id );
                  if ( cs ) {
                    this.placementCours( cij, true );
                  }
                });
              }
            });
          });
        }
      },
      err => {
        console.error(err);
      });
  }

  // placement d'un cours
  placementCours(c, bIndep) {
    this.unselectCours();
    c.show = false;

    // si il y a un cours deja place sur ce jour
    if ( this.mois[ c.anneeMois ].jours[ c.jour ].cplace != null ) {
      this.deplacementCours( this.mois[ c.anneeMois ].jours[ c.jour ].cplace );
    }

    // pour tous les mois
    Object.keys(this.mois).forEach( km => {

      // pour tous les jours du mois
      Object.keys(this.mois[km].jours).forEach( kj => {

        // pour tous les cours du jour
        if ( !bIndep ) {
          this.mois[km].jours[kj].cours.forEach( cj => {

            // determine si le jour contient le cours a deplacer
            if ( cj.idCours === c.idCours && cj.codeLieu.toString() === c.codeLieu.toString()
            && cj.codeFormation === c.codeFormation ) {
              this.mois[km].jours[kj].cplace = cj;
            }
          });
        } else {

          this.mois[km].jours[kj].coursIndependants.forEach( cij => {

            // si c'est le cours concerne pas le deplacement
            if ( cij.id === c.id ) {
              this.mois[km].jours[kj].cplace = cij;
            }
          });
        }

        // recherche la semaine concernee
        if ( this.mois[km].jours[kj].cplace !== null ) {
          this.semaines.forEach( s => {

            // determine si le jour et contenu dans cette semaine
            const sj = s.jours.find( ji => {
              return ji.anneeMois === km && ji.jour === kj;
            });
            if ( sj ) {
              s.class = 'select_week';
            }
          });
        }
      });
    });

    // mise a jour du nombre d'heures
    this.updateHours();
  }

  // deplacement d'un cours
  deplacementCours(c) {
    this.unselectCours();
    c.show = true;

    // pour tous les mois
    const mois = Object.assign( {}, this.mois );
    Object.keys(mois).forEach( km => {

      // pour tous les jours du mois
      Object.keys(mois[km].jours).forEach( kj => {

        // determine si le jour contient le cours a deplacer
        if ( mois[km].jours[kj].cplace != null &&
            mois[km].jours[kj].cplace.codeLieu.toString() === c.codeLieu.toString() &&
            (
              ( c.indep && mois[km].jours[kj].cplace.id === c.id )
              ||
              ( !c.indep &&
              mois[km].jours[kj].cplace.idCours === c.idCours &&
              mois[km].jours[kj].cplace.codeFormation === c.codeFormation )
            ) ) {

          mois[km].jours[kj].cplace = null;

          // recherche la semaine concernee
          this.semaines.forEach( s => {

            // determine si le jour et contenu dans cette semaine
            const sj = s.jours.find( ji => {
              return ji.anneeMois === km && ji.jour === kj;
            });
            if ( sj ) {
              s.class = 'select_empty';
            }
          });
        }
      });
    });
    this.mois = mois;

    // mise a jour du nombre d'heures
    this.updateHours();
  }

  // actualise le nombre d'heure total
  updateHours() {

    // pour tous les jours contenant des cours places
    let iNbrHours = 0;
    this.totalHeureNotification = '';
    Object.keys(this.mois).forEach( km => {
      Object.keys(this.mois[km].jours).forEach( kj => {
        if ( this.mois[km].jours[kj].cplace != null ) {
          iNbrHours += 7;
        }
      });
    });
    if ( iNbrHours === 0 ) {
      return;
    }
    this.totalHeureNotification = "Nombre d'heures : " + iNbrHours + "h";
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

  // recupere la description d'un cours pour le tootip
  getDescCours( c, bIndep ) {
    let sDesc = ( bIndep ? c.longName : c.libelleModule ) + ', ';
    const lieu = this.lieux.find( l => l.codeLieu.toString() === c.codeLieu.toString() );
    if ( !bIndep ) {
      sDesc += this.formations.find( f => f.codeFormation === c.codeFormation ).libelleFormation + ', ';
    }
    return sDesc + lieu.libelle;
  }

  // click sur une semaine
  clickSemaine( s ) {
    document.getElementsByName( s.anchor )[ 0 ].scrollIntoView();
    window.scrollTo( 0, window.scrollY - 60 );
  }

  // evenement d'information, sur le click d'un cours
  onClick( e ) {

    // si c'est un click sur un element pris en charge
    if ( [ 'badge cours_pos', 'select_empty', 'select_week', 'select_week_empty',
      'select_week_info' ].indexOf( e.target.className ) !== -1 ) {
      return;
    }

    // deselection des cours
    this.unselectCours();

    // desactive la propagation de l'evenement
    e.stopPropagation();
  }

  // selection d'un cours
  selectCours( c, bIndep ) {
    if ( this.messageNotification !== '' ) {
      this.unselectCours();
    }

    // recupere les promotions du cours
    this.navmodPromotions = c.promotions;

    // si c'est un cours independant
    if ( bIndep ) {
      this.messageNotification = this.getDescCours( c, true );
      return;
    }
    this.messageNotification = c.libelleModule + ', ' + c.libelleFormationLong;

    // pour tous les cours similaires
    const mois = Object.assign( {}, this.mois );
    Object.keys(mois).forEach( km => {

      // pour tous les jours du mois
      Object.keys(mois[km].jours).forEach( kj => {

        // ce jour contient il des cours similaires ?
        const coursPresent = mois[km].jours[kj].cours.find( ji => {
          return ji.idModule === c.idModule;
        });

        // determine si le jour contient le cours a deplacer
        if ( coursPresent || ( mois[km].jours[kj].cplace != null && mois[km].jours[kj].cplace.idModule === c.idModule ) ) {
          mois[km].jours[kj].color = "#f4ac41";

          // recherche la semaine concernee
          this.semaines.forEach( s => {

            // determine si le jour et contenu dans cette semaine
            const sj = s.jours.find( ji => {
              return ji.anneeMois === km && ji.jour === kj;
            });
            if ( sj && s.class !== 'select_week_info' ) {
              s.hiddenClass = s.class;
              s.class = 'select_week_info';
            }
          });
        }
      });
    });
    this.mois = mois;
  }

  // deselection d'un cours
  unselectCours() {
    if ( this.messageNotification === '' ) {
      return;
    }
    this.messageNotification = '';
    this.navmodPromotions = [];

    // pour tous les cours similaires
    const mois = Object.assign( {}, this.mois );
    Object.keys(mois).forEach( km => {

      // pour tous les jours du mois
      Object.keys(mois[km].jours).forEach( kj => {

        // determine la couleur de fond
        mois[km].jours[kj].color = ( mois[km].jours[kj].cours.length + mois[km].jours[kj].coursIndependants.length ) > 0
          || mois[km].jours[kj].cplace != null ? "#ffffff" : "#e8e8e8";
      });
    });
    this.mois = mois;

    // pour toutes les semaines;
    this.semaines.forEach( s => {
      if ( s.hiddenClass !== '' ) {
        s.class = s.hiddenClass;
        s.hiddenClass = '';
      }
    });
  }

  // ajout d'un promotion dans le filtre
  addPromotionFilter( p ) {
    const pf = this.navmodPromotionsFilter.find( pfi => pfi.codePromotion === p.codePromotion );
    if ( !pf ) {
      this.navmodPromotionsFilter.push( p );
    }
  }

  // supprime une promotion du filtre
  delPromotionFilter( p ) {
    const navmodPromotionsFilter = [];
    this.navmodPromotionsFilter.forEach( pf => {
      if ( pf.codePromotion !== p.codePromotion ) {
        navmodPromotionsFilter.push( pf );
      }
    } );
    this.navmodPromotionsFilter = navmodPromotionsFilter;
  }

  // determine si un cours contient un element du filtre des promotions
  coursInPromotionFilter( c ) {
    if ( !c.promotions ) {
      return false;
    }
    let asPromotion = false;
    c.promotions.forEach( cp => {
      if ( this.navmodPromotionsFilter.find( pfi => pfi.codePromotion === cp.codePromotion ) ) {
        asPromotion = true;
        return;
      }
    });
    return asPromotion;
  }

  // determine si une semaine contient un cour
  semaineInPromotionFilter( s ) {
    let asPromotion = false;
    s.promotions.forEach( sp => {
      if ( !asPromotion && this.navmodPromotionsFilter.find( pfi => pfi.codePromotion === sp.codePromotion ) ) {
        asPromotion = true;
        return;
      }
    });
    return asPromotion;
  }

  // demande d'enregistrement du calendrier
  demandeEnregistrer() {

    // recupere l'ensemble des messages
    const oMessages = [];
    const oLstCoursPlaces = [];
    let bError = false;

    // pour tous les mois
    Object.keys(this.mois).forEach( km => {

      // pour tous les jours du mois
      Object.keys(this.mois[km].jours).forEach( kj => {

        // si le cours est place
        if ( this.mois[km].jours[kj].cplace != null ) {
          oLstCoursPlaces.push( this.mois[km].jours[kj].cplace );
        }
      });
    });

    // si il y a au mois un cours de positionne
    if ( oLstCoursPlaces.length === 0 ) {
      oMessages.push( {"type": "error", "text": "vous devez renseigner au moins 1 cours" } );
      bError = true;
    }

    // determine si il y a des pre-requis
    this.moduleWithRequirement.forEach( m => {

      // determine si un cours est lie au module pre-requis
      const cp = oLstCoursPlaces.find( c => {
        return !c.indep && m.moduleId.toString() === c.idModule.toString();
      });

      // determine si au mois une ligne de validation existe
      if ( cp ) {
        let bValidationLigne = true;
        for ( let iV = 0; iV < m.validation.length; iV++ ) {
          const v = m.validation[ iV ];
          bValidationLigne = true;
          for ( let iVM = 0; iVM < v.modules.length; iVM++ ) {
            const vm = v.modules[ iVM ];

              // determine si le module pre-requis est la selection des cours
              const mInSelection = oLstCoursPlaces.find( c => {
                return vm.moduleId.toString() === c.idModule.toString();
              });
              if ( !mInSelection ) {
                bValidationLigne = false;
                break;
              }
          }
          if ( bValidationLigne ) {
            break;
          }
        }
        if ( !bValidationLigne ) {
          oMessages.push( {"type": "info", "text": "pré-requis non valide pour : " + this.getOrLoadDescModule( cp.idModule.toString() ) } );
        }
      }
    });

    // determine si il y a des cours repartis et non lies qui ne sont pas places
    const oCoursPlacesHeures = [];
    oLstCoursPlaces.forEach( c => {
      if ( !(c.libelleCours in oCoursPlacesHeures) ) {
        oCoursPlacesHeures[ c.libelleCours ] = 0;
      }
      oCoursPlacesHeures[ c.libelleCours ] += 7;
    });

    // recherche si il existe des cours non places comprenant plus d'heures
    this.objectKeys( oCoursPlacesHeures ).forEach( libelleCours => {

      // pour tous les cours similaires
      let bCoursPlusHeures = false;
      Object.keys(this.mois).forEach( km => {
        if ( bCoursPlusHeures ) {
          return;
        }

        // pour tous les jours du mois
        Object.keys(this.mois[km].jours).forEach( kj => {
          if ( bCoursPlusHeures ) {
            return;
          }

          // si c'est le meme cours avec plus d'heures
          const coursPresent = this.mois[km].jours[kj].cours.find( ji => {
            return ji.libelleCours === libelleCours && ji.dureeReelleEnHeures > oCoursPlacesHeures[ libelleCours ];
          });
          if ( coursPresent && !bCoursPlusHeures ) {
            bCoursPlusHeures = true;
          }
        });
      });
      if ( bCoursPlusHeures ) {
        oMessages.push( {"type": "info",
          "text": "le cours suivant est-il complet ? " + libelleCours } );
      }
    });

    // si il y a des messages d'avertissement
    if ( oMessages.length > 0 ) {
      let sMessageInfo = "";
      let sMessageError = "";
      oMessages.forEach( m => {
        if (m["type"] === "error" ) {
          sMessageError += "\t- " + m.text + "\n";
        } else if (m["type"] === "info" ) {
          sMessageInfo += "\t- " + m.text + "\n";
        }
      });

      // message d'alerte ou de demande de confirmation
      const fAfficheM = bError ? alert : confirm;
      const bDialog = fAfficheM( ( sMessageInfo !== "" ? "Message d'information :\n" + sMessageInfo : "" ) +
       ( sMessageError !== "" ? "Message d'erreur :\n" + sMessageError : "" ) +
        ( !bError ? "\nVoulez vous continuer ?" : "" ) );
      if ( bError || !bDialog ) {
        return;
      }
    }

    // recupere les cours et les modules independants positionnes
    const oLstCoursSave = [];
    const oLstIndepSave = [];
    oLstCoursPlaces.forEach( c => {
      if ( !c.indep && !oLstCoursSave.find( idCours => idCours === c.idCours ) ) {
        oLstCoursSave.push( c.idCours );
      } else if ( c.indep && !oLstIndepSave.find( id => id === c.id ) ) {
        oLstIndepSave.push( c.id );
      }
    });

    this.afficher = false;
    this.registerError = null;
    // enregistrement du calendrier
    const body = new CalendatrCoursModel();
    body.coursIds = oLstCoursSave;
    body.coursIndependantIds = oLstIndepSave;
    this.calendarService.addCoursToCalendar( this.id, body ).subscribe(
      () => {

        // redirection vers le detail du calendrier
        this.router.navigate(['/calendar/' + this.id + '/details']);
        return;
      },
      err => {
        console.error(err);
        this.registerError = err.error;
        this.afficher = true;
      });
  }

  // traitement des redimentionnements
  onResizeWindow(e) {

    // determine la taille d'un element
    const iSizeOneWeek = ( window.innerHeight - 250 ) / this.semaines.length;
    const divs = document.querySelectorAll("div.select_empty, div.select_week, div.select_week_empty, div.select_week_info");
    for ( let i = 0; i < divs.length; i++ ) {
      divs[ i ][ "style" ].height = iSizeOneWeek + "px";
    }
  }

  getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }
}
