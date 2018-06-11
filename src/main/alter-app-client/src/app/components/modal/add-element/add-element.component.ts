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

  addElement() {
    this.add.emit({
      name: 'FORMATION',
      value: 'MS2I-SI7'
    });
    this.activeModal.dismiss('Cross click');
  }
}
