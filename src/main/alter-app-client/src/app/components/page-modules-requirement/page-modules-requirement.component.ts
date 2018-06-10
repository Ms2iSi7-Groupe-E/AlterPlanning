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
  // concernant la partie des sources
  sourceCodeTitre;
  sourceFormation;
  sourceModule = '';
  sourcesModules = [];
  sourceSelected;
  sourceIdsModulesTitreFiltre = [];
  // concernant la partie des cibles
  targetCodeTitre;
  targetFormation;
  targetModule = '';
  targetModules = [];
  targetSelected;
  sourceIdsModulesFomationFiltre = [];

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

  // sur la selection d'un filtre
  selectTitreSource() {
    this.sourceIdsModulesTitreFiltre = [];

    // si aucun titre n'est selectionne
    if (this.sourceCodeTitre == null) {
      this.findModuleSource();
      return;
    }

    // recupere la liste des formation d'un titre
    this.titreService.getFormations(this.sourceCodeTitre).subscribe(
      res => {

        // recherche de tous les modules associes a chaque formation
        for (let i = 0; i < res.length; ++i) {
          this.formationService.getModules(res[i].codeFormation).subscribe(
            resM => {

              // ajout des modules au filtres
              for (let a = 0; a < resM.length; ++a) {
                if (this.sourceIdsModulesTitreFiltre.indexOf(resM[a].idModule) === - 1) {
                  this.sourceIdsModulesTitreFiltre.push(resM[a].idModule);
                }
              }

              // pour la derniere recherche
              if ( i === res.length - 1 ) {
                this.findModuleSource();
              }
            },
            errM => {
              console.error(errM);
            }
          );
        }
      },
      err => {
        console.error(err);
      }
    );
  }

  // sur la selection d'une formation
  selectFormationSource() {
    this.sourceIdsModulesFomationFiltre = [];

    // si aucune formation n'est selectionnee
    if (this.sourceFormation == null) {
      this.findModuleSource();
      return;
    }

    this.formationService.getModules(this.sourceFormation).subscribe(
      res => {

        // ajout des modules au filtres
        for (let a = 0; a < res.length; ++a) {
          if (this.sourceIdsModulesFomationFiltre.indexOf(res[a].idModule) === - 1) {
            this.sourceIdsModulesFomationFiltre.push(res[a].idModule);
          }
        }

        this.findModuleSource();
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

    // filtre les resultats a afficher
    let showRes: any[];
    showRes = [];
    for (const item of this.dataModules) {

      // filtre sur le libelle du module
      if ( this.sourceModule.length > 1 && item.libelle.toLowerCase().indexOf( this.sourceModule.toLowerCase() ) === -1 ) {
        continue;
      }

      // si il y a un filtre sur les modules d'un titre
      if ( this.sourceIdsModulesTitreFiltre.length > 0 && this.sourceIdsModulesTitreFiltre.indexOf(item.idModule) === - 1 ) {
        continue;
      }

      // si il y a un filtre sur les modules d'une formation
      if ( this.sourceIdsModulesFomationFiltre.length > 0 && this.sourceIdsModulesFomationFiltre.indexOf(item.idModule) === - 1 ) {
        continue;
      }

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
