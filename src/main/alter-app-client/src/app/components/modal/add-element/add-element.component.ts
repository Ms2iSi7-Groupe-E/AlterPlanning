import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {FormationService} from "../../../services/formation.service";
import {ModuleService} from "../../../services/module.service";
import {StagiaireService} from "../../../services/stagiaire.service";
import {ConstraintTypes} from "../../../models/enums/constraint-types";

@Component({
  selector: 'app-add-element',
  templateUrl: './add-element.component.html',
  styleUrls: ['./add-element.component.scss']
})
export class AddElementComponent implements OnInit {

  @Output() add = new EventEmitter<any>();

  CONSTRAINT_TYPE = ConstraintTypes;

  formations = [];
  modules = [];
  stagiaires = [];

  selectedFormation = null;
  selectedModule = null;
  selectedStagiaire = null;
  selectedPeriodeDebut = null;
  selectedPeriodeFin = null;

  constructor(public activeModal: NgbActiveModal,
              private formationService: FormationService,
              private moduleService: ModuleService,
              private stagiaireService: StagiaireService) {}

  ngOnInit() {
    this.formationService.getFormations().subscribe(res => {
        this.formations = res;
      },
      err => {
        console.error(err);
      });

    this.moduleService.getModules().subscribe(res => {
        this.modules = res;
      },
      err => {
        console.error(err);
      });

    this.stagiaireService.getStagiaires().subscribe(res => {
        this.stagiaires = res;
      },
      err => {
        console.error(err);
      });
  }

  addElement(type: ConstraintTypes) {
    let value = null;
    let title = null;

    switch (type) {
      case ConstraintTypes.AJOUT_FORMATION:
        value = this.formations.find(f => f.codeFormation === this.selectedFormation);
        title = 'Formation : ' + value.libelleLong + ' - ' + value.libelleCourt;
        break;

      case ConstraintTypes.AJOUT_MODULE:
        value = this.modules.find(m => m.idModule === this.selectedModule);
        title = 'Module : ' + value.libelleCourt + ' - ' + value.libelle;
        break;

      case ConstraintTypes.EN_MEME_TEMPS_QUE:
        value = this.stagiaires.find(s => s.codeStagiaire === this.selectedStagiaire);
        title = 'En même temps que : ' + value.prenom + ' ' + value.nom;
        break;

      case ConstraintTypes.AJOUT_PERIODE:
        value = {
          from: new Date(this.selectedPeriodeDebut.year, this.selectedPeriodeDebut.month - 1, this.selectedPeriodeDebut.day),
          to: new Date(this.selectedPeriodeFin.year, this.selectedPeriodeFin.month - 1, this.selectedPeriodeFin.day)
        };
        title = 'Période du ' + value.from.toLocaleDateString() + ' au ' + value.to.toLocaleDateString();
        break;

      default:
        return;
    }

    this.add.emit({
      type,
      value,
      title,
      cssClass: 'constraint-ajout'
    });

    this.activeModal.dismiss('Cross click');
  }
}