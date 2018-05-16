import { Component, OnInit } from '@angular/core';
import { ModuleService } from '../../services/module.service';

@Component({
  selector: 'app-page-modules-requirement',
  templateUrl: './page-modules-requirement.component.html',
  styleUrls: ['./page-modules-requirement.component.scss']
})
export class PageModulesRequirementComponent implements OnInit {
  sourceTitle = '';
  sourceFormation = '';
  sourceModule = '';
  sourcesModules = [];

  constructor(private moduleService: ModuleService) { }

  ngOnInit() {

  }

  findModuleSource(){

    // controle de la taille des saisies
    if( this.sourceTitle.length < 3 && this.sourceFormation.length < 3 && this.sourceModule.length < 3 ){
      return;
    }

    this.moduleService.getModules().subscribe(
      res => {

        // filtre les resultats a afficher
        let showRes = [];
        for (let item of res) {

          // filtre sur le libelle du module
          if( this.sourceModule.length > 2 && item.Libelle.toLowerCase().indexOf( this.sourceModule.toLowerCase() ) == -1 ){
            continue;
          }

          //TODO: les autres criteres

          showRes.push(item);

          // si la taille maximum est atteinte
          if( showRes.length == 4 ){
            break;
          }
        }
        this.sourcesModules = showRes;
      },
      err => {
        console.error(err);
      }
    );
  }

  findModuleTarget(){

  }
}
