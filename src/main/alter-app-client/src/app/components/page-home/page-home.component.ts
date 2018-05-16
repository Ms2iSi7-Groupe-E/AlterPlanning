import {Component, OnInit} from '@angular/core';
import { ModelsService } from '../../services/models.service';
//import { CourService } from '../../services/cour.service';

@Component({
  selector: 'app-home',
  templateUrl: './page-home.component.html',
  styleUrls: ['./page-home.component.scss']
})
export class PageHomeComponent implements OnInit {
  models = [];
  selectedModels = null;
  searchCriteria = [];

  constructor(private modelsService: ModelsService) { }

  ngOnInit() {
    this.models = this.modelsService.getModels();
  }

  changeModels() {
    console.log(this.selectedModels);
  }
  /*cours = [];

  constructor(private courService: CourService) { }

  ngOnInit() {
    this.courService.getCours().subscribe(
      res => {
        //console.log(res);
        this.cours = res;
      },
      err => {
        console.error(err);
      }
    );
  }*/

}
