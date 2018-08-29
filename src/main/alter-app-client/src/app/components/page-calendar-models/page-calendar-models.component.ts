import {Component, OnInit} from '@angular/core';
import {Subject} from "rxjs/Subject";
import {DatatableFrench} from "../../helper/datatable-french";
import {CalendarModelService} from "../../services/calendar-model.service";

@Component({
  selector: 'app-page-calendar-models',
  templateUrl: './page-calendar-models.component.html',
  styleUrls: ['./page-calendar-models.component.scss']
})
export class PageCalendarModelsComponent implements OnInit {

  models = [];
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<void> = new Subject();

  constructor(private calendarModelService: CalendarModelService) {
    this.dtOptions = {
      order: [],
      columnDefs: [{targets: 'no-sort', orderable: false}],
      language: DatatableFrench.getLanguages(),
    };
  }

  ngOnInit() {
    this.calendarModelService.getModels().subscribe(res => {
      this.models = res;
      this.dtTrigger.next();
    }, console.error);
  }

  deleteModel(model) {
    const answer = confirm("Voulez-vous vraiment supprimer ce modèle de calendrier ?");
    if (answer) {
      this.calendarModelService.deleteModel(model.id).subscribe(() => {
        this.models = this.models.filter(m => m.id !== model.id);
      }, console.error);
    }
  }
}
