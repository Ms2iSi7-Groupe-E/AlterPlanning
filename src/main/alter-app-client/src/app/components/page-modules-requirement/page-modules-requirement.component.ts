import { Component, OnInit } from '@angular/core';
import { ModuleService } from '../../services/module.service';
import { TitreService } from '../../services/titre.service';
import { FormationService } from '../../services/formation.service';

@Component({
  selector: 'app-page-modules-requirement',
  templateUrl: './page-modules-requirement.component.html',
  styleUrls: ['./page-modules-requirement.component.scss']
})
export class PageModulesRequirementComponent implements OnInit {
  dataModules = [];
  dataTitres = [];
  dataFormations = [];
  sourceCodeTitre = '';
  sourceFormation = '';
  sourceModule = '';
  sourcesModules = [];
  sourceSelected = null;
  targetCodeTitre = '';
  targetFormation = '';
  targetModule = '';
  targetModules = [];
  targetSelected = null;

  constructor(private moduleService: ModuleService, private titreService: TitreService, private formationService: FormationService) { }

  ngOnInit() {

    // recuperation de la liste des modules
    this.moduleService.getModules().subscribe(
      res => {
        this.dataModules = res;
        this.sourcesModules = res;
        this.targetModules = res;
      },
      err => {
        console.error(err);
      }
    );

    // recuperation de la liste des titres
    this.titreService.getTitres().subscribe(
      res => {
        this.dataTitres = res;
      },
      err => {
        console.error(err);
      }
    );

    // recuperation de la liste des formations
    this.formationService.getFormations().subscribe(
      res => {
        for (let i = 0; i < res.length; ++i) {
          res[i].libelleLong =  res[i].libelleLong + ' - ' + res[i].libelleCourt;
        }
        this.dataFormations = res;
      },
      err => {
        console.error(err);
      }
    );
  }

  selectTitreSource() {

    // recupere la liste des formation d'un titre
    this.titreService.getFormations(this.sourceCodeTitre).subscribe(
      res => {
        // this.dataTitres = res;
        console.log(res);
      },
      err => {
        console.error(err);
      }
    );
  }

  findModuleSource() {

    // controle de la taille des saisies
    if ( this.sourceCodeTitre === '' && this.sourceFormation.length < 1 && this.sourceModule.length < 1 ) {
      this.sourcesModules = this.dataModules;
      return;
    }

    // si il y a un titre de renseigne
    // recupere toutes le formation d'un titre
      // pour chaque formation, recuperer l'ensemble des modules

    // filtre les resultats a afficher
    let showRes: any[];
    showRes = [];
    for (const item of this.dataModules) {

      // filtre sur le libelle du module
      if ( this.sourceModule.length > 1 && item.libelle.toLowerCase().indexOf( this.sourceModule.toLowerCase() ) === -1 ) {
        continue;
      }

      // TODO: les autres criteres

      showRes.push(item);
    }
    this.sourcesModules = showRes;
  }

  filterTargetModules(modules) {
    let lstModules: any[];
    lstModules = [];
    for (const item of modules) {
      if ( this.sourceSelected != null && ( this.sourceSelected.idModule === item.idModule ) ) {
        continue;
      }
      lstModules.push(item);
    }
    return lstModules;
  }

  findModuleTarget() {

    // controle de la taille des saisies
    if ( this.targetCodeTitre === '' && this.targetFormation.length < 1 && this.targetModule.length < 1 ) {
      this.targetModules = this.dataModules;
      return;
    }

    // filtre les resultats a afficher
    let showRes: any[];
    showRes = [];
    for (const item of this.dataModules) {

      // filtre sur le libelle du module
      if ( this.targetModule.length > 1 && item.libelle.toLowerCase().indexOf( this.targetModule.toLowerCase() ) === -1 ) {
        continue;
      }

      // si c'est le module source
      if ( this.sourceSelected != null && this.sourceSelected.idModule === item.idModule ) {
        continue;
      }

      // TODO: les autres criteres

      showRes.push(item);
    }
    this.targetModules = showRes;
  }
}
