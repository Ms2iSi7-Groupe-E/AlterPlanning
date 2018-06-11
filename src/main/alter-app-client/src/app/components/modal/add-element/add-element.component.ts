import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {FormationService} from "../../../services/formation.service";
import {ModuleService} from "../../../services/module.service";
import {StagiaireService} from "../../../services/stagiaire.service";

@Component({
  selector: 'app-add-element',
  templateUrl: './add-element.component.html',
  styleUrls: ['./add-element.component.scss']
})
export class AddElementComponent implements OnInit {

  @Output() add = new EventEmitter<any>();

  private readonly AJOUT_FORMATION = 'AJOUT_FORMATION';
  private readonly AJOUT_MODULE = 'AJOUT_MODULE';
  private readonly AJOUT_PERIODE = 'AJOUT_PERIODE';
  private readonly EN_MEME_TEMPS_QUE = 'EN_MEME_TEMPS_QUE';

  formations = [];
  modules = [];
  stagiaires = [];

  selectedFormation = null;
  selectedModule = null;
  selectedStagiaire = null;
  selectedPeriodeDebut = null;
  selectedPeriodeFin = null;

  constructor(private activeModal: NgbActiveModal,
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

  addElement(type) {
    let value = null;

    switch (type) {
      case this.AJOUT_FORMATION:
        value = this.formations.find(f => f.codeFormation === this.selectedFormation);
        break;

      case this.AJOUT_MODULE:
        value = this.modules.find(m => m.idModule === this.selectedModule);
        break;

      case this.EN_MEME_TEMPS_QUE:
        value = this.stagiaires.find(s => s.codeStagiaire === this.selectedStagiaire);
        break;

      case this.AJOUT_PERIODE:
        value = {
          from: new Date(this.selectedPeriodeDebut.year, this.selectedPeriodeDebut.month - 1, this.selectedPeriodeDebut.day),
          to: new Date(this.selectedPeriodeFin.year, this.selectedPeriodeFin.month - 1, this.selectedPeriodeFin.day)
        };
        break;

      default:
        return;
    }

    this.add.emit({ type, value });

    this.activeModal.dismiss('Cross click');
  }
}
