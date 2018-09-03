import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {FormationService} from "../../../services/formation.service";
import {ModuleService} from "../../../services/module.service";
import {StagiaireService} from "../../../services/stagiaire.service";
import {ConstraintTypes} from "../../../models/enums/constraint-types";
import {IndependantModuleService} from "../../../services/independant-module.service";

@Component({
  selector: 'app-add-element',
  templateUrl: './add-element.component.html',
  styleUrls: ['./add-element.component.scss']
})
export class AddElementComponent implements OnInit {

  @Output() add = new EventEmitter<any>();

  CONSTRAINT_TYPE = ConstraintTypes;

  formations = [];
  allModules = [];
  modules = [];
  independantModules = [];
  stagiaires = [];

  selectedFormation = null;
  selectedModule = null;
  selectedStagiaire = null;
  selectedPeriodeDebut = null;
  selectedPeriodeFin = null;

  constructor(public activeModal: NgbActiveModal,
              private formationService: FormationService,
              private moduleService: ModuleService,
              private independantModuleService: IndependantModuleService,
              private stagiaireService: StagiaireService) {}

  ngOnInit() {
    this.formationService.getFormations().subscribe(res => this.formations = res, console.error);
    this.moduleService.getModules().subscribe(res => {
      this.modules = res;
      this.independantModuleService.getCours().subscribe(res1 => {
        this.independantModules = res1;
        this.concactAllModules();
      }, console.error);
    }, console.error);
    this.stagiaireService.getStagiaires().subscribe(res => this.stagiaires = res, console.error);
  }

  addElement(type: ConstraintTypes) {
    let value = null;
    let title = null;
    let t = type;

    switch (type) {
      case ConstraintTypes.AJOUT_FORMATION:
        const formation = this.formations.find(f => f.codeFormation === this.selectedFormation);
        value = this.selectedFormation;
        title = 'Formation : ' + formation.libelleLong + ' - ' + formation.libelleCourt;
        break;

      case ConstraintTypes.AJOUT_MODULE: {
        if (this.selectedModule.isIndependantModule) {
          t = ConstraintTypes.AJOUT_MODULE_INDEPENDANT;
        }
        value = this.selectedModule.id;
        title = 'Module : ' + this.selectedModule.shortName + ' - ' + this.selectedModule.longName;
        break;
      }

      case ConstraintTypes.EN_MEME_TEMPS_QUE:
        const stagiaire = this.stagiaires.find(s => s.codeStagiaire === this.selectedStagiaire);
        value = this.selectedStagiaire;
        title = 'En même temps que : ' + stagiaire.prenom + ' ' + stagiaire.nom;
        break;

      case ConstraintTypes.AJOUT_PERIODE:
        const periode = {
          from: new Date(this.selectedPeriodeDebut.year, this.selectedPeriodeDebut.month - 1, this.selectedPeriodeDebut.day),
          to: new Date(this.selectedPeriodeFin.year, this.selectedPeriodeFin.month - 1, this.selectedPeriodeFin.day)
        };
        value = periode.from.toLocaleDateString() + ' - ' + periode.to.toLocaleDateString();
        title = 'Période du ' + periode.from.toLocaleDateString() + ' au ' + periode.to.toLocaleDateString();
        break;

      default:
        return;
    }

    this.add.emit({
      type: t,
      value,
      title,
      cssClass: 'constraint-ajout'
    });

    this.activeModal.dismiss('Cross click');
  }

  changeModule(e) {
    this.selectedModule = e;
  }

  concactAllModules () {
    const am = this.modules.concat(this.independantModules);
    this.allModules = am.map(mod => {
      const obj = {};
      obj['id'] = "idModule" in mod ? mod.idModule : mod.id;
      obj['isIndependantModule'] = !("idModule" in mod);
      obj['shortName'] = "idModule" in mod ? mod.libelleCourt : mod.shortName;
      obj['longName'] = "idModule" in mod ? mod.libelle : mod.longName;
      return obj;
    });
  }
}
