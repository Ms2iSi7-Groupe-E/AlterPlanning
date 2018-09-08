import {Component, OnInit} from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {LieuService} from "../../services/lieu.service";
import {EntrepriseService} from "../../services/entreprise.service";
import {StagiaireService} from "../../services/stagiaire.service";
import {AddElementComponent} from "../modal/add-element/add-element.component";
import {DispenseElementComponent} from "../modal/dispense-element/dispense-element.component";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {CalendarService} from "../../services/calendar.service";
import {CalendarModel} from "../../models/calendar.model";
import {ConstraintTypes} from "../../models/enums/constraint-types";
import {CalendarModelService} from "../../services/calendar-model.service";
import {FormationService} from "../../services/formation.service";
import {ModuleService} from "../../services/module.service";
import {IndependantModuleService} from "../../services/independant-module.service";

@Component({
  selector: 'app-propose-calendar',
  templateUrl: './page-calendar-proposal.component.html',
  styleUrls: ['./page-calendar-proposal.component.scss']
})
export class PageCalendarProposalComponent implements OnInit {
  error = null;
  models = [];
  lieux = [];
  allStagiaires = [];
  allEntreprises = [];
  stagiaires = [];
  entreprises = [];
  constraints = [];
  selectedModel = null;
  selectedDateDebut = null;
  selectedDateFin = null;
  selectedHeureMin = null;
  selectedEntreprise = null;
  selectedStagiaire = null;
  selectedHeureMax = null;
  selectedLieux = [];

  CONSTRAINT_TYPE = ConstraintTypes;

  searching = false;

  constructor(private modalService: NgbModal,
              private router: Router,
              private route: ActivatedRoute,
              private calendarService: CalendarService,
              private lieuService: LieuService,
              private entrepriseService: EntrepriseService,
              private calendarModelService: CalendarModelService,
              private formationService: FormationService,
              private moduleService: ModuleService,
              private independantModuleService: IndependantModuleService,
              private stagiaireService: StagiaireService) { }

  ngOnInit() {
    this.lieuService.getLieuxTeachningCourses().subscribe(res => {
      this.lieux = res;
    }, console.error);

    this.entrepriseService.getEntreprises().subscribe(res => {
      this.allEntreprises = res;
      this.entreprises = res;
    }, console.error);

    this.stagiaireService.getStagiaires().subscribe(res => {
      this.allStagiaires = res;
      this.stagiaires = res;
    }, console.error);

    this.calendarModelService.getModels().subscribe(res => {
      this.models = res;
    }, console.error);

    const queryParams: Params = Object.assign({}, this.route.snapshot.queryParams);
    const calendarId = queryParams.fromCalendar ? queryParams.fromCalendar : null;

    if (calendarId !== null) {
      this.calendarService.getCalendar(calendarId).subscribe(res => {
        this.completeFormsFromCalendar(res);
      }, console.error);
    }
  }

  get formIsValid() {
    return this.selectedLieux.length > 0 && this.constraints.length > 0;
  }

  completeFormsFromCalendar(calendar) {
    if (calendar.startDate) {
      const dateDeb = new Date(calendar.startDate);
      this.selectedDateDebut = !dateDeb ? null : {
        year: dateDeb.getFullYear(),
        month: dateDeb.getMonth() + 1,
        day: dateDeb.getDate()
      };
    }

    if (calendar.endDate) {
      const dateFin = new Date(calendar.endDate);
      this.selectedDateFin = !dateFin ? null : {
        year: dateFin.getFullYear(),
        month: dateFin.getMonth() + 1,
        day: dateFin.getDate()
      };
    }

    if (calendar.stagiaire) {
      this.selectedStagiaire = calendar.stagiaire.codeStagiaire;
      this.changeStagiaire();
    }

    if (calendar.entreprise) {
      this.selectedEntreprise = calendar.entreprise.codeEntreprise;
      this.changeEntreprise();
    }

    calendar.constraints.forEach(c => {
      const type = c.constraintType;
      const value = c.constraintValue;

      if (type === this.CONSTRAINT_TYPE.LIEUX) {
        this.selectedLieux = [...this.selectedLieux, parseInt(value, 10)];
      }

      if (type === this.CONSTRAINT_TYPE.HEURES_MIN) {
        this.selectedHeureMin = parseInt(value, 10);
      }

      if (type === this.CONSTRAINT_TYPE.HEURES_MAX) {
        this.selectedHeureMax = parseInt(value, 10);
      }

      if (type === this.CONSTRAINT_TYPE.AJOUT_FORMATION) {
        this.formationService.getFormation(value).subscribe(res => {
          const title = 'Formation : ' + res.libelleLong + ' - ' + res.libelleCourt;
          this.addConstraintAddElement(type, value, title);
        }, console.error);
      }

      if (type === this.CONSTRAINT_TYPE.AJOUT_MODULE) {
        this.moduleService.getModuleById(value).subscribe(res => {
          const title = 'Module : ' + res.libelleCourt + ' - ' + res.libelle;
          this.addConstraintAddElement(type, value, title);
        }, console.error);
      }

      if (type === this.CONSTRAINT_TYPE.AJOUT_MODULE_INDEPENDANT) {
        this.independantModuleService.getCoursById(value).subscribe(res => {
          const title = 'Module : ' + res.shortName + ' - ' + res.longName;
          this.addConstraintAddElement(type, value, title);
        }, console.error);
      }

      if (type === this.CONSTRAINT_TYPE.AJOUT_PERIODE) {
        const splitDate = value.split(' - ');
        const pattern = /(\d{2})\/(\d{2})\/(\d{4})/;
        const startDate = new Date(splitDate[0].replace(pattern, '$3-$2-$1'));
        const endDate = new Date(splitDate[1].replace(pattern, '$3-$2-$1'));
        const title = 'Période du ' + startDate.toLocaleDateString() + ' au ' + endDate.toLocaleDateString();
        this.addConstraintAddElement(type, value, title);
      }

      if (type === this.CONSTRAINT_TYPE.DISPENSE_PERIODE) {
        const splitDate = value.split(' - ');
        const pattern = /(\d{2})\/(\d{2})\/(\d{4})/;
        const startDate = new Date(splitDate[0].replace(pattern, '$3-$2-$1'));
        const endDate = new Date(splitDate[1].replace(pattern, '$3-$2-$1'));
        const title = 'Dispense de la période du ' + startDate.toLocaleDateString() + ' au ' + endDate.toLocaleDateString();
        this.addConstraintDispenseElement(type, value, title);
      }

      if (type === this.CONSTRAINT_TYPE.DISPENSE_MODULE) {
        this.moduleService.getModuleById(value).subscribe(res => {
          const title = 'Dispense de module : ' + res.libelleCourt + ' - ' + res.libelle;
          this.addConstraintDispenseElement(type, value, title);
        }, console.error);
      }
    });
  }

  changeModels() {
    console.log(this.selectedModel);
    // TODO
  }

  changeEntreprise() {
    if (this.selectedEntreprise === null) {
      this.stagiaires = this.allStagiaires;
    } else {
      this.stagiaires = [];
      this.entrepriseService.getStagiairesForEntreprise(this.selectedEntreprise).subscribe(res => {
        this.stagiaires = res;
      }, console.error);
    }
  }

  changeStagiaire() {
    if (this.selectedStagiaire === null) {
      this.entreprises = this.allEntreprises;
    } else {
      this.entreprises = [];

      this.stagiaireService.getEntreprisesForStagiaire(this.selectedStagiaire).subscribe(res => {
        this.entreprises = res;
      }, console.error);
    }
  }

  generateCalendar() {
    this.searching = true;
    this.error = null;
    const calendarModel = new CalendarModel();
    calendarModel.stagiaireId = this.selectedStagiaire;
    calendarModel.entrepriseId = this.selectedEntreprise;

    const startDate = !this.selectedDateDebut ? null :
      new Date(this.selectedDateDebut.year, this.selectedDateDebut.month - 1, this.selectedDateDebut.day);
    const endDate = !this.selectedDateFin ? null :
      new Date(this.selectedDateFin.year, this.selectedDateFin.month - 1, this.selectedDateFin.day);

    calendarModel.startDate = startDate;
    calendarModel.endDate = endDate;

    this.selectedLieux.forEach(codeLieu => {
      calendarModel.constraints.push({
        type: ConstraintTypes.LIEUX,
        value: codeLieu
      });
    });

    if (this.selectedModel) {
      // TODO
      calendarModel.constraints.push({
        type: ConstraintTypes.A_PARTIR_DE_MODELE,
        value: this.selectedModel
      });
    }

    if (this.selectedHeureMin) {
      calendarModel.constraints.push({
        type: ConstraintTypes.HEURES_MIN,
        value: this.selectedHeureMin
      });
    }

    if (this.selectedHeureMax) {
      calendarModel.constraints.push({
        type: ConstraintTypes.HEURES_MAX,
        value: this.selectedHeureMax
      });
    }

    this.constraints.forEach(c => {
      calendarModel.constraints.push({
        type: c.type,
        value: c.value
      });
    });

    this.calendarService.addCalendar(calendarModel).subscribe(res => {
      this.searching = false;
      this.router.navigate(['/calendar/' + res.id + '/processing']);
    }, err => {
      this.searching = false;
      this.error = 'Une erreur est survenue, contacter votre administrateur.';
      console.error(err);
    });
  }

  openModalAddElement() {
    const modalRef = this.modalService.open(AddElementComponent, { size: 'lg' });
    modalRef.componentInstance.add.subscribe(res => this.constraints.push(res));
  }

  openModalDispenseElement() {
    const modalRef = this.modalService.open(DispenseElementComponent, { size: 'lg' });
    modalRef.componentInstance.dispense.subscribe(res => this.constraints.push(res));
  }

  clearAllConstraints() {
    this.constraints = [];
  }

  removeConstraint(index) {
    this.constraints.splice(index, 1);
  }

  addConstraintAddElement(type, value, title,) {
    this.addConstraint(type, value, title, 'constraint-ajout');
  }

  addConstraintDispenseElement(type, value, title,) {
    this.addConstraint(type, value, title, 'constraint-dispense');
  }

  addConstraint(type, value, title, cssClass) {
    this.constraints.push({
      type,
      value,
      title,
      cssClass
    });
  }
}
