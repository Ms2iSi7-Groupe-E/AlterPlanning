import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {StagiaireService} from "../../../services/stagiaire.service";
import {ModuleService} from "../../../services/module.service";
import {ConstraintTypes} from "../../../models/enums/constraint-types";

@Component({
  selector: 'app-dispense-element',
  templateUrl: './dispense-element.component.html',
  styleUrls: ['./dispense-element.component.scss']
})
export class DispenseElementComponent implements OnInit {

  @Output() dispense = new EventEmitter<any>();

  CONSTRAINT_TYPE = ConstraintTypes;

  modules = [];
  stagiaires = [];

  selectedModule = null;
  selectedStagiaire = null;
  selectedPeriodeDebut = null;
  selectedPeriodeFin = null;

  constructor(public activeModal: NgbActiveModal,
              private moduleService: ModuleService,
              private stagiaireService: StagiaireService) {}

  ngOnInit() {
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

  dispenseElement(type: ConstraintTypes) {
    let value = null;
    let title = null;

    switch (type) {
      case ConstraintTypes.DISPENSE_MODULE:
        value = this.modules.find(m => m.idModule === this.selectedModule);
        title = 'Dispense de module : ' + value.libelleCourt + ' - ' + value.libelle;
        break;

      case ConstraintTypes.PAS_EN_MEME_TEMPS_QUE:
        value = this.stagiaires.find(s => s.codeStagiaire === this.selectedStagiaire);
        title = 'Pas en même temps que : ' + value.prenom + ' ' + value.nom;
        break;

      case ConstraintTypes.DISPENSE_PERIODE:
        value = {
          from: new Date(this.selectedPeriodeDebut.year, this.selectedPeriodeDebut.month - 1, this.selectedPeriodeDebut.day),
          to: new Date(this.selectedPeriodeFin.year, this.selectedPeriodeFin.month - 1, this.selectedPeriodeFin.day)
        };
        title = 'Dispense de la période du ' + value.from.toLocaleDateString() + ' au ' + value.to.toLocaleDateString();
        break;

      default:
        return;
    }

    this.dispense.emit({
      type,
      value,
      title,
      cssClass: 'constraint-dispense'
    });

    this.activeModal.dismiss('Cross click');
  }
}