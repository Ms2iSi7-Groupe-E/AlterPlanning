import {Component, OnInit, ViewChild} from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {HandleIndependantModuleComponent} from "../modal/handle-independant-module/handle-independant-module.component";
import {ActionTypes} from "../../models/enums/action-types";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "../../../../node_modules/rxjs/Subject";
import {DatatableFrench} from "../../helper/datatable-french";

@Component({
  selector: 'app-page-independant-modules',
  templateUrl: './page-independant-modules.component.html',
  styleUrls: ['./page-independant-modules.component.scss']
})
export class PageIndependantModulesComponent implements OnInit {

  @ViewChild(DataTableDirective)
  dtElement: DataTableDirective;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<void> = new Subject();

  constructor(private modalService: NgbModal) {
    this.dtOptions = {
      order: [],
      columnDefs: [{targets: 'no-sort', orderable: false}],
      language: DatatableFrench.getLanguages(),
    };
  }

  ngOnInit() {
  }

  openCreateModal() {
    const modalRef = this.modalService.open(HandleIndependantModuleComponent, { size: 'lg' });
    modalRef.componentInstance.action = ActionTypes.CREATE;
    modalRef.componentInstance.save.subscribe(res => {
      console.log(res);
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
