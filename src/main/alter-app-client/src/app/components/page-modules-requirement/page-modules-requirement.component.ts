import { Component, OnInit } from '@angular/core';
import { ModuleService } from '../../services/module.service';

@Component({
  selector: 'app-page-modules-requirement',
  templateUrl: './page-modules-requirement.component.html',
  styleUrls: ['./page-modules-requirement.component.scss']
})
export class PageModulesRequirementComponent implements OnInit {
  dataModules = [];
  sourceTitle = '';
  sourceFormation = '';
  sourceModule = '';
  sourcesModules = [];
  sourceSelected = null;
  targetTitle = '';
  targetFormation = '';
  targetModule = '';
  targetModules = [];
  targetSelected = null;

  constructor(private moduleService: ModuleService) { }

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
  }

  findModuleSource(){

    // controle de la taille des saisies
    if( this.sourceTitle.length < 1 && this.sourceFormation.length < 1 && this.sourceModule.length < 1 ){
      this.sourcesModules = this.dataModules;
      return;
    }

    // filtre les resultats a afficher
    let showRes = [];
    for (let item of this.dataModules) {

      // filtre sur le libelle du module
      if( this.sourceModule.length > 1 && item.libelle.toLowerCase().indexOf( this.sourceModule.toLowerCase() ) == -1 ){
        continue;
      }

      //TODO: les autres criteres

      showRes.push(item);

      // si la taille maximum est atteinte
      if( showRes.length == 5 ){
        break;
      }
    }
    this.sourcesModules = showRes;
  }

  filterTargetModules(modules){
    let lstModules = [];
    for (let item of modules) {
      if( this.sourceSelected != null && ( this.sourceSelected.idModule == item.idModule ) ){
        continue;
      }
      lstModules.push(item);
    }
    return lstModules;
  }

  findModuleTarget(){

    // controle de la taille des saisies
    if( this.targetTitle.length < 1 && this.targetFormation.length < 1 && this.targetModule.length < 1 ){
      this.targetModules = this.dataModules;
      return;
    }

    // filtre les resultats a afficher
    let showRes = [];
    for (let item of this.dataModules) {

      // filtre sur le libelle du module
      if( this.targetModule.length > 1 && item.libelle.toLowerCase().indexOf( this.targetModule.toLowerCase() ) == -1 ){
        continue;
      }

      // si c'est le module source
      if( this.sourceSelected != null && this.sourceSelected.idModule == item.idModule ){
        continue;
      }

      //TODO: les autres criteres

      showRes.push(item);

      // si la taille maximum est atteinte
      if( showRes.length == 4 ){
        break;
      }
    }
    this.targetModules = showRes;
  }
}
