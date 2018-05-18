import { Component, OnInit } from '@angular/core';
import {ParameterService} from "../../services/parameter.service";

@Component({
  selector: 'app-page-parameters',
  templateUrl: './page-parameters.component.html',
  styleUrls: ['./page-parameters.component.scss']
})
export class PageParametersComponent implements OnInit {
  parameters = [];

  constructor(private parameterService : ParameterService) { }

  ngOnInit() {
    this.parameterService.getParamters().subscribe(
      res => {
        this.parameters = res;
      },
      err => {
        console.error(err);
      }
    )
  }

}
