import {Component, OnInit} from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ModelsService} from '../../services/models.service';
import {LieuService} from "../../services/lieu.service";
import {EntrepriseService} from "../../services/entreprise.service";
import {StagiaireService} from "../../services/stagiaire.service";
import {AddElementComponent} from "../modal/add-element/add-element.component";
import {DispenseElementComponent} from "../modal/dispense-element/dispense-element.component";
import {Router} from "@angular/router";
import {CalendarService} from "../../services/calendar.service";
import {CalendarModel} from "../../models/calendar.model";
import {ConstraintTypes} from "../../models/enums/constraint-types";

@Component({
  selector: 'app-propose-calendar',
  templateUrl: './page-calendar-proposal.component.html',
  styleUrls: ['./page-calendar-proposal.component.scss']
})
export class PageCalendarProposalComponent implements OnInit {
  models = [];
  lieux = [];
  allStagiaires = [];
  allEntreprises = [];
  stagiaires = [];
  entreprises = [];
  constraints = [];
  selectedModels = null;
  selectedDateDebut = null;
  selectedDateFin = null;
  selectedHeureMin = null;
  selectedEntreprise = null;
  selectedStagiaire = null;
  selectedHeureMax = null;
  selectedLieux = [];

  constructor(private modalService: NgbModal,
              private router: Router,
              private calendarService: CalendarService,
              private modelsService: ModelsService,
              private lieuService: LieuService,
              private entrepriseService: EntrepriseService,
              private stagiaireService: StagiaireService) { }

  ngOnInit() {
    this.models = this.modelsService.getModels();

    this.lieuService.getLieuxTeachningCourses().subscribe(
      res => {
        this.lieux = res;
      },
      err => {
        console.error(err);
      }
    );

    this.entrepriseService.getEntreprises().subscribe(
      res => {
        this.allEntreprises = res;
        this.entreprises = res;
      },
      err => {
        console.error(err);
      }
    );

    this.stagiaireService.getStagiaires().subscribe(
      res => {
        this.allStagiaires = res;
        this.stagiaires = res;
      },
      err => {
        console.error(err);
      }
    );
  }

  get formIsValid() {
    return this.selectedLieux.length > 0 && this.constraints.length > 0;
  }

  changeModels() {
    // console.log(this.selectedModels);
  }

  changeEntreprise() {
    if (this.selectedEntreprise === null) {
      this.stagiaires = this.allStagiaires;
    } else {
      this.stagiaires = [];
      this.entrepriseService.getStagiairesForEntreprise(this.selectedEntreprise).subscribe(
        res => {
          this.stagiaires = res;
        },
        err => {
          console.error(err);
        }
      );
    }
  }

  changeStagiaire() {
    if (this.selectedStagiaire === null) {
      this.entreprises = this.allEntreprises;
    } else {
      this.entreprises = [];

      this.stagiaireService.getEntreprisesForStagiaire(this.selectedStagiaire).subscribe(
        res => {
          this.entreprises = res;
        },
        err => {
          console.error(err);
        }
      );
    }
  }

  generateCalendar() {
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

    if (this.selectedModels) {
      // TODO: add constraint for model
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
      this.router.navigate(['/calendar/' + res.id + '/processing']);
    }, err => {
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
}