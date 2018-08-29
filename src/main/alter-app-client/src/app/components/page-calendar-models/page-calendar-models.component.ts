import {Component, OnInit, ViewChild} from '@angular/core';
import {Subject} from "rxjs/Subject";
import {DatatableFrench} from "../../helper/datatable-french";
import {CalendarModelService} from "../../services/calendar-model.service";
import {CalendarModelNameComponent} from "../modal/calendar-model-name/calendar-model-name.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {CalendarModelModel} from "../../models/calendar-model.model";
import {DataTableDirective} from "angular-datatables";

@Component({
  selector: 'app-page-calendar-models',
  templateUrl: './page-calendar-models.component.html',
  styleUrls: ['./page-calendar-models.component.scss']
})
export class PageCalendarModelsComponent implements OnInit {

  @ViewChild(DataTableDirective)
  dtElement: DataTableDirective;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<void> = new Subject();

  models = [];

  constructor(private modalService: NgbModal,
              private calendarModelService: CalendarModelService) {
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
        this.updateDatatable();
      }, console.error);
    }
  }

  duplicateModel(model) {
    const modalRef = this.modalService.open(CalendarModelNameComponent, { size: 'lg' });
    modalRef.componentInstance.validate.subscribe(res => {
      const body = new CalendarModelModel();
      body.name = res.name;
      this.calendarModelService.duplicateCalendarModel(model.id, body).subscribe(newModel => {
        this.models.push(newModel);
        this.updateDatatable();
      }, console.error);
    });
  }

  updateDatatable() {
    this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
      // Destroy the table first
      dtInstance.destroy();
      // Call the dtTrigger to rerender again
      this.dtTrigger.next();
    });
  }
}
