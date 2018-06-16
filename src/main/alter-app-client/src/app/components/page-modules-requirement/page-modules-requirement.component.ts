import { Component, OnInit } from '@angular/core';
import { ModuleService } from '../../services/module.service';
import { TitreService } from '../../services/titre.service';
import { FormationService } from '../../services/formation.service';
import { RequirementModel } from "../../models/requirement.model";

@Component({
  selector: 'app-page-modules-requirement',
  templateUrl: './page-modules-requirement.component.html',
  styleUrls: ['./page-modules-requirement.component.scss']
})
export class PageModulesRequirementComponent implements OnInit {
  // concernant les donnees de references
  dataModules = [];
  dataTitres = [];
  dataFormations = [];
  // concernant la partie des sources
  sourceCodeTitre;
  sourceFormation;
  sourceModule = '';
  sourcesModules = [];
  sourceSelected;
  sourceRequirementSelected = [];
  sourceIdsModulesTitreFiltre = [];
  sourceIdsModulesFomationFiltre = [];
  // concernant la partie des cibles
  targetCodeTitre;
  targetFormation;
  targetModule = '';
  targetModules = [];
  targetIdsModulesTitreFiltre = [];
  targetIdsModulesFomationFiltre = [];
  // concernant la liste des modules ayants des pre-requis
  moduleWithRequirement = [];
  moduleWithRequirementFiltre = [];
  requirementOrOperator = true;
  listeModule = '';
  listePrerequis = '';

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

    // recuperation de la liste des modules ayants des pre-requis
    this.updateModuleWithRequirement();
  }

  // mise a jour de la liste des modules ayants des pre-requis
  updateModuleWithRequirement() {
    this.moduleWithRequirement = [];
    this.moduleService.getModulesWithRequirement().subscribe(
      res => {
        this.moduleWithRequirement = res;
        this.findModuleListe();
      },
      err => {
        console.error(err);
      }
    );
  }

  // sur la selection d'un filtre source
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

  // sur la selection d'une formation source
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

  // sur la selection d'un filtre cible
  selectTitreTarget() {
    this.targetIdsModulesTitreFiltre = [];

    // si aucun titre n'est selectionne
    if (this.targetCodeTitre == null) {
      this.findModuleTarget();
      return;
    }

    // recupere la liste des formation d'un titre
    this.titreService.getFormations(this.targetCodeTitre).subscribe(
      res => {

        // recherche de tous les modules associes a chaque formation
        for (let i = 0; i < res.length; ++i) {
          this.formationService.getModules(res[i].codeFormation).subscribe(
            resM => {

              // ajout des modules au filtres
              for (let a = 0; a < resM.length; ++a) {
                if (this.targetIdsModulesTitreFiltre.indexOf(resM[a].idModule) === - 1) {
                  this.targetIdsModulesTitreFiltre.push(resM[a].idModule);
                }
              }

              // pour la derniere recherche
              if ( i === res.length - 1 ) {
                this.findModuleTarget();
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

  // sur la selection d'une formation cible
  selectFormationTarget() {
    this.targetIdsModulesFomationFiltre = [];

    // si aucune formation n'est selectionnee
    if (this.targetFormation == null) {
      this.findModuleTarget();
      return;
    }

    this.formationService.getModules(this.targetFormation).subscribe(
      res => {

        // ajout des modules au filtres
        for (let a = 0; a < res.length; ++a) {
          if (this.targetIdsModulesFomationFiltre.indexOf(res[a].idModule) === - 1) {
            this.targetIdsModulesFomationFiltre.push(res[a].idModule);
          }
        }

        this.findModuleTarget();
      },
      err => {
        console.error(err);
      }
    );
  }

  // click sur la selection d'un module source
  clickSource(module) {

    this.sourceSelected = module;
    this.sourceRequirementSelected = [];

    // recherche si il existe des pre-requis
    this.moduleService.getRequirementByModule(module.idModule).subscribe(
      res => {
        this.sourceRequirementSelected = res.requirements;
      },
      err => {
        console.error(err);
      }
    );
  }

  // click sur la suppression d'un pre-requis
  clickDelete(r) {

    const body = new RequirementModel();
    body.or = r.or;
    body.requiredModuleId = r.moduleId;

    // supprime le pre-requis
    this.moduleService.deleteRequirementByModule(this.sourceSelected.idModule, body).subscribe(
      res => {
        this.updateModuleWithRequirement();
        this.clickSource(this.sourceSelected);
      },
      err => {
        console.error(err);
      }
    );
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

      // si il y a un filtre sur les modules d'un titre
      if ( this.targetIdsModulesTitreFiltre.length > 0 && this.targetIdsModulesTitreFiltre.indexOf(item.idModule) === - 1 ) {
        continue;
      }

      // si il y a un filtre sur les modules d'une formation
      if ( this.targetIdsModulesFomationFiltre.length > 0 && this.targetIdsModulesFomationFiltre.indexOf(item.idModule) === - 1 ) {
        continue;
      }

      showRes.push(item);
    }
    this.targetModules = showRes;
  }

  // retourne un module en fonction d'un identifiant
  getModuleById ( idModule: string ) {
    for (const item of this.dataModules) {
      if ( item.idModule === idModule ) {
        return item;
      }
    }
    return null;
  }

  // ajout d'un element de pre-requis a un module
  addRequirement (module) {
    if ( this.sourceSelected == null ) {
      return;
    }
    const body = new RequirementModel();
    body.or = this.requirementOrOperator;
    body.requiredModuleId = module.idModule;
    this.moduleService.addRequirementForModule(this.sourceSelected.idModule, body).subscribe(
      res => {
        this.updateModuleWithRequirement();
        this.clickSource(this.sourceSelected);
      },
      err => {
        console.error(err);
      }
    );
  }

  findModuleListe() {

    // controle de la taille des saisies
    if ( this.listeModule.trim() === '' && this.listePrerequis.trim() === '' ) {
      this.moduleWithRequirementFiltre = this.moduleWithRequirement;
      return;
    }

    // filtre les resultats a afficher
    let showRes: any[];
    showRes = [];
    for (const m of this.moduleWithRequirement) {

      // recupere le module concerne
      if ( this.listeModule.length > 1 ) {
        const module = this.getModuleById(m.moduleId);

        // filtre sur le libelle du module
        if ( module.libelle.toLowerCase().indexOf( this.listeModule.toLowerCase() ) === -1 ) {
          continue;
        }
      }

      // filtre sur les module pre-requis
      if ( this.listePrerequis.length > 1 ) {
        let isPresent = false;
        for (const r of m.requirements) {

          // recupere le module concerne
          const module = this.getModuleById(r.moduleId);

          // filtre sur le libelle du module
          if ( module.libelle.toLowerCase().indexOf( this.listePrerequis.toLowerCase() ) !== -1 ) {
            isPresent = true;
            break;
          }
        }
        if ( !isPresent ) {
          continue;
        }
      }
      showRes.push(m);
    }
    this.moduleWithRequirementFiltre = showRes;
  }
}
