import { Component, OnInit } from '@angular/core';
import {ParameterService} from "../../services/parameter.service";
import {ParameterModel} from "../../models/parameter.model";

@Component({
  selector: 'app-page-parameters',
  templateUrl: './page-parameters.component.html',
  styleUrls: ['./page-parameters.component.scss']
})
export class PageParametersComponent implements OnInit {
  parameters = [];
  formation_colors = [];
  course_colors = [];
  switch_colors = [];
  switch_values = [];


  constructor(private parameterService: ParameterService) { }

  ngOnInit() {
    this.parameterService.getParamters().subscribe(
      res => {
        this.parameters = res;
        this.formation_colors = this.parameters.filter(p => p.key.startsWith("FORMATION"));
        this.course_colors = this.parameters.filter(p => p.key.startsWith("COURSE"));
        this.switch_colors = this.parameters.filter(p => p.key.startsWith("SWITCH_COLOR"));
        this.switch_values = this.parameters.filter(p => p.key.startsWith("SWITCH_VALUE"));
      },
      err => {
        console.error(err);
      }
    );
  }

  colorPickerChanged(key: string, value: string) {
    const body = new ParameterModel();
    body.value = value;
    this.parameterService.updateParameter(key, body).subscribe();
  }

  switchValueChanged(key: string, event) {
    if (!isNaN(event.target.value)) {
      const body = new ParameterModel();
      body.value = event.target.value;
      this.parameterService.updateParameter(key, body).subscribe();
    }
  }


}
