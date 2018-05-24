import {Component, OnInit} from '@angular/core';
import {ModelsService} from '../../services/models.service';
import {LieuService} from "../../services/lieu.service";

@Component({
  selector: 'app-home',
  templateUrl: './page-home.component.html',
  styleUrls: ['./page-home.component.scss']
})
export class PageHomeComponent implements OnInit {
  models = [];
  lieux = [];
  selectedModels = null;
  searchCriteria = [];

  constructor(private modelsService: ModelsService, private lieuService: LieuService) { }

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
