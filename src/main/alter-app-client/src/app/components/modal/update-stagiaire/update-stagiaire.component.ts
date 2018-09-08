import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {StagiaireService} from "../../../services/stagiaire.service";
import {EntrepriseService} from "../../../services/entreprise.service";

@Component({
  selector: 'app-update-stagiaire',
  templateUrl: './update-stagiaire.component.html',
  styleUrls: ['./update-stagiaire.component.scss']
})
export class UpdateStagiaireComponent implements OnInit {

  @Output() validate = new EventEmitter<any>();
  @Input() codeEntreprise = null;
  @Input() codeStagiaire = null;

  stagiaires = [];
  selectedStagiaire;

  constructor(public activeModal: NgbActiveModal,
              private stagiaireService: StagiaireService,
              private entrepriseService: EntrepriseService) {}

  ngOnInit() {
    this.selectedStagiaire = this.codeStagiaire;
    if (this.codeEntreprise === null) {
      this.stagiaireService.getStagiaires().subscribe(res => {
        this.stagiaires = res;
        this.stagiaires.unshift({
          codeStagiaire: '',
          prenom: 'A définir'
        });
      }, console.error);
    } else {
      this.entrepriseService.getStagiairesForEntreprise(this.codeEntreprise).subscribe(res => {
        this.stagiaires = res;
        this.stagiaires.unshift({
          codeStagiaire: '',
          prenom: 'A définir'
        });
      }, console.error);
    }
  }

  clickOnValid() {
    this.selectedStagiaire = this.selectedStagiaire === '' ? null : this.selectedStagiaire;
    this.validate.emit({ stagiaire: this.selectedStagiaire });
    this.activeModal.dismiss('Cross click');
  }

}
