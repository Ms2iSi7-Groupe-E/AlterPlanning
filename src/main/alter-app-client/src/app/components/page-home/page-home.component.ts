import {Component, OnInit} from '@angular/core';
import {ModelsService} from '../../services/models.service';
import {LieuService} from "../../services/lieu.service";
import {EntrepriseService} from "../../services/entreprise.service";
import {StagiaireService} from "../../services/stagiaire.service";

@Component({
  selector: 'app-home',
  templateUrl: './page-home.component.html',
  styleUrls: ['./page-home.component.scss']
})
export class PageHomeComponent implements OnInit {
  models = [];
  lieux = [];
  allStagiaires = [];
  allEntreprises = [];
  stagiaires = [];
  entreprises = [];
  selectedModels = null;
  selectedDateDebut = null;
  selectedDateFin = null;
  selectedHeureMin = null;
  selectedEntreprise = null;
  selectedStagiaire = null;
  selectedHeureMax = null;
  selectedLieux = [];

  constructor(private modelsService: ModelsService,
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
    console.log('entreprise', this.selectedEntreprise);
    console.log('stagiaire', this.selectedStagiaire);
    console.log('model', this.selectedModels);
    console.log('lieux', this.selectedLieux);
    console.log('dateDebut', this.selectedDateDebut);
    console.log('dateFin', this.selectedDateFin);
    console.log('heureMin', this.selectedHeureMin);
    console.log('heureMax', this.selectedHeureMax);
  }
}
