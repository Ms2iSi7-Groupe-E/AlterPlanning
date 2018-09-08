import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {StagiaireService} from "../../../services/stagiaire.service";
import {EntrepriseService} from "../../../services/entreprise.service";

@Component({
  selector: 'app-update-entreprise',
  templateUrl: './update-entreprise.component.html',
  styleUrls: ['./update-entreprise.component.scss']
})
export class UpdateEntrepriseComponent implements OnInit {

  @Output() validate = new EventEmitter<any>();
  @Input() codeEntreprise = null;
  @Input() codeStagiaire = null;

  entreprises = [];
  selectedEntreprise;

  constructor(public activeModal: NgbActiveModal,
              private stagiaireService: StagiaireService,
              private entrepriseService: EntrepriseService) {}

  ngOnInit() {
    this.selectedEntreprise = this.codeEntreprise;
    if (this.codeStagiaire === null) {
      this.entrepriseService.getEntreprises().subscribe(res => {
        this.entreprises = res;
        this.entreprises.unshift({
          codeEntreprise: null,
          raisonSociale: 'A définir',
        });
      }, console.error);
    } else {
      this.stagiaireService.getEntreprisesForStagiaire(this.codeStagiaire).subscribe(res => {
        this.entreprises = res;
        this.entreprises.unshift({
          codeEntreprise: null,
          raisonSociale: 'A définir',
        });
      }, console.error);
    }
  }

  clickOnValid() {
    this.validate.emit({ entreprise: this.selectedEntreprise });
    this.activeModal.dismiss('Cross click');
  }

}
